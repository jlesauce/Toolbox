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

package org.jls.toolbox.widget;

import javax.swing.JInternalFrame;

import org.jls.toolbox.widget.ui.DesktopPane;

/**
 * Permet de créer une fenêtre interne de l'application afin d'être affichée
 * dans un {@link DesktopPane}.
 * 
 * @author LE SAUCE Julien
 * @date Feb 23, 2015
 */
public class InternalFrame extends JInternalFrame {

    private static final long serialVersionUID = -3025021128703149423L;

    private final String id;

    /**
     * Permet d'instancier une fenêtre par défaut.
     * 
     * @param id
     *            Identifiant unique de la fenêtre permettant sa gestion par le
     *            panneau {@link DesktopPane}.
     */
    public InternalFrame(final String id) {
        this(null, id);
    }

    /**
     * Permet d'instancier une fenêtre et de spécifier son titre.
     * 
     * @param id
     *            Identifiant unique de la fenêtre permettant sa gestion par le
     *            panneau {@link DesktopPane}.
     * @param title
     *            Titre de la fenêtre.
     */
    public InternalFrame(final String id, final String title) {
        this(id, title, true);
    }

    /**
     * Permet d'instancier une fenêtre, de spécifier son titre et si la fenêtre est
     * redimensionnable ou non.
     * 
     * @param id
     *            Identifiant unique de la fenêtre permettant sa gestion par le
     *            panneau {@link DesktopPane}.
     * @param title
     *            Titre de la fenêtre.
     * @param isResizable
     *            <code>true</code> pour que la fenêtre soit redimensionnable,
     *            <code>false</code> sinon.
     */
    public InternalFrame(final String id, final String title, boolean isResizable) {
        this(id, title, isResizable, true, true, true);
    }

    /**
     * Permet d'instancier une fenêtre et de spécifier ses paramètres.
     * 
     * @param id
     *            Identifiant unique de la fenêtre permettant sa gestion par le
     *            {@link DesktopPane}.
     * @param title
     *            Titre de la fenêtre.
     * @param resizable
     *            <code>true</code> pour que la fenêtre soit redimensionnable,
     *            <code>false</code> sinon.
     * @param closable
     *            <code>true</code> pour que la fenêtre puisse être fermée,
     *            <code>false</code> sinon.
     * @param maximizable
     *            <code>true</code> pour que la fenêtre puisse être agrandie,
     *            <code>false</code> sinon.
     * @param iconifiable
     *            <code>true</code> pour que la fenêtre puisse être iconifiée,
     *            <code>false</code> sinon.
     */
    public InternalFrame(final String id, final String title, boolean resizable, boolean closable, boolean maximizable,
            boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.id = id;
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Renvoie l'identifiant unique de la fenêtre.
     * 
     * @return Identifiant unique de la fenêtre.
     */
    public String getId () {
        return id;
    }
}
