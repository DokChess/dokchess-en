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
import org.dokchess.opening.polyglot.AuswahlModus;
import org.dokchess.opening.polyglot.PolyglotOpeningBook;
import org.dokchess.rules.ChessRules;
import org.dokchess.rules.DefaultChessRules;
import org.dokchess.textui.xboard.XBoard;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Startet DokChess auf der Kommandozeile. Verdrahtet stdin und stdout fuer das
 * XBoard-Protokoll und setzt die Eroeffnungsbibliothek, falls angegeben.
 *
 * @author StefanZ
 */
public final class Main {

    /**
     * Konstruktor private, um keine Exemplare von dieser (Main-)Klasse bauen koennen.
     */
    private Main() {
    }

    /**
     * Einstiegspunkt. Startet das XBoard-Protokoll und die Engine.
     *
     * @param args Kommendozeilenparameter. Dateiname eines (optionalen) Eroeffnungsbuches.
     */
    public static void main(String[] args) {

        OpeningLibrary bibliothek = null;

        if (args.length > 0) {
            String dateiName = args[0];
            File eroeffnungen = new File(dateiName);
            if (!eroeffnungen.canRead()) {
                System.err.printf("Kann Eroeffnungsbibliothek aus [%s] nicht lesen.%n", args[0]);
                System.exit(1);
            } else {
                try {
                    PolyglotOpeningBook pob = new PolyglotOpeningBook(eroeffnungen);
                    pob.setAuswahlModus(AuswahlModus.HAEUFIGSTER);
                    bibliothek = pob;
                } catch (IOException e) {
                    System.err.printf(e.getMessage());
                    System.exit(1);
                }

            }
        }

        ChessRules chessRules = new DefaultChessRules();
        Engine engine = new DefaultEngine(chessRules, bibliothek);

        XBoard xBoard = xBoardBauen();
        xBoard.setEngine(engine);
        xBoard.setChessRules(chessRules);

        xBoard.play();
    }

    static XBoard xBoardBauen() {
        XBoard xBoard = new XBoard();

        InputStreamReader isr = new InputStreamReader(System.in);
        xBoard.setInput(isr);

        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        xBoard.setOutput(writer);
        return xBoard;
    }

}
