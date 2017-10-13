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

import java.text.ParseException;

import javax.swing.text.DefaultFormatter;

/**
 * Permet de créer un formatter pour {@link IncrementalSpinner}.
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class IncrementalFormatter extends DefaultFormatter {

    private static final long serialVersionUID = 364245243908040450L;

    private final Integer minimum;
    private final Integer maximum;

    /**
     * Permet d'instancier un formatter en spécifiant les valeurs limites du modèle.
     * 
     * @param min
     *            Valeur minimale.
     * @param max
     *            Valeur maximale.
     */
    public IncrementalFormatter(Integer min, Integer max) {
        super();
        this.maximum = max;
        this.minimum = min;
        setAllowsInvalid(true);
        setCommitsOnValidEdit(false);
        setOverwriteMode(false);
    }

    @Override
    public Object stringToValue (String text) throws ParseException {
        Integer val = null;
        // Si la chaîne est vide
        if (text.isEmpty()) {
            // On renvoie la valeur minimale si elle existe, sinon 0
            if (this.minimum != null) {
                val = this.minimum;
            } else {
                val = 0;
            }
        } else {
            // On essaye de parser la chaîne de texte en décimal
            try {
                val = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                throw new ParseException("Incorrect decimal value.", 0);
            }
            // Si la valeur est supérieure au maximum
            if (this.maximum != null) {
                // On sature la valeur et on lance une exception
                if (val > this.maximum) {
                    val = this.maximum;
                    throw new ParseException("(value <= maximum) is false.", 0);
                }
            }
            // Si la valeur est inférieure au minimum
            if (this.minimum != null) {
                // On sature la valeur et on lance une exception
                if (val < this.minimum) {
                    val = this.minimum;
                    throw new ParseException("(value >= minimum) is false.", 0);
                }
            }
        }
        return val;
    }

    @Override
    public String valueToString (Object value) throws ParseException {
        // Si la valeur reçue est une chaîne
        if (value instanceof String) {
            String text = (String) value;
            Integer val = Integer.parseInt(text);
            // S'il y a un maximum et que la valeur est supérieure à celui-ci
            if (this.maximum != null) {
                if (val > this.maximum) {
                    // On sature la valeur
                    val = this.maximum;
                }
            }
            // S'il y a un minimum et que la valeur est inférieure à celui-ci
            if (this.minimum != null) {
                if (val < this.minimum) {
                    // On sature la valeur
                    val = this.minimum;
                }
            }
            return Integer.toString(val);
        }
        // Sinon si la valeur reçue n'est pas une chaîne
        else {
            // Si c'est un Integer
            if (value instanceof Integer) {
                int val = ((Integer) value).intValue();

                // S'il y a un maximum et que la valeur est supérieure à
                // celui-ci
                if (this.maximum != null) {
                    if (val > this.maximum) {
                        // On sature la valeur
                        val = this.maximum;
                    }
                }
                // S'il y a un minimum et que la valeur est inférieure à
                // celui-ci
                if (this.minimum != null) {
                    if (val < this.minimum) {
                        // On sature la valeur
                        val = this.minimum;
                    }
                }
                return Integer.toString(val);
            }
        }
        throw new ParseException("Invalid input", 0);
    }
}
