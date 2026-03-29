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
            PieceType.BISHOP);
    private static final Set<PieceType> QUEEN_ROOK = EnumSet.of(PieceType.QUEEN,
            PieceType.ROOK);

    /**
     * Tool class, constructor not visible.
     */
    private Tools() {
    }

    /**
     * Returns whether the given square is attacked by any piece of the given colour.
     * Occupancy of the square itself does not matter; the method is suitable for
     * check / checkmate detection and for castling legality.
     *
     * @param position position to read attacking pieces from
     * @param square   square to test
     * @param colour   colour of the potential attacking side
     * @return true if a piece of that colour attacks the square
     */
    public static boolean isSquareAttacked(Position position, Square square,
                                           Colour colour) {

        // Diagonal (queen / bishop)
        boolean bishopOrQueenDiagonalAttack = isAttackedAlongRay(position,
                square, colour, 1, 1, QUEEN_BISHOP)
                || isAttackedAlongRay(position, square, colour, -1, -1,
                QUEEN_BISHOP)
                || isAttackedAlongRay(position, square, colour, 1, -1,
                QUEEN_BISHOP)
                || isAttackedAlongRay(position, square, colour, -1, 1,
                QUEEN_BISHOP);
        if (bishopOrQueenDiagonalAttack) {
            return true;
        }

        // Orthogonal (queen / rook)
        boolean rookOrQueenLineAttack = isAttackedAlongRay(position, square,
                colour, 1, 0, QUEEN_ROOK)
                || isAttackedAlongRay(position, square, colour, 0, 1,
                QUEEN_ROOK)
                || isAttackedAlongRay(position, square, colour, -1, 0,
                QUEEN_ROOK)
                || isAttackedAlongRay(position, square, colour, 0, -1,
                QUEEN_ROOK);
        if (rookOrQueenLineAttack) {
            return true;
        }

        // Knight
        boolean knightAttack = isSquareAttackedFromSquare(position, square,
                colour, 1, 2, PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, 1, -2,
                PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, -1, 2,
                PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, -1, -2,
                PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, 2, 1,
                PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, 2, -1,
                PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, -2, 1,
                PieceType.KNIGHT)
                || isSquareAttackedFromSquare(position, square, colour, -2, -1,
                PieceType.KNIGHT);
        if (knightAttack) {
            return true;
        }

        // Pawn
        int pawnRankDelta = colour == Colour.WHITE ? +1 : -1;
        boolean pawnAttack = isSquareAttackedFromSquare(position, square,
                colour, 1, pawnRankDelta, PieceType.PAWN)
                || isSquareAttackedFromSquare(position, square, colour, -1, pawnRankDelta,
                PieceType.PAWN);
        if (pawnAttack) {
            return true;
        }

        // King
        boolean kingAttack = isSquareAttackedFromSquare(position, square,
                colour, 0, 1, PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, 0, -1,
                PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, 1, 0,
                PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, -1, 0,
                PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, 1, 1,
                PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, 1, -1,
                PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, -1, 1,
                PieceType.KING)
                || isSquareAttackedFromSquare(position, square, colour, -1, -1,
                PieceType.KING);

        if (kingAttack) {
            return true;
        }

        return false;
    }

    /**
     * Walks along a ray from {@code square} in direction ({@code dFile}, {@code dRank}) and returns
     * whether the first piece encountered on that ray is of {@code colour} and has a type in {@code pieceTypes}.
     * File and rank deltas match {@link Movement#addReachableSquaresInDirection}.
     */
    private static boolean isAttackedAlongRay(Position position,
                                              Square square, Colour colour, int dFile, int dRank,
                                              Set<PieceType> pieceTypes) {

        int rank = square.getRank();
        int file = square.getFile();
        boolean moreSquares = true;

        while (moreSquares) {

            file += dFile;
            rank += dRank;

            if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
                Piece piece = position.getPiece(rank, file);
                if (piece != null) {
                    moreSquares = false;
                    if (piece.getColour() == colour) {
                        if (pieceTypes.contains(piece.getType())) {
                            return true;
                        }
                    }
                }
            } else {
                moreSquares = false;
            }
        }
        return false;
    }

    /**
     * Literal translation of {@code istFeldAngegriffenVonFeld}: whether {@code square} is attacked
     * from the square one step away in direction ({@code dFile}, {@code dRank}) — i.e. whether that
     * neighbour square contains a piece of {@code colour} and {@code pieceType} (knight, king, pawn).
     */
    private static boolean isSquareAttackedFromSquare(Position position,
                                                        Square square, Colour colour, int dFile,
                                                        int dRank, PieceType pieceType) {

        int file = square.getFile() + dFile;
        int rank = square.getRank() + dRank;

        if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
            Piece piece = position.getPiece(rank, file);
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
