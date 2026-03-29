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
 * XBoard protocol adapter over a {@link Reader}/{@link Writer} pair. Implements
 * a small subset of commands sufficient to drive the engine from a GUI.
 *
 * @author StefanZ
 */
public class XBoard implements Observer<Move> {

    private Reader input;

    private BufferedReader bufferedReader;

    private Writer output;

    private ChessRules chessRules;

    private Engine engine;

    private Move bestMove;

    private Position position = new Position();

    private MoveParser moveParser = new MoveParser();

    /**
     * Sets protocol input (dependency injection). Typically {@link System#in};
     * tests may supply another {@link Reader}.
     *
     * @param input character source for incoming commands
     */
    public void setInput(Reader input) {
        this.input = input;
        this.bufferedReader = new BufferedReader(this.input);
    }

    /**
     * Sets protocol output. Typically {@link System#out}; tests may use a
     * {@link java.io.StringWriter} or other {@link Writer}.
     *
     * @param output sink for outgoing lines
     */
    public void setOutput(Writer output) {
        this.output = output;
    }

    /**
     * Sets chess rules for validating opponent moves. Optional: if {@code null},
     * incoming moves are not checked against the rule set.
     *
     * @param chessRules rules implementation, or {@code null}
     */
    public void setChessRules(ChessRules chessRules) {
        this.chessRules = chessRules;
    }

    /**
     * Sets the engine that selects moves. Required for play.
     *
     * @param engine engine implementation
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * Runs the read/process/write loop until a quit command or end of input.
     */
    public void play() {

        boolean running = true;
        while (running) {

            String line = readLine();

            if (line == null || line.equals("quit")) {
                running = false;
                continue;
            }

            if (line.equals("xboard")) {
                writeLine("");
                continue;
            }

            if (line.equals("protover 2")) {
                writeLine("feature done=1");
                continue;
            }

            if (line.equals("new")) {
                position = new Position();
                engine.setupPieces(position);
                continue;
            }

            if (line.equals("go")) {
                Observable<Move> moveObservable = engine.determineYourMove();
                moveObservable.subscribe(this);
                continue;
            }

            Move move = moveParser.fromXboard(line, position);
            if (move != null) {

                if (chessRules != null) {
                    Collection<Move> legalMoves = chessRules
                            .getLegalMoves(position);
                    if (!legalMoves.contains(move)) {
                        writeLine("Illegal move: " + line);
                        continue;
                    }
                }

                engine.performMove(move);
                position = position.performMove(move);

                Observable<Move> moveObservable = engine.determineYourMove();
                moveObservable.subscribe(this);
                continue;
            }

            writeLine("Error (unknown command): " + line);
        }

        engine.close();
    }

    String readLine() {
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    void writeLine(String line) {
        try {
            synchronized (output) {
                output.write(line + "\n");
                output.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCompleted() {
        writeLine(moveParser.toXboard(this.bestMove));
        this.engine.performMove(this.bestMove);
        this.position = this.position.performMove(this.bestMove);
    }

    @Override
    public void onError(Throwable e) {
        writeLine("tellusererror " + e.getMessage());
    }

    @Override
    public void onNext(Move move) {
        writeLine("# better move found: " + move);
        this.bestMove = move;
    }
}
