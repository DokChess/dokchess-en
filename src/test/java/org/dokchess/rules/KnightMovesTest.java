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

import org.dokchess.domain.Colour;
import org.dokchess.domain.Move;
import org.dokchess.domain.Piece;
import org.dokchess.domain.PieceType;
import org.dokchess.domain.Position;
import org.dokchess.domain.Square;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.dokchess.domain.Squares.*;

/**
 * Tests for {@link KnightMoves}.
 */
public class KnightMovesTest {

    private static final Piece WHITE_KNIGHT = new Piece(PieceType.KNIGHT,
            Colour.WHITE);

    @Test
    public void knightMovesFromD5() {
        Position whiteKnightAtD5 = new Position("8/8/8/3N4/8/8/8/8 w - - 0 1");

        KnightMoves knightMoves = new KnightMoves();
        List<Move> moves = new ArrayList<Move>();

        knightMoves.addMoveCandidates(d5, whiteKnightAtD5, moves);

        Assert.assertEquals(8, moves.size());
        for (Move move : moves) {
            Assert.assertEquals(d5, move.getFrom());
        }

        Square[] targets = {c7, e7, b6, f6, b4, f4, c3, e3};
        for (Square target : targets) {
            Move moveToTest = new Move(WHITE_KNIGHT, d5, target);
            Assert.assertTrue(moveToTest.toString(), moves.contains(moveToTest));
        }
    }

    @Test
    public void singleKnightInCorner() {

        {
            Position whiteKnightAtA1 = new Position("8/8/8/8/8/8/8/N7 w - - 0 1");

            KnightMoves knightMoves = new KnightMoves();
            List<Move> moves = new ArrayList<Move>();

            knightMoves.addMoveCandidates(a1, whiteKnightAtA1, moves);

            Assert.assertEquals(2, moves.size());
            for (Move move : moves) {
                Assert.assertEquals(a1, move.getFrom());
            }

            Square[] targets = {b3, c2};
            for (Square target : targets) {
                Move moveToTest = new Move(WHITE_KNIGHT, a1, target);
                Assert.assertTrue(moves.contains(moveToTest));
            }
        }

        {
            Position whiteKnightAtH8 = new Position("7N/8/8/8/8/8/8/8 w - - 0 1");

            KnightMoves knightMoves = new KnightMoves();
            List<Move> moves = new ArrayList<Move>();

            knightMoves.addMoveCandidates(h8, whiteKnightAtH8, moves);
            Assert.assertEquals(2, moves.size());

            for (Move move : moves) {
                Assert.assertEquals(h8, move.getFrom());
            }

            Square[] targets = {g6, f7};
            for (Square target : targets) {
                Move moveToTest = new Move(WHITE_KNIGHT, h8, target);
                Assert.assertTrue(moves.contains(moveToTest));
            }
        }
    }

    @Test
    public void singleKnightCanCapture() {
        Position whiteKnightWithCaptures = new Position(
                "8/8/5B2/3N4/1n6/4r3/8/8 w - - 0 1");

        KnightMoves knightMoves = new KnightMoves();
        List<Move> moves = new ArrayList<Move>();

        knightMoves.addMoveCandidates(d5, whiteKnightWithCaptures, moves);

        Assert.assertEquals(7, moves.size());
        for (Move move : moves) {
            Assert.assertEquals(d5, move.getFrom());
        }

        Move captureKnight = new Move(WHITE_KNIGHT, d5, b4, true);
        Move captureRook = new Move(WHITE_KNIGHT, d5, e3, true);

        Assert.assertTrue(captureKnight.toString(), moves.contains(captureKnight));
        Assert.assertTrue(captureRook.toString(), moves.contains(captureRook));
    }
}

