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

package org.dokchess.opening.polyglot;

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PolyglotOpeningBookTest {

    /**
     * Missing file causes an exception.
     */
    @Test(expected = IOException.class)
    public void missingFile() throws IOException {
        File input = new File("fileDoesNotExist.bin");
        new PolyglotOpeningBook(input);
    }

    /**
     * The test data of this file contains only one opnening:
     * Giuoco Piano Opening. 1. e2-e4 e7-e5 2. S g1-f3 S b8-c6 3. L f1-c4 L f8-c5
     */
    @Test
    public void giuocoPianoOpening() throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "org/dokchess/opening/polyglot/GiuocoPiano.bin");

        PolyglotOpeningBook book = new PolyglotOpeningBook(is);

        // 1. e2-e4
        Position stellung = new Position();
        Move move = book.liefereZug(stellung);
        Assert.assertEquals("e2", move.getFrom().toString());
        Assert.assertEquals("e4", move.getTo().toString());

        // 1. ... e7-e5
        stellung = stellung.performMove(move);
        move = book.liefereZug(stellung);
        Assert.assertEquals("e7", move.getFrom().toString());
        Assert.assertEquals("e5", move.getTo().toString());

        // 2. S g1-f3
        stellung = stellung.performMove(move);
        move = book.liefereZug(stellung);
        Assert.assertEquals("g1", move.getFrom().toString());
        Assert.assertEquals("f3", move.getTo().toString());
    }
}
