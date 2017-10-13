/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 LE SAUCE Julien
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jls.toolbox.math;

/**
 * Permet de représenter un point d'après ses coordonnées cartésiennes (x,y,z).
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class Point {

    private double x;
    private double y;
    private double z;

    /**
     * Permet d'instancier un point à partir de ses coordonnées cartésiennes (x,y).
     * 
     * @param x
     *            Abscisse du point.
     * @param y
     *            Ordonnée du point.
     */
    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
        this.z = 0.0;
    }

    /**
     * Permet d'instancier un point à partir de ses coordonnées cartésiennes
     * (x,y,z).
     * 
     * @param x
     *            Abscisse du point.
     * @param y
     *            Ordonnée du point.
     * @param z
     *            Cote du point.
     */
    public Point(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX () {
        return this.x;
    }

    public void setX (final double x) {
        this.x = x;
    }

    public double getY () {
        return this.y;
    }

    public void setY (final double y) {
        this.y = y;
    }

    public double getZ () {
        return this.z;
    }

    public void setZ (final double z) {
        this.z = z;
    }
}
