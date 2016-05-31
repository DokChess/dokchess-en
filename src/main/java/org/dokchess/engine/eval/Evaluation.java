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

package org.dokchess.engine.eval;

import org.dokchess.domain.Colour;
import org.dokchess.domain.Position;

/**
 * Evaluation of a position from a players point of view.
 *
 * @author StefanZ
 */
public interface Evaluation {

    /**
     * Best value possible.
     */
    int BEST = Integer.MAX_VALUE;

    /**
     * Worst value possible
     */
    int WORST = Integer.MIN_VALUE;

    /**
     * value for a balanced poisition.
     */
    int BALANCED = 0;

    /**
     * Returns an evaluation value for the given position from the
     * view of the specified player's color.
     * The higher the better.
     *
     * @param position position to evaluate
     * @param pointOfView player, from which view to evaluate
     * @return evaluation, 0 is balanced, the higher the better for the player
     */
    int evaluatePosition(Position position, Colour pointOfView);
}
