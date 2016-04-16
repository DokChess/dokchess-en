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

package org.dokchess.domain;

import org.junit.Assert;
import org.junit.Test;

import static org.dokchess.domain.Squares.*;

public class MoveTest {

    private static final Piece WEISSER_BAUER = new Piece(PieceType.PAWN,
            Colour.WHITE);

    @Test
    public void testEqualsHashcode() {

        Move z1 = new Move(WEISSER_BAUER, e2, e4);
        Move z2 = new Move(WEISSER_BAUER, e2, e4);

        Assert.assertEquals(z1, z2);
        Assert.assertEquals(z1.hashCode(), z2.hashCode());
    }

    @Test
    public void testToString() {

        Move z1 = new Move(WEISSER_BAUER, e2, e4);
        String s = z1.toString();

        Assert.assertEquals("e2-e4", s);
    }
}