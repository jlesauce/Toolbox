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

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;

import org.jls.toolbox.widget.format.HexFormatter;

/**
 * Implémentation de l'éditeur par défaut {@link DefaultEditor} d'un
 * {@link JSpinner} afin de créer un éditeur de nombres au format hexadécimal.
 * 
 * @author LE SAUCE Julien
 * @date Feb 24, 2015
 */
public class HexSpinnerEditor extends DefaultEditor {

	private static final long serialVersionUID = -802407674000716992L;

	/**
	 * Permet d'instancier un éditeur hexadécimal et de lui associer un
	 * {@link HexSpinner}.
	 * 
	 * @param spinner
	 *            Spinner hexadécimal.
	 */
	public HexSpinnerEditor (HexSpinner spinner) {
		super(spinner);
		HexSpinnerModel model = spinner.getModel();
		HexFormatter formatter = new HexFormatter(model.getMinimum(), model.getMaximum(), false, false, false);
		JFormattedTextField ftextfield = new JFormattedTextField(formatter);

		ftextfield.setName("HexSpinner.formattedTextField");
		ftextfield.setValue(spinner.getValue());
		ftextfield.addPropertyChangeListener(this);
		ftextfield.setEditable(true);
		ftextfield.setInheritsPopupMenu(true);
		ftextfield.setHorizontalAlignment(JTextField.RIGHT);

		String toolTipText = spinner.getToolTipText();
		if (toolTipText != null) {
			ftextfield.setToolTipText(toolTipText);
		}
		remove(getComponent(0));
		add(ftextfield);
	}
}