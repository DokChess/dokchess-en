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

import org.dokchess.domain.Colour;
import org.dokchess.domain.Move;
import org.dokchess.domain.Position;

import java.util.Collection;

/**
 * The Laws of Chess.
 *
 * @author stefanz
 */
public interface ChessRules {

    /**
     * Returns the starting position of the game. White begins.
     *
     * @return starting position.
     */
    Position getStartingPosition();

    /**
     * Returns the set of all legal moves for a given position. The current player
     * is determined from the position. In case of a mate or stalemate an empty
     * collection is the result. Thus the method never returns null.
     *
     * @param position game situation to examine
     * @return set of legal moves, empty in case of a mate or stalemate.
     */
    Collection<Move> getLegalMoves(Position position);

    /**
     * Checks whether the king of the given colour is attacked by the opponent.
     *
     * @param position game situation to examine
     * @param colour   side of the king which is tested for an attack
     * @return true in case of check
     */
    boolean isCheck(Position position, Colour colour);

    /**
     * Check whether the given position is a mate. I.e. the king of the current player
     * is under attack, and no legal move changes this. The player to move has lost the game.
     *
     * @param position game situation to examine
     * @return true in case of a mate
     */
    boolean isCheckmate(Position position);

    /**
     * Check whether the given position is a stalemate. I.e. the current player has no valid
     * move, but the king is not under attack. The game is considered a draw.
     *
     * @param position game situation to examine
     * @return true in case of a stalemate
     */
    boolean isStalemate(Position position);
}
