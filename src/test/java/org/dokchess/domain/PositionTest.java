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

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.dokchess.domain.CastlingType.*;
import static org.dokchess.domain.Colour.BLACK;
import static org.dokchess.domain.Colour.WHITE;
import static org.dokchess.domain.PieceType.*;
import static org.dokchess.domain.Squares.*;
import static org.junit.Assert.*;


public class PositionTest {

    private static final Piece WHITE_PAWN = new Piece(PAWN,
            WHITE);
    private static final Piece WHITE_QUEEN = new Piece(QUEEN,
            WHITE);
    private static final Piece WHITE_KING = new Piece(KING,
            WHITE);
    private static final Piece WHITE_ROOK = new Piece(ROOK,
            WHITE);

    @Test
    public void pawnAdvancesOne() {
        Position pos = new Position();

        Move move = new Move(WHITE_PAWN, e2, e3);

        Position newPos = pos.performMove(move);

        assertNull(newPos.getPiece(e2));
        assertNull(newPos.getEnPassantSquare());
        assertEquals(WHITE_PAWN, newPos.getPiece(e3));

        assertEquals(BLACK, newPos.getToMove());
    }

    @Test
    public void pawnAdvancesTwo() {
        Position pos = new Position();

        Move move = new Move(WHITE_PAWN, e2, e4);

        Position newPos = pos.performMove(move);

        assertNull(newPos.getPiece(e2));
        assertEquals(e3, newPos.getEnPassantSquare());
        assertEquals(WHITE_PAWN, newPos.getPiece(e4));

        assertEquals(BLACK, newPos.getToMove());
    }

    /**
     * Promotion of a white pawn to a queen.
     */
    @Test
    public void pawnPromotedToQueen() {
        Position pos = new Position("4k3/1P6/4K3/8/8/8/8/8 w - - 0 1");

        Move move = new Move(WHITE_PAWN, b7, b8, QUEEN);
        Position newPos = pos.performMove(move);

        assertNull(newPos.getPiece(b7));
        assertEquals(WHITE_QUEEN, newPos.getPiece(b8));
    }

    /**
     * White castles kingside (short castling).
     */
    @Test
    public void whiteKingsideCastling() {
        Position pos = new Position("rnbqkbnr/ppp3pp/3ppp2/8/8/4PN2/PPPPBPPP/RNBQK2R w KQkq - 0 1");

        Move move = new Move(WHITE_KING, e1, g1);
        assertTrue(move.isCastlingKingside());

        Position newPos = pos.performMove(move);

        // King and rook have moved
        assertNull(newPos.getPiece(e1));
        assertNull(newPos.getPiece(h1));

        assertEquals(WHITE_KING, newPos.getPiece(g1));
        assertEquals(WHITE_ROOK, newPos.getPiece(f1));

        assertTrue(newPos.castlingAllowed((BLACK_KINGSIDE)));
        assertTrue(newPos.castlingAllowed((BLACK_QUEENSIDE)));
        assertFalse(newPos.castlingAllowed((WHITE_KINGSIDE)));
        assertFalse(newPos.castlingAllowed((WHITE_QUEENSIDE)));
    }

    /**
     * White moves a rook or the king; castling rights update accordingly.
     */
    @Test
    public void whiteCastlingRightsUpdatedWhenMovingRookOrKing() {
        Position position = new Position("4k3/8/1Q6/8/8/8/8/R3K2R w KQ - 0 1");

        // Rook moves; castling remains possible on the other wing
        Move moveQueensideRook = new Move(WHITE_ROOK, a1, b1);
        Position afterQueensideRook = position.performMove(moveQueensideRook);
        assertFalse(afterQueensideRook.castlingAllowed(WHITE_QUEENSIDE));
        assertTrue(afterQueensideRook.castlingAllowed(WHITE_KINGSIDE));

        // Same idea, other wing
        Move moveKingsideRook = new Move(WHITE_ROOK, h1, g1);
        Position afterKingsideRook = position.performMove(moveKingsideRook);
        assertTrue(afterKingsideRook.castlingAllowed(WHITE_QUEENSIDE));
        assertFalse(afterKingsideRook.castlingAllowed(WHITE_KINGSIDE));

        // King moves; White can no longer castle
        Move moveKing = new Move(WHITE_KING, e1, e2);
        Position afterKing = position.performMove(moveKing);
        assertFalse(afterKing.castlingAllowed(WHITE_KINGSIDE));
        assertFalse(afterKing.castlingAllowed(WHITE_QUEENSIDE));
    }


    @Test
    public void squaresWithWhitePieces() {
        Position pos = new Position();

        Set<Square> squares = pos.squaresWithColour(WHITE);
        assertEquals(16, squares.size());
        assertTrue("White piece a1", squares.contains(a1));
    }

    @Test
    public void findSquaresWithWhitePawns() {
        Position position = new Position();

        List<Square> squares = position.findSquaresWith(WHITE_PAWN);
        assertEquals(8, squares.size());
        assertTrue("White pawn e2", squares.contains(e2));
    }

    @Test
    public void findKingSquares() {
        Position position = new Position();

        Square blackSquare = position.findSquareWithKing(BLACK);
        assertEquals(e8, blackSquare);

        Square whiteSquare = position.findSquareWithKing(WHITE);
        assertEquals(e1, whiteSquare);
    }

    @Test
    public void findKingsOnAnEmptyBoard() {
        Position position = new Position("8/8/8/8/8/8/8/8 w - - 0 1");

        Square blackSquare = position.findSquareWithKing(BLACK);
        assertNull(blackSquare);

        Square whiteSquare = position.findSquareWithKing(WHITE);
        assertNull(whiteSquare);
    }
}
