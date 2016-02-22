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

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * Permet de créer un spinner incrémental. La différence avec le spinner normal
 * est que les valeurs intermédiaires (entre chaque pas d'incrémentation) sont
 * interdites. Lorsqu'une valeur intermédiaire est tapée, on remplace la valeur
 * par son arrondi le plus proche dans la plage des valeurs autorisées.
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class IncrementalSpinner extends JSpinner {

	private static final long serialVersionUID = 2778798208761728109L;

	/**
	 * Permet d'instancier un spinner incrémental.
	 * 
	 * @param spinnerModel
	 *            Modèle de données du spinner.
	 */
	public IncrementalSpinner (IncrementalSpinnerModel spinnerModel) {
		super(spinnerModel);
	}

	@Override
	protected JComponent createEditor (SpinnerModel model) {
		return new IncrementalEditor(this);
	}

	@Override
	public IncrementalSpinnerModel getModel () {
		return (IncrementalSpinnerModel) super.getModel();
	}
}