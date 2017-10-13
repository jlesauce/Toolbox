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

package org.jls.toolbox.math.chart;

import java.util.Observable;

/**
 * Modèle abstrait permettant de représenter les données d'une courbe.
 * 
 * @author LE SAUCE Julien
 * @date Feb 13, 2015
 */
public abstract class AbstractXYZDataModel extends Observable {

    /**
     * Permet de récupérer la valeur en abscisse à l'indice donné dans la série de
     * données.
     * 
     * @param index
     *            Indice du point souhaité dans la série de données.
     * @return Abscisse du point.
     */
    public abstract double getX (final int index);

    /**
     * Permet de récupérer la valeur en ordonnée à l'indice donné dans la série de
     * données.
     * 
     * @param index
     *            Indice du point souhaité dans la série de données.
     * @return Ordonnée du point.
     */
    public abstract double getY (final int index);

    /**
     * Permet de récupérer la valeur en z à l'indice donné dans la série de données.
     * 
     * @param index
     *            Indice du point souhaité dans la série de données.
     * @return Valeur en z du point.
     */
    public abstract double getZ (final int index);

    /**
     * Permet de récupérer la taille de la série de données.
     * 
     * @return Taille de la série de données.
     */
    public abstract int getSize ();

    /**
     * Permet de récupérer l'identifiant de la série de données (le nom de la
     * courbe).
     * 
     * @return Identifiant de la série de données.
     */
    public abstract String getKey ();

    /**
     * Permet à partir des valeurs x et y de récupérer la valeur en z. Pour cela on
     * va balayer la liste des données de la courbe à la recherche du couple
     * correspondant à x et y à un epsilon près.
     * 
     * @param x
     *            Valeur en abscisse.
     * @param y
     *            Valeur en ordonnée.
     * @param epsilon
     *            Seuil permettant de déterminer l'égalité à un epsilon près.
     * @return Indice du point dans la série de données.
     */
    public abstract int indexOf (double x, double y, double epsilon);

    /**
     * Permet de notifier les différents observateurs du modèle d'une modification
     * de celui-ci.
     * 
     * @param arg
     *            (Optionnel) Il est possible de spécifier un objet à l'observateur.
     */
    public final void notifyChanged (Object... arg) {
        setChanged();
        if (arg.length == 0) {
            notifyObservers();
        } else if (arg.length == 1 && arg[0] != null) {
            notifyObservers(arg[0]);
        } else {
            throw new IllegalArgumentException("Only one argument allowed");
        }
        clearChanged();
    }
}
