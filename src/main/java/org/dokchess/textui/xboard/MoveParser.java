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

import org.dokchess.domain.*;

/**
 * Converts {@link Move} instances to XBoard wire format and back.
 *
 * @author StefanZ
 */
public class MoveParser {

    /**
     * Parses a move given as a string into a {@link Move}. The move is enriched
     * with context from the position (for example whether a capture occurs).
     *
     * @param input    move text from XBoard (e.g. {@code e2e4}, {@code e7e8q})
     * @param position current position for context
     * @return move instance, or {@code null} if the string is not a valid move
     */
    public Move fromXboard(final String input, final Position position) {

        Move move = null;

        if (input.matches("[a-h][1-8][a-h][1-8][qrnb]?")) {

            Square from = new Square(input.substring(0, 2));
            Square to = new Square(input.substring(2, 4));
            boolean capture = false;
            PieceType promotionPieceType = null;
            Piece piece = position.getPiece(from);

            if (position.getPiece(to) != null) {
                capture = true;
            }

            if (piece != null && piece.getType() == PieceType.PAWN
                    && (to.getRank() == 0 || to.getRank() == 7)) {
                char promotionLetter = input.charAt(4);
                promotionPieceType = PieceType.fromLetter(promotionLetter);
            }

            move = new Move(piece, from, to, capture, promotionPieceType);
        }

        return move;
    }

    /**
     * Formats a {@link Move} as a string for the XBoard protocol.
     *
     * @param move move to format
     * @return corresponding protocol line (including the {@code move } prefix)
     */
    public String toXboard(final Move move) {

        StringBuilder sb = new StringBuilder();
        sb.append("move ");

        sb.append(move.getFrom());
        sb.append(move.getTo());

        if (move.isPromotion()) {
            char c = move.getPromotion().getLetter();
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
