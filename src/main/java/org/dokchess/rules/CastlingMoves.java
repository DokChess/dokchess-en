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
package org.dokchess.rules;


import org.dokchess.domain.*;

import java.util.List;

import static org.dokchess.domain.Squares.*;

class CastlingMoves extends Movement {

    private static final Piece WEISSER_KOENIG = new Piece(PieceType.KING, Colour.WHITE);
    private static final Piece SCHWARZER_KOENIG = new Piece(PieceType.KING, Colour.BLACK);

    @Override
    public void addMoveCandidates(Square from, Position position,
                                  List<Move> target) {

        switch (position.getToMove()) {
            case WHITE:
                if (position.getCastlingsAvailable().contains(CastlingType.WHITE_KINGSIDE)) {
                    if (alleFelderFrei(position, f1, g1)
                            && keinesDerFelderAngegriffen(position, Colour.BLACK, e1,
                            f1, g1)) {
                        Move rochadeKurz = new Move(WEISSER_KOENIG, e1, g1);
                        target.add(rochadeKurz);
                    }
                }
                if (position.getCastlingsAvailable().contains(CastlingType.WHITE_QUEENSIDE)) {
                    if (alleFelderFrei(position, b1, c1, d1)
                            && keinesDerFelderAngegriffen(position, Colour.BLACK, e1,
                            d1, c1)) {
                        Move rochadeLang = new Move(WEISSER_KOENIG, e1, c1);
                        target.add(rochadeLang);
                    }
                }
                break;

            case BLACK:
                if (position.getCastlingsAvailable().contains(CastlingType.BLACK_KINGSIDE)) {
                    if (alleFelderFrei(position, f8, g8)
                            && keinesDerFelderAngegriffen(position, Colour.WHITE, e8, f8,
                            g8)) {
                        Move rochadeKurz = new Move(SCHWARZER_KOENIG, e8, g8);
                        target.add(rochadeKurz);
                    }
                }
                if (position.getCastlingsAvailable().contains(CastlingType.BLACK_QUEENSIDE)) {
                    if (alleFelderFrei(position, b8, c8, d8)
                            && keinesDerFelderAngegriffen(position, Colour.WHITE, e8, d8,
                            c8)) {
                        Move rochadeLang = new Move(SCHWARZER_KOENIG, e8, c8);
                        target.add(rochadeLang);
                    }
                }
                break;
        }
    }

    protected boolean keinesDerFelderAngegriffen(Position stellung,
                                                 Colour farbe, Square... felder) {
        for (Square feld : felder) {
            if (Tools.istFeldAngegriffen(stellung, feld, farbe)) {
                return false;
            }
        }
        return true;
    }

    protected boolean alleFelderFrei(Position stellung, Square... felder) {
        for (Square feld : felder) {
            if (stellung.getPiece(feld) != null) {
                return false;
            }
        }
        return true;
    }
}
