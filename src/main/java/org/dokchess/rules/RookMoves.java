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

import org.dokchess.domain.Position;
import org.dokchess.domain.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Moves of the rook.
 *
 * @author stefanz
 */
class RookMoves extends ComplexMovement {

    @Override
    protected List<Square> getReachableSquares(Position position, Square from) {
        List<Square> squares = new ArrayList<Square>();

        fuegeFelderInRichtungHinzuFallsErreichbar(position, from, 0, 1, squares);
        fuegeFelderInRichtungHinzuFallsErreichbar(position, from, 1, 0, squares);
        fuegeFelderInRichtungHinzuFallsErreichbar(position, from, 0, -1, squares);
        fuegeFelderInRichtungHinzuFallsErreichbar(position, from, -1, 0, squares);

        return squares;
    }

}
