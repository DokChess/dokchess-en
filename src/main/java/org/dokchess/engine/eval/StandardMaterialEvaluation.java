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
package org.dokchess.engine.eval;

import org.dokchess.domain.Colour;
import org.dokchess.domain.Piece;
import org.dokchess.domain.Position;

/**
 * Evaluation based solely on piece values.
 * <p/>
 * Each piece type has a value (pawn 1, knight 3, …, queen 9); pieces on the board are summed
 * accordingly. Own pieces count positive, opponent pieces negative. With equal material the
 * score is 0; losing a queen, for example, lowers the score by 9. Square placement does not
 * matter.
 *
 * @author StefanZ
 */
public class StandardMaterialEvaluation implements Evaluation {

    @Override
    public int evaluatePosition(Position position, Colour pointOfView) {
        int total = 0;

        for (int rank = 0; rank < 8; ++rank) {
            for (int file = 0; file < 8; ++file) {
                Piece piece = position.getPiece(rank, file);
                if (piece != null) {
                    int value = pieceValue(piece);
                    if (piece.getColour() == pointOfView) {
                        total += value;
                    } else {
                        total -= value;
                    }
                }
            }
        }

        return total;
    }

    /**
     * Material value of the given piece.
     */
    protected int pieceValue(final Piece piece) {
        switch (piece.getType()) {
            case PAWN:
                return 1;
            case KNIGHT:
            case BISHOP:
                return 3;
            case ROOK:
                return 5;
            case QUEEN:
                return 9;
            default:
                return 0;
        }
    }
}
