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
import org.dokchess.engine.eval.ReineMaterialBewertung;
import org.dokchess.rules.DefaultChessRules;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Einfache Tests fuer den Minimax-Algorithmus
 */
public class MinimaxAlgorithmusTest {

    private MinimaxAlgorithmus algorithmus;

    @Before
    public void setup() {
        algorithmus = new MinimaxAlgorithmus();
        algorithmus.setBewertung(new ReineMaterialBewertung());
        algorithmus.setChessRules(new DefaultChessRules());
    }

    /**
     * Aus der Grundstellung wird ein Zug gefunden ...
     */
    @Test
    public void grundstellung() {
        algorithmus.setTiefe(4);

        Position stellung = new Position();
        Move z = algorithmus.ermittleBestenZug(stellung);
        Assert.assertNotNull(z);
    }
}
