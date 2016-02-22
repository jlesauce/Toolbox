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
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;

/**
 * Permet de créer un éditeur pour {@link IncrementalSpinner}.
 * 
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class IncrementalEditor extends DefaultEditor {

	private static final long serialVersionUID = -5089568991074611043L;

	/**
	 * Permet d'instancier l'éditeur et de lui associer le spinner.
	 * 
	 * @param spinner
	 *            Spinner incrémental.
	 */
	public IncrementalEditor (IncrementalSpinner spinner) {
		super(spinner);
		IncrementalSpinnerModel model = spinner.getModel();
		IncrementalFormatter formatter = new IncrementalFormatter(model.getMinimum(), model.getMaximum());
		JFormattedTextField ftextfield = new JFormattedTextField(formatter);

		ftextfield.setName("IncrementalSpinner.formattedTextField");
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