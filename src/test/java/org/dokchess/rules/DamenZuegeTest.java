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

public class DamenZuegeTest {

    private static final Piece DAME_WEISS = new Piece(PieceType.QUEEN,
            Colour.WHITE);

    @Test
    public void dameInDerEcke() {

        {
            Position weisseDameA1 = new Position("8/8/8/8/8/8/8/Q7 w - - 0 1");

            QueenMoves queenMoves = new QueenMoves();
            List<Move> zuege = new ArrayList<Move>();

            queenMoves.addMoveCandidates(a1, weisseDameA1, zuege);

            Assert.assertEquals(21, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(a1, zug.getFrom());
            }
            Square[] ziele = {b1, c1, d1, e1, f1, g1, h1, a2, a3, a4, a5, a6,
                    a7, a8, b2, c3, d4, e5, f6, g7, h8};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(DAME_WEISS, a1, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }

        {
            Position weisseDameH8 = new Position("7Q/8/8/8/8/8/8/8 w - - 0 1");

            QueenMoves queenMoves = new QueenMoves();
            List<Move> zuege = new ArrayList<Move>();

            queenMoves.addMoveCandidates(h8, weisseDameH8, zuege);
            Assert.assertEquals(21, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(h8, zug.getFrom());
            }
            Square[] ziele = {h1, h2, h3, h4, h5, h6, h7, a8, b8, c8, d8, e8,
                    f8, g8, a1, b2, c3, d4, e5, f6, g7};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(DAME_WEISS, h8, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void dameImZentrum() {

        Position weisseDameD5 = new Position("8/8/8/3Q4/8/8/8/8 w - - 0 1");

        QueenMoves queenMoves = new QueenMoves();
        List<Move> zuege = new ArrayList<Move>();

        queenMoves.addMoveCandidates(d5, weisseDameD5, zuege);

        Assert.assertEquals(27, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(d5, zug.getFrom());
        }
        Square[] ziele = {a5, b5, c5, e5, f5, g5, h5, d1, d2, d3, d4, d6, d7,
                d8, a2, b3, c4, e5, f7, g8};
        for (Square ziel : ziele) {
            Move zuTesten = new Move(DAME_WEISS, d5, ziel);
            Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
        }
    }

    @Test
    public void dameSchlaegt() {

        Position weisseDameSchlaegtZweiFiguren = new Position(
                "8/8/n2N4/8/8/3Q1pb1/8/1B6 w - - 0 1");

        QueenMoves queenMoves = new QueenMoves();
        List<Move> zuege = new ArrayList<Move>();

        queenMoves.addMoveCandidates(d3, weisseDameSchlaegtZweiFiguren,
                zuege);

        Assert.assertEquals(19, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(d3, zug.getFrom());
        }

        Move schlaegtSchwarzenSpringer = new Move(DAME_WEISS, d3, a6, true);
        Move schlaegtSchwarzenBauern = new Move(DAME_WEISS, d3, f3, true);

        Assert.assertTrue(schlaegtSchwarzenSpringer.toString(),
                zuege.contains(schlaegtSchwarzenSpringer));
        Assert.assertTrue(schlaegtSchwarzenBauern.toString(),
                zuege.contains(schlaegtSchwarzenBauern));
    }
}
