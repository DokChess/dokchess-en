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
import rx.Observer;

/**
 * Handler role in a chain-of-responsibility for choosing a move: each link may
 * produce a move or delegate to the {@link #next} handler.
 *
 * @author StefanZ
 */
abstract class DetermineMove {

    private final DetermineMove next;

    public DetermineMove(DetermineMove next) {
        this.next = next;
    }

    /**
     * Attempts to determine a move for {@code position}, notifying {@code observer}.
     * The default implementation forwards to the next handler in the chain.
     *
     * @param position current game position
     * @param observer receiver for the chosen move (RxJava observer)
     */
    public void determineMove(Position position, Observer<Move> observer) {
        if (next != null) {
            next.determineMove(position, observer);
        }
    }

    /**
     * Cancels any ongoing move search along this chain (e.g. when the position
     * changes or the engine is closed).
     */
    public void cancelCurrentSearch() {
        if (next != null) {
            next.cancelCurrentSearch();
        }
    }
}
