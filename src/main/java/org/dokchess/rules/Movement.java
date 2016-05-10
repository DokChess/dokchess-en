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
package org.dokchess.rules;

import org.dokchess.domain.Move;
import org.dokchess.domain.Piece;
import org.dokchess.domain.Position;
import org.dokchess.domain.Square;

import java.util.List;

/**
 * Abstract base type for movement types in chess.
 *
 * @author stefanz
 */
abstract class Movement {

    /**
     * Ermittelt Zugkandidaten von einem Feld aus, und fuegt sie der uebergebenen Liste hinzu. Die Methode ist fuer
     * die unterschiedlichen Gangarten der Figuren entsprechend zu implementieren.
     *
     * @param from     source square which contains the moving piece
     * @param position position to examine
     * @param target   target list for move candidates
     */
    abstract void addMoveCandidates(Square from, Position position,
                                    List<Move> target);

    /**
     * Marschiert von einem Feld aus eine Richtung entlang und fuegt alle Felder einer Liste hinzu,
     * die so erreichbar sind. Wird fuer Turm und aenliches verwendet.
     *
     * @param position position to examine
     * @param from     Startfeld, von dem losmarschiert wird.
     * @param dx       direction dx
     * @param dy       direction dy
     * @param target   Zielliste fuer die erreichbaren Felder
     */
    protected final void fuegeFelderInRichtungHinzuFallsErreichbar(final Position position,
                                                                   final Square from, final int dx, final int dy, final List<Square> target) {

        Piece figurDieZieht = position.getPiece(from);
        boolean weiter = true;
        int reihe = from.getRank();
        int linie = from.getFile();

        while (weiter) {

            linie += dx;
            reihe += dy;

            if (isOnBoard(reihe, linie)) {
                Piece zuSchlagendeFigur = position.getPiece(reihe, linie);
                if (zuSchlagendeFigur == null) {
                    target.add(new Square(reihe, linie));
                } else {
                    if (zuSchlagendeFigur.getColour() != figurDieZieht
                            .getColour()) {
                        // Schlagen
                        target.add(new Square(reihe, linie));
                    }
                    weiter = false;
                }
            } else {
                weiter = false;
            }
        }
    }

    /**
     * Guckt von einem Feld aus in Richtung eines anderes Feldes. Wenn es erreichbar ist, wird es einer
     * Zielliste hinzugefuegt. Dabei wird beruecksichtigt, ob das Zielffeld frei ist, oder von einer gegnerischen
     * Figur besetzt ist, die geschlagen werden kann.
     *
     * @param position   position to examine
     * @param from       source suare
     * @param dx         direction dx
     * @param dy         direction dy
     * @param targetList target list for reachable squares
     */
    protected final void addSquareIfReachable(final Position position,
                                              final Square from, final int dx, final int dy, final List<Square> targetList) {

        int toRank = from.getRank() + dx;
        int toFile = from.getFile() + dy;

        if (isOnBoard(toRank, toFile)) {
            Piece pieceToMove = position.getPiece(from);
            Piece pieceToCapture = position.getPiece(toRank, toFile);
            if (pieceToCapture == null) {
                // Move to a free sqaure
                targetList.add(new Square(toRank, toFile));
            } else {
                if (pieceToCapture.getColour() != pieceToMove.getColour()) {
                    // capture
                    targetList.add(new Square(toRank, toFile));
                }
            }
        }
    }

    /**
     * Checks if coordinates are on chessboard.
     *
     * @param rank Rank
     * @param file File
     * @return true, if square on board, i.e. both values between 0..7
     */
    private boolean isOnBoard(final int rank, final int file) {
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }
}
