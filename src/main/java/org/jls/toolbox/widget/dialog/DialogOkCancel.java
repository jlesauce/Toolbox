/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 LE SAUCE Julien
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
 */

package org.jls.toolbox.widget.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jls.toolbox.util.ResourceManager;

import net.miginfocom.swing.MigLayout;

/**
 * Permet de créer une fenêtre de dialogue OK/Cancel customisable. La fenêtre
 * créée sera modale par défaut (voir {@link JDialog#setModal(boolean)}. Les
 * boutons OK et Cancel sont ajoutés à la suite du composant spécifié lors de la
 * création de la fenêtre, la fenêtre peut alors être affichée via la méthode
 * {@link #showDialog()}.
 * 
 * @author LE SAUCE Julien
 * @date Mar 16, 2015
 */
public class DialogOkCancel extends Dialog implements ActionListener {

    private static final long serialVersionUID = 2354616214737733368L;

    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;

    private int userOption;

    private JButton btnOk;
    private JButton btnCancel;

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
    public DialogOkCancel(final JFrame parent, final String title, final JComponent content) {
        super(parent, title, content);
        this.userOption = DialogOkCancel.CANCEL_OPTION;
    }

    @Override
    public int showDialog () {
        createComponents();
        setStyle();
        addListeners();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParentFrame());
        setVisible(true);
        return this.userOption;
    }

    @Override
    protected void onValidAction () {
        this.userOption = DialogOkCancel.OK_OPTION;
        dispose();
    }

    @Override
    protected void onCancelAction () {
        this.userOption = DialogOkCancel.CANCEL_OPTION;
        dispose();
    }

    @Override
    protected void createComponents () {
        ResourceManager p = ResourceManager.getInstance();
        this.btnOk = new JButton(p.getString("toolbox.label.ok"));
        this.btnCancel = new JButton(p.getString("toolbox.label.cancel"));
    }

    @Override
    protected void setStyle () {
        setLayout(new MigLayout());
        add(getContent(), "grow, wrap");
        add(this.btnOk, "split, span, center");
        add(this.btnCancel, "");
    }

    @Override
    protected void addListeners () {
        this.btnOk.addActionListener(this);
        this.btnCancel.addActionListener(this);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        /*
         * JButton
         */
        if (e.getSource() instanceof JButton) {
            JButton btn = (JButton) e.getSource();

            // OK
            if (this.btnOk.equals(btn)) {
                onValidAction();
            }
            // Cancel
            if (this.btnCancel.equals(btn)) {
                onCancelAction();
            }
        }
    }

    /**
     * Renvoie l'option sélectionnée par l'utilisateur.
     * 
     * @return Option sélectionnée par l'utilisateur ({@link #OK_OPTION} ou
     *         {@link #CANCEL_OPTION}).
     */
    public int getUserOption () {
        return this.userOption;
    }
}
