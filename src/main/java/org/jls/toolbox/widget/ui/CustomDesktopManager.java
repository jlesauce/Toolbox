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

package org.jls.toolbox.widget.ui;

import java.awt.Dimension;

import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * {@link DesktopManager} permettant de s'assurer que lorsque l'utilisateur
 * déplace une fenêtre contenue dans un {@link JDesktopPane}, celle-ci ne puisse
 * pas sortir des limites du panneau.
 * 
 * @author LE SAUCE Julien
 * @date Feb 19, 2015
 */
public class CustomDesktopManager extends DefaultDesktopManager {

	private static final long serialVersionUID = 6139283750584155275L;

	@Override
	public void dragFrame (JComponent f, int newX, int newY) {
		if (f instanceof JInternalFrame) {
			JInternalFrame frame = (JInternalFrame) f;
			JDesktopPane desk = frame.getDesktopPane();
			Dimension d = desk.getSize();
			boolean tooFarLeft = newX < 0;
			boolean tooFarRight = newX + frame.getWidth() > d.width;
			boolean tooHigh = newY < 0;
			boolean tooLow = newY + frame.getHeight() > d.height;
			int x = newX, y = newY;
			// Horizontalement
			if (tooFarLeft) { // Trop à gauche
				x = 0;
			} else if (tooFarRight) { // Trop à droite
				x = d.width - Math.min(frame.getWidth(), d.width);
			}
			// Verticalement
			if (tooHigh) { // Trop haut
				y = 0;
			} else if (tooLow) { // Trop bas
				y = d.height - Math.min(frame.getHeight(), d.height);
			}
			super.dragFrame(f, x, y);
		} else {
			super.dragFrame(f, newX, newY);
		}
	}
}