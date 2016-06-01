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

package org.dokchess.engine.search;

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import rx.Observer;

/**
 * Decribes an (asynchronous) search for moves.
 *
 * @author StefanZ
 */
public interface Search {

    /**
     * Starts a search for a move on the specified position.
     * Returns gradually better moves as events on the passed observer.
     * The end of the search (no better move found) is also signaled to the observer.
     *
     * @param position position where the search starts
     * @param observer observer for notifications
     */
    void searchMove(Position position, Observer<Move> observer);

    /**
     * Cancels the current search.
     */
    void cancelSearch();

    /**
     * Closes the search completely.
     * No moves may be determined after calling this method.
     */
    void close();
}