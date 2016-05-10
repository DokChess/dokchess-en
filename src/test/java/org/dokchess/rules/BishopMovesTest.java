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

import org.dokchess.domain.Move;
import org.dokchess.domain.Piece;
import org.dokchess.domain.Position;
import org.dokchess.domain.Square;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.dokchess.domain.Colour.WHITE;
import static org.dokchess.domain.PieceType.BISHOP;
import static org.dokchess.domain.Squares.*;

public class BishopMovesTest {

    private static final Piece LAEUFER_WEISS = new Piece(BISHOP,
            WHITE);

    @Test
    public void einzelnerLaeuferInDerEcke() {

        {
            Position weisserLaeuferA1 = new Position("8/8/8/8/8/8/8/B7 w - - 0 1");

            BishopMoves bishopMoves = new BishopMoves();
            List<Move> zuege = new ArrayList<Move>();

            bishopMoves.addMoveCandidates(a1, weisserLaeuferA1, zuege);

            Assert.assertEquals(7, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(a1, zug.getFrom());
            }
            Square[] ziele = {b2, c3, d4, e5, f6, g7, h8};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(LAEUFER_WEISS, a1, ziel);
                Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
            }
        }

        {
            Position weisserLaeuferH1 = new Position("8/8/8/8/8/8/8/7B w - - 0 1");

            BishopMoves bishopMoves = new BishopMoves();
            List<Move> zuege = new ArrayList<Move>();

            bishopMoves.addMoveCandidates(h1, weisserLaeuferH1, zuege);
            Assert.assertEquals(7, zuege.size());
            Square h1 = new Square("h1");
            for (Move zug : zuege) {
                Assert.assertEquals(h1, zug.getFrom());
            }
            Square[] ziele = {a8, b7, c6, d5, e4, f3, g2};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(LAEUFER_WEISS, h1, ziel);
                Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void einzelnerLaeuferInDerMitte() {

        {
            Position weisserLaeuferD4 = new Position("8/8/8/8/3B4/8/8/8 w - - 0 1");

            BishopMoves bishopMoves = new BishopMoves();
            List<Move> zuege = new ArrayList<Move>();

            bishopMoves.addMoveCandidates(d4, weisserLaeuferD4, zuege);

            Assert.assertEquals(13, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(d4, zug.getFrom());
            }
            Square[] ziele = {a1, b2, c3, e5, f6, g7, h8, a7, b6, c5, e3, f2, g1};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(LAEUFER_WEISS, d4, ziel);
                Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void einzelnerLaeuferSchlaegt() {

        {
            Position weisserLaeuferSchlaegtZweiFiguren = new Position("8/6n1/1N6/8/3B1r2/8/5r2/6p1 w - - 0 1");

            BishopMoves bishopMoves = new BishopMoves();
            List<Move> zuege = new ArrayList<Move>();

            bishopMoves.addMoveCandidates(d4,
                    weisserLaeuferSchlaegtZweiFiguren, zuege);

            Assert.assertEquals(9, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(d4, zug.getFrom());
            }

            Move schwarzenTurmSchlagen = new Move(LAEUFER_WEISS, d4, f2, true);
            Move schwarzenSpringerSchlagen = new Move(LAEUFER_WEISS, d4, g7, true);

            Assert.assertTrue(schwarzenTurmSchlagen.toString(),
                    zuege.contains(schwarzenTurmSchlagen));
            Assert.assertTrue(schwarzenSpringerSchlagen.toString(),
                    zuege.contains(schwarzenSpringerSchlagen));
        }
    }
}