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

package org.dokchess.engine.integration;

import org.dokchess.domain.Colour;
import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.dokchess.engine.DefaultEngine;
import org.dokchess.engine.Engine;
import org.dokchess.rules.ChessRules;
import org.dokchess.rules.DefaultChessRules;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The engine in standard configuration plays white agaist a
 * computer opponent basically with random move,
 * <p>
 * The latter plays arbitrary moves (valid according to the chess
 * rules), but prefers capture and pawn moves in order to drive the game.
 */
public class EngineGegenZufallIntegTest {

    Position board = null;
    Engine dokChess = null;
    ChessRules rules = null;

    @Test
    public void playWholeGame() throws InterruptedException {

        rules = new DefaultChessRules();
        dokChess = new DefaultEngine(rules);
        board = new Position();
        dokChess.setupPieces(board);

        Observable<Move> whiteMoves = dokChess.determineYourMove();
        whiteMoves.subscribe(new ZugHinRueck());

        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (gameOver()) {
                    executor.shutdown();
                }
            }
        }, 10, 5, TimeUnit.SECONDS);

        executor.awaitTermination(5, TimeUnit.MINUTES);

        // At the end of the game black to move has lost.
        Assert.assertTrue(board.getToMove() == Colour.BLACK);
        Assert.assertTrue(rules.isCheckmate(board));
    }

    synchronized boolean gameOver() {
        return rules.isCheckmate(board) || rules.isStalemate(board);
    }

    synchronized void move(Move move) {
        board = board.performMove(move);
        dokChess.performMove(move);
    }

    class ZugHinRueck implements rx.Observer<Move> {

        Move bestMove = null;

        @Override
        public void onNext(Move zug) {
            bestMove = zug;
        }

        @Override
        public void onCompleted() {

            move(bestMove);

            if (!gameOver()) {

                Move zug = determineBlackMove();
                move(zug);

                if (!gameOver()) {
                    bestMove = null;
                    Observable<Move> subject = dokChess.determineYourMove();
                    subject.subscribe(this);
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            Assert.fail(e.getMessage());
        }

        Move determineBlackMove() {
            Assert.assertTrue(board.getToMove() == Colour.BLACK);
            Collection<Move> moves = rules.getLegalMoves(board);
            Assert.assertFalse(moves.isEmpty());

            SortedSet<Move> sortedMoves = new TreeSet<>(new MoveOrder());
            sortedMoves.addAll(moves);

            return sortedMoves.first();
        }


        /**
         * Preferred moves of the other computer player.
         */
        class MoveOrder implements Comparator<Move> {

            @Override
            public int compare(Move z1, Move z2) {
                return zugWert(z2) - zugWert(z1);
            }

            int zugWert(Move move) {
                int value = 0;

                if (move.isCapture()) {
                    value += 1000;
                }

                if (move.isCastling()) {
                    value += 100;
                }

                if (move.isPawnMove()) {
                    value += 10;
                }

                return value;
            }
        }
    }
}
