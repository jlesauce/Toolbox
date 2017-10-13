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

package org.jls.toolbox.widget.spinner;

import javax.swing.AbstractSpinnerModel;

/**
 * Modèle de données d'un spinner incrémental. Ce modèle permet d'incrémenter
 * d'un nombre de pas différent de 1 mais en interdisant les valeurs
 * intermédiaires.
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class IncrementalSpinnerModel extends AbstractSpinnerModel {

    private static final long serialVersionUID = 8638639885643183124L;

    private int value;
    private int offset;
    private Integer minimum;
    private Integer maximum;
    private int stepSize;

    /**
     * Permet d'instancier un modèle de données à partir de la valeur initiale, des
     * limites et du pas d'incrémentation.
     * 
     * @param value
     *            Valeur initiale du modèle.
     * @param minimum
     *            Valeur minimale.
     * @param maximum
     *            Valeur maximale.
     * @param stepSize
     *            Pas d'incrémentation entre deux valeurs consécutives.
     */
    public IncrementalSpinnerModel(int value, Integer minimum, Integer maximum, int stepSize) {
        super();
        this.value = value;
        this.offset = value;
        this.maximum = maximum;
        this.minimum = minimum;
        this.stepSize = stepSize;
    }

    @Override
    public Object getValue () {
        return this.value;
    }

    @Override
    public void setValue (Object value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("Value must be an instance of java.lang.Number");
        }
        int newVal = (int) value;

        // Si la nouvelle valeur est supérieure au maximum
        // (null implique pas de limite)
        if (this.maximum != null && newVal > this.maximum) {
            // On sature la valeur
            newVal = this.maximum;
        }

        // Si la nouvelle valeur est inférieure au minimum
        // (null implique pas de limite)
        if (this.minimum != null && newVal < this.minimum) {
            // On sature la valeur
            newVal = this.minimum;
        }

        // Si la valeur n'est pas autorisée
        if (newVal % this.stepSize != 0) {
            // On l'arrondi à la valeur autorisée la plus proche
            newVal = roundValue(newVal);
        }

        // On met à jour le modèle
        this.value = newVal;
        fireStateChanged();
    }

    @Override
    public Object getNextValue () {
        return incrementValue(+1);
    }

    @Override
    public Object getPreviousValue () {
        return incrementValue(-1);
    }

    /**
     * Renvoie la valeur du modèle incrémentée du nombre de pas spécifié. On
     * récupère la valeur du modèle et on l'incrémente du nombre de pas multiplié
     * par le pas d'incrémentation <i>stepSize</i>. La fonction renvoie simplement
     * la valeur incrémentée (le modèle n'est pas modifié).
     * 
     * @param inc
     *            Nombre de pas d'incrémentation.
     * @return Valeur du modèle incrémentée.
     */
    private int incrementValue (int inc) {
        int newValue = this.value + this.stepSize * inc;
        // Si la valeur incrémentée est supérieure au maximum
        if (this.maximum != null && newValue > this.maximum) {
            // On sature la valeur
            return this.maximum;
        }
        // Si la valeur incrémentée est inférieure au minimum
        if (this.minimum != null && newValue < this.minimum) {
            // On sature la valeur
            return this.minimum;
        }
        // Sinon on renvoie la valeur incrémentée
        else {
            // Si la valeur est une valeur autorisée
            if (newValue % this.stepSize == 0) {
                return newValue;
            } else { // Sinon on l'arrondi
                return roundValue(newValue);
            }
        }
    }

    /**
     * Permet d'arrondir une valeur interdite à l'entier autorisé inférieur en
     * tenant compte du gap et de l'offset par rapport à 0.
     * 
     * @param value
     *            Valeur à arrondir.
     * @return Valeur autorisée la plus proche.
     */
    private int roundValue (int value) {
        int newVal = (value - this.offset) / this.stepSize;
        newVal = newVal * this.stepSize + this.offset;

        return newVal;
    }

    public int getOffset () {
        return this.offset;
    }

    public void setOffset (int offset) {
        this.offset = offset;
    }

    public Integer getMinimum () {
        return this.minimum;
    }

    public void setMinimum (Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum () {
        return this.maximum;
    }

    public void setMaximum (Integer maximum) {
        this.maximum = maximum;
    }

    public int getStepSize () {
        return this.stepSize;
    }

    public void setStepSize (int stepSize) {
        this.stepSize = stepSize;
    }

    public void setValue (int value) {
        this.value = value;
    }
}
