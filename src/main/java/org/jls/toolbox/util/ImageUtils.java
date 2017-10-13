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

package org.jls.toolbox.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * Permet de centraliser les fonctions utiles sur les images.
 * 
 * @author LE SAUCE Julien
 * @date 21 mai 2015
 */
public class ImageUtils {

    /**
     * Classe non instanciable.
     */
    private ImageUtils() {
        throw new AssertionError();
    }

    /**
     * Permet de redimensionner une image aux dimensions précisées.
     * 
     * @param src
     *            Image d'origine.
     * @param width
     *            Largeur de l'imge de sortie.
     * @param height
     *            Hauteur de l'image de sortie.
     * @return Image redimensionnée.
     */
    public final static Image scaleImage (Image src, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return img;
    }

    /**
     * Permet de redimensionner une image aux dimensions précisées.
     * 
     * @param src
     *            Image d'origine.
     * @param width
     *            Largeur de l'imge de sortie.
     * @param height
     *            Hauteur de l'image de sortie.
     * @return Image redimensionnée.
     */
    public final static ImageIcon scaleImage (ImageIcon src, int width, int height) {
        return new ImageIcon(scaleImage(src.getImage(), width, height));
    }

    /**
     * Permet de redimensionner une image selon un facteur précisé en argument.
     * 
     * @param source
     *            Image à redimensionner.
     * @param factor
     *            Facteur de redimensionnement.
     * @return Image redimensionnée.
     */
    public final static Image scaleImage (final Image source, final double factor) {
        int width = (int) (source.getWidth(null) * factor);
        int height = (int) (source.getHeight(null) * factor);
        return scaleImage(source, width, height);
    }

    /**
     * Permet de redimensionner une image selon un facteur précisé en argument.
     * 
     * @param src
     *            Image à redimensionner.
     * @param factor
     *            Facteur de redimensionnement.
     * @return Image redimensionnée.
     */
    public final static ImageIcon scaleImage (final ImageIcon src, final double factor) {
        return new ImageIcon(scaleImage(src.getImage(), factor));
    }
}
