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
package org.dokchess.rules;

import org.dokchess.domain.Colour;
import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AufSchachPruefenTest {

    private ChessRules chessRules;

    @Before
    public void setup() {
        chessRules = new DefaultChessRules();
    }

    @Test
    public void weisserBauerGibtSchach() {

        Position stellung1 = new Position("8/5k2/6P1/8/8/8/8/2K5 w - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung1, Colour.BLACK));

        Position stellung2 = new Position("8/5k2/4P3/8/8/8/8/2K5 w - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung2, Colour.BLACK));
    }

    @Test
    public void schwarzerBauerGibtSchach() {

        Position stellung1 = new Position("4k3/8/8/8/8/2p5/3K4/8 b - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung1, Colour.WHITE));

        Position stellung2 = new Position("4k3/8/8/8/8/4p3/3K4/8 b - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung2, Colour.WHITE));
    }

    @Test
    public void weisseDameGibtSchach() {

        // Gerade
        Position stellung1 = new Position("8/5k2/8/8/8/8/8/2K2Q2 w - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung1, Colour.BLACK));

        // Schraeg
        Position stellung2 = new Position("8/5k2/8/8/8/8/Q7/2K5 w - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung2, Colour.BLACK));

        // Schraeg, aber Figur im Weg
        Position stellung3 = new Position("8/5k2/8/8/2R5/8/Q7/2K5 w - - 0 1");
        Assert.assertFalse("nicht im Schach",
                chessRules.isCheck(stellung3, Colour.BLACK));
    }

    @Test
    public void weisserLaeuferGibtSchach() {

        // Schraeg 1
        Position stellung1 = new Position("8/5k2/6B1/8/8/8/8/2K5 w - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung1, Colour.BLACK));

        // Schraeg 2
        Position stellung2 = new Position("8/5k2/8/8/8/8/B7/2K5 w - - 0 1");
        Assert.assertTrue("im Schach",
                chessRules.isCheck(stellung2, Colour.BLACK));

        // Schraeg, aber Figur im Weg
        Position stellung3 = new Position("8/5k2/8/8/2R5/8/B7/2K5 w - - 0 1");
        Assert.assertFalse("nicht im Schach",
                chessRules.isCheck(stellung3, Colour.BLACK));
    }

}
