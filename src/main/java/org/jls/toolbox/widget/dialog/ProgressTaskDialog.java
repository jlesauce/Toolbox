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

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jls.toolbox.widget.Console;

import net.miginfocom.swing.MigLayout;

/**
 * Permet d'afficher une barre de progression à laquelle est associé un
 * {@link SwingWorker}. La tâche à réaliser doit régulièrement notifier de sa
 * progression puis de sa réalisation pour correctement mettre à jour la fenêtre
 * de dialogue.
 * 
 * @author LE SAUCE Julien
 * @date Feb 26, 2015
 */
public class ProgressTaskDialog extends JDialog implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 4792006925536181624L;

	private final SwingWorker<?, ?> worker;
	private final boolean waitForUser;
	private final boolean showConsole;

	private boolean isStarted;
	private boolean isStopped;

	private JProgressBar progressBar;
	private JButton btnStart;
	private JButton btnStop;
	private Console console;

	/**
	 * Permet d'instancier une barre de progression.
	 * 
	 * @param worker
	 *            Instance de {@link SwingWorker} représentant la tâche à
	 *            réaliser.
	 * @param parent
	 *            Fenêtre appelante.
	 */
	public ProgressTaskDialog (final SwingWorker<?, ?> worker, final JFrame parent) {
		this(worker, parent, null);
	}

	/**
	 * Permet d'instancier une barre de progression.
	 * 
	 * @param worker
	 *            Instance de {@link SwingWorker} représentant la tâche à
	 *            réaliser.
	 * @param parent
	 *            Fenêtre appelante.
	 * @param waitForUser
	 *            <code>true</code> pour ajouter des boutons de lancement et
	 *            d'arrêt afin que l'utilisateur puisse lancer lui même la
	 *            tâche, ou <code>false</code> pour n'affiquer que la barre de
	 *            progression (dans ce cas l'appel à la fonction
	 *            <i>showGui()</i> initiera directement la tâche).
	 */
	public ProgressTaskDialog (final SwingWorker<?, ?> worker, final JFrame parent, boolean waitForUser) {
		this(worker, parent, null, waitForUser);
	}

	/**
	 * Permet d'instancier une barre de progression.
	 * 
	 * @param worker
	 *            Instance de {@link SwingWorker} représentant la tâche à
	 *            réaliser.
	 * @param parent
	 *            Fenêtre appelante.
	 * @param title
	 *            Titre de la barre de progression.
	 */
	public ProgressTaskDialog (final SwingWorker<?, ?> worker, final JFrame parent, final String title) {
		this(worker, parent, title, true);
	}

	/**
	 * Permet d'instancier une barre de progression.
	 * 
	 * @param worker
	 *            Instance de {@link SwingWorker} représentant la tâche à
	 *            réaliser.
	 * @param parent
	 *            Fenêtre appelante.
	 * @param title
	 *            Titre de la barre de progression.
	 * @param waitForUser
	 *            <code>true</code> pour ajouter des boutons de lancement et
	 *            d'arrêt afin que l'utilisateur puisse lancer lui même la
	 *            tâche, ou <code>false</code> pour n'affiquer que la barre de
	 *            progression (dans ce cas l'appel à la fonction
	 *            <i>showGui()</i> initiera directement la tâche).
	 */
	public ProgressTaskDialog (final SwingWorker<?, ?> worker, final JFrame parent, final String title,
			final boolean waitForUser) {
		this(worker, parent, title, waitForUser, false);
	}

	/**
	 * Permet d'instancier une barre de progression.
	 * 
	 * @param worker
	 *            Instance de {@link SwingWorker} représentant la tâche à
	 *            réaliser.
	 * @param parent
	 *            Fenêtre appelante.
	 * @param title
	 *            Titre de la barre de progression.
	 * @param waitForUser
	 *            <code>true</code> pour ajouter des boutons de lancement et
	 *            d'arrêt afin que l'utilisateur puisse lancer lui même la
	 *            tâche, ou <code>false</code> pour n'affiquer que la barre de
	 *            progression (dans ce cas l'appel à la fonction
	 *            <i>showGui()</i> initiera directement la tâche).
	 * @param showConsole
	 *            <code>true</code> pour afficher une console de sortie
	 *            (peut-être utilisé lorsque le worker a été développé pour
	 *            afficher du texte en utilisant la méthode
	 *            {@link SwingWorker#firePropertyChange(String, Object, Object)}
	 *            via la clé de propriété "output".
	 *            <p>
	 *            Par exemple :
	 *            <p>
	 *            <code>firePropertyChange("output", "old text", "new text to add");</code>
	 */
	public ProgressTaskDialog (final SwingWorker<?, ?> worker, final JFrame parent, final String title,
			final boolean waitForUser, final boolean showConsole) {
		super(parent, title);
		this.worker = worker;
		this.waitForUser = waitForUser;
		this.showConsole = showConsole;
		this.isStarted = false;
		this.isStopped = false;
		createView();
		setStyle();
		setOnListeners();
		this.worker.addPropertyChangeListener(this);
	}

	/**
	 * Permet d'afficher la barre de progression.
	 */
	public void showGui () {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run () {
				pack();
				setLocationRelativeTo(getOwner());
				setVisible(true);
				if (!isWaitingForUser()) {
					setStarted(true);
					getWorker().execute();
				}
			}
		});
	}

	/**
	 * Permet de notifier à l'interface graphique que la tâche est terminée.
	 */
	public void notifyDone () {
		this.btnStart.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.isStarted = false;
		this.isStopped = true;
		this.btnStop.setText("Exit");
		if (!this.waitForUser) {
			dispose();
		}
	}

	/**
	 * Permet de créer les différents éléments qui composent la vue.
	 */
	private void createView () {
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		this.btnStart = new JButton("Start");
		this.btnStart.setActionCommand("start");
		this.btnStop = new JButton("Exit");
		this.btnStop.setActionCommand("stop");
		if (this.showConsole) {
			this.console = new Console();
		}
	}

	/**
	 * Permet, une fois les composants créés, de placer correctement les
	 * éléments qui composent la vue.
	 */
	private void setStyle () {
		if (this.waitForUser) {
			setLayout(new MigLayout("fill"));
			add(this.btnStart, "split 3, span, left");
			add(this.btnStop, "");
			add(this.progressBar, "wmin 250lp, grow, wrap");
			if (this.showConsole) {
				JScrollPane scroll = new JScrollPane(this.console);
				add(scroll, "hmin 200lp, span, grow, push");
			}
		} else {
			this.btnStop.setText("Cancel");
			setLayout(new MigLayout("fill"));
			add(this.progressBar, "wmin 250lp, pushx");
			add(this.btnStop, "wrap");
			if (this.showConsole) {
				JScrollPane scroll = new JScrollPane(this.console);
				add(scroll, "hmin 200lp, span, grow, push");
			}
		}
	}

	/**
	 * Permet d'ajouter les différents écouteurs de la vue.
	 */
	private void setOnListeners () {
		this.btnStart.addActionListener(this);
		this.btnStop.addActionListener(this);
	}

	@Override
	public void propertyChange (PropertyChangeEvent evt) {
		// Progress
		if (evt.getPropertyName().equals("progress")) {
			int progress = (Integer) evt.getNewValue();
			this.progressBar.setValue(progress);
		}
		// State
		else if (evt.getPropertyName().equals("state")) {
			switch (evt.getNewValue().toString()) {
				case "DONE":
					notifyDone();
					break;
				default:
					break;
			}
		}
		// Console output
		else if (evt.getPropertyName().equals("output")) {
			if (this.showConsole) {
				this.console.print(evt.getNewValue().toString());
			}
		}
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		// Start
		if (this.btnStart.equals(btn)) {
			this.btnStart.setEnabled(false);
			this.btnStop.setText("Stop");
			this.btnStop.setEnabled(true);
			this.isStarted = true;
			this.worker.execute();
		}
		// Stop
		else if (this.btnStop.equals(btn)) {
			if (!this.isStopped && this.isStarted) {
				this.btnStart.setEnabled(false);
				this.worker.removePropertyChangeListener(this);
				this.worker.cancel(false);
				this.isStarted = false;
				this.isStopped = true;
				this.btnStop.setText("Exit");
			} else {
				dispose();
			}
		}
	}

	protected boolean isWaitingForUser () {
		return this.waitForUser;
	}

	protected boolean isStarted () {
		return isStarted;
	}

	protected void setStarted (boolean isStarted) {
		this.isStarted = isStarted;
	}

	protected SwingWorker<?, ?> getWorker () {
		return worker;
	}
}