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

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AuswahlModusTest {

    PolyglotOpeningBook buch = null;

    @Before
    public void buchLaden() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "org/dokchess/opening/polyglot/demoBook.bin");
        buch = new PolyglotOpeningBook(is);
    }


    @Test
    public void haeufigster() {
        buch.setAuswahlModus(AuswahlModus.HAEUFIGSTER);

        Position stellung = new Position();
        List<BookEntry> eintraege = buch.findEntriesByFen(stellung.toString());

        // Zug mit hoechstem Gewicht in der Liste ermitteln
        int maxGewicht = Integer.MIN_VALUE;
        BookEntry zug1 = null;
        for (BookEntry e : eintraege) {
            if (e.getWeightAsInt() > maxGewicht) {
                zug1 = e;
                maxGewicht = e.getWeightAsInt();
            }
        }

        // Pruefen, dass der geliefert wird.
        Move zug2 = buch.lookUpMove(stellung);
        Assert.assertEquals(zug1.getMoveFrom(), zug2.getFrom().toString());
        Assert.assertEquals(zug1.getMoveTo(), zug2.getTo().toString());
    }

    @Test
    public void erster() {
        buch.setAuswahlModus(AuswahlModus.ERSTER);

        Position stellung = new Position();
        List<BookEntry> eintraege = buch.findEntriesByFen(stellung.toString());
        BookEntry zug1 = eintraege.get(0);

        Move zug2 = buch.lookUpMove(stellung);
        Assert.assertEquals(zug1.getMoveFrom(), zug2.getFrom().toString());
        Assert.assertEquals(zug1.getMoveTo(), zug2.getTo().toString());
    }

    @Test
    public void zufaellig() {
        buch.setAuswahlModus(AuswahlModus.PER_ZUFALL);

        Position anfang = new Position();
        Move zug = buch.lookUpMove(anfang);

        Assert.assertNotNull(zug);
    }
}
