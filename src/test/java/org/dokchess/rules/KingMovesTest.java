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

public class KingMovesTest {

    private static final Piece KOENIG_WEISS = new Piece(PieceType.KING,
            Colour.WHITE);

    @Test
    public void koenigInDerEcke() {

        {
            Position weisserKoenigA1 = new Position("8/8/8/8/8/8/8/K7 w - - 0 1");

            KingMoves kingMoves = new KingMoves();
            List<Move> zuege = new ArrayList<Move>();

            kingMoves.addMoveCandidates(a1, weisserKoenigA1, zuege);

            Assert.assertEquals(3, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(a1, zug.getFrom());
            }
            Square[] ziele = {a2, b2, b1};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(KOENIG_WEISS, a1, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }

        {
            Position weisserKoenigH8 = new Position("7K/8/8/8/8/8/8/8 w - - 0 1");

            KingMoves kingMoves = new KingMoves();
            List<Move> zuege = new ArrayList<Move>();

            kingMoves.addMoveCandidates(h8, weisserKoenigH8, zuege);
            Assert.assertEquals(3, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(h8, zug.getFrom());
            }
            Square[] ziele = {g8, g7, h7};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(KOENIG_WEISS, h8, ziel);
                Assert.assertTrue(zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void koenigImZentrum() {

        {
            Position weisserKoenigD5 = new Position("8/8/8/3K4/8/8/8/8 w - - 0 1");

            KingMoves kingMoves = new KingMoves();
            List<Move> zuege = new ArrayList<Move>();

            kingMoves.addMoveCandidates(d5, weisserKoenigD5, zuege);

            Assert.assertEquals(8, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(d5, zug.getFrom());
            }
            Square[] ziele = {c6, c6, e6, c5, e5, c4, d4, e4};
            for (Square ziel : ziele) {
                Move zuTesten = new Move(KOENIG_WEISS, d5, ziel);
                Assert.assertTrue(zuTesten.toString(), zuege.contains(zuTesten));
            }
        }
    }

    @Test
    public void koenigSchlaegt() {

        {
            Position weisserKoenigSchlaegtZweiFiguren = new Position("8/8/4n3/2NKr3/3R4/8/8/8 w - - 0 1");

            KingMoves kingMoves = new KingMoves();
            List<Move> zuege = new ArrayList<Move>();

            kingMoves.addMoveCandidates(d5,
                    weisserKoenigSchlaegtZweiFiguren, zuege);

            Assert.assertEquals(6, zuege.size());
            for (Move zug : zuege) {
                Assert.assertEquals(d5, zug.getFrom());
            }

            Move schlaegtSchwarzenSpringer = new Move(KOENIG_WEISS, d5, e6, true);
            Move schlaegtScvhwarzenTurm = new Move(KOENIG_WEISS, d5, e5, true);

            Assert.assertTrue(schlaegtSchwarzenSpringer.toString(),
                    zuege.contains(schlaegtSchwarzenSpringer));
            Assert.assertTrue(schlaegtScvhwarzenTurm.toString(),
                    zuege.contains(schlaegtScvhwarzenTurm));
        }
    }

}
