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
package org.dokchess.textui.xboard;

import org.dokchess.domain.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveParserTest {

    private static final Piece WEISSER_KOENIG = new Piece(PieceType.KING,
            Colour.WHITE);
    private static final Piece WEISSER_BAUER = new Piece(PieceType.PAWN,
            Colour.WHITE);

    @Test
    public void einfacheZuegeVonXBoard() {

        Position position = new Position();
        MoveParser parser = new MoveParser();

        // King pawn one square
        {
            String input = "e2e3";
            Move move = parser.fromXboard(input, position);
            assertNotNull(move);
            assertEquals(Squares.e2, move.getFrom());
            assertEquals(Squares.e3, move.getTo());
            assertEquals(WEISSER_BAUER, move.getPiece());
            assertTrue(move.isPawnMove());
            assertFalse(move.isPawnAdvancesTwo());
        }

        // King pawn two squares
        {
            String input = "e2e4";
            Move move = parser.fromXboard(input, position);
            assertNotNull(move);
            assertEquals(Squares.e2, move.getFrom());
            assertEquals(Squares.e4, move.getTo());
            assertEquals(WEISSER_BAUER, move.getPiece());
            assertTrue(move.isPawnMove());
            assertTrue(move.isPawnAdvancesTwo());
        }
    }

    @Test
    public void umwandlungInDameVonXBoard() {

        Position position = new Position("8/2k3P1/8/8/8/8/4K3/8 w - - 0 1");
        MoveParser parser = new MoveParser();

        String input = "g7g8q";
        Move move = parser.fromXboard(input, position);

        assertNotNull(move);
        assertEquals(Squares.g7, move.getFrom());
        assertEquals(Squares.g8, move.getTo());
        assertEquals(WEISSER_BAUER, move.getPiece());
        assertTrue(move.isPawnMove());
        assertTrue(move.isPromotion());
        assertEquals(PieceType.QUEEN, move.getPromotion());
    }

    @Test
    public void rochadeNachXBoard() {
        MoveParser parser = new MoveParser();
        Move kingsideCastle = new Move(WEISSER_KOENIG, Squares.e1, Squares.g1);

        String output = parser.toXboard(kingsideCastle);
        assertEquals("move e1g1", output);
    }

    @Test
    public void umwandlungInDameNachXBoard() {

        MoveParser parser = new MoveParser();
        Move promotion = new Move(WEISSER_BAUER, Squares.h7, Squares.h8,
                PieceType.QUEEN);

        String output = parser.toXboard(promotion);
        assertEquals("move h7h8q", output);
    }

}
