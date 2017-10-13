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

package org.jls.toolbox.widget.ui;

import org.jls.toolbox.widget.InternalFrame;

/**
 * Objet associé lors d'un évènement survenant sur un {@link DesktopPane}.
 * 
 * @see DesktopPaneListener
 * 
 * @author LE SAUCE Julien
 * @date 15 mai 2015
 */
public class DesktopPaneEvent {

    private final DesktopPane source;

    private InternalFrame frame;

    /**
     * Permet d'instancier un évènement.
     * 
     * @param src
     *            Source de l'évènement.
     */
    public DesktopPaneEvent(final DesktopPane src) {
        this(src, null);
    }

    /**
     * Permet d'instancier un évènement.
     * 
     * @param src
     *            Source de l'évènement.
     * @param srcFrame
     *            Fenêtre source de l'évènement.
     */
    public DesktopPaneEvent(final DesktopPane src, final InternalFrame srcFrame) {
        this.source = src;
        this.frame = srcFrame;
    }

    /**
     * Renvoie la source de l'évènement.
     * 
     * @return Source de l'évènement.
     */
    public DesktopPane getSource () {
        return this.source;
    }

    /**
     * Renvoie la fenêtre source de l'évènement si l'évènement a été déclenché par
     * une fenêtre interne.
     * 
     * @return Fenêtre source de l'évènement si l'évènement a été déclenché par une
     *         fenêtre interne, <code>null</code> sinon.
     */
    public InternalFrame getFrame () {
        return this.frame;
    }

    /**
     * Permet de spécifier la fenêtre source de l'évènement s'il a été déclenché par
     * une fenêtre interne.
     * 
     * @param frame
     *            Fenêtre source de l'évènement.
     */
    public void setFrame (InternalFrame frame) {
        this.frame = frame;
    }
}
