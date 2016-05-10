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
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.dokchess.domain.Colour.BLACK;
import static org.dokchess.domain.Colour.WHITE;
import static org.dokchess.domain.PieceType.PAWN;
import static org.dokchess.domain.Squares.*;

public class BauernZuegeTest {

    private static final Piece BAUER_WEISS = new Piece(PAWN,
            WHITE);

    private static final Piece BAUER_SCHWARZ = new Piece(PAWN,
            BLACK);

    @Test
    public void einzelnerWeisserBauerAmStart() {

        Position einzelnerWeisserBauerAmStart = new Position(
                "8/8/8/8/8/8/1P6/8 w - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(b2, einzelnerWeisserBauerAmStart,
                zuege);

        Assert.assertEquals(2, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(b2, zug.getFrom());
        }

        Assert.assertEquals(2, zuege.size());
        Move einFeld = new Move(BAUER_WEISS, b2, b3);
        Move zweiFelder = new Move(BAUER_WEISS, b2, b4);

        Assert.assertTrue(einFeld.toString(), zuege.contains(einFeld));
        Assert.assertTrue(zweiFelder.toString(), zuege.contains(zweiFelder));
    }

    @Test
    public void einzelnerWeisserBauerInDerMitte() {

        Position einzelnerWeisserBauerInDerMitte = new Position(
                "8/8/8/4P3/8/8/8/8 w - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e5,
                einzelnerWeisserBauerInDerMitte, zuege);

        Assert.assertEquals(1, zuege.size());
        Move einFeld = new Move(BAUER_WEISS, e5, e6);
        Assert.assertTrue(einFeld.toString(), zuege.contains(einFeld));
    }

    @Test
    public void einzelnerWeisserBauerKannSchlagen() {

        Position einzelnerWeisserBauerKannSchlagen = new Position(
                "8/8/8/2N1n3/3P4/8/8/8 w - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(d4,
                einzelnerWeisserBauerKannSchlagen, zuege);

        Assert.assertEquals(2, zuege.size());

        Move einFeld = new Move(BAUER_WEISS, d4, d5);
        Assert.assertTrue(einFeld.toString(), zuege.contains(einFeld));

        Move schlagen = new Move(BAUER_WEISS, d4, e5, true);
        Assert.assertTrue(einFeld.toString(), zuege.contains(schlagen));
    }

    @Test
    public void einzelnerWeisserBauerUmwandlung() {

        Position einzelnerWeisserBauerUmwandlung = new Position(
                "5n2/4P3/8/8/8/8/8/8 w - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e7,
                einzelnerWeisserBauerUmwandlung, zuege);

        Assert.assertEquals(8, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(e7, zug.getFrom());
            Assert.assertTrue(zug.isPromotion());
        }
    }

    @Test
    public void keineUmwandlungWennGrundlinieBelegtWeiss() {

        Position einzelnerWeisserBauerUmwandlung = new Position(
                "4n3/4P3/8/8/8/8/8/8 w - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e7,
                einzelnerWeisserBauerUmwandlung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void keineUmwandlungWennGrundlinieBelegtSchwarz() {

        Position einzelnerWeisserBauerUmwandlung = new Position(
                "8/8/8/8/8/8/4p3/4N3 b - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e2,
                einzelnerWeisserBauerUmwandlung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void einzelnerSchwarzerBauerAmStart() {

        Position einzelnerSchwarzerBauerAmStart = new Position(
                "8/4p3/8/8/8/8/8/8 b - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e7, einzelnerSchwarzerBauerAmStart,
                zuege);

        Assert.assertEquals(2, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(e7, zug.getFrom());
        }

        Move einFeld = new Move(BAUER_SCHWARZ, e7, e6);
        Move zweiFelder = new Move(BAUER_SCHWARZ, e7, e5);

        Assert.assertTrue(einFeld.toString(), zuege.contains(einFeld));
        Assert.assertTrue(zweiFelder.toString(), zuege.contains(zweiFelder));
    }

    @Test
    public void weisserBauerBlockiert() {

        Position weisserBauerBlockiert = new Position(
                "8/8/8/8/4b3/1b6/1P2P3/8 w - - 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e2, weisserBauerBlockiert, zuege);

        Assert.assertEquals(1, zuege.size());
        for (Move zug : zuege) {
            Assert.assertEquals(e2, zug.getFrom());
        }

        Move einFeld = new Move(BAUER_WEISS, e2, e3);

        Assert.assertTrue(einFeld.toString(), zuege.contains(einFeld));
    }

    @Test
    public void schwarzerBauerEnPassant() {

        Position schwarzerBauerEnPassant = new Position(
                "4k3/8/8/8/4pPp1/8/8/4K3 b - f3 1 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(e4, schwarzerBauerEnPassant, zuege);
        bauernzuege.addMoveCandidates(g4, schwarzerBauerEnPassant, zuege);

        Assert.assertEquals(4, zuege.size());

        Move schlag1 = new Move(BAUER_SCHWARZ, e4, f3, true);
        Move schlag2 = new Move(BAUER_SCHWARZ, g4, f3, true);

        Assert.assertTrue(zuege.contains(schlag1));
        Assert.assertTrue(zuege.contains(schlag2));
    }

    @Test
    public void weisserBauerEnPassant() {

        Position weisserBauerEnPassant = new Position(
                "4k3/8/8/3PpP2/8/8/8/4K3 w - e6 0 1");

        PawnMoves bauernzuege = new PawnMoves();
        List<Move> zuege = new ArrayList<Move>();

        bauernzuege.addMoveCandidates(d5, weisserBauerEnPassant, zuege);
        bauernzuege.addMoveCandidates(f5, weisserBauerEnPassant, zuege);

        Assert.assertEquals(4, zuege.size());

        Move schlag1 = new Move(BAUER_WEISS, d5, e6, true);
        Move schlag2 = new Move(BAUER_WEISS, f5, e6, true);

        Assert.assertTrue(zuege.contains(schlag1));
        Assert.assertTrue(zuege.contains(schlag2));
    }

}
