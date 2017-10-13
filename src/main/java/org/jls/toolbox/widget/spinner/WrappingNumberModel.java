/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 LE SAUCE Julien
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

package org.jls.toolbox.widget.spinner;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Permet de créer un modèle pour {@link JSpinner} qui permet lorsque le minimum
 * ou le maximum est atteint de repartir sur, respectivement, le maximum ou le
 * minimum, au lieu de bloquer la valeur aux limites.
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class WrappingNumberModel extends SpinnerNumberModel {

    private static final long serialVersionUID = 8777236444254445358L;

    /**
     * Permet d'instancier un modèle par défaut.
     */
    public WrappingNumberModel() {
        this(0, null, null, 1);
    }

    /**
     * Permet d'instancier un modèle en spécifiant les valeurs limites et la valeur
     * d'incrément.
     * 
     * @param value
     *            Valeur initiale du modèle.
     * @param minimum
     *            Valeur minimale.
     * @param maximum
     *            Valeur maximale.
     * @param stepSize
     *            Incrément entre deux valeurs consécutives.
     */
    public WrappingNumberModel(int value, int minimum, int maximum, int stepSize) {
        this(value, new Integer(minimum), new Integer(maximum), (Number) stepSize);
    }

    /**
     * Permet d'instancier un modèle en spécifiant les valeurs limites et la valeur
     * d'incrément.
     * 
     * @param value
     *            Valeur initiale du modèle.
     * @param minimum
     *            Valeur minimale.
     * @param maximum
     *            Valeur maximale.
     * @param stepSize
     *            Incrément entre deux valeurs consécutives.
     */
    public WrappingNumberModel(long value, long minimum, long maximum, long stepSize) {
        this(value, new Long(minimum), new Long(maximum), (Number) stepSize);
    }

    /**
     * Permet d'instancier un modèle en spécifiant les valeurs limites et la valeur
     * d'incrément.
     * 
     * @param value
     *            Valeur initiale du modèle.
     * @param minimum
     *            Valeur minimale.
     * @param maximum
     *            Valeur maximale.
     * @param stepSize
     *            Incrément entre deux valeurs consécutives.
     */
    public WrappingNumberModel(Number value, Comparable<?> minimum, Comparable<?> maximum, Number stepSize) {
        super(value, minimum, maximum, stepSize);
    }

    @Override
    public Object getNextValue () {
        if (getValue().equals(getMaximum())) {
            return getMinimum();
        } else {
            return super.getNextValue();
        }
    }

    @Override
    public Object getPreviousValue () {
        if (getValue().equals(getMinimum())) {
            return getMaximum();
        } else {
            return super.getPreviousValue();
        }
    }
}
