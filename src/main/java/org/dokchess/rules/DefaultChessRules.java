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

import java.util.*;

public class DefaultChessRules implements ChessRules {

    private KnightMoves knightMoves = new KnightMoves();

    private RookMoves rookMoves = new RookMoves();

    private QueenMoves queenMoves = new QueenMoves();

    private BishopMoves bishopMoves = new BishopMoves();

    private PawnMoves pawnMoves = new PawnMoves();

    private KingMoves kingMoves = new KingMoves();

    private CastlingMoves castlingMoves = new CastlingMoves();

    @Override
    public Collection<Move> getLegalMoves(Position position) {

        List<Move> moves = new ArrayList<Move>();
        Colour sideToMove = position.getToMove();

        Set<Square> ownSquares = position.squaresWithColour(sideToMove);
        for (Square square : ownSquares) {

            Piece piece = position.getPiece(square);
            switch (piece.getType()) {

                case KNIGHT:
                    knightMoves.addMoveCandidates(square, position,
                            moves);
                    break;

                case QUEEN:
                    queenMoves.addMoveCandidates(square, position,
                            moves);
                    break;

                case ROOK:
                    rookMoves
                            .addMoveCandidates(square, position, moves);
                    break;

                case BISHOP:
                    bishopMoves.addMoveCandidates(square, position,
                            moves);
                    break;

                case PAWN:
                    pawnMoves.addMoveCandidates(square, position,
                            moves);
                    break;

                case KING:
                    kingMoves.addMoveCandidates(square, position,
                            moves);
                    castlingMoves.addMoveCandidates(square, position,
                            moves);
            }
        }

        Iterator<Move> iterator = moves.iterator();
        while (iterator.hasNext()) {
            Move move = iterator.next();
            Position newPos = position.performMove(move);
            if (isCheck(newPos, sideToMove)) {
                iterator.remove();
            }
        }

        return moves;
    }

    @Override
    public Position getStartingPosition() {
        return new Position();
    }

    @Override
    public boolean isCheck(Position position, Colour colour) {
        Square squareWithKing = position.findSquareWithKing(colour);
        return Tools.istFeldAngegriffen(position, squareWithKing, colour.otherColour());
    }

    @Override
    public boolean isCheckmate(Position position) {
        Colour sideToMove = position.getToMove();
        if (isCheck(position, sideToMove)) {
            Collection<Move> zuege = getLegalMoves(position);
            if (zuege.size() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStalemate(Position position) {
        Collection<Move> moves = getLegalMoves(position);
        if (moves.isEmpty()) {
            Colour amZug = position.getToMove();
            return !isCheck(position, amZug);
        }
        return false;
    }
}
