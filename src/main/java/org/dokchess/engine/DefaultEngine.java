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

package org.dokchess.engine;

import org.dokchess.domain.Move;
import org.dokchess.domain.Position;
import org.dokchess.engine.eval.ReineMaterialEvaluation;
import org.dokchess.engine.search.MinimaxParalleleSuche;
import org.dokchess.opening.Eroeffnungsbibliothek;
import org.dokchess.rules.ChessRules;
import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * Default-Implementierung einer Engine.
 * <p/>
 * Ben&ouml;tigt zum Arbeiten eine Implementierung der Spielregeln, die
 * Er&ouml;ffnungsbibliothek ist optional.
 *
 * @author StefanZ
 */
public class DefaultEngine implements Engine {

    private Position stellung;

    private ZugErmitteln zugErmitteln;

    public DefaultEngine(ChessRules chessRules) {
        this(chessRules, null);
    }

    public DefaultEngine(ChessRules chessRules,
                         Eroeffnungsbibliothek eroeffnungsbibliothek) {

        this.stellung = new Position();

        MinimaxParalleleSuche minimax = new MinimaxParalleleSuche();
        minimax.setTiefe(4);
        minimax.setChessRules(chessRules);
        minimax.setEvaluation(new ReineMaterialEvaluation());

        AusSuche ausSuche = new AusSuche(minimax);

        if (eroeffnungsbibliothek != null) {
            this.zugErmitteln = new AusBibliothek(eroeffnungsbibliothek, ausSuche);
        } else {
            this.zugErmitteln = ausSuche;
        }

    }

    @Override
    public void setupPieces(Position stellung) {
        this.stellung = stellung;
        zugErmitteln.aktuelleErmittlungBeenden();
    }

    @Override
    public Observable<Move> determineYourMove() {
        ReplaySubject<Move> subject = ReplaySubject.create();
        zugErmitteln.ermittelZug(stellung, subject);
        return subject;
    }

    @Override
    public void ziehen(Move zug) {
        stellung = stellung.performMove(zug);
        zugErmitteln.aktuelleErmittlungBeenden();
    }

    @Override
    public void schliessen() {
        zugErmitteln.aktuelleErmittlungBeenden();
    }
}
