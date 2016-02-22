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

import java.awt.event.ActionEvent;
import java.util.Set;

/**
 * Listener on the actions triggered by an {@link AbstractView}.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public class CommandListener implements java.awt.event.ActionListener {

	private final AbstractView view;
	private final Set<?> listeners;

	/**
	 * Instanciates a new action.
	 * 
	 * @param view
	 *            Graphical interface publishing the user actions.
	 */
	public CommandListener (final AbstractView view) {
		this.view = view;
		this.listeners = view.getListeners();
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		for (Object o : this.listeners) {
			Invoker.invoke(o, e, this.view);
		}
	}
}