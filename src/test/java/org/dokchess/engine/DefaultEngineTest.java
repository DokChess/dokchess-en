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

package org.dokchess.engine;

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.dokchess.opening.polyglot.PolyglotOpeningBook;
import org.dokchess.rules.ChessRules;
import org.dokchess.rules.DefaultChessRules;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Einfache Tests, eher Richtung Smoke-Test ...
 */
public class DefaultEngineTest {

    @Test
    public void ohneEroeffnungsbiblithek() {

        ChessRules regeln = new DefaultChessRules();
        DefaultEngine engine = new DefaultEngine(regeln);

        Position anfang = new Position();
        engine.setupPieces(anfang);

        Observable<Move> observable = engine.determineYourMove();
        Assert.assertNotNull(observable);
    }

    @Test
    public void mitEroeffnungsbiblithek() throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream(
                "org/dokchess/opening/polyglot/GiuocoPiano.bin");
        PolyglotOpeningBook buch = new PolyglotOpeningBook(is);
        is.close();

        ChessRules regeln = new DefaultChessRules();
        DefaultEngine engine = new DefaultEngine(regeln, buch);

        Position anfang = new Position();
        engine.setupPieces(anfang);

        Observable<Move> observable = engine.determineYourMove();
        Assert.assertNotNull(observable);
    }


}
