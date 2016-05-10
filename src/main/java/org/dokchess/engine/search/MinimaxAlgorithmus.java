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

import org.dokchess.domain.Colour;
import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.dokchess.engine.eval.Bewertung;
import org.dokchess.rules.ChessRules;

import java.util.Collection;

public class MinimaxAlgorithmus {

    protected ChessRules chessRules;

    protected Bewertung bewertung;

    private static final int MATT_BEWERTUNG = Bewertung.AM_BESTEN / 2;

    private int tiefe;

    /**
     * Setzt die Bewertungsfunktion, anhand derer die Stellungen bei Erreichen
     * der maximalen Suchtiefe bewertet werden.
     */
    public void setBewertung(Bewertung bewertung) {
        this.bewertung = bewertung;
    }

    /**
     * Setzt eine Implementierung der Spielregeln, anhand erlaubte moegliche
     * Zuege und auch Matt und Patt erkannt werden.
     */
    public void setChessRules(ChessRules chessRules) {
        this.chessRules = chessRules;
    }

    /**
     * Setzt die maximale Suchtiefe in Halbzuegen, das heisst bei 4 zieht jeder
     * Spieler zweimal.
     *
     * @param tiefe Suchtiefe in Halbzuegen
     */
    public void setTiefe(int tiefe) {
        this.tiefe = tiefe;
    }

    public Move ermittleBestenZug(Position stellung) {

        Colour spielerFarbe = stellung.getToMove();
        Collection<Move> zuege = chessRules.getLegalMoves(stellung);

        int besterWert = Bewertung.AM_SCHLECHTESTEN;
        Move besterZug = null;

        for (Move zug : zuege) {
            Position neueStellung = stellung.performMove(zug);

            int wert = bewerteStellungRekursiv(neueStellung, spielerFarbe);

            if (wert > besterWert) {
                besterWert = wert;
                besterZug = zug;
            }
        }

        return besterZug;
    }


    protected int bewerteStellungRekursiv(Position stellung, Colour spielerFarbe) {
        return bewerteStellungRekursiv(stellung, 1, spielerFarbe);
    }


    protected int bewerteStellungRekursiv(Position stellung, int aktuelleTiefe,
                                          Colour spielerFarbe) {

        if (aktuelleTiefe == tiefe) {
            return bewertung.bewerteStellung(stellung, spielerFarbe);
        } else {
            Collection<Move> zuege = chessRules.getLegalMoves(stellung);
            if (zuege.isEmpty()) {

                // PATT
                if (!chessRules
                        .isCheck(stellung, stellung.getToMove())) {
                    return Bewertung.AUSGEGLICHEN;
                }

                // MATT
                // Tiefe mit einrechnen, um fruehes Matt zu bevorzugen
                if (stellung.getToMove() == spielerFarbe) {
                    return -(MATT_BEWERTUNG - aktuelleTiefe);
                } else {
                    return MATT_BEWERTUNG - aktuelleTiefe;
                }

            } else {
                if (aktuelleTiefe % 2 == 0) {
                    // Max
                    int max = Bewertung.AM_SCHLECHTESTEN;
                    for (Move zug : zuege) {
                        Position neueStellung = stellung.performMove(zug);
                        int wert = bewerteStellungRekursiv(neueStellung,
                                aktuelleTiefe + 1, spielerFarbe);
                        if (wert > max) {
                            max = wert;
                        }
                    }
                    return max;
                } else {
                    // Min
                    int min = Bewertung.AM_BESTEN;
                    for (Move zug : zuege) {
                        Position neueStellung = stellung.performMove(zug);
                        int wert = bewerteStellungRekursiv(neueStellung,
                                aktuelleTiefe + 1, spielerFarbe);
                        if (wert < min) {
                            min = wert;
                        }
                    }
                    return min;
                }
            }
        }
    }


}
