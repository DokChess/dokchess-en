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
package org.dokchess.engine;

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import rx.Observable;

/**
 * Core API of the engine subsystem. The engine chooses moves from a game position;
 * that position can be supplied from outside. The engine is stateful and plays
 * one game at a time.
 *
 * @author StefanZ
 */
public interface Engine {

    /**
     * Sets the state of the engine to the specified position.
     * If currently a move calculation is running, this will be canceled.
     *
     * @param position the new position
     */
    void setupPieces(Position position);

    /**
     * Starts move selection for the current position. The method is non-blocking:
     * the engine may compute in the background. Better moves are reported via
     * {@code onNext} on the returned {@link Observable}; the end of the search
     * is signaled with {@code onCompleted}. The engine does not apply moves to
     * its internal position; callers use {@link #performMove(Move)} for that.
     *
     * @return observable stream of the engine's chosen move (and possibly
     *         intermediate improvements)
     */
    Observable<Move> determineYourMove();

    /**
     * Applies the given move to the engine's internal game state. If a move
     * search is in progress, it is cancelled.
     *
     * @param move the move to play
     */
    void performMove(Move move);

    /**
     * Closes the engine. The method makes it possible to free resources.
     * No move calculations are allowed afterwards.
     */
    void close();
}
