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

package org.jls.toolbox.widget.spinner;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * {@link JSpinner} permettant d'afficher des nombres à virgules.
 * 
 * @author LE SAUCE Julien
 * @date Mar 12, 2015
 * @param <T>
 *            Type de données à aficher.
 */
public class DecimalSpinner<T> extends JSpinner {

	private static final long serialVersionUID = -827579762711543293L;

	/**
	 * Permet d'instancier un spinner.
	 * 
	 * @param value
	 *            Valeur initiale du spinner.
	 * @param min
	 *            Valeur minimale.
	 * @param max
	 *            Valeur maximale.
	 * @param step
	 *            Pas entre deux valeurs consécutives.
	 */
	public DecimalSpinner (Number value, Comparable<T> min, Comparable<T> max, Number step) {
		super(new SpinnerNumberModel(value, min, max, step));
		setLocale(Locale.ENGLISH);
		NumberEditor editor = new NumberEditor(this);
		editor.setLocale(Locale.ENGLISH);
		editor.getFormat().setGroupingUsed(false);
		editor.getFormat().applyLocalizedPattern("#.###");
		editor.getFormat().applyPattern("#.###");
		editor.getFormat().setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		setEditor(editor);
	}
}