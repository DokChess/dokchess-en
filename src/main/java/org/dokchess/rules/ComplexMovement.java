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

import org.dokchess.domain.Move;
import org.dokchess.domain.Piece;
import org.dokchess.domain.Position;
import org.dokchess.domain.Square;

import java.util.List;

/**
 * Abstrakte Oberklasse fuer alle komplexen Gangarten im Schach, gibt eine Implementierung fuer {@link #fuegeZugkandidatenHinzu(Feld, Stellung, List)} vor.
 */
public abstract class ComplexMovement extends Movement {

    @Override
    void addMoveCandidates(Square from, Position position, List<Move> target) {
        Piece ownPiece = position.getPiece(from);

        List<Square> reachable = getReachableSquares(position, from);
        for (Square to : reachable) {
            Move m = new Move(ownPiece, from, to, !position.isFree(to));
            target.add(m);
        }
    }

    protected abstract List<Square> getReachableSquares(Position position,
                                                        Square from);

}
