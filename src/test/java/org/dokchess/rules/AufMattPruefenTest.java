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

import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AufMattPruefenTest {

    private ChessRules chessRules;

    @Before
    public void setup() {
        chessRules = new DefaultChessRules();
    }

    /**
     * Die Anfangsstellung ist kein Matt.
     */
    @Test
    public void anfangsstellung() {
        Position anfangsstellung = new Position();
        Assert.assertFalse("Anfangsstellung",
                chessRules.isCheckmate(anfangsstellung));
    }


    /**
     * Schaefermatt ist matt.
     */
    @Test
    public void schaeferMatt() {
        Position stellung = new Position("r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");
        Assert.assertTrue("Schaefermatt",
                chessRules.isCheckmate(stellung));
    }

}
