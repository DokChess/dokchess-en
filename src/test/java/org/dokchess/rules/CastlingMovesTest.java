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

import static org.dokchess.domain.Squares.e1;
import static org.dokchess.domain.Squares.e8;

public class CastlingMovesTest {

    private static final Piece WEISSER_KOENIG = new Piece(PieceType.KING, Colour.WHITE);
    private static final Piece SCHWARZER_KOENIG = new Piece(PieceType.KING, Colour.BLACK);

    @Test
    public void weisseRochadenErlaubt() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/3p4/3P4/8/PPP2PPP/R3K2R w KQkq - 0 1");

        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e1, stellung, zuege);
        Assert.assertEquals(2, zuege.size());
        for (Move zug : zuege) {
            Assert.assertTrue(zug.getPiece().equals(WEISSER_KOENIG));
            Assert.assertEquals(e1, zug.getFrom());
        }
    }

    @Test
    public void weisseRochadenNichtErlaubt() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/3p4/3P4/8/PPP2PPP/R3K2R w kq - 0 1");

        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e1, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void schwarzeRochadenErlaubt() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/3p4/3P4/8/PPP2PPP/R3K2R b KQkq - 0 1");

        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e8, stellung, zuege);
        Assert.assertEquals(2, zuege.size());
        for (Move zug : zuege) {
            Assert.assertTrue(zug.getPiece().equals(SCHWARZER_KOENIG));
            Assert.assertEquals(e8, zug.getFrom());
        }
    }

    @Test
    public void schwarzeRochadenNichtErlaubt() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/3p4/3P4/8/PPP2PPP/R3K2R b KQ - 0 1");

        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e8, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void felderMuesserFreiSeinBeiWeiss() {
        Position stellung = new Position("rn2kb1r/ppp2ppp/8/3p4/3P4/8/PPP2PPP/RN2KB1R w KQkq - 0 1");
        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e1, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void felderMuesserFreiSeinBeiSchwarz() {
        Position stellung = new Position("rn2kb1r/ppp2ppp/8/3p4/3P4/8/PPP2PPP/RN2KB1R b KQkq - 0 1");
        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e8, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void koenigDarfNichtImSchachStehenBeiWeiss() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/1B1pq3/3P4/8/PPP2PPP/R3K2R w KQkq - 0 1");
        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e1, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void koenigDarfNichtImSchachStehenBeiSchwarz() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/1B1pq3/3P4/8/PPP2PPP/R3K2R b KQkq - 0 1");
        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e8, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void koenigDarfNichtUeberAngriffendeFelderRochierenBeiWeiss() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/1b6/1B1Q1b2/8/PPP2PPP/R3K2R w KQkq - 0 1");
        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e1, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

    @Test
    public void koenigDarfNichtUeberAngriffendeFelderRochierenBeiSchwarz() {
        Position stellung = new Position("r3k2r/ppp2ppp/8/1b6/1B1Q1b2/8/PPP2PPP/R3K2R b KQkq - 0 1");
        CastlingMoves castlingMoves = new CastlingMoves();
        List<Move> zuege = new ArrayList<Move>();

        castlingMoves.addMoveCandidates(e8, stellung, zuege);
        Assert.assertEquals(0, zuege.size());
    }

}
