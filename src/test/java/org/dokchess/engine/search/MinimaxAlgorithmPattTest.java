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
import org.dokchess.engine.eval.StandardMaterialEvaluation;
import org.dokchess.rules.DefaultChessRules;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests fuer Patt im Minimax-Algorithmus
 */
public class MinimaxAlgorithmPattTest {

    private MinimaxAlgorithm algorithmus;

    @Before
    public void setup() {
        algorithmus = new MinimaxAlgorithm();
        algorithmus.setEvaluation(new StandardMaterialEvaluation());
        algorithmus.setChessRules(new DefaultChessRules());
    }

    /**
     * Weiss rettet sich in ein Patt.
     * <p/>
     * Quelle des Beispiels: Wikipedia.
     * http://de.wikipedia.org/wiki/Spie%c3%9f_(Schach)
     */
    @Test
    public void abwicklungZumPatt() {
        algorithmus.setDepth(4);

        Position stellung = new Position("1k6/1p5R/8/8/1q6/p7/P7/K7 w - - 0 1");
        Move z = algorithmus.determineBestMove(stellung);

        Assert.assertEquals("R h7xb7", z.toString());
    }

    /**
     * Schwarz am Zug ist patt.
     */
    @Test
    public void keinZugBeiPatt() {
        algorithmus.setDepth(4);

        Position stellung = new Position("k7/8/1KN5/8/8/8/8/8 b - - 0 1");
        Move z = algorithmus.determineBestMove(stellung);

        Assert.assertNull(z);
    }
}
