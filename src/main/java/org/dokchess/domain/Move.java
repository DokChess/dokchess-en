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

package org.dokchess.domain;

/**
 * Single move of a player. In addition to source and target square
 * it contains further information helpful for analysis. The class is immutable.
 *
 * @author StefanZ
 */
public final class Move {

    private final Piece piece;

    private final Square from;

    private final Square to;

    private final boolean capture;

    private final PieceType promotion;

    /**
     * Creates a simple move: a piece moves from a source square to a target square.
     * For castling, the king moves two squares toward the rook.
     * <p/>
     * For captures and promotions, other constructors are available.
     *
     * @param piece     piece to move
     * @param from      source square
     * @param to        target square
     */
    public Move(Piece piece, Square from, Square to) {
        this(piece, from, to, false, null);
    }

    /**
     * Creates a move.
     *
     * @param piece     piece to move
     * @param from      source square
     * @param to        target square
     * @param capture   whether a piece is captured
     * @param promotion new piece type in case of promotion
     */
    public Move(Piece piece, Square from, Square to, boolean capture,
                PieceType promotion) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.capture = capture;
        this.promotion = promotion;
    }

    /**
     * Creates a move.
     *
     * @param piece     piece to move
     * @param from      source square
     * @param to        target square
     * @param capture   whether a piece is captured
     */
    public Move(Piece piece, Square from, Square to, boolean capture) {
        this(piece, from, to, capture, null);
    }

    /**
     * Creates a move.
     *
     * @param piece     piece to move
     * @param from      source square
     * @param to        target square
     * @param promotion new piece type in case of promotion
     */
    public Move(Piece piece, Square from, Square to, PieceType promotion) {
        this(piece, from, to, false, promotion);
    }

    /**
     * Returns the source square of the move.
     *
     * @return the source square
     */
    public Square getFrom() {
        return from;
    }

    /**
     * Returns the target square of the move.
     *
     * @return the target square
     */
    public Square getTo() {
        return to;
    }

    /**
     * Returns the new piece type in case of pawn promotion.
     * <p/>
     * Possible values: QUEEN, ROOK, KNIGHT, BISHOP.
     *
     * @return the chosen piece type, or {@code null} if there is no promotion
     */
    public PieceType getPromotion() {
        return promotion;
    }

    /**
     * Returns the moving piece. For castling this is the king; for promotion it is still the pawn.
     *
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns whether the move is a capture.
     *
     * @return {@code true} if a piece is captured
     */
    public boolean isCapture() {
        return capture;
    }

    /**
     * Returns whether the moving piece is a pawn. The move may be a normal move, a capture, a promotion, etc.
     *
     * @return {@code true} if the moving piece is a pawn
     */
    public boolean isPawnMove() {
        return piece.getType() == PieceType.PAWN;
    }

    /**
     * Returns whether this move advances a pawn two squares from its starting rank.
     *
     * @return {@code true} if a pawn moves two ranks forward
     */
    public boolean isPawnAdvancesTwo() {
        return piece.getType() == PieceType.PAWN
                && Math.abs(from.getRank() - to.getRank()) == 2;
    }

    /**
     * Returns whether the move is a promotion.
     *
     * @return {@code true} in case of a promotion
     */
    public boolean isPromotion() {
        return promotion != null;
    }

    /**
     * Returns whether the move is castling.
     *
     * @return {@code true} for a castling move
     */
    public boolean isCastling() {
        return piece.getType() == PieceType.KING
                && Math.abs(from.getFile() - to.getFile()) == 2;
    }

    /**
     * Returns whether the move is kingside castling (O-O).
     * Kingside: e1–g1 (White), e8–g8 (Black).
     *
     * @return {@code true} for kingside castling
     */
    public boolean isCastlingKingside() {
        return isCastling() && to.getFile() == 6;
    }

    /**
     * Returns whether the move is queenside castling (O-O-O).
     * Queenside: e1–c1 (White), e8–c8 (Black).
     *
     * @return {@code true} for queenside castling
     */
    public boolean isCastlingQueenside() {
        return isCastling() && to.getFile() == 2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((piece == null) ? 0 : piece.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        result = prime * result
                + ((promotion == null) ? 0 : promotion.hashCode());
        result = prime * result + (capture ? 1231 : 1237);
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Move other = (Move) obj;
        if (piece == null) {
            if (other.piece != null) {
                return false;
            }
        } else if (!piece.equals(other.piece)) {
            return false;
        }
        if (to == null) {
            if (other.to != null) {
                return false;
            }
        } else if (!to.equals(other.to)) {
            return false;
        }
        if (promotion != other.promotion) {
            return false;
        }
        if (capture != other.capture) {
            return false;
        }
        if (from == null) {
            if (other.from != null) {
                return false;
            }
        } else if (!from.equals(other.from)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a readable string representation of the move.
     *
     * @return the move as a human-readable string
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.piece.getType() != PieceType.PAWN) {
            sb.append(piece.getType().getLetter());
            sb.append(' ');
        }
        sb.append(from);
        if (capture) {
            sb.append('x');
        } else {
            sb.append('-');
        }
        sb.append(to);

        if (promotion != null) {
            sb.append(' ');
            sb.append(promotion.getLetter());
        }

        return sb.toString();
    }
}
