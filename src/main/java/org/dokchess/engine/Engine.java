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
 * Zentrale Schnittstelle des Engine-Subsystems. Ermittlung des n&auml;chsten
 * Zuges, ausgehend von einer Spielsituation. Diese Situation kann von aussen
 * vorgegeben werden. Die Engine ist zustandsbehaftet und spielt stets eine
 * Partie zur gleichen Zeit.
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
     * Starts the determination of a move for the current game situation.
     * Returns move candidates asynchronously via an Observable.
     * The engine does not perform the moves.
     *
     * Als Ergebnis wird ein Observable zur&uuml;ckgeliefert,
     * d.h. die Methode blockiert nicht, die Engine rechnet ggf. im Hintergrund.
     * Neue beste Z&uuml;ge werden &uuml;ber onNext() gemeldet, das Ende der Berechnung mit onComplete.
     *
     * @return Observable, ueber das der beste Zug aus Sicht der Engine &uuml;bermittelt wird, sowie Zwischenergebnisse.
     */
    Observable<Move> determineYourMove();

    /**
     * F&uuml;hrt den angegebenen Zug aus, d.h. &auml;ndert den Zustand der
     * Engine. Falls aktuell eine Zugermittlung l&auml;uft, wird diese abgebrochen.
     *
     * @param zug der auszuf&uuml;rende Zug.
     */
    void performMove(Move zug);

    /**
     * Closes the engine. The method makes it possible to free resources.
     * No move calculations are allowed afterwards.
     */
    void close();
}
