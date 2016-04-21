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

import static  org.dokchess.domain.Squares.*;
import static  org.dokchess.domain.PieceType.*;
import static  org.dokchess.domain.Colour.*;

public class ForsythEdwardsNotationTest {

    /**
     * Converting the starting position to FEN.
     */
    @Test
    public void startingPositionToString() {

        Position s = new Position();

        String fen = ForsythEdwardsNotation.toString(s);
        Assert.assertEquals(
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen);
    }

    /**
     * Position with en passant in String.
     */
    @Test
    public void enPassantToString() {

        Position s = new Position();
        Move z = new Move(new Piece(PAWN, WHITE), e2, e4);
        s = s.performMove(z);

        String fen = ForsythEdwardsNotation.toString(s);
        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fen);
    }

    /**
     * Conversion of a FEN String to a position object.
     */
    @Test
    public void scholarsMateFenToPosition() {

        Position pos = new Position();
        String fen = "r1bqkb1r/pppp1ppp/2n2n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 1";
        ForsythEdwardsNotation.fromString(pos, fen);

        Assert.assertEquals(WHITE, pos.getToMove());
        Assert.assertEquals(new Piece(QUEEN, WHITE), pos.getPiece(h5));
        Assert.assertEquals(new Piece(PAWN, BLACK), pos.getPiece(e5));
    }
}
