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

package org.jls.toolbox.widget.dialog;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

/**
 * Permet de créer une fenêtre de dialogue customisable. La fenêtre créée sera
 * modale par défaut (voir {@link JDialog#setModal(boolean)}. La fenêtre peut
 * alors être affichée via la méthode {@link #showDialog()}.
 * 
 * @author LE SAUCE Julien
 * @date Mar 16, 2015
 */
public class Dialog extends JDialog {

	private static final long serialVersionUID = 2354616214737733368L;

	private final JFrame parent;
	private final Component content;

	/**
	 * Permet d'instancier une fenêtre de dialogue.
	 * 
	 * @param parent
	 *            Parent de la fenêtre.
	 * @param title
	 *            Titre de la fenêtre de dialogue.
	 * @param content
	 *            Composant à ajouter à la fenêtre de dialogue.
	 */
	public Dialog (final JFrame parent, final String title, final Component content) {
		super(parent, title, true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		this.parent = parent;
		this.content = content;
	}

	/**
	 * Permet d'afficher le dialogue à l'utilisateur.
	 * 
	 * @return Option sélectionnée par l'utilisateur (OK ou Cancel).
	 */
	public int showDialog () {
		createComponents();
		setStyle();
		addListeners();
		pack();
		setLocationRelativeTo(this.parent);
		setVisible(true);
		return 0;
	}

	/**
	 * Action réalisée lorsque l'utilisateur clique sur le bouton <i>OK</i>.
	 */
	protected void onValidAction () {
		dispose();
	}

	/**
	 * Action réalisée lorsque l'utilisateur clique sur le bouton <i>Cancel</i>.
	 */
	protected void onCancelAction () {
		dispose();
	}

	/**
	 * Permet d'instancier les différents éléments qui composent l'interface
	 * graphique.
	 */
	protected void createComponents () {
		//
	}

	/**
	 * Permet de créer l'interface graphique à partir des éléments qui la
	 * compose.
	 */
	protected void setStyle () {
		getContentPane().setLayout(new MigLayout("fill"));
		getContentPane().add(this.content, "grow, wrap");
	}

	/**
	 * Permet d'ajouter les différents écouteurs sur les composants de
	 * l'interface graphique.
	 */
	protected void addListeners () {
		//
	}

	public JFrame getParentFrame () {
		return this.parent;
	}

	public Component getContent () {
		return this.content;
	}
}