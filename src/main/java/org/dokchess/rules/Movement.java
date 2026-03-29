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
     * Computes move candidates from a square and appends them to the given list. Subclasses implement this
     * according to how each piece type moves.
     *
     * @param from     source square which contains the moving piece
     * @param position position to examine
     * @param target   target list for move candidates
     */
    abstract void addMoveCandidates(Square from, Position position,
                                    List<Move> target);

    /**
     * Steps from a square along a direction and appends every square that can be reached in a straight line.
     * Used for rook-like sliding (and similar).
     *
     * @param position position to examine
     * @param from     starting square
     * @param dx       direction delta along file
     * @param dy       direction delta along rank
     * @param target   list to receive reachable squares
     */
    protected final void addReachableSquaresInDirection(final Position position,
                                                        final Square from, final int dx, final int dy, final List<Square> target) {

        Piece movingPiece = position.getPiece(from);
        boolean moreSquares = true;
        int rank = from.getRank();
        int file = from.getFile();

        while (moreSquares) {

            file += dx;
            rank += dy;

            if (isOnBoard(rank, file)) {
                Piece occupant = position.getPiece(rank, file);
                if (occupant == null) {
                    target.add(new Square(rank, file));
                } else {
                    if (occupant.getColour() != movingPiece
                            .getColour()) {
                        // capture
                        target.add(new Square(rank, file));
                    }
                    moreSquares = false;
                }
            } else {
                moreSquares = false;
            }
        }
    }

    /**
     * Looks one step from a square in the given direction. If that square is reachable, it is appended to the
     * target list. The destination may be empty or occupied by an opponent piece that can be captured.
     *
     * @param position   position to examine
     * @param from       source square
     * @param dx         direction delta along rank
     * @param dy         direction delta along file
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
                // move to an empty square
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
