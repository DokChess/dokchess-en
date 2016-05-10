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

import org.dokchess.domain.*;

import java.util.EnumSet;
import java.util.Set;

final class Tools {

    private static final Set<PieceType> QUEEN_BISHOP = EnumSet.of(PieceType.QUEEN,
            PieceType.KING.BISHOP);
    private static final Set<PieceType> QUEEN_ROOK = EnumSet.of(PieceType.QUEEN,
            PieceType.ROOK);

    /**
     * Tool class, constructor not visible.
     */
    private Tools() {
    }

    /**
     * Prueft ob das angegebene Feld von einer Figur in der angegebenen Farbe
     * angegriffen wird. Dabei ist unerheblich, ob das zu pruefende Feld besetzt
     * ist. Die Methode kann daher sowohl fuer eine Schach/Matt-Pruefung,als
     * auch auf gueltige Rochade eingesetzt werden.
     *
     * @param position Stellung, der die ggf. angreifenden Figuren entnommen werden
     * @param square   zu pruefendes Feld
     * @param colour   Farbe des potentiellen Angreifers
     * @return true, falls das Feld von einer Figur passender Farbe angegriffen
     * wird.
     */
    public static boolean istFeldAngegriffen(Position position, Square square,
                                             Colour colour) {

        // Schraeg (Dame/Laeufer)
        boolean dameLauferAngriff = istFeldAngegriffenInRichtung(position,
                square, colour, 1, 1, QUEEN_BISHOP)
                || istFeldAngegriffenInRichtung(position, square, colour, -1, -1,
                QUEEN_BISHOP)
                || istFeldAngegriffenInRichtung(position, square, colour, 1, -1,
                QUEEN_BISHOP)
                || istFeldAngegriffenInRichtung(position, square, colour, -1, 1,
                QUEEN_BISHOP);
        if (dameLauferAngriff) {
            return true;
        }

        // Gerade (Dame/Turm)
        boolean dameTurmAngriff = istFeldAngegriffenInRichtung(position, square,
                colour, 1, 0, QUEEN_ROOK)
                || istFeldAngegriffenInRichtung(position, square, colour, 0, 1,
                QUEEN_ROOK)
                || istFeldAngegriffenInRichtung(position, square, colour, -1, 0,
                QUEEN_ROOK)
                || istFeldAngegriffenInRichtung(position, square, colour, 0, -1,
                QUEEN_ROOK);
        if (dameTurmAngriff) {
            return true;
        }

        // Springer
        boolean springerAngriff = istFeldAngegriffenVonFeld(position, square,
                colour, 1, 2, PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, 1, -2,
                PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, -1, 2,
                PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, -1, -2,
                PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, 2, 1,
                PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, 2, -1,
                PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, -2, 1,
                PieceType.KNIGHT)
                || istFeldAngegriffenVonFeld(position, square, colour, -2, -1,
                PieceType.KNIGHT);
        if (springerAngriff) {
            return true;
        }

        // Bauer
        int delta = colour == Colour.WHITE ? +1 : -1;
        boolean bauernAngriff = istFeldAngegriffenVonFeld(position, square,
                colour, 1, delta, PieceType.PAWN)
                || istFeldAngegriffenVonFeld(position, square, colour, -1, delta,
                PieceType.PAWN);
        if (bauernAngriff) {
            return true;
        }

        // Koenig
        boolean koenigsAngriff = istFeldAngegriffenVonFeld(position, square,
                colour, 0, 1, PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, 0, -1,
                PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, 1, 0,
                PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, -1, 0,
                PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, 1, 1,
                PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, 1, -1,
                PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, -1, 1,
                PieceType.KING)
                || istFeldAngegriffenVonFeld(position, square, colour, -1, -1,
                PieceType.KING);

        if (koenigsAngriff) {
            return true;
        }

        return false;
    }

    private static boolean istFeldAngegriffenInRichtung(Position position,
                                                        Square square, Colour colour, int dx, int dy, Set<PieceType> pieceTypes) {

        int rank = square.getRank();
        int file = square.getFile();
        boolean weiter = true;

        while (weiter) {

            file += dx;
            rank += dy;

            if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
                Piece figur = position.getPiece(rank, file);
                if (figur != null) {
                    weiter = false;
                    if (figur.getColour() == colour) {
                        if (pieceTypes.contains(figur.getType())) {
                            return true;
                        }
                    }
                }
            } else {
                weiter = false;
            }
        }
        return false;
    }

    private static boolean istFeldAngegriffenVonFeld(Position position,
                                                     Square square, Colour colour, int dx, int dy, PieceType pieceType) {

        int linie = square.getFile() + dx;
        int reihe = square.getRank() + dy;

        if (linie >= 0 && linie < 8 && reihe >= 0 && reihe < 8) {
            Piece piece = position.getPiece(reihe, linie);
            if (piece != null) {
                if (piece.getColour() == colour) {
                    if (piece.getType() == pieceType) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
