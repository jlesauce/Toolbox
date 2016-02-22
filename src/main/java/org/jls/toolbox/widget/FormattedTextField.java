/*#
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
 #*/

package org.jls.toolbox.widget;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;

import org.jls.toolbox.widget.format.Format;

/**
 * Permet de créer un {@link JFormattedTextField}.
 * 
 * @author LE SAUCE Julien
 * @date Feb 24, 2015
 */
public class FormattedTextField extends JFormattedTextField {

	private static final long serialVersionUID = 2547767831102601192L;

	/**
	 * Permet d'instancier un champ de texte au format par défaut.
	 */
	public FormattedTextField () {
		super();
	}

	/**
	 * Permet d'instancier un champ de texte au format par défaut.
	 * 
	 * @param groupingSeparator
	 *            Permet de spécifier le caractère de séparation des milliers.
	 */
	public FormattedTextField (final char groupingSeparator) {
		super(Format.getDecimalFormat(groupingSeparator, '.'));
	}

	/**
	 * Permet d'instancier un champ de texte en spécifiant le format utilisé.
	 * 
	 * @param format
	 *            {@link NumberFormat} permettant d'afficher la valeur du champ
	 *            de texte.
	 */
	public FormattedTextField (final NumberFormat format) {
		super(format);
	}
}