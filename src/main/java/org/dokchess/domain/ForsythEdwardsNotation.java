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

package org.dokchess.domain;

import java.util.EnumSet;


/**
 * The Forsyth-Edwards Notation (FEN) is a compact representation of a chess board position
 * as a single character string. Supported by many chess tools.
 * Used in DokChess by unit and integration tests.
 *
 * @author StefanZ
 */
final class ForsythEdwardsNotation {

    /**
     * Tool class, no public constructor
     */
    private ForsythEdwardsNotation() {
    }

    /**
     *
     * Set up the position from a given FEN String.
     *
     * @param pos target position as an object
     * @param fen source position as a FEN string
     */
    public static void fromString(final Position pos, final String fen) {

        String[] groups = fen.split(" ");

        int rank = 0;
        int file = 0;

        String pieces = groups[0];

        for (int i = 0; i < pieces.length(); ++i) {

            char piece = pieces.charAt(i);

            if (Character.isDigit(piece)) {
                int n = Integer.parseInt("" + piece);
                for (int j = 0; j < n; ++j) {
                    pos.setPiece(rank, file, null);
                    file++;
                }
            } else if (piece == '/') {
                rank++;
                file = 0;
            } else {
                PieceType pieceType = PieceType.ausBuchstabe(piece);
                Colour side = Character.isUpperCase(piece) ? Colour.WHITE
                        : Colour.BLACK;
                Piece p = new Piece(pieceType, side);
                pos.setPiece(rank, file, p);
                file++;
            }
        }

        String toMove = groups[1];
        char side = toMove.charAt(0);
        switch (side) {
            case 'w':
                pos.setToMove(Colour.WHITE);
                break;
            case 'b':
                pos.setToMove(Colour.BLACK);
                break;
            default:
                throw new IllegalArgumentException(side + " no valid colour.");
        }

        String castlingsAvailable = groups[2];
        if (castlingsAvailable.equals("-")) {
            pos.setCastlingsAvailable(EnumSet.noneOf(CastlingType.class));
        } else {
            EnumSet<CastlingType> rechte = EnumSet.noneOf(CastlingType.class);
            for (int i = 0; i < castlingsAvailable.length(); ++i) {
                char c = castlingsAvailable.charAt(i);
                rechte.add(CastlingType.fromLetter(c));
            }
            pos.setCastlingsAvailable(rechte);
        }

        String enPassant = groups[3];
        if (enPassant.equals("-")) {
            pos.setEnPassantSquare(null);
        } else {
            Square enPassantFeld = new Square(enPassant);
            pos.setEnPassantSquare(enPassantFeld);
        }
    }

    /**
     * Create the FEN string representaion for a given position.
     *
     * @param position the source position
     * @return FEN-Notation of the position
     */
    public static String toString(final Position position) {

        StringBuffer sb = new StringBuffer();

        for (int rank = 0; rank < 8; ++rank) {
            int free = 0;
            for (int file = 0; file < 8; ++file) {

                Piece piece = position.getPiece(rank, file);
                if (piece == null) {
                    free++;
                } else {
                    if (free > 0) {
                        sb.append(free);
                        free = 0;
                    }
                    sb.append(piece.alsBuchstabe());
                }
            }
            if (free > 0) {
                sb.append(free);
            }
            if (rank < 7) {
                sb.append("/");
            }
        }

        sb.append(" ");

        switch (position.getToMove()) {
            case WHITE:
                sb.append('w');
                break;
            case BLACK:
                sb.append('b');
                break;
            default:
                throw new IllegalArgumentException("Invalid colour");
        }

        sb.append(" ");

        if (position.getCastlingsAvailable().size() == 0) {
            sb.append("-");
        } else {
            for (CastlingType r : CastlingType.values()) {
                if (position.getCastlingsAvailable().contains(r)) {
                    sb.append(r.asLetter());
                }
            }
        }

        sb.append(" ");
        if (position.getEnPassantSquare() == null) {
            sb.append("-");
        } else {
            sb.append(position.getEnPassantSquare());
        }

        // TODO: missing implementation here

        sb.append(" 0 1");

        return sb.toString();
    }
}
