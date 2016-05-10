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
package org.dokchess.engine.eval;

import org.dokchess.domain.Colour;
import org.dokchess.domain.Piece;
import org.dokchess.domain.Position;

/**
 * Bewertung ausschliesslich anhand des Figurenwertes.
 * <p/>
 * Jede Figurenart enth&auml;lt einen Wert (Bauer 1, Springer 3, ..., Dame 9),
 * die Figuren auf dem Brett werden entsprechend aufsummiert. Eigene Figuren
 * z&auml;hlen positiv, gegnerische negativ. Entsprechend ist bei ausgeglichenem
 * Material das Ergebnis 0, verliert man z.B. eine Dame, sinkt der Wert um 9. Es
 * spielt also keine Rolle, wo die Figur steht.
 *
 * @author StefanZ
 */
public class ReineMaterialEvaluation implements Evaluation {

    @Override
    public int evaluatePosition(Position position, Colour pointOfView) {
        int summe = 0;

        for (int reihe = 0; reihe < 8; ++reihe) {
            for (int linie = 0; linie < 8; ++linie) {
                Piece figur = position.getPiece(reihe, linie);
                if (figur != null) {
                    double wert = figurenWert(figur);
                    if (figur.getColour() == pointOfView) {
                        summe += wert;
                    } else {
                        summe -= wert;
                    }
                }
            }
        }

        return summe;
    }

    /**
     * Materialwert der Figur.
     */
    protected int figurenWert(final Piece f) {
        switch (f.getType()) {
            case PAWN:
                return 1;
            case KNIGHT:
            case BISHOP:
                return 3;
            case ROOK:
                return 5;
            case QUEEN:
                return 9;
            default:
                return 0;
        }
    }
}
