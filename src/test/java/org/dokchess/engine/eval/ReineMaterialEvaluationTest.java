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
import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Test;

/**
 * Sehr einfache Tests fuer die Materialbewertung.
 *
 * @author StefanZ
 */
public class ReineMaterialEvaluationTest {

    @Test
    public void beiGrundStellungKommt0raus() {

        Position stellung = new Position();
        Evaluation evaluation = new ReineMaterialEvaluation();

        int wert = evaluation.evaluatePosition(stellung, stellung.getToMove());
        Assert.assertEquals(Evaluation.BALANCED, wert);
    }

    @Test
    public void dameIstBesserAlsTurm() {

        // Weiss hat eine Dame, schwarz einen Turm.
        Position stellung = new Position("8/Q1K5/8/8/8/6k1/6r1/8 w - - 0 1");
        Evaluation evaluation = new ReineMaterialEvaluation();

        int wert = evaluation.evaluatePosition(stellung, Colour.WHITE);
        Assert.assertTrue(wert > 0);
    }

}
