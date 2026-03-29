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

/**
 * Polyglot (.bin) opening book backed by an in-memory list of {@link BookEntry} records.
 * {@link SelectionMode} controls how a move is chosen when several entries match a position.
 */
public class PolyglotOpeningBook implements OpeningLibrary {

    private SelectionMode selectionMode;

    private final List<BookEntry> entries = new ArrayList<>();

    /**
     * Loads a book from a file. Selection mode defaults to {@link SelectionMode#FIRST}.
     *
     * @param file readable polyglot book file
     */
    public PolyglotOpeningBook(File file) throws FileNotFoundException, IOException {
        this.selectionMode = SelectionMode.FIRST;
        readData(file);
    }

    /**
     * Loads a book from a stream. Selection mode defaults to {@link SelectionMode#FIRST}.
     *
     * @param inputStream polyglot book bytes (caller may close the stream afterwards)
     */
    public PolyglotOpeningBook(InputStream inputStream) throws FileNotFoundException, IOException {
        this.selectionMode = SelectionMode.FIRST;
        readData(inputStream);
    }

    /**
     * Sets how moves are picked when multiple book entries match the same position key.
     */
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    @Override
    public Move lookUpMove(Position position) {

        String fen = position.toString();
        List<BookEntry> matches = findEntriesByFen(fen);

        if (matches != null && !matches.isEmpty()) {

            if (selectionMode == SelectionMode.MOST_PLAYED) {
                Collections.sort(matches);
            } else if (selectionMode == SelectionMode.RANDOM) {
                Collections.shuffle(matches);
            }

            BookEntry chosen = matches.get(0);

            Square fromSquare = new Square(chosen.getMoveFrom());
            Square toSquare = new Square(chosen.getMoveTo());
            Piece piece = position.getPiece(fromSquare);
            boolean capture = position.getPiece(toSquare) != null;

            return new Move(piece, fromSquare, toSquare, capture);
        }
        return null;
    }

    final void readData(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            readData(fileInputStream);
        }
    }

    final void readData(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        while (bufferedInputStream.available() > 0) {
            byte[] rawEntry = new byte[16];
            bufferedInputStream.read(rawEntry);
            BookEntry bookEntry = new BookEntry(rawEntry);
            entries.add(bookEntry);
        }
    }

    List<BookEntry> getEntries() {
        return entries;
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
        List<BookEntry> result = new ArrayList<>();

        for (BookEntry bookEntry : entries) {
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
