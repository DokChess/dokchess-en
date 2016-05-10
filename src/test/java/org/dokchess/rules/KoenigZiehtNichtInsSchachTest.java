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
import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

import static org.dokchess.domain.PieceType.KING;
import static org.dokchess.domain.Squares.*;


public class KoenigZiehtNichtInsSchachTest {

    private static final Piece KOENIG_WEISS = new Piece(KING,
            Colour.WHITE);

    @Test
    public void koenigZiehtNichtInsSchachDurchAnderenKoenig() {

        // Schwarzer Koenig in Opposition
        String fen = "8/8/3k4/8/3K4/8/8/8 w - - 0 1";

        Position stellung = new Position(fen);
        ChessRules regeln = new DefaultChessRules();
        Collection<Move> zuege = regeln.getLegalMoves(stellung);

        Move illegal1 = new Move(KOENIG_WEISS, d4, c5);
        Move illegal2 = new Move(KOENIG_WEISS, d4, d5);
        Move illegal3 = new Move(KOENIG_WEISS, d4, e5);

        Assert.assertFalse(zuege.contains(illegal1));
        Assert.assertFalse(zuege.contains(illegal2));
        Assert.assertFalse(zuege.contains(illegal3));

        Move legal = new Move(KOENIG_WEISS, d4, d3);
        Assert.assertTrue(zuege.contains(legal));

        Assert.assertEquals(5, zuege.size());

    }

    @Test
    public void koenigZiehtNichtInsSchachDurchLaeufer() {

        // Zwei Schwarze Laeufer
        String fen = "3k4/1b6/7b/8/3K4/8/8/8 w - - 0 1";

        Position stellung = new Position(fen);
        ChessRules regeln = new DefaultChessRules();
        Collection<Move> zuege = regeln.getLegalMoves(stellung);


        Move illegal1 = new Move(KOENIG_WEISS, d4, d5);
        Move illegal2 = new Move(KOENIG_WEISS, d4, e3);

        Assert.assertFalse(zuege.contains(illegal1));
        Assert.assertFalse(zuege.contains(illegal2));

        Move legal1 = new Move(KOENIG_WEISS, d4, c5);
        Move legal2 = new Move(KOENIG_WEISS, d4, d3);
        Move legal3 = new Move(KOENIG_WEISS, d4, e5);

        Assert.assertTrue(zuege.contains(legal1));
        Assert.assertTrue(zuege.contains(legal2));
        Assert.assertTrue(zuege.contains(legal3));

        Assert.assertEquals(5, zuege.size());

    }

}
