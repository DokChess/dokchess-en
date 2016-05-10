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

package org.dokchess.textui.xboard;

import org.dokchess.domain.*;

/**
 * Wandelt Zug-Objekte in das Format von XBoard um, und umgekehrt.
 *
 * @author StefanZ
 */
public class ZugParser {

    /**
     * Wandelt einen als Zeichenkette eingegebenen Zug in ein Zug-Objekt um.
     * Die Methode reichert das Zugobjekt mit einigen Informationen an (zum Beispiel, ob eine
     * Figur geschlagen wird), dazu wird die aktuelle Stellung benotigt.
     *
     * @param eingabe  eingelesener Zug
     * @param stellung Stellung, fuer Kontext-Informationen
     * @return Zug-Objekt, oder null, falls kein gueltiger Zug.
     */
    public Move vonXboard(final String eingabe, final Position stellung) {

        Move zug = null;

        if (eingabe.matches("[a-h][1-8][a-h][1-8][qrnb]?")) {

            Square from = new Square(eingabe.substring(0, 2));
            Square to = new Square(eingabe.substring(2, 4));
            boolean schlagen = false;
            PieceType neueFigurenArt = null;
            Piece figur = stellung.getPiece(from);

            if (stellung.getPiece(to) != null) {
                schlagen = true;
            }

            if (figur != null && figur.getType() == PieceType.PAWN
                    && (to.getRank() == 0 || to.getRank() == 7)) {
                char neueFigur = eingabe.charAt(4);
                neueFigurenArt = PieceType.fromLetter(neueFigur);
            }

            zug = new Move(figur, from, to, schlagen, neueFigurenArt);
        }

        return zug;
    }

    /**
     * Wandelt ein Zug-Objekt in das Format fuer das XBoard-Protokoll zurueck.
     *
     * @param zug der umzuwandelnde Zug.
     * @return die entsprechende Zeichenkette.
     */
    public String nachXboard(final Move zug) {

        StringBuilder sb = new StringBuilder();
        sb.append("move ");

        sb.append(zug.getFrom());
        sb.append(zug.getTo());

        if (zug.isPromotion()) {
            char c = zug.getPromotion().getLetter();
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
