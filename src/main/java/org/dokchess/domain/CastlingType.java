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
 * Castlings still available in a game as an enumeration.
 *
 * @author StefanZ
 */
public enum CastlingType {

    /** White can castle kingside */
    WHITE_KINGSIDE('K'),

    /** White can castle queenside */
    WHITE_QUEENSIDE('Q'),

    /** Black can castle kingside */
    BLACK_KINGSIDE('k'),

    /** Black can castle queenside */
    BLACK_QUEENSIDE('q');

    private char letter;

    private CastlingType(char letter) {
        this.letter = letter;
    }

    /**
     * Get the correspondig castling availability from a single letter according to FEN .
     *
     * @param c letter, 'K', 'Q', 'k' oder 'q'
     * @return rorrespondig castling availability, or null
     */
    public static CastlingType fromLetter(char c) {
        switch (c) {
            case 'K':
                return WHITE_KINGSIDE;
            case 'Q':
                return WHITE_QUEENSIDE;
            case 'k':
                return BLACK_KINGSIDE;
            case 'q':
                return BLACK_QUEENSIDE;
        }
        return null;
    }

    /**
     * as a single letter according to FEN.
     *
     * @return castling availability as a sinhle letter.
     */
    public char asLetter() {
        return this.letter;
    }
}
