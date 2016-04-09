/*
 * Copyright (c) 2010-2016 Stefan Zoerner
 *
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
 * A square on the chessboard.
 *
 * The chessboard is composed of an 8 x 8 grid of 64 equal squares alternately
 * light (the "white" squares) and dark (the "black" squares).
 *
 * The class is immutable.
 *
 * @author StefanZ
 */
public final class Square {

    /**
     * File (column) of the square, values 0-7, 0 is the a file.
     */
    private final int file;

    /**
     * rank (row) of the square, values 0-7, 0 is the rank 8
     */
    private final int rank;

    /**
     * Creates a square by coordinates.
     *
     * @param rank rank of the sqaure, 0-7
     * @param file file of the sqaure, 0-7, 0 is a
     */
    public Square(final int rank, final int file) {
        this.rank = rank;
        this.file = file;
    }

    /**
     * Creates a square by name.
     *
     * @param name Name des sqaure, e.g. "e4"
     */
    public Square(final String name) {

        // a - h
        char nameFile = name.charAt(0);

        // 1 - 8
        char nameRank = name.charAt(1);

        if (Character.isUpperCase(nameFile)) {
            nameFile = Character.toLowerCase(nameFile);
        }

        this.rank = (7 - (nameRank - '1'));
        this.file = nameFile - 'a';
    }

    /**
     * @return Number of the file (column) of the square (0-7), 0 corresponds to file a
     */
    public int getFile() {
        return this.file;
    }

    /**
     * @return Number of the rank (row) of the square (0-7)
     */
    public int getRank() {
        return this.rank;
    }

    @Override
    public int hashCode() {
        return rank * 8 + file;
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
        Square other = (Square) obj;
        if (rank != other.rank) {
            return false;
        }
        if (file != other.file) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(2);

        String files = "abcdefgh";
        String ranks = "87654321";

        sb.append(files.charAt(this.file));
        sb.append(ranks.charAt(this.rank));

        return sb.toString();
    }
}
