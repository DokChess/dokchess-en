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

import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link DefaultChessRules#isStalemate(Position)}.
 */
public class StalemateDetectionTest {

    private ChessRules chessRules;

    @Before
    public void setUp() {
        chessRules = new DefaultChessRules();
    }

    /**
     * The standard starting position is not stalemate.
     */
    @Test
    public void startingPositionIsNotStalemate() {
        Position position = new Position();
        Assert.assertFalse("starting position",
                chessRules.isStalemate(position));
    }

    /**
     * Scholar's mate is checkmate, not stalemate.
     */
    @Test
    public void scholarMateIsNotStalemate() {
        String fen = "r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1";
        Position position = new Position(fen);
        Assert.assertFalse("Scholar's mate",
                chessRules.isStalemate(position));
    }

    /**
     * A basic stalemate: Black to move, no legal moves, not in check.
     */
    @Test
    public void simpleStalemate() {
        String fen = "7k/8/7K/8/8/8/8/6Q1 b - - 0 1";
        Position position = new Position(fen);
        Assert.assertTrue("simple stalemate",
                chessRules.isStalemate(position));
    }
}
