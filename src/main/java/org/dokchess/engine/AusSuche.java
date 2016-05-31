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
import org.dokchess.engine.search.Search;
import rx.Observer;

class AusSuche extends ZugErmitteln {

    private Search search;

    public AusSuche(final Search search) {
        this(search, null);
    }

    public AusSuche(final Search search,
                    final ZugErmitteln nachfolger) {
        super(nachfolger);
        this.search = search;
    }

    @Override
    public void ermittelZug(Position stellung, Observer<Move> subject) {
        search.searchMove(stellung, subject);
        super.ermittelZug(stellung, subject);
    }


}
