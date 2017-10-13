/*
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
 */

package org.jls.toolbox.widget.ui;

import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDesktopPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jls.toolbox.widget.InternalFrame;

/**
 * Panneau permettant d'afficher et de gérer des fenêtre internes à
 * l'application en mode bureau virtuel.
 * 
 * @author LE SAUCE Julien
 * @date Feb 23, 2015
 */
public class DesktopPane extends JDesktopPane implements InternalFrameListener {

    private static final long serialVersionUID = 3703341748085042784L;

    private static final int DEFAULT_X_POS = 20;
    private static final int DEFAULT_Y_POS = 20;
    private static final int X_GAP = 10;
    private static final int Y_GAP = 10;

    private final Logger logger;
    private final HashSet<DesktopPaneListener> listeners;
    private final HashMap<String, InternalFrame> frames;

    private int xPos;
    private int yPos;

    /**
     * Permet d'instancier un panneau.
     */
    public DesktopPane() {
        super();
        this.logger = LogManager.getLogger();
        this.listeners = new HashSet<>();
        this.frames = new HashMap<>();
        this.xPos = DesktopPane.DEFAULT_X_POS;
        this.yPos = DesktopPane.DEFAULT_Y_POS;
    }

    /**
     * Permet d'ajouter un écouteur sur les évènements déclenchés par ce
     * {@link DesktopPane}.
     * 
     * @param listener
     *            Ecouteur sur les évènements déclenchés par ce {@link DesktopPane}.
     * @return Renvoie <code>true</code> si l'élément n'était pas déjà contenu dans
     *         la liste.
     */
    public boolean addDesktopPaneListener (final DesktopPaneListener listener) {
        return this.listeners.add(listener);
    }

    /**
     * Permet de retirer un écouteur sur les évènements déclenchés par ce
     * {@link DesktopPane}.
     * 
     * @param listener
     *            Ecouteur sur les évènements déclenchés par ce {@link DesktopPane}.
     * @return Renvoie <code>true</code> si l'élément était effectivement contenu
     *         dans la liste.
     */
    public boolean removeDesktopPaneListener (final DesktopPaneListener listener) {
        return this.listeners.remove(listener);
    }

    /**
     * Permet d'ajouter une fenêtre interne au panneau. Le fenêtre sera alors
     * identifiée grâce à l'identifiant unique renvoyé par la méthode
     * {@link InternalFrame#getId()}.
     * 
     * @param frame
     *            Fenêtre à ajouter.
     */
    public void addFrame (final InternalFrame frame) {
        // Vérifications
        if (frame == null) {
            throw new NullPointerException("Frame cannot be null");
        }
        if (frame.getId() == null || frame.getId().isEmpty()) {
            throw new IllegalArgumentException("Frame must have an id");
        }
        if (this.frames.containsKey(frame.getId())) {
            throw new IllegalStateException("Frame key already exists : " + frame.getId());
        }
        // Positionnement
        frame.setLocation(this.xPos, this.yPos);
        this.xPos += DesktopPane.X_GAP;
        this.yPos += DesktopPane.Y_GAP;
        // Ajout de la fenêtre
        this.frames.put(frame.getId(), frame);
        add(frame);
        this.logger.debug("Frame {} added to desktop", frame.getId());
        // Dimensionnement
        frame.pack();
        Dimension d = getSize();
        Dimension s = frame.getSize();
        frame.setMinimumSize(new Dimension(200, 100));
        frame.setVisible(true);
        frame.setSize(Math.min(s.width, d.width - 100), Math.min(s.height, d.height - 100));
        frame.toFront();
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
            this.logger.error("Failed to select internal frame", e);
        }
        // Ecouteur sur la fermeture de la fenêtre
        frame.addInternalFrameListener(this);
    }

    /**
     * Renvoie la fenêtre associée à l'identifiant spécifié ou <code>null</code> si
     * l'identifiant n'existe pas.
     * 
     * @param key
     *            Identifiant unique associé à une fenêtre interne du panneau.
     * @return Fenêtre interne associé à l'identifiant spécifié ou <code>null</code>
     *         si l'identifiant n'existe pas.
     */
    public InternalFrame getFrame (final String key) {
        return this.frames.get(key);
    }

    /**
     * Renvoie la liste des fenêtres contenues dans le {@link DesktopPane}.
     * 
     * @return liste des fenêtres contenues dans le {@link DesktopPane}.
     */
    public Collection<InternalFrame> getFrames () {
        ArrayList<InternalFrame> frames = new ArrayList<>(this.frames.values());
        return frames;
    }

    /**
     * Renvoie la liste des identifiants des fenêtres contenues dans le
     * {@link DesktopPane}.
     * 
     * @return Liste des identifiants des fenêtres contenues dans le
     *         {@link DesktopPane}.
     */
    public Set<String> getFramesKeySet () {
        return this.frames.keySet();
    }

    @Override
    public void internalFrameOpened (InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing (InternalFrameEvent e) {
        InternalFrame frame = (InternalFrame) e.getSource();
        if (frame.getDefaultCloseOperation() == InternalFrame.DISPOSE_ON_CLOSE) {
            this.frames.remove(frame.getId());
            this.logger.debug("Frame {} removed from desktop", frame.getId());
            frame.dispose();
            frame = null;
        }
        // Notification des abonnés
        DesktopPaneEvent event = new DesktopPaneEvent(this, frame);
        for (DesktopPaneListener l : this.listeners) {
            l.frameClosed(event);
        }
    }

    @Override
    public void internalFrameClosed (InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified (InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified (InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated (InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated (InternalFrameEvent e) {
    }
}
