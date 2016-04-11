/*
 * Copyright (c) 2010-2016 Stefan Zoerner
 * This file is part of DokChess.
 *
 *  DokChess is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  DokChess is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with DokChess.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dokchess.domain;

/**
 * A chessman, a single piece in the game of chess.
 * The class is immutable.
 *
 * @author StefanZ
 */
public final class Piece {

    /**
     * piece colour. e.g. WHITE.
     */
    private final Colour colour;

    /**
     * piece type. e.g. PAWN.
     */
    private final PieceType type;

    /**
     * Creates an imutable piece.
     *
     * @param t piece type. e.g. PAWN.
     * @param c piece colour. e.g. WHITE.
     */
    public Piece(final PieceType t, final Colour c) {
        this.type = t;
        this.colour = c;
    }

    /**
     * piece type. e.g. PAWN.
     *
     * @return the type.
     */
    public PieceType getType() {
        return type;
    }

    /**
     * piece colour. e.g. WHITE.
     *
     * @return the colour.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * String representation as used in FEN. Lowercase letters are black pieces,
     * uppercase are white. 'K' fpr instance is a white King, 'q' a black queen.
     **
     * @return representation of the piece as a single letter
     */
    public char alsBuchstabe() {
        char buchstabe = type.getLetter();
        if (colour == Colour.BLACK) {
            buchstabe = Character.toLowerCase(buchstabe);
        }
        return buchstabe;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(colour);
        sb.append(" ");
        sb.append(type);

        return sb.toString();
    }

    /**
     * Test whether the piece has the given type.
     *
     * @param t type to check.
     * @return true, if yes
     */
    public boolean is(final PieceType t) {
        return this.type == t;
    }

    /**
     * Test whether the piece has the given colour.
     *
     * @param c colour to check.
     * @return true, if yes.
     */
    public boolean is(final Colour c) {
        return this.colour == c;
    }

    @Override
    public int hashCode() {
        return (colour.ordinal() * 10) + type.ordinal();
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
        Piece otherPiece = (Piece) obj;
        if (type != otherPiece.type) {
            return false;
        }
        if (colour != otherPiece.colour) {
            return false;
        }
        return true;
    }
}
