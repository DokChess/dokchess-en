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
 * Tests fuer Matt im Minimax-Algorithmus
 */
public class MinimaxAlgorithmMattTest {

    private MinimaxAlgorithm algorithmus;

    @Before
    public void setup() {
        algorithmus = new MinimaxAlgorithm();
        algorithmus.setEvaluation(new StandardMaterialEvaluation());
        algorithmus.setChessRules(new DefaultChessRules());
    }

    /**
     * Matt in einem Zug (Schaefermatt). Siegzug fuer weiss: Dame h5xf7
     */
    @Test
    public void schaeferMatt() {

        algorithmus.setDepth(2);

        Position stellung = new Position(
                "r1bqkb1r/pppp1ppp/2n2n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 1");
        Move z = algorithmus.determineBestMove(stellung);

        Assert.assertEquals("Q h5xf7", z.toString());
    }
}
