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
 * Beschreibt eine Spielsituation. Hierzu zaehlen die Figuren auf dem
 * Brett(Stellung), die Farbe am Zug, Rochaderechte usw.
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
     * Creates the starting position. White begins.
     */
    public Position() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    /**
     * Creates a position from the FEN String representation
     *
     * @param fen Stellung als zeichenkette in FEN-Notation
     */
    public Position(final String fen) {
        this.board = new Piece[NUMBER_OF_RANKS][NUMBER_OF_FILES];
        ForsythEdwardsNotation.fromString(this, fen);
    }

    /**
     * Copy constructor. For internal use only (class is imutable anyway).
     *
     * @param s
     */
    Position(Position s) {
        this.toMove = s.toMove;
        this.castlingsAvailable = s.castlingsAvailable;
        this.enPassantSquare = s.enPassantSquare;
        this.board = new Piece[NUMBER_OF_RANKS][];
        System.arraycopy(s.board, 0, this.board, 0, NUMBER_OF_RANKS);
    }

    /**
     * Returns which side is next to move.
     *
     * @return black or white
     */
    public Colour getToMove() {
        return toMove;
    }

    void setToMove(Colour toMove) {
        this.toMove = toMove;
    }

    /**
     * Return the piece at the given coordinates, or null if free.
     *
     * @param rank rank of the square
     * @param file line of the sqaure
     * @return piece occupying the square, or null if empty
     */
    public Piece getPiece(int rank, int file) {
        return board[rank][file];
    }

    void setPiece(int rank, int file, Piece piece) {
        board[rank][file] = piece;
    }

    /**
     * Return the piece at the given square, or null if free.
     *
     * @param square the square
     * @return piece occupying the square, or null if empty
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
     * Get a list of squares occupied by a certain piece (type and colour).
     *
     * @param piece searched piece
     * @return list of sqaures occupied by this piece
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
     * Get the sqaure with the given king, of null if he is missing.
     *
     * @param colour colour of the king to find
     * @return sqaure with this king, or null if none
     */
    public Square findSquareWithKing(final Colour colour) {
        for (int reihe = 0; reihe < NUMBER_OF_RANKS; ++reihe) {
            for (int linie = 0; linie < NUMBER_OF_FILES; ++linie) {
                Piece aktFigur = getPiece(reihe, linie);
                if (aktFigur != null && aktFigur.is(KING)
                        && aktFigur.is(colour)) {
                    return new Square(reihe, linie);
                }
            }
        }
        return null;
    }

    public Set<Square> squaresWithColour(final Colour colour) {
        HashSet<Square> felder = new HashSet<Square>();

        for (int rank = 0; rank < NUMBER_OF_RANKS; ++rank) {
            for (int file = 0; file < NUMBER_OF_FILES; ++file) {
                Piece aktFigur = getPiece(rank, file);
                if (aktFigur != null && aktFigur.is(colour)) {
                    felder.add(new Square(rank, file));
                }
            }
        }

        return felder;
    }

    public Position performMove(Move move) {
        Position neueStellung = copyPositionWithAffectedRanks(this,
                move);



        // move
        neueStellung.setPiece(move.getFrom(), null);


        if (move.isPromotion()) {
            // Umwandlung des Bauern in neue Figur
            neueStellung.setPiece(move.getTo(),
                    new Piece(move.getPromotion(), toMove));
        } else {
            neueStellung.setPiece(move.getTo(), move.getPiece());
        }

        // En Passant Feld setzen
        if (move.isPawnAdvancesTwo()) {
            int delta = move.getPiece().is(WHITE) ? -1 : +1;
            neueStellung.enPassantSquare = new Square(move.getFrom().getRank()
                    + delta, move.getFrom().getFile());
        } else {
            neueStellung.enPassantSquare = null;
        }

        if (move.isCastling()) {
            rochadeDurchfuehrenBehandeln(move, neueStellung);
        } else {
            rochadeRechteKorrigieren(move, neueStellung);
        }

        neueStellung.toMove = this.toMove.otherColour();

        return neueStellung;
    }

    /**
     * Check whether a castling is still allowed.
     *
     * @param ct castling type, e.g. white king side
     * @return true, if this castling is still allowed
     */
    public boolean castlingAllowed(final CastlingType ct) {
        return castlingsAvailable.contains(ct);
    }

    private void rochadeDurchfuehrenBehandeln(Move move, Position newPos) {
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

    private void rochadeRechteKorrigieren(Move move, Position newPos) {

        // Ggf. Rochaderechte loeschen
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
     * Returns if the given square is free. I.e. no piece is on this square.
     *
     * @param s sqaure to test
     * @return true, if no piece on this square.
     */
    public boolean isFree(Square s) {
        return this.getPiece(s) == null;
    }

    /**
     * Returns the position as FEN.
     */
    @Override
    public String toString() {
        return ForsythEdwardsNotation.toString(this);
    }
}
