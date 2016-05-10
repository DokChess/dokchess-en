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

/**
 * Created by stefanz on 02.01.15.
 */
public class BewerteterZug implements Comparable<BewerteterZug> {

    private Move zug;

    private int wert;

    public BewerteterZug(Move zug, int wert) {
        this.zug = zug;
        this.wert = wert;
    }

    public Move getZug() {
        return zug;
    }

    public int getWert() {
        return wert;
    }

    @Override
    public int compareTo(BewerteterZug andererZug) {
        return this.wert - andererZug.getWert();
    }
}
