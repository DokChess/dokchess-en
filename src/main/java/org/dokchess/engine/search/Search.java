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
 * Beischreibt eine (asynchrone) Suche nach Z&uuml;gen.
 *
 * @author StefanZ
 */
public interface Search {

    /**
     * Startet eine Suche nach einem Zug f&uuml;r die angegebene Stellung.
     * Liefert nach und nach bessere Z&uuml;ge als Ereignisse an den &uuml;bergebenen
     * Observer.
     * Das Ende der Suche (keinen besseren Zug mehr gefunden) wird ebenfalls
     * an den Observer signalisiert.
     *
     * @param position Stellung, auf der die Suche aufsetzt.
     * @param observer Observer, an den Suchergebnisse zu melden sind.
     */
    void searchMove(Position position, Observer<Move> observer);

    /**
     * Bricht die aktuelle Suche ab.
     */
    void cancelSearch();

    /**
     * Schlie&szlig;t die Suche vollst&auml;ndig.
     * Anschlie&szlig;end d&uuml;rfen keine Z&uuml;ge mehr damit
     * ermittelt werden.
     */
    void close();
}