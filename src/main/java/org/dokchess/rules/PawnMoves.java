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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.dokchess.domain.PieceType.*;

class PawnMoves extends Movement {

    private static final Set<PieceType> CASTLING_OPTIONS;

    static {
        CASTLING_OPTIONS = EnumSet.noneOf(PieceType.class);
        CASTLING_OPTIONS.add(QUEEN);
        CASTLING_OPTIONS.add(ROOK);
        CASTLING_OPTIONS.add(BISHOP);
        CASTLING_OPTIONS.add(KNIGHT);
    }

    public void addMoveCandidates(Square from, Position position,
                                  List<Move> target) {

        Piece ownPawn = position.getPiece(from);
        int delta1 = position.getToMove() == Colour.WHITE ? -1 : +1;
        int delta2 = position.getToMove() == Colour.WHITE ? -2 : +2;

        {
            // advance one square
            //
            Square to = new Square(from.getRank() + delta1, from.getFile());
            if (position.getPiece(to) == null) {
                if (to.getRank() != 0 && to.getRank() != 7) {
                    // Keine Umwandlung
                    Move z = new Move(ownPawn, from, to);
                    target.add(z);
                } else {
                    // Castling
                    for (PieceType newPiecetype : CASTLING_OPTIONS) {
                        Move z = new Move(ownPawn, from, to, newPiecetype);
                        target.add(z);
                    }
                }
            }
        }
        {
            // advance two squares
            //

            int startRank = position.getToMove() == Colour.WHITE ? 6 : 1;

            if (from.getRank() == startRank) {
                if (position.getPiece(startRank + delta1, from.getFile()) == null) {
                    if (position.getPiece(startRank + delta2, from.getFile()) == null) {
                        Square to = new Square(startRank + delta2,
                                from.getFile());
                        Move z = new Move(ownPawn, from, to);
                        target.add(z);
                    }
                }
            }
        }
        {

            // Capture
            //
            List<Square> squares = new ArrayList<Square>();
            addSquareIfReachable(position, from, delta1, +1, squares);
            addSquareIfReachable(position, from, delta1, -1, squares);
            for (Square to : squares) {
                if (to.getRank() != 0 && to.getRank() != 7) {
                    if (position.getPiece(to) != null) {
                        // Schlagen, keine Umwandlung
                        Move m = new Move(ownPawn, from, to, true);
                        target.add(m);
                    } else if (to.equals(position.getEnPassantSquare())) {
                        // Schlagen, en passent
                        Move m = new Move(ownPawn, from, to, true);
                        target.add(m);
                    }

                } else {
                    if (position.getPiece(to) != null) {
                        // Castling
                        for (PieceType neueFigurenart : CASTLING_OPTIONS) {
                            Move z = new Move(ownPawn, from, to, true,
                                    neueFigurenart);
                            target.add(z);
                        }
                    }
                }
            }
        }
    }
}
