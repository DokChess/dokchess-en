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
package org.dokchess.opening.polyglot;

/**
 * Beschreibt moegliche Strategien, nach denen ein Zug in der Eroefffnungsbibliothek
 * ausgewaehlt wird, wenn es mehrere zur Auswahl gibt.
 *
 * @author StefanZ
 */
public enum SelectionMode {

    /**
     * Der erste Zug in der Kandidatenliste wird ausgewaehlt.
     */
    ERSTER,

    /**
     * Der laut Bibliothek am haeufigsten gespielte Zug wird ausgewaehlt.
     */
    HAEUFIGSTER,

    /**
     * Der Zug wird per Zufall ausgewaehlt.
     */
    PER_ZUFALL
}
