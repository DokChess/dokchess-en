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

import java.util.List;

import static org.dokchess.domain.Squares.*;

class CastlingMoves extends Movement {

    private static final Piece WHITE_KING = new Piece(PieceType.KING, Colour.WHITE);
    private static final Piece BLACK_KING = new Piece(PieceType.KING, Colour.BLACK);

    @Override
    public void addMoveCandidates(Square from, Position position,
                                  List<Move> target) {

        switch (position.getToMove()) {
            case WHITE:
                if (position.getCastlingsAvailable().contains(CastlingType.WHITE_KINGSIDE)) {
                    if (areAllSquaresEmpty(position, f1, g1)
                            && noneOfSquaresAreAttacked(position, Colour.BLACK, e1,
                            f1, g1)) {
                        Move kingsideCastle = new Move(WHITE_KING, e1, g1);
                        target.add(kingsideCastle);
                    }
                }
                if (position.getCastlingsAvailable().contains(CastlingType.WHITE_QUEENSIDE)) {
                    if (areAllSquaresEmpty(position, b1, c1, d1)
                            && noneOfSquaresAreAttacked(position, Colour.BLACK, e1,
                            d1, c1)) {
                        Move queensideCastle = new Move(WHITE_KING, e1, c1);
                        target.add(queensideCastle);
                    }
                }
                break;

            case BLACK:
                if (position.getCastlingsAvailable().contains(CastlingType.BLACK_KINGSIDE)) {
                    if (areAllSquaresEmpty(position, f8, g8)
                            && noneOfSquaresAreAttacked(position, Colour.WHITE, e8, f8,
                            g8)) {
                        Move kingsideCastle = new Move(BLACK_KING, e8, g8);
                        target.add(kingsideCastle);
                    }
                }
                if (position.getCastlingsAvailable().contains(CastlingType.BLACK_QUEENSIDE)) {
                    if (areAllSquaresEmpty(position, b8, c8, d8)
                            && noneOfSquaresAreAttacked(position, Colour.WHITE, e8, d8,
                            c8)) {
                        Move queensideCastle = new Move(BLACK_KING, e8, c8);
                        target.add(queensideCastle);
                    }
                }
                break;
        }
    }

    protected boolean noneOfSquaresAreAttacked(Position position,
                                               Colour attackingColour, Square... squares) {
        for (Square square : squares) {
            if (Tools.isSquareAttacked(position, square, attackingColour)) {
                return false;
            }
        }
        return true;
    }

    protected boolean areAllSquaresEmpty(Position position, Square... squares) {
        for (Square square : squares) {
            if (position.getPiece(square) != null) {
                return false;
            }
        }
        return true;
    }
}
