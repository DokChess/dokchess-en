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

public class ZugParserTest {

    private static final Piece WEISSER_KOENIG = new Piece(PieceType.KING,
            Colour.WHITE);
    private static final Piece WEISSER_BAUER = new Piece(PieceType.PAWN,
            Colour.WHITE);

    @Test
    public void einfacheZuegeVonXBoard() {

        Position stellung = new Position();
        ZugParser parser = new ZugParser();

        // Koenigsbauer 1 vor
        {
            String eingabe = "e2e3";
            Move zug = parser.vonXboard(eingabe, stellung);
            assertNotNull(zug);
            assertEquals(Squares.e2, zug.getFrom());
            assertEquals(Squares.e3, zug.getTo());
            assertEquals(WEISSER_BAUER, zug.getPiece());
            assertTrue(zug.isPawnMove());
            assertFalse(zug.isPawnAdvancesTwo());
        }

        // Koenigsbauer 2 vor
        {
            String eingabe = "e2e4";
            Move zug = parser.vonXboard(eingabe, stellung);
            assertNotNull(zug);
            assertEquals(Squares.e2, zug.getFrom());
            assertEquals(Squares.e4, zug.getTo());
            assertEquals(WEISSER_BAUER, zug.getPiece());
            assertTrue(zug.isPawnMove());
            assertTrue(zug.isPawnAdvancesTwo());
        }
    }

    @Test
    public void umwandlungInDameVonXBoard() {

        Position stellung = new Position("8/2k3P1/8/8/8/8/4K3/8 w - - 0 1");
        ZugParser parser = new ZugParser();

        String eingabe = "g7g8q";
        Move zug = parser.vonXboard(eingabe, stellung);

        assertNotNull(zug);
        assertEquals(Squares.g7, zug.getFrom());
        assertEquals(Squares.g8, zug.getTo());
        assertEquals(WEISSER_BAUER, zug.getPiece());
        assertTrue(zug.isPawnMove());
        assertTrue(zug.isPromotion());
        assertEquals(PieceType.QUEEN, zug.getPromotion());
    }

    @Test
    public void rochadeNachXBoard() {
        ZugParser parser = new ZugParser();
        Move rochadeKurz = new Move(WEISSER_KOENIG, Squares.e1, Squares.g1);

        String ausgabe = parser.nachXboard(rochadeKurz);
        assertEquals("move e1g1", ausgabe);
    }

    @Test
    public void umwandlungInDameNachXBoard() {

        ZugParser parser = new ZugParser();
        Move umwandlung = new Move(WEISSER_BAUER, Squares.h7, Squares.h8,
                PieceType.QUEEN);

        String ausgabe = parser.nachXboard(umwandlung);
        assertEquals("move h7h8q", ausgabe);
    }

}
