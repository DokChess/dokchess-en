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

import static org.dokchess.domain.Colour.WHITE;
import static org.dokchess.domain.PieceType.PAWN;
import static org.dokchess.domain.Squares.e2;
import static org.dokchess.domain.Squares.e4;


public class MoveTest {

    private static final Piece WHITE_PAWN = new Piece(PAWN, WHITE);

    @Test
    public void testEqualsHashcode() {

        Move m1 = new Move(WHITE_PAWN, e2, e4);
        Move m2 = new Move(WHITE_PAWN, e2, e4);

        Assert.assertEquals(m1, m2);
        Assert.assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void testToString() {

        Move m = new Move(WHITE_PAWN, e2, e4);
        String s = m.toString();

        Assert.assertEquals("e2-e4", s);
    }
}