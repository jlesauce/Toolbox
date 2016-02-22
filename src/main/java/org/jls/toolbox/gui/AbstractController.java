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

package org.jls.toolbox.gui;

/**
 * Represents a controller in the Model-View-Controller pattern.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public abstract class AbstractController {

	private final AbstractModel model;

	/**
	 * Instanciates a default controller.
	 * 
	 * @param model
	 *            Data model associated with this controller.
	 */
	public AbstractController (final AbstractModel model) {
		this.model = model;
	}

	/**
	 * Creates the associated view.
	 * 
	 * @return Associated view.
	 */
	public abstract AbstractView createGui ();

	/**
	 * Returns the data model.
	 * 
	 * @return Data model.
	 */
	public AbstractModel getModel () {
		return this.model;
	}
}