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

package org.dokchess.engine;

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.dokchess.engine.eval.StandardMaterialEvaluation;
import org.dokchess.engine.search.MinimaxParallelSearch;
import org.dokchess.opening.OpeningLibrary;
import org.dokchess.rules.ChessRules;
import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * Default {@link Engine} implementation.
 * <p>
 * Requires a {@link ChessRules} implementation to operate; an {@link OpeningLibrary}
 * is optional.
 *
 * @author StefanZ
 */
public class DefaultEngine implements Engine {

    private Position position;

    private DetermineMove movePipeline;

    public DefaultEngine(ChessRules chessRules) {
        this(chessRules, null);
    }

    public DefaultEngine(ChessRules chessRules,
                         OpeningLibrary openingLibrary) {

        this.position = new Position();

        MinimaxParallelSearch minimax = new MinimaxParallelSearch();
        minimax.setDepth(4);
        minimax.setChessRules(chessRules);
        minimax.setEvaluation(new StandardMaterialEvaluation());

        FromSearch fromSearch = new FromSearch(minimax);

        if (openingLibrary != null) {
            this.movePipeline = new FromLibrary(openingLibrary, fromSearch);
        } else {
            this.movePipeline = fromSearch;
        }

    }

    @Override
    public void setupPieces(Position position) {
        this.position = position;
        movePipeline.cancelCurrentSearch();
    }

    @Override
    public Observable<Move> determineYourMove() {
        ReplaySubject<Move> subject = ReplaySubject.create();
        movePipeline.determineMove(this.position, subject);
        return subject;
    }

    @Override
    public void performMove(Move move) {
        position = position.performMove(move);
        movePipeline.cancelCurrentSearch();
    }

    @Override
    public void close() {
        movePipeline.cancelCurrentSearch();
    }
}
