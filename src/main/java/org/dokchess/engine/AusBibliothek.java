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

class AusBibliothek extends ZugErmitteln {

    private OpeningLibrary bibliothek;

    public AusBibliothek(OpeningLibrary bibliothek,
                         ZugErmitteln nachfolger) {
        super(nachfolger);
        this.bibliothek = bibliothek;
    }

    @Override
    public void ermittelZug(Position stellung, Observer<Move> subject) {
        Move zug = bibliothek.lookUpMove(stellung);
        if (zug != null) {
            subject.onNext(zug);
            subject.onCompleted();
        } else {
            super.ermittelZug(stellung, subject);
        }
    }
}
