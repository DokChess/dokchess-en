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
package org.dokchess;

import org.dokchess.engine.DefaultEngine;
import org.dokchess.engine.Engine;
import org.dokchess.opening.OpeningLibrary;
import org.dokchess.opening.polyglot.PolyglotOpeningBook;
import org.dokchess.opening.polyglot.SelectionMode;
import org.dokchess.rules.ChessRules;
import org.dokchess.rules.DefaultChessRules;
import org.dokchess.textui.xboard.XBoard;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Command-line entry point for DokChess. Wires stdin/stdout for the XBoard protocol
 * and loads an opening book when a path is given.
 *
 * @author StefanZ
 */
public final class Main {

    /**
     * Private constructor so this class cannot be instantiated.
     */
    private Main() {
    }

    /**
     * Starts the XBoard protocol (stdin/stdout) and the engine.
     *
     * @param args optional command-line argument: path to a polyglot opening book file
     */
    public static void main(String[] args) {

        OpeningLibrary openingLibrary = null;

        if (args.length > 0) {
            String fileName = args[0];
            File openingBookFile = new File(fileName);
            if (!openingBookFile.canRead()) {
                System.err.printf("Cannot read opening book from [%s].%n", args[0]);
                System.exit(1);
            } else {
                try {
                    PolyglotOpeningBook polyglotBook = new PolyglotOpeningBook(openingBookFile);
                    polyglotBook.setSelectionMode(SelectionMode.MOST_PLAYED);
                    openingLibrary = polyglotBook;
                } catch (IOException e) {
                    System.err.printf(e.getMessage());
                    System.exit(1);
                }

            }
        }

        ChessRules chessRules = new DefaultChessRules();
        Engine engine = new DefaultEngine(chessRules, openingLibrary);

        XBoard xBoard = buildXBoard();
        xBoard.setEngine(engine);
        xBoard.setChessRules(chessRules);

        xBoard.play();
    }

    static XBoard buildXBoard() {
        XBoard xBoard = new XBoard();

        InputStreamReader isr = new InputStreamReader(System.in);
        xBoard.setInput(isr);

        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        xBoard.setOutput(writer);
        return xBoard;
    }

}
