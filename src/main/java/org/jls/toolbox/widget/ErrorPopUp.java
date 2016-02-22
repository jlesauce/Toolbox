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

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import org.apache.logging.log4j.LogManager;
import org.jls.toolbox.util.ResourceManager;

/**
 * Permet d'afficher une popup d'erreur avec le détail de l'erreur.
 * 
 * @author LE SAUCE Julien
 * @date Feb 19, 2015
 */
public class ErrorPopUp extends JDialog {

	private static final long serialVersionUID = 3322085996195994230L;

	/**
	 * Permet de créer une popup d'erreur à partir d'une exception.
	 * 
	 * @param parent
	 *            Fenêtre parente.
	 * @param e
	 *            Exception dont on souhaite afficher le détail.
	 */
	public static void showExceptionDialog (Frame parent, Exception e) {
		ErrorPopUp.showExceptionDialog(parent, e, null);
	}

	/**
	 * Permet de créer une popup d'erreur à partir d'une exception et d'un
	 * message descriptif.
	 * 
	 * @param parent
	 *            Fenêtre parente.
	 * @param e
	 *            Exception dont on souhaite afficher le détail.
	 * @param msg
	 *            Message descriptif de l'erreur.
	 */
	public static void showExceptionDialog (final Frame parent, final Throwable e, final String msg) {
		final JDialog dialog = new JDialog(parent, true);
		JPanel panel = new JPanel();
		ResourceManager p = ResourceManager.getInstance();
		ImageIcon icon = p.getIcon("icon.error");
		Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);

		String trace = "";
		for (StackTraceElement l : e.getStackTrace()) {
			trace += l.toString() + "\n";
		}

		JLabel lblIcon = new JLabel(icon);
		JLabel lblException = new JLabel(e.getClass().getSimpleName());
		JLabel lblMessage = new JLabel(msg);
		JLabel lblExMessage = new JLabel("> " + e.getMessage());
		JTextArea textArea = new JTextArea(trace);
		JScrollPane scrollpane = new JScrollPane(textArea);
		JButton btnOK = new JButton("OK");
		JButton btnExport = new JButton("Export");

		lblIcon.setSize(50, 50);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);

		panel.setLayout(new MigLayout("fill, insets 10lp 10lp 10lp 10lp"));
		panel.add(lblIcon, "split, span, left");
		panel.add(lblException, "wrap");
		if (msg != null) {
			panel.add(lblMessage, "wrap");
		}
		panel.add(lblExMessage, "gap top 10lp, wrap");
		panel.add(btnExport, "span, right, wrap");
		panel.add(scrollpane, "gap top 25lp, hmax 400lp, pushy, grow, wrap");
		panel.add(btnOK, "span, center");

		/*
		 * Fermeture de la fenêtre si on clique sur OK.
		 */
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed (ActionEvent e) {
				dialog.dispose();
			}
		});
		/*
		 * Export de l'exception dans un fichier
		 */
		btnExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed (ActionEvent e1) {
				exportToFile(e, dialog);
			}
		});

		dialog.add(panel);
		dialog.setTitle(e.getClass().getSimpleName());
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}

	/**
	 * Permet d'exporter l'exception dans un fichier texte.
	 * 
	 * @param e
	 *            Exception à exporter.
	 * @param parent
	 *            Composant parent à partir duquel afficher le sélecteur de
	 *            fichier.
	 */
	protected static void exportToFile (final Throwable e, Component parent) {
		FileChooser chooser = new FileChooser(System.getProperty("user.dir"));
		int ret = chooser.showSaveDialog(parent);
		if (ret == FileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try (PrintStream stream = new PrintStream(file)) {
				e.printStackTrace(stream);
			} catch (IOException e1) {
				LogManager.getLogger().error("Cannot export exception", e1);
			}
		}
	}
}