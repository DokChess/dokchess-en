/*
 * Copyright (c) 2010-2016 Stefan Zoerner
 *
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

import org.junit.Assert;
import org.junit.Test;

public class SquareTest {

    @Test
    public void stringConstruction() {

        Square a8 = new Square("a8");
        Assert.assertEquals(0, a8.getRank());
        Assert.assertEquals(0, a8.getFile());
        Assert.assertEquals("a8", a8.toString());

        Square h8 = new Square("h8");
        Assert.assertEquals(0, h8.getRank());
        Assert.assertEquals(7, h8.getFile());
        Assert.assertEquals("h8", h8.toString());

        Square a1 = new Square("a1");
        Assert.assertEquals(7, a1.getRank());
        Assert.assertEquals(0, a1.getFile());
        Assert.assertEquals("a1", a1.toString());

        Square h1 = new Square("h1");
        Assert.assertEquals(7, h1.getRank());
        Assert.assertEquals(7, h1.getFile());
        Assert.assertEquals("h1", h1.toString());
    }

    @Test
    public void coordinateConstruction() {

        Square a8 = new Square(0, 0);
        Assert.assertEquals("a8", a8.toString());

        Square h8 = new Square(0, 7);
        Assert.assertEquals("h8", h8.toString());

        Square a1 = new Square(7, 0);
        Assert.assertEquals("a1", a1.toString());

        Square h1 = new Square(7, 7);
        Assert.assertEquals("h1", h1.toString());
    }

    @Test
    public void compareWithEquals() {
        Square a1 = new Square("a1");
        Square h1 = new Square("h1");
        Square otherH1 = new Square("h1");

        Assert.assertFalse(a1.equals(h1));
        Assert.assertFalse(a1.equals(null));
        Assert.assertFalse(a1.equals("Hallo"));

        Assert.assertTrue(a1.equals(a1));
        Assert.assertTrue(h1.equals(otherH1));
    }

    @Test
    public void hashCodesAreTheSame() {
        Square h1 = new Square("h1");
        Square otherH1 = new Square("h1");
        Assert.assertTrue(h1.hashCode() == otherH1.hashCode());
    }
}