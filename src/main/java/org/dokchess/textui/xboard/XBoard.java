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

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.dokchess.engine.Engine;
import org.dokchess.rules.ChessRules;
import rx.Observable;
import rx.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

/**
 * Implementierung des XBoard-Protokolls. Enthaelt nur die wichtigsten Befehle.
 *
 * @author StefanZ
 */
public class XBoard implements Observer<Move> {

    private Reader input;

    private BufferedReader bufEingabe;

    private Writer output;

    private ChessRules chessRules;

    private Engine engine;

    private Move besterZug;

    private Position stellung = new Position();

    private ZugParser parser = new ZugParser();

    /**
     * Setzt die Protokoll-Eingabe per Dependency Injection. Typischerweise ist
     * das die Standardeingabe (stdin); z.B. f&uuml;r automatische Tests kann
     * eine andere Quelle verwendet werden.
     *
     * @param input
     */
    public void setInput(Reader input) {
        this.input = input;
        this.bufEingabe = new BufferedReader(this.input);
    }

    /**
     * Setzt die Protokoll-Ausgabe. Typischerweise ist das die Standardausgabe
     * (stdout), f&uuml;r automatische Tests kann eine andere Senke verwendet
     * werden.
     *
     * @param output die Ausgabe
     */
    public void setOutput(Writer output) {
        this.output = output;
    }

    /**
     * Setzt eine Implementierung der Spielregeln. Optional, ohne Spielregeln
     * werden die eingehenden Zuege nicht geprueft.
     *
     * @param chessRules die Spielregeln
     */
    public void setChessRules(ChessRules chessRules) {
        this.chessRules = chessRules;
    }

    /**
     * Setzt eine Implementierung der Engine. Erforderlich zum Spielen.
     *
     * @param engine die Engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * Startet die eigentliche Kommunikation (Eingabe/Verarbeitung/Ausgabe) in
     * einer Endlosschleife, bis zum Beenden-Kommando.
     */
    public void play() {


        boolean running = true;
        while (running) {

            String eingelesen = lesen();

            if (eingelesen == null || eingelesen.equals("quit")) {
                running = false;
                continue;
            }

            if (eingelesen.equals("xboard")) {
                schreiben("");
                continue;
            }

            if (eingelesen.equals("protover 2")) {
                schreiben("feature done=1");
                continue;
            }

            if (eingelesen.equals("new")) {
                stellung = new Position();
                engine.setupPieces(stellung);
                continue;
            }

            if (eingelesen.equals("go")) {
                Observable<Move> s = engine.determineYourMove();
                s.subscribe(this);
                continue;
            }

            Move zug = parser.vonXboard(eingelesen, stellung);
            if (zug != null) {

                if (chessRules != null) {
                    Collection<Move> gueltigeZuege = chessRules
                            .getLegalMoves(stellung);
                    if (!gueltigeZuege.contains(zug)) {
                        schreiben("Illegal move: " + eingelesen);
                        continue;
                    }
                }

                engine.performMove(zug);
                stellung = stellung.performMove(zug);

                Observable<Move> s = engine.determineYourMove();
                s.subscribe(this);
                continue;
            }

            schreiben("Error (unknown command): " + eingelesen);
        }

        engine.close();
    }

    String lesen() {
        String zeile = null;
        try {
            zeile = bufEingabe.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return zeile;
    }

    void schreiben(String zeile) {
        try {
            synchronized (output) {
                output.write(zeile + "\n");
                output.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCompleted() {
        schreiben(parser.nachXboard(this.besterZug));
        this.engine.performMove(this.besterZug);
        this.stellung = this.stellung.performMove(this.besterZug);
    }

    @Override
    public void onError(Throwable e) {
        schreiben("tellusererror " + e.getMessage());
    }

    @Override
    public void onNext(Move zug) {
        schreiben("# better move found: " + zug);
        this.besterZug = zug;
    }
}
