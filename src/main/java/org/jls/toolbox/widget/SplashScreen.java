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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.ImageObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Permet de créer un écran de démarrage ou splash screen qui sera affiché lors
 * du lancement de l'application.
 * 
 * @author LE SAUCE Julien
 * @date Feb 9, 2015
 */
public class SplashScreen extends Frame {

    private static final long serialVersionUID = 5393048810570745511L;

    private static final int IMAGE_ID = 0;
    protected static final ImageObserver NO_OBSERVER = null;

    private final Logger logger;
    private final Image image;

    /**
     * Permet d'instancier le splash screen.
     * 
     * @param img
     *            Image du splash screen.
     */
    public SplashScreen(final Image img) {
        if (img != null) {
            this.logger = LogManager.getLogger();
            this.image = img;
            MediaTracker mediaTracker = new MediaTracker(this);
            setSize(img.getWidth(SplashScreen.NO_OBSERVER), img.getHeight(SplashScreen.NO_OBSERVER));
            center();
            mediaTracker.addImage(img, SplashScreen.IMAGE_ID);
            try {
                mediaTracker.waitForID(SplashScreen.IMAGE_ID);
            } catch (InterruptedException e) {
                this.logger.error("Cannot track image load", e);
            }
        } else {
            throw new NullPointerException("Image is null");
        }
    }

    /**
     * Permet d'afficher le splash screen.
     */
    public void splash () {
        SplashWindow splashWindow = new SplashWindow(this, this.image);
        splashWindow.showGui();
    }

    /**
     * Permet de centrer la fenêtre affichée.
     */
    private void center () {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frame = getBounds();
        setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
    }

    /**
     * Fenêtre contenant l'image du splash screen.
     * 
     * @author LE SAUCE Julien
     * @date Feb 11, 2015
     */
    private final class SplashWindow extends Window {

        private static final long serialVersionUID = -67110377585090567L;

        private final Image img;

        /**
         * Permet d'instancier la fenêtre.
         * 
         * @param parent
         *            Fenêtre parente.
         * @param img
         *            Image du splash screen.
         */
        public SplashWindow(Frame parent, Image img) {
            super(parent);
            this.img = img;
        }

        @Override
        public void paint (Graphics graphics) {
            if (this.img != null) {
                graphics.drawImage(this.img, 0, 0, this);
            }
        }

        /**
         * Permet d'afficher la fenêtre.
         */
        public void showGui () {
            setSize(this.img.getWidth(SplashScreen.NO_OBSERVER), this.img.getHeight(SplashScreen.NO_OBSERVER));
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle window = getBounds();
            setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
            setVisible(true);
        }
    }
}
