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

public class PieceTest {

    @Test
    public void toStringTest() {

        Piece whitePawn = new Piece(PieceType.KING.PAWN, Colour.WHITE);
        Assert.assertEquals("WHITE PAWN", whitePawn.toString());

        Piece blackQueen = new Piece(PieceType.QUEEN, Colour.BLACK);
        Assert.assertEquals("BLACK QUEEN", blackQueen.toString());
    }
}
