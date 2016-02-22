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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

/**
 * Panneau permettant d'afficher une liste de choix sous forme de boutons
 * radios.
 * 
 * @author LE SAUCE Julien
 * @date 3 juin 2015
 */
public class RadioSelector extends JPanel implements ActionListener {

	private static final long serialVersionUID = -7727154348874490018L;

	private final String text;
	private final LinkedHashMap<String, JRadioButton> items;

	private String selectedKey;

	private JLabel label;

	/**
	 * Permet d'instancier une simple liste de boutons radios.
	 */
	public RadioSelector () {
		this(null);
	}

	/**
	 * Permet d'instancier une liste de boutons radios et d'afficher du texte en
	 * haut de la liste.
	 * 
	 * @param label
	 *            Texte à afficher au dessus de la liste.
	 */
	public RadioSelector (final String label) {
		super();
		this.text = label;
		this.items = new LinkedHashMap<>();
		this.selectedKey = null;
		createComponents();
		setStyle();
	}

	/**
	 * Permet d'ajouter un choix dans la liste des choix du panneau.
	 * 
	 * @param key
	 *            Clé permettant d'identifier le choix sélectionné par
	 *            l'utilisateur.
	 * @param text
	 *            Texte affiché pour ce choix.
	 */
	public void addItem (final String key, final String text) {
		if (this.items.containsKey(key)) {
			throw new IllegalArgumentException("Key already exists : " + key);
		}
		JRadioButton btn = new JRadioButton(text, false);
		btn.setActionCommand(key);
		btn.addActionListener(this);
		add(btn, "wrap");
		this.items.put(key, btn);
		actionPerformed(new ActionEvent(btn, ActionEvent.ACTION_PERFORMED, key));
	}

	/**
	 * Permet d'instancier les différents éléments qui composent l'interface
	 * graphique.
	 */
	private void createComponents () {
		if (this.text != null) {
			this.label = new JLabel(this.text);
		}
	}

	/**
	 * Permet de créer l'interface graphique à partir des éléments qui la
	 * compose.
	 */
	private void setStyle () {
		setLayout(new MigLayout(""));
		if (this.label != null) {
			add(this.label, "gap bottom 5lp, wrap");
		}
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		/*
		 * JRadioButton
		 */
		if (e.getSource() instanceof JRadioButton) {
			JRadioButton btn = (JRadioButton) e.getSource();

			// Désélection des autres boutons
			for (JRadioButton b : this.items.values()) {
				if (!b.equals(btn)) {
					b.setSelected(false);
				}
			}
			// Mise à jour de la clé
			this.selectedKey = btn.getActionCommand();
		}
	}

	/**
	 * Renvoie la clé sélectionnée par l'utilisateur.
	 * 
	 * @return Clé sélectionnée par l'utilisateur.
	 */
	public String getSelectedKey () {
		return this.selectedKey;
	}

	/**
	 * Permet de mettre à jour la clé sélectionnée par l'utilisateur.
	 * 
	 * @param key
	 *            Clé sélectionnée par l'utilisateur.
	 */
	protected void setSelectedKey (String key) {
		this.selectedKey = key;
	}
}