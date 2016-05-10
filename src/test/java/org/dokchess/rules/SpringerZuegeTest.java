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

import org.dokchess.domain.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.dokchess.domain.Squares.*;

public class SpringerZuegeTest {

    private static final Piece SPRINGER_WEISS = new Piece(PieceType.KNIGHT,
            Colour.WHITE);

    @Test
    public void springerRad() {
        Position weisserSpringerD5 = new Position("8/8/8/3N4/8/8/8/8 w - - 0 1");

        KnightMoves knightMoves = new KnightMoves();
        List<Move> zuege = new ArrayList<Move>();

        knightMoves.addMoveCandidates(d5, weisserSpringerD5, zuege);

        System.out.println(zuege);

        Assert.assertEquals(8, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(d5, zug.getFrom());
        }
        Square[] ziele = {c7, e7, b6, f6, b4, f4, c3, e3};
        for (Square ziel : ziele) {
            Move zuTesten = new Move(SPRINGER_WEISS, d5, ziel);
            Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
        }
    }

    @Test
    public void einzelnerSpringerInDerEcke() {

        {
            Position weisserSpringerA1 = new Position(
                    "8/8/8/8/8/8/8/N7 w - - 0 1");

            KnightMoves knightMoves = new KnightMoves();
            List<Move> zuege = new ArrayList<Move>();

            knightMoves.addMoveCandidates(a1, weisserSpringerA1, zuege);

            Assert.assertEquals(2, zuege.size());

            for (Move zug : zuege) {
                Assert.assertEquals(a1, zug.getFrom());
            }
            Square[] ziele = {b3, c2};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(SPRINGER_WEISS, a1, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }

        {
            Position weisserSpringerH8 = new Position(
                    "7N/8/8/8/8/8/8/8 w - - 0 1");

            KnightMoves knightMoves = new KnightMoves();
            List<Move> zuege = new ArrayList<Move>();

            knightMoves.addMoveCandidates(h8, weisserSpringerH8, zuege);
            Assert.assertEquals(2, zuege.size());

            for (Move zug : zuege) {
                Assert.assertEquals(h8, zug.getFrom());
            }
            Square[] ziele = {g6, f7};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(SPRINGER_WEISS, h8, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void einzelnerSpringerSchlaegt() {
        Position weisserSpringKannZweiSchlagen = new Position(
                "8/8/5B2/3N4/1n6/4r3/8/8 w - - 0 1");

        KnightMoves knightMoves = new KnightMoves();
        List<Move> zuege = new ArrayList<Move>();

        knightMoves.addMoveCandidates(d5,
                weisserSpringKannZweiSchlagen, zuege);

        Assert.assertEquals(7, zuege.size());

        for (Move zug : zuege) {
            Assert.assertEquals(d5, zug.getFrom());
        }

        Move schwarzenSpringerSchlagen = new Move(SPRINGER_WEISS, d5, b4, true);
        Move schwarzenTurmSchlagen = new Move(SPRINGER_WEISS, d5, e3, true);

        Assert.assertTrue(schwarzenSpringerSchlagen.toString(),
                zuege.contains(schwarzenSpringerSchlagen));
        Assert.assertTrue(schwarzenTurmSchlagen.toString(),
                zuege.contains(schwarzenTurmSchlagen));
    }

}
