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
     * Creates a simple move. Moves a piece from a source sqaure to a target square.
     * For a castling the king moves two squares in direction of the rook.
     * <p/>
     * For capture ans promotion other constructors are available.
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
     * @param capture   is capture of a piece
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
     * @param capture   is capture of a piece
     */
    public Move(Piece piece, Square from, Square to, boolean capture) {
        this(piece, from, to, capture, null);
    }

    /**
     * * Creates a move.
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
     * Get the source square of the move.
     *
     * @return the source square.
     */
    public Square getFrom() {
        return from;
    }

    /**
     * Get the target square of the move.
     *
     * @return the target sqaure.
     */
    public Square getTo() {
        return to;
    }

    /**
     * Returns the new piece type in case of a pawn promotion
     * <p/>
     * Possible values: QUEEN, ROOK, KNIGHT, BISHOP.
     *
     * @return the piece type choosen, or null in case of no promotion.
     */
    public PieceType getPromotion() {
        return promotion;
    }

    /**
     * Returns the moving piece. In case of castling, this is a king, in case of promotion, it is a pawn.
     *
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns whether the move is a capture.
     *
     * @return true, if a piece is captured.
     */
    public boolean istSchlagen() {
        return capture;
    }

    /**
     * Returns whether the moving piece is a pawn. The move can be a "normal" move, a capture, a promotion ...
     *
     * @return true if the moving piece is a pawn.
     */
    public boolean istBauernZug() {
        return piece.getType() == PieceType.PAWN;
    }

    /**
     * Liefert zurueck, ob mit dem Zug ein Bauer 2 Felder vor bewegt wird.
     *
     * @return true, falls ein Bauer zwei Feldet vor bewegt wird.
     */

    public boolean isPawnAdvancesTwo() {
        return piece.getType() == PieceType.PAWN.PAWN
                && Math.abs(from.getRank() - to.getRank()) == 2;
    }

    /**
     * Liefert zur&uuml;ck, ob der Zug eine Umwandlung ist.
     *
     * @return true in case of a promotion.
     */
    public boolean isPromotion() {
        return promotion != null;
    }

    /**
     * Liefert zur&uuml;ck, ob der Zug eine Rochade ist.
     *
     * @return true bei einer Rochade.
     */
    public boolean isCastling() {
        return piece.getType() == PieceType.KING
                && Math.abs(from.getFile() - to.getFile()) == 2;
    }

    /**
     * Liefert zur&uuml;ck, ob der Zug eine kurze Rochade ist.
     * Kurze Rochaden: e1 - g1 (weiss), e8 - g8 (schwarz).
     *
     * @return true bei einer kurzen Rochade.
     */
    public boolean istRochadeKurz() {
        return isCastling() && to.getFile() == 6;
    }

    /**
     * Liefert zur&uuml;ck, ob der Zug eine lange Rochade ist.
     * Lange Rochaden: e1 - c1 (weiss), e8 - c8 (schwarz).
     *
     * @return true bei einer langen Rochade.
     */
    public boolean istRochadeLang() {
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
     * Liefert eine schoene String-Reprasentation des Zuges zurueck.
     *
     * @return Zug als lesbare Zeichenkette
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
