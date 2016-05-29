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
import org.dokchess.domain.Piece;
import org.dokchess.domain.Position;
import org.dokchess.domain.Square;
import org.dokchess.opening.OpeningLibrary;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolyglotOpeningBook implements OpeningLibrary {

    private AuswahlModus auswahlModus;

    private List<BookEntry> eintraege = new ArrayList<BookEntry>();

    public PolyglotOpeningBook(File datei) throws FileNotFoundException, IOException {
        this.auswahlModus = AuswahlModus.ERSTER;
        readData(datei);
    }

    public PolyglotOpeningBook(InputStream is) throws FileNotFoundException, IOException {
        this.auswahlModus = AuswahlModus.ERSTER;
        readData(is);
    }


    public void setAuswahlModus(AuswahlModus auswahlModus) {
        this.auswahlModus = auswahlModus;
    }

    @Override
    public Move lookUpMove(Position stellung) {

        String fen = stellung.toString();
        List<BookEntry> treffer = findEntriesByFen(fen);

        if (treffer != null && treffer.size() > 0) {

            if (auswahlModus == AuswahlModus.HAEUFIGSTER) {
                Collections.sort(treffer);
            } else if (auswahlModus == AuswahlModus.PER_ZUFALL) {
                Collections.shuffle(treffer);
            }

            BookEntry eintrag = treffer.get(0);

            Square von = new Square(eintrag.getMoveFrom());
            Square nach = new Square(eintrag.getMoveTo());
            Piece figur = stellung.getPiece(von);
            boolean schlagen = stellung.getPiece(nach) != null;

            Move zug = new Move(figur, von, nach, schlagen);

            return zug;
        } else {
            return null;
        }
    }

    final void readData(File datei) throws IOException {
        FileInputStream fis = new FileInputStream(datei);
        readData(fis);
        fis.close();
    }

    final void readData(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);

        while (bis.available() > 0) {
            byte[] entry = new byte[16];
            bis.read(entry);
            BookEntry bookEntry = new BookEntry(entry);
            eintraege.add(bookEntry);
        }
    }

    List<BookEntry> getEntries() {
        return eintraege;
    }

    List<BookEntry> findEntriesByFen(String fen) {
        long key = FenTools.calculateKeyFromFen(fen);
        return findEntriesByKey(key);
    }

    List<BookEntry> findEntriesByKey(long key) {
        byte[] bytesKey = PolyglotTools.longToByteArray(key);
        return findEntriesByKey(bytesKey);
    }

    List<BookEntry> findEntriesByKey(byte[] key) {
        List<BookEntry> result = new ArrayList<BookEntry>();

        for (BookEntry bookEntry : eintraege) {
            boolean equal = true;
            byte[] bookEntryKey = bookEntry.getKey();
            for (int i = 0; i < 8 && equal; ++i) {
                if (bookEntryKey[i] != key[i]) {
                    equal = false;
                }
            }
            if (equal) {
                result.add(bookEntry);
            }
        }
        return result;
    }

}
