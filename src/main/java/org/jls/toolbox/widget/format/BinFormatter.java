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

package org.jls.toolbox.widget.format;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.text.DefaultFormatter;

/**
 * Permet de créer un formatter binaire, c'est-à-dire un formatter interdisant
 * l'utilisation des caractères autres que [0-1]. Il permet également de fixer
 * une limite minimale et maximale au nombre tapé. Ce formatter peut être
 * utilisé dans les {@link JFormattedTextField} ou dans les {@link JSpinner} par
 * exemple.
 * 
 * @author LE SAUCE Julien
 * @date Feb 27, 2015
 */
public class BinFormatter extends DefaultFormatter {

    private static final long serialVersionUID = 6119814107073219693L;

    private Long minimum;
    private Long maximum;
    private int nbDigits;

    /**
     * Permet d'instancier un formatter et de spécifier les valeurs limites du
     * modèle.
     * 
     * @param min
     *            Valeur minimale pouvant être affichée.
     * @param max
     *            Valeur maximale pouvant être affichée.
     */
    public BinFormatter(Long min, Long max) {
        this(min, max, true, true, true);
    }

    /**
     * Permet d'instancier un formatter et de lui spécifier ses différents
     * paramètres.
     * 
     * @param min
     *            Valeur minimale pouvant être affichée.
     * @param max
     *            Valeur maximale pouvant être affichée.
     * @param allowInvalid
     *            Voir {@link DefaultFormatter#setAllowsInvalid(boolean)}.
     * @param commitsOnValidEdit
     *            Voir {@link DefaultFormatter#setCommitsOnValidEdit(boolean)}.
     * @param overwriteMode
     *            Voir {@link DefaultFormatter#setOverwriteMode(boolean)}.
     */
    public BinFormatter(Long min, Long max, boolean allowInvalid, boolean commitsOnValidEdit, boolean overwriteMode) {
        this(min, max, 0, allowInvalid, commitsOnValidEdit, overwriteMode);
    }

    /**
     * Permet d'instancier un formatter et de lui spécifier ses différents
     * paramètres.
     * 
     * @param min
     *            Valeur minimale pouvant être affichée.
     * @param max
     *            Valeur maximale pouvant être affichée.
     * @param nbDigits
     *            Permet de spécifier le nombre de digits sur lesquels effectuer un
     *            zero padding (ceci permet d'afficher les zéros non significatifs
     *            de la chaîne binaire affichée). Il est possible de ne pas
     *            effectuer de zero padding en spécifiant la valeur <i>0</i>.
     * @param allowInvalid
     *            Voir {@link DefaultFormatter#setAllowsInvalid(boolean)}.
     * @param commitsOnValidEdit
     *            Voir {@link DefaultFormatter#setCommitsOnValidEdit(boolean)}.
     * @param overwriteMode
     *            Voir {@link DefaultFormatter#setOverwriteMode(boolean)}.
     */
    public BinFormatter(Long min, Long max, int nbDigits, boolean allowInvalid, boolean commitsOnValidEdit,
            boolean overwriteMode) {
        super();
        this.maximum = max;
        this.minimum = min;
        this.nbDigits = nbDigits;
        setAllowsInvalid(allowInvalid);
        setCommitsOnValidEdit(commitsOnValidEdit);
        setOverwriteMode(overwriteMode);
    }

    @Override
    public Object stringToValue (String text) throws ParseException {
        Long val = null;
        // Si la chaîne est vide
        if (text.isEmpty()) {
            // On renvoie la valeur minimale si elle existe, sinon 0
            if (this.minimum != null) {
                val = this.minimum;
            } else {
                val = 0L;
            }
        } else {
            // On essaye de parser la chaîne de texte binaire en décimal
            try {
                val = Long.parseLong(text, 2);
            } catch (NumberFormatException e) {
                throw new ParseException("Incorrect binary string", 0);
            }
            // Si la valeur est supérieure au maximum
            if (this.maximum != null) {
                // On sature la valeur et on lance une exception
                if (val > this.maximum) {
                    val = this.maximum;
                    throw new ParseException("Value must be smaller than maximum", 0);
                }
            }
            // Si la valeur est inférieure au minimum
            if (this.minimum != null) {
                // On sature la valeur et on lance une exception
                if (val < this.minimum) {
                    val = this.minimum;
                    throw new ParseException("Value must be higher than minimum", 0);
                }
            }
        }
        return val;
    }

    @Override
    public String valueToString (Object value) throws ParseException {
        if (value instanceof Long) {
            long val = ((Long) value).longValue();
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
            return zeroPadding((long) value, this.nbDigits);
        } else {
            throw new ParseException("Value must be an instance of java.lang.Long", 0);
        }
    }

    /**
     * Permet d'effectuer un zero padding sur la chaîne binaire renvoyée afin
     * d'afficher les zéros non significatifs.
     * 
     * @param value
     *            Valeur décimale à afficher au format binaire.
     * @param nbDigits
     *            Nombre de digits sur lesquels effectuer un zero padding.
     * @return Chaîne de texte au format binaire affichant les zéros non
     *         significatifs sur le nombre de digits spécifié.
     */
    private static String zeroPadding (long value, int nbDigits) {
        String binStr = Long.toBinaryString(value);
        while (binStr.length() < nbDigits) {
            binStr = "0" + binStr;
        }
        return binStr;
    }
}
