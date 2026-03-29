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
import org.dokchess.opening.OpeningLibrary;
import rx.Observer;

/**
 * Chain link that tries the {@link OpeningLibrary} first; if no book move exists,
 * delegates to the next handler.
 */
class FromLibrary extends DetermineMove {

    private final OpeningLibrary openingLibrary;

    public FromLibrary(OpeningLibrary openingLibrary, DetermineMove next) {
        super(next);
        this.openingLibrary = openingLibrary;
    }

    @Override
    public void determineMove(Position position, Observer<Move> observer) {
        Move move = openingLibrary.lookUpMove(position);
        if (move != null) {
            observer.onNext(move);
            observer.onCompleted();
        } else {
            super.determineMove(position, observer);
        }
    }
}
