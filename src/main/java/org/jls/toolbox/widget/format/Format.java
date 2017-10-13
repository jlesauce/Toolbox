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

package org.jls.toolbox.widget.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;

/**
 * Permet de créer différents formatter destiné à être utilisé dans des
 * {@link JFormattedTextField} ou des {@link JSpinner}.
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class Format {

    /**
     * Format décimal par défaut permettant d'afficher des nombres entiers ou à
     * virgule.
     * 
     * @return Format décimal.
     */
    public static DecimalFormat getDecimalFormat () {
        return getDecimalFormat("#,##0.###", ',', '.');
    }

    /**
     * Format décimal permettant d'afficher des nombres entiers ou à virgule.
     * 
     * @param groupingSeperator
     *            Caractère de séparation des milliers.
     * @param decimalSeparator
     *            Caractère de séparation des nombres à virgule.
     * @return Format décimal.
     */
    public static DecimalFormat getDecimalFormat (char groupingSeperator, char decimalSeparator) {
        return getDecimalFormat("#,##0.###", groupingSeperator, decimalSeparator);
    }

    /**
     * Format décimal permettant d'afficher des nombres entiers ou à virgule.
     * 
     * @param pattern
     *            Voir {@link DecimalFormat}.
     * @return Format décimal.
     */
    public static DecimalFormat getDecimalFormat (final String pattern) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        return new DecimalFormat(pattern, symbols);
    }

    /**
     * Format décimal permettant d'afficher des nombres entiers ou à virgule.
     * 
     * @param pattern
     *            Voir {@link DecimalFormat}.
     * @param groupingSeperator
     *            Caractère de séparation des milliers.
     * @param decimalSeparator
     *            Caractère de séparation des nombres à virgule.
     * @return Format décimal.
     */
    public static DecimalFormat getDecimalFormat (final String pattern, char groupingSeperator, char decimalSeparator) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(groupingSeperator);
        return new DecimalFormat(pattern, symbols);
    }
}
