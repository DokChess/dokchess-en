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

/**
 * {@link PawnMoves}: single and double advance, captures, promotion, blocked paths, en passant.
 */
public class PawnMovesTest {

    private static final Piece WHITE_PAWN = new Piece(PAWN,
            WHITE);

    private static final Piece BLACK_PAWN = new Piece(PAWN,
            BLACK);

    @Test
    public void singleWhitePawnAtStart() {

        Position position = new Position(
                "8/8/8/8/8/8/1P6/8 w - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(b2, position,
                moves);

        Assert.assertEquals(2, moves.size());
        for (Move move : moves) {
            Assert.assertEquals(b2, move.getFrom());
        }

        Assert.assertEquals(2, moves.size());
        Move oneSquare = new Move(WHITE_PAWN, b2, b3);
        Move twoSquares = new Move(WHITE_PAWN, b2, b4);

        Assert.assertTrue(oneSquare.toString(), moves.contains(oneSquare));
        Assert.assertTrue(twoSquares.toString(), moves.contains(twoSquares));
    }

    @Test
    public void singleWhitePawnInMiddle() {

        Position position = new Position(
                "8/8/8/4P3/8/8/8/8 w - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e5,
                position, moves);

        Assert.assertEquals(1, moves.size());
        Move oneSquare = new Move(WHITE_PAWN, e5, e6);
        Assert.assertTrue(oneSquare.toString(), moves.contains(oneSquare));
    }

    @Test
    public void singleWhitePawnCanCapture() {

        Position position = new Position(
                "8/8/8/2N1n3/3P4/8/8/8 w - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(d4,
                position, moves);

        Assert.assertEquals(2, moves.size());

        Move oneSquare = new Move(WHITE_PAWN, d4, d5);
        Assert.assertTrue(oneSquare.toString(), moves.contains(oneSquare));

        Move captureKnight = new Move(WHITE_PAWN, d4, e5, true);
        Assert.assertTrue(captureKnight.toString(), moves.contains(captureKnight));
    }

    @Test
    public void singleWhitePawnPromotion() {

        Position position = new Position(
                "5n2/4P3/8/8/8/8/8/8 w - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e7,
                position, moves);

        Assert.assertEquals(8, moves.size());
        for (Move move : moves) {
            Assert.assertEquals(e7, move.getFrom());
            Assert.assertTrue(move.isPromotion());
        }
    }

    @Test
    public void noPromotionIfBackRankOccupiedWhite() {

        Position position = new Position(
                "4n3/4P3/8/8/8/8/8/8 w - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e7,
                position, moves);
        Assert.assertEquals(0, moves.size());
    }

    @Test
    public void noPromotionIfBackRankOccupiedBlack() {

        Position position = new Position(
                "8/8/8/8/8/8/4p3/4N3 b - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e2,
                position, moves);
        Assert.assertEquals(0, moves.size());
    }

    @Test
    public void singleBlackPawnAtStart() {

        Position position = new Position(
                "8/4p3/8/8/8/8/8/8 b - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e7, position,
                moves);

        Assert.assertEquals(2, moves.size());
        for (Move move : moves) {
            Assert.assertEquals(e7, move.getFrom());
        }

        Move oneSquare = new Move(BLACK_PAWN, e7, e6);
        Move twoSquares = new Move(BLACK_PAWN, e7, e5);

        Assert.assertTrue(oneSquare.toString(), moves.contains(oneSquare));
        Assert.assertTrue(twoSquares.toString(), moves.contains(twoSquares));
    }

    @Test
    public void whitePawnBlocked() {

        Position position = new Position(
                "8/8/8/8/4b3/1b6/1P2P3/8 w - - 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e2, position, moves);

        Assert.assertEquals(1, moves.size());
        for (Move move : moves) {
            Assert.assertEquals(e2, move.getFrom());
        }

        Move oneSquare = new Move(WHITE_PAWN, e2, e3);

        Assert.assertTrue(oneSquare.toString(), moves.contains(oneSquare));
    }

    @Test
    public void blackPawnEnPassant() {

        Position position = new Position(
                "4k3/8/8/8/4pPp1/8/8/4K3 b - f3 1 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(e4, position, moves);
        pawnMoves.addMoveCandidates(g4, position, moves);

        Assert.assertEquals(4, moves.size());

        Move captureFromE4 = new Move(BLACK_PAWN, e4, f3, true);
        Move captureFromG4 = new Move(BLACK_PAWN, g4, f3, true);

        Assert.assertTrue(moves.contains(captureFromE4));
        Assert.assertTrue(moves.contains(captureFromG4));
    }

    @Test
    public void whitePawnEnPassant() {

        Position position = new Position(
                "4k3/8/8/3PpP2/8/8/8/4K3 w - e6 0 1");

        PawnMoves pawnMoves = new PawnMoves();
        List<Move> moves = new ArrayList<Move>();

        pawnMoves.addMoveCandidates(d5, position, moves);
        pawnMoves.addMoveCandidates(f5, position, moves);

        Assert.assertEquals(4, moves.size());

        Move captureFromD5 = new Move(WHITE_PAWN, d5, e6, true);
        Move captureFromF5 = new Move(WHITE_PAWN, f5, e6, true);

        Assert.assertTrue(moves.contains(captureFromD5));
        Assert.assertTrue(moves.contains(captureFromF5));
    }

}
