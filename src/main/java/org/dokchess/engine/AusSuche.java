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
import org.dokchess.engine.search.Suche;
import rx.Observer;

class AusSuche extends ZugErmitteln {

    private Suche suche;

    public AusSuche(final Suche suche) {
        this(suche, null);
    }

    public AusSuche(final Suche suche,
                    final ZugErmitteln nachfolger) {
        super(nachfolger);
        this.suche = suche;
    }

    @Override
    public void ermittelZug(Position stellung, Observer<Move> subject) {
        suche.zugSuchen(stellung, subject);
        super.ermittelZug(stellung, subject);
    }


}
