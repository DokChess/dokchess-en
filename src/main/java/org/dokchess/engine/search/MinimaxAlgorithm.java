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
import org.dokchess.engine.eval.Evaluation;
import org.dokchess.rules.ChessRules;

import java.util.Collection;

/**
 * Depth-limited minimax over legal moves, using a pluggable {@link Evaluation}
 * at the leaf nodes. Checkmate and stalemate are handled explicitly.
 */
public class MinimaxAlgorithm {

    protected ChessRules chessRules;

    protected Evaluation evaluation;

    /** Base magnitude used when scoring checkmate (prefers shorter mates via depth term). */
    private static final int CHECKMATE_SCORE = Evaluation.BEST / 2;

    private int depth;

    /**
     * Sets the evaluation function used at the maximum search depth.
     */
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * Sets the chess rules used to generate legal moves and to detect check,
     * checkmate, and stalemate.
     */
    public void setChessRules(ChessRules chessRules) {
        this.chessRules = chessRules;
    }

    /**
     * Sets the maximum search depth in half-moves (plies). For example, {@code 4}
     * means each side may move twice in the lookahead.
     *
     * @param depth search depth in plies
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Returns the best move for the side to move in {@code position} using minimax
     * at the configured depth. Blocks until finished; deterministic for a given
     * position and rule order.
     *
     * @param position root position to search
     * @return best move according to minimax, or {@code null} if there are no moves
     */
    public Move determineBestMove(Position position) {

        Colour playerColour = position.getToMove();
        Collection<Move> moves = chessRules.getLegalMoves(position);

        int bestValue = Evaluation.WORST;
        Move bestMove = null;

        for (Move move : moves) {
            Position newPos = position.performMove(move);

            int value = evaluatePositionRecursive(newPos, playerColour);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Recursive evaluation from ply depth 1 for the given root player colour.
     */
    protected int evaluatePositionRecursive(Position position, Colour rootPlayerColour) {
        return evaluatePositionRecursive(position, 1, rootPlayerColour);
    }

    /**
     * Minimax with alternating min/max layers at odd/even ply depths.
     *
     * @param position     position to evaluate
     * @param currentDepth 1-based ply counter from the root
     * @param rootPlayerColour side to maximise at the root (engine's player)
     */
    protected int evaluatePositionRecursive(Position position, int currentDepth,
                                            Colour rootPlayerColour) {

        if (currentDepth == depth) {
            return evaluation.evaluatePosition(position, rootPlayerColour);
        }
        Collection<Move> legalMoves = chessRules.getLegalMoves(position);
        if (legalMoves.isEmpty()) {

            // Stalemate
            if (!chessRules
                    .isCheck(position, position.getToMove())) {
                return Evaluation.BALANCED;
            }

            // Checkmate — include depth so shorter mates score higher
            if (position.getToMove() == rootPlayerColour) {
                return -(CHECKMATE_SCORE - currentDepth);
            }
            return CHECKMATE_SCORE - currentDepth;

        }
        if (currentDepth % 2 == 0) {
            // Max layer
            int max = Evaluation.WORST;
            for (Move move : legalMoves) {
                Position childPosition = position.performMove(move);
                int score = evaluatePositionRecursive(childPosition,
                        currentDepth + 1, rootPlayerColour);
                if (score > max) {
                    max = score;
                }
            }
            return max;
        }
        // Min layer
        int min = Evaluation.BEST;
        for (Move move : legalMoves) {
            Position childPosition = position.performMove(move);
            int score = evaluatePositionRecursive(childPosition,
                    currentDepth + 1, rootPlayerColour);
            if (score < min) {
                min = score;
            }
        }
        return min;
    }

}
