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

import java.util.*;

import static org.dokchess.domain.CastlingType.*;
import static org.dokchess.domain.Colour.BLACK;
import static org.dokchess.domain.Colour.WHITE;
import static org.dokchess.domain.PieceType.KING;
import static org.dokchess.domain.PieceType.ROOK;
import static org.dokchess.domain.Squares.*;

/**
 * Represents a chess position: piece placement, side to move, castling rights, etc.
 *
 * @author StefanZ
 */
public final class Position {

    private static final int NUMBER_OF_RANKS = 8;
    private static final int NUMBER_OF_FILES = 8;

    private Colour toMove;

    private Piece[][] board;

    private Square enPassantSquare;

    private Set<CastlingType> castlingsAvailable;

    /**
     * Creates the starting position. White moves first.
     */
    public Position() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    /**
     * Creates a position from a FEN string.
     *
     * @param fen position as a string in Forsyth–Edwards notation
     */
    public Position(final String fen) {
        this.board = new Piece[NUMBER_OF_RANKS][NUMBER_OF_FILES];
        ForsythEdwardsNotation.fromString(this, fen);
    }

    /**
     * Copy constructor. For internal use only (the class is effectively immutable to callers).
     *
     * @param source position to copy
     */
    Position(Position source) {
        this.toMove = source.toMove;
        this.castlingsAvailable = source.castlingsAvailable;
        this.enPassantSquare = source.enPassantSquare;
        this.board = new Piece[NUMBER_OF_RANKS][];
        System.arraycopy(source.board, 0, this.board, 0, NUMBER_OF_RANKS);
    }

    /**
     * Returns which side is to move.
     *
     * @return {@link Colour#WHITE} or {@link Colour#BLACK}
     */
    public Colour getToMove() {
        return toMove;
    }

    void setToMove(Colour toMove) {
        this.toMove = toMove;
    }

    /**
     * Returns the piece at the given coordinates, or {@code null} if the square is empty.
     *
     * @param rank rank index (0–7)
     * @param file file index (0–7)
     * @return piece on the square, or {@code null} if empty
     */
    public Piece getPiece(int rank, int file) {
        return board[rank][file];
    }

    void setPiece(int rank, int file, Piece piece) {
        board[rank][file] = piece;
    }

    /**
     * Returns the piece on the given square, or {@code null} if the square is empty.
     *
     * @param square the square
     * @return piece on the square, or {@code null} if empty
     */
    public Piece getPiece(Square square) {
        return board[square.getRank()][square.getFile()];
    }

    void setPiece(Square square, Piece piece) {
        board[square.getRank()][square.getFile()] = piece;
    }

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public Set<CastlingType> getCastlingsAvailable() {
        return castlingsAvailable;
    }

    void setCastlingsAvailable(Set<CastlingType> castlingsAvailable) {
        this.castlingsAvailable = castlingsAvailable;
    }

    /**
     * Returns all squares occupied by a piece equal to the given one (type and colour).
     *
     * @param piece piece to search for
     * @return squares where such a piece stands
     */
    public List<Square> findSquaresWith(final Piece piece) {
        List<Square> squares = new ArrayList<>();

        for (int rank = 0; rank < NUMBER_OF_RANKS; ++rank) {
            for (int file = 0; file < NUMBER_OF_FILES; ++file) {
                Piece currentPiece = getPiece(rank, file);
                if (currentPiece != null && currentPiece.equals(piece)) {
                    squares.add(new Square(rank, file));
                }
            }
        }

        return squares;
    }

    /**
     * Returns the square of the king of the given colour, or {@code null} if that king is not on the board.
     *
     * @param colour colour of the king to find
     * @return square of that king, or {@code null}
     */
    public Square findSquareWithKing(final Colour colour) {
        for (int rank = 0; rank < NUMBER_OF_RANKS; ++rank) {
            for (int file = 0; file < NUMBER_OF_FILES; ++file) {
                Piece piece = getPiece(rank, file);
                if (piece != null && piece.is(KING)
                        && piece.is(colour)) {
                    return new Square(rank, file);
                }
            }
        }
        return null;
    }

    public Set<Square> squaresWithColour(final Colour colour) {
        HashSet<Square> squares = new HashSet<Square>();

        for (int rank = 0; rank < NUMBER_OF_RANKS; ++rank) {
            for (int file = 0; file < NUMBER_OF_FILES; ++file) {
                Piece piece = getPiece(rank, file);
                if (piece != null && piece.is(colour)) {
                    squares.add(new Square(rank, file));
                }
            }
        }

        return squares;
    }

    public Position performMove(Move move) {
        Position newPosition = copyPositionWithAffectedRanks(this,
                move);

        // apply piece movement
        newPosition.setPiece(move.getFrom(), null);

        if (move.isPromotion()) {
            // pawn promoted to a new piece type
            newPosition.setPiece(move.getTo(),
                    new Piece(move.getPromotion(), toMove));
        } else {
            newPosition.setPiece(move.getTo(), move.getPiece());
        }

        // en passant target square (if any)
        if (move.isPawnAdvancesTwo()) {
            int delta = move.getPiece().is(WHITE) ? -1 : +1;
            newPosition.enPassantSquare = new Square(move.getFrom().getRank()
                    + delta, move.getFrom().getFile());
        } else {
            newPosition.enPassantSquare = null;
        }

        if (move.isCastling()) {
            completeCastling(move, newPosition);
        } else {
            adjustCastlingRights(move, newPosition);
        }

        newPosition.toMove = this.toMove.otherColour();

        return newPosition;
    }

    /**
     * Returns whether the given castling type is still allowed.
     *
     * @param ct castling type, e.g. White kingside
     * @return {@code true} if that castling is still available
     */
    public boolean castlingAllowed(final CastlingType ct) {
        return castlingsAvailable.contains(ct);
    }

    /**
     * After the king has moved for castling, moves the corresponding rook and updates castling rights.
     */
    private void completeCastling(Move move, Position newPos) {
        if (move.isCastlingKingside()) {
            newPos.castlingsAvailable = EnumSet.copyOf(castlingsAvailable);
            switch (getToMove()) {
                case WHITE:
                    newPos.setPiece(h1, null);
                    newPos.setPiece(f1, new Piece(ROOK, WHITE));
                    newPos.castlingsAvailable.remove(WHITE_KINGSIDE);
                    newPos.castlingsAvailable.remove(WHITE_QUEENSIDE);
                    break;
                case BLACK:
                    newPos.setPiece(h8, null);
                    newPos.setPiece(f8, new Piece(ROOK, BLACK));
                    newPos.castlingsAvailable.remove(BLACK_KINGSIDE);
                    newPos.castlingsAvailable.remove(BLACK_QUEENSIDE);
                    break;
            }
        } else if (move.isCastlingQueenside()) {
            newPos.castlingsAvailable = EnumSet.copyOf(castlingsAvailable);
            switch (getToMove()) {
                case WHITE:
                    newPos.setPiece(a1, null);
                    newPos.setPiece(d1, new Piece(ROOK, WHITE));
                    newPos.castlingsAvailable.remove(WHITE_KINGSIDE);
                    newPos.castlingsAvailable.remove(WHITE_QUEENSIDE);
                    break;
                case BLACK:
                    newPos.setPiece(a8, null);
                    newPos.setPiece(d8, new Piece(ROOK, BLACK));
                    newPos.castlingsAvailable.remove(BLACK_KINGSIDE);
                    newPos.castlingsAvailable.remove(BLACK_QUEENSIDE);
                    break;
            }
        }
    }

    /**
     * Updates castling rights when the move is not castling (king or rook moves may forfeit rights).
     */
    private void adjustCastlingRights(Move move, Position newPos) {

        if (castlingsAvailable.size() > 0) {
            if (move.getPiece().is(KING)) {
                newPos.castlingsAvailable = EnumSet.copyOf(castlingsAvailable);
                switch (toMove) {
                    case WHITE:
                        newPos.castlingsAvailable.remove(WHITE_KINGSIDE);
                        newPos.castlingsAvailable.remove(WHITE_QUEENSIDE);
                        break;
                    case BLACK:
                        newPos.castlingsAvailable.remove(BLACK_KINGSIDE);
                        newPos.castlingsAvailable.remove(BLACK_QUEENSIDE);
                        break;
                }
            } else if (move.getPiece().is(ROOK)) {
                newPos.castlingsAvailable = EnumSet.copyOf(castlingsAvailable);
                switch (toMove) {
                    case WHITE:
                        if (move.getFrom().equals(a1)) {
                            newPos.castlingsAvailable.remove(WHITE_QUEENSIDE);
                        } else if (move.getFrom().equals(h1)) {
                            newPos.castlingsAvailable.remove(WHITE_KINGSIDE);
                        }
                        break;
                    case BLACK:
                        if (move.getFrom().equals(a8)) {
                            newPos.castlingsAvailable.remove(BLACK_QUEENSIDE);
                        } else if (move.getFrom().equals(h8)) {
                            newPos.castlingsAvailable.remove(BLACK_KINGSIDE);
                        }
                        break;
                }
            }
        }
    }

    private Position copyPositionWithAffectedRanks(Position source,
                                                   Move move) {
        Position newPos = new Position(source);

        int rank1 = move.getFrom().getRank();
        int rank2 = move.getTo().getRank();

        newPos.board[rank1] = new Piece[NUMBER_OF_FILES];
        System.arraycopy(source.board[rank1], 0, newPos.board[rank1],
                0, NUMBER_OF_FILES);
        if (rank2 != rank1) {
            newPos.board[rank2] = new Piece[NUMBER_OF_FILES];
            System.arraycopy(source.board[rank2], 0,
                    newPos.board[rank2], 0, NUMBER_OF_FILES);
        }

        return newPos;
    }

    /**
     * Returns whether the given square is empty.
     *
     * @param s square to test
     * @return {@code true} if no piece occupies the square
     */
    public boolean isFree(Square s) {
        return this.getPiece(s) == null;
    }

    /**
     * Returns the position as a FEN string.
     */
    @Override
    public String toString() {
        return ForsythEdwardsNotation.toString(this);
    }
}
