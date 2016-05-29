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
import org.dokchess.engine.Engine;
import org.dokchess.rules.ChessRules;
import org.dokchess.rules.DefaultChessRules;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class XBoardTest {

    /**
     * Smoke-Test fuer XBoard. Die Engine wird sofort verlassen,
     * nachdem das xboard-Protocol steht.
     */
    @Test
    public void sofortVerlassen() {

        String eingegeben = "xboard\nprotover 2\nquit\n";

        XBoard xBoard = new XBoard();
        xBoard.setOutput(new StringWriter());
        xBoard.setEngine(new MockEngine());
        xBoard.setChessRules(null);

        Reader eingabe = new StringReader(eingegeben);
        xBoard.setInput(eingabe);

        xBoard.play();
    }

    /**
     * Eingabe eines unbekannten Befehls.
     */
    @Test
    public void unsinnEingeben() {
        XBoard xBoard = new XBoard();

        xBoard.setEngine(new MockEngine());
        xBoard.setChessRules(null);

        String s = "Quak\nquit\n";
        Reader eingabe = new StringReader(s);
        xBoard.setInput(eingabe);

        Writer ausgabe = new StringWriter();
        xBoard.setOutput(ausgabe);

        xBoard.play();

        Assert.assertTrue(ausgabe.toString().contains("Quak"));
    }

    /**
     * Weiss versucht den Turm auf a1 nach vorn zu performMove.
     */
    @Test
    public void ungueltigerZug() {

        XBoard xBoard = new XBoard();

        Reader eingabe = new StringReader("xboard\nprotover 2\nnew\na1a2\nquit\n");
        Writer ausgabe = new StringWriter();

        xBoard.setInput(eingabe);
        xBoard.setOutput(ausgabe);

        ChessRules chessRules = new DefaultChessRules();
        xBoard.setChessRules(chessRules);
        xBoard.setEngine(new MockEngine());

        xBoard.play();
        Assert.assertTrue(ausgabe.toString().contains("Illegal move"));
    }

    /**
     * Weiss zieht e2-e4, Engine antwortet mit e7-e5.
     */
    @Test
    public void gueltigerZug() {

        Piece weisserBauer = new Piece(PieceType.PAWN, Colour.WHITE);
        Move e2e4 = new Move(weisserBauer, Squares.e2, Squares.e4);
        Move e7e5 = new Move(weisserBauer, Squares.e7, Squares.e5);

        XBoard xBoard = new XBoard();

        Reader eingabe = new StringReader("xboard\nprotover 2\nnew\ne2e4\nquit\n");
        Writer ausgabe = new StringWriter();

        xBoard.setInput(eingabe);
        xBoard.setOutput(ausgabe);

        ChessRules chessRules = new DefaultChessRules();
        xBoard.setChessRules(chessRules);

        Engine engine = new MockEngine(e7e5);
        engine.setupPieces(new Position());
        engine.performMove(e2e4);

        xBoard.setEngine(engine);

        xBoard.play();
        Assert.assertTrue(ausgabe.toString().contains("move e7e5"));
    }
}
