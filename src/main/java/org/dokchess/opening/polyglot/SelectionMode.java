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
package org.dokchess.opening.polyglot;

/**
 * Strategies for choosing a move from the opening book when several candidates exist.
 *
 * @author StefanZ
 */
public enum SelectionMode {

    /**
     * Pick the first move in the candidate list (book order).
     */
    FIRST,

    /**
     * Pick the move with the highest weight in the book (most played).
     */
    MOST_PLAYED,

    /**
     * Pick a move uniformly at random from the candidates.
     */
    RANDOM
}
