/*
 * Copyright (c) 2010-2016 Stefan Zoerner
 * This file is part of DokChess.
 *
 * DokChess is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DokChess is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DokChess.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dokchess.engine.search;

import org.dokchess.domain.Colour;
import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import rx.Observer;
import rx.subjects.ReplaySubject;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Parallel minimax at the root: each legal move is evaluated in its own task on a
 * thread pool. Results are streamed as {@link RatedMove} instances; the best move
 * is forwarded to the given {@link Observer}.
 */
public class MinimaxParallelSearch extends MinimaxAlgorithm implements Search {

    private ExecutorService executorService;

    private ReplaySubject<RatedMove> currentSearchResults;

    public MinimaxParallelSearch() {
        int cores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(cores);
    }

    @Override
    public final void searchMove(Position position, Observer<Move> subject) {
        Collection<Move> legalMoves = chessRules.getLegalMoves(position);
        if (legalMoves.size() > 0) {
            ReplaySubject<RatedMove> searchResults = ReplaySubject.create();
            currentSearchResults = searchResults;

            BestMoveReporter reporter = new BestMoveReporter(subject, legalMoves.size());
            searchResults.subscribe(reporter);

            for (Move move : legalMoves) {
                RootMoveEvaluationTask task =
                        new RootMoveEvaluationTask(position, move, searchResults);
                searchResults.subscribe(task);
                executorService.execute(task);
            }
        } else {
            subject.onCompleted();
        }
    }

    @Override
    public final void cancelSearch() {
        if (currentSearchResults != null) {
            currentSearchResults.onCompleted();
            currentSearchResults = null;
        }
    }

    @Override
    public void close() {
        this.cancelSearch();
        this.executorService.shutdown();
    }

    /**
     * Evaluates one root move in a worker thread and publishes a {@link RatedMove}.
     */
    class RootMoveEvaluationTask implements Runnable, Observer<RatedMove> {

        private final Position position;

        private final Move move;

        private final ReplaySubject<RatedMove> searchResults;

        private boolean computationFinished = false;

        RootMoveEvaluationTask(Position position, Move move,
                               ReplaySubject<RatedMove> searchResults) {
            this.position = position;
            this.move = move;
            this.searchResults = searchResults;
        }

        @Override
        public void run() {
            if (!computationFinished) {
                Colour rootPlayerColour = position.getToMove();
                Position positionAfterMove = position.performMove(move);
                int score = evaluatePositionRecursive(positionAfterMove, rootPlayerColour);
                searchResults.onNext(new RatedMove(move, score));
            }
        }

        @Override
        public void onCompleted() {
            this.computationFinished = true;
        }

        @Override
        public void onError(Throwable e) {
            this.computationFinished = true;
        }

        @Override
        public void onNext(RatedMove ratedMove) {
        }
    }

    /**
     * Forwards the strongest {@link RatedMove} seen so far to the observer and
     * completes when all root moves have reported.
     */
    class BestMoveReporter implements Observer<RatedMove> {

        private final Observer<Move> subject;

        private final int candidateCount;

        private int completedCount;

        private RatedMove bestRated = null;

        BestMoveReporter(Observer<Move> subject, int candidateCount) {
            this.subject = subject;
            this.candidateCount = candidateCount;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public synchronized void onNext(RatedMove ratedMove) {

            if (bestRated == null || bestRated.getRating() < ratedMove.getRating()) {
                bestRated = ratedMove;
                subject.onNext(ratedMove.getMove());
            }

            completedCount += 1;
            if (completedCount == candidateCount) {
                subject.onCompleted();
            }
        }
    }
}
