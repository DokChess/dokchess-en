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
 * Created by stefanz on 09.04.16.
 */
public enum PieceType {


    /**
     * The king may move to an adjoining square.
     */
    KING('K'),

    /**
     * The queen may move to any square along the file, the rank or a diagonal on which
     * it stands. When making these moves the queen may not move over any intervening pieces.
     */
    QUEEN('Q'),

    /**
     * The rook may move to any square along the file or the rank on which it stands.
     * When making these moves the rook may not move over any intervening pieces.
     */
    ROOK('R'),

    /**
     * The bishop may move to any square along a diagonal on which it stands.
     * When making these moves the bishop may not move over any intervening pieces.
     */
    BISHOP('B'),

    /**
     * The knight may move to one of the squares nearest to that on which it stands
     * but not on the same rank, file or diagonal.
     */
    KNIGHT('N'),

    /**
     * The pawn may move forward to the square immediately in front of it on the same
     * file, provided that this square is unoccupied. on its first move the pawn may
     * move advance two squares along the same file, provided that both squares are
     * unoccupied.
     */
    PAWN('P');

    private char letter;

    private PieceType(char letter) {
        this.letter = letter;
    }

    /**
     * Returns the piece type for a given letter as used in chess notations.
     * Letter my be upper or lower case.
     *
     * @param c letter, e.g. 'q' fuer queen.
     * @return the piece type.
     * @throws java.lang.IllegalArgumentException not a valid letter.
     */
    public static PieceType fromLetter(final char c) {
        switch (c) {
            case 'k':
            case 'K':
                return KING;
            case 'q':
            case 'Q':
                return QUEEN;
            case 'r':
            case 'R':
                return ROOK;
            case 'b':
            case 'B':
                return BISHOP;
            case 'n':
            case 'N':
                return KNIGHT;
            case 'p':
            case 'P':
                return PAWN;
            default:
                throw new IllegalArgumentException("not a valid piece type");
        }
    }

    /**
     * Returns the letter for this piece type, as used in english notations.
     * For instance Q for the queen.
     *
     * @return letter for the piece type.
     */
    public char getLetter() {
        return letter;
    }
}