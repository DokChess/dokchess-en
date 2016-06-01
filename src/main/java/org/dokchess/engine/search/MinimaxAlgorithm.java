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

public class MinimaxAlgorithm {

    protected ChessRules chessRules;

    protected Evaluation evaluation;

    private static final int MATT_BEWERTUNG = Evaluation.BEST / 2;

    private int depth;

    /**
     * Setzt die Bewertungsfunktion, anhand derer die Stellungen bei Erreichen
     * der maximalen Suchtiefe bewertet werden.
     */
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * Setzt eine Implementierung der Spielregeln, anhand erlaubte moegliche
     * Zuege und auch Matt und Patt erkannt werden.
     */
    public void setChessRules(ChessRules chessRules) {
        this.chessRules = chessRules;
    }

    /**
     * Set the maximum search depth in half moves. That means at 4 each player moves twice.
     *
     * @param depth serach depth in half moves
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Determines the optimal move according to minimax for the position passed and
     * given evaluation at fixed search depth.
     * The method blocks and is deterministic.
     *
     * @param position position to examine
     * @return best move according to minimax
     */
    public Move determineBestMove(Position position) {

        Colour playerColour = position.getToMove();
        Collection<Move> moves = chessRules.getLegalMoves(position);

        int bestValue = Evaluation.WORST;
        Move bestMove = null;

        for (Move move : moves) {
            Position newPos = position.performMove(move);

            int value = bewerteStellungRekursiv(newPos, playerColour);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }


    protected int bewerteStellungRekursiv(Position stellung, Colour spielerFarbe) {
        return bewerteStellungRekursiv(stellung, 1, spielerFarbe);
    }


    protected int bewerteStellungRekursiv(Position stellung, int aktuelleTiefe,
                                          Colour spielerFarbe) {

        if (aktuelleTiefe == depth) {
            return evaluation.evaluatePosition(stellung, spielerFarbe);
        } else {
            Collection<Move> zuege = chessRules.getLegalMoves(stellung);
            if (zuege.isEmpty()) {

                // PATT
                if (!chessRules
                        .isCheck(stellung, stellung.getToMove())) {
                    return Evaluation.BALANCED;
                }

                // MATT
                // Tiefe mit einrechnen, um fruehes Matt zu bevorzugen
                if (stellung.getToMove() == spielerFarbe) {
                    return -(MATT_BEWERTUNG - aktuelleTiefe);
                } else {
                    return MATT_BEWERTUNG - aktuelleTiefe;
                }

            } else {
                if (aktuelleTiefe % 2 == 0) {
                    // Max
                    int max = Evaluation.WORST;
                    for (Move zug : zuege) {
                        Position neueStellung = stellung.performMove(zug);
                        int wert = bewerteStellungRekursiv(neueStellung,
                                aktuelleTiefe + 1, spielerFarbe);
                        if (wert > max) {
                            max = wert;
                        }
                    }
                    return max;
                } else {
                    // Min
                    int min = Evaluation.BEST;
                    for (Move zug : zuege) {
                        Position neueStellung = stellung.performMove(zug);
                        int wert = bewerteStellungRekursiv(neueStellung,
                                aktuelleTiefe + 1, spielerFarbe);
                        if (wert < min) {
                            min = wert;
                        }
                    }
                    return min;
                }
            }
        }
    }


}
