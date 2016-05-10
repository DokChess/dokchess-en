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

package org.dokchess.textui.integration;

import org.dokchess.engine.DefaultEngine;
import org.dokchess.engine.Engine;
import org.dokchess.rules.ChessRules;
import org.dokchess.rules.DefaultChessRules;
import org.dokchess.textui.xboard.XBoard;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Start the XBoard-protocol incl. engine. Move and wait for an answer.
 *
 * @author stefanz
 */
public class XBoardIntegTest {

    @Test
    public void letsStartToPlay() throws InterruptedException {

        String input = "xboard\nprotover 2\ne2e4\n";
        final StringWriter writer = new StringWriter();

        XBoard xBoard = new XBoard();
        xBoard.setOutput(writer);
        ChessRules rulez = new DefaultChessRules();
        Engine engine = new DefaultEngine(rulez);

        xBoard.setEngine(engine);
        xBoard.setChessRules(rulez);

        Reader eingabe = new StringReader(input);
        xBoard.setInput(eingabe);

        xBoard.spielen();

        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                String text;
                synchronized (writer) {
                    text = writer.toString();
                }
                if (text.contains("move ")) {
                    executor.shutdown();
                }
            }
        }, 3, 1, TimeUnit.SECONDS);
        executor.awaitTermination(1, TimeUnit.MINUTES);

        String text;
        synchronized (writer) {
            text = writer.toString();
        }

        Assert.assertTrue(text.contains("move "));
    }
}