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

package org.jls.toolbox.widget.button;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.JLabel;

/**
 * Permet de créer un bouton de sélection à deux états.
 * 
 * @author LE SAUCE Julien
 * @date 15 mai 2015
 */
public class Switch extends AbstractButton {

    private static final long serialVersionUID = 6533185801091547103L;

    private static final Color color_unselectedState = new Color(215, 80, 80);
    private static final Color color_selectedState = new Color(80, 215, 80);
    private static final Color color_text = new Color(0, 0, 0, 100);
    private static final Color color_shape = new Color(255, 255, 255, 100);

    private final String lblTrue;
    private final String lblFalse;
    private boolean state;

    private Color colorBright = new Color(220, 220, 220);
    private Color colorDark = new Color(150, 150, 150);
    private Color light = new Color(220, 220, 220, 100);

    private Font font = new JLabel().getFont();
    private int gap = 5;
    private int globalWitdh = 0;
    private Dimension thumbBounds;
    private int max;

    /**
     * Permet d'instancier un bouton de sélection.
     * 
     * @param trueLbl
     *            Texte associé à la valeur <code>true</code>.
     * @param falseLbl
     *            Texte associé à la valeur <code>false</code>.
     */
    public Switch(String trueLbl, String falseLbl) {
        if (trueLbl.isEmpty() || falseLbl.isEmpty()) {
            throw new IllegalArgumentException("Labels cannot be empty");
        }
        this.lblTrue = trueLbl;
        this.lblFalse = falseLbl;
        this.state = false;

        double trueLenth = getFontMetrics(getFont()).getStringBounds(trueLbl, getGraphics()).getWidth();
        double falseLenght = getFontMetrics(getFont()).getStringBounds(falseLbl, getGraphics()).getWidth();
        this.max = (int) Math.max(trueLenth, falseLenght);
        this.gap = Math.max(5, 5 + (int) Math.abs(trueLenth - falseLenght));
        this.thumbBounds = new Dimension(max + gap * 2, 20);
        this.globalWitdh = max + thumbBounds.width + gap * 2;
        setModel(new DefaultButtonModel());
        setSelected(false);

        // ActionListener
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased (MouseEvent e) {
                if (new Rectangle(getPreferredSize()).contains(e.getPoint())) {
                    setSelected(!isSelected());
                    for (ActionListener l : getActionListeners()) {
                        l.actionPerformed(
                                new ActionEvent(Switch.this, ActionEvent.ACTION_PERFORMED, "selectionChanged"));
                    }
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize () {
        return new Dimension(this.globalWitdh, this.thumbBounds.height);
    }

    @Override
    public void setSelected (boolean isSelected) {
        if (isSelected) {
            this.state = true;
            setText(this.lblTrue);
            setBackground(Switch.color_selectedState);
        } else {
            this.state = false;
            setText(this.lblFalse);
            setBackground(Switch.color_unselectedState);
        }
        super.setSelected(isSelected);
    }

    @Override
    public void setText (String text) {
        super.setText(text);
    }

    @Override
    public int getHeight () {
        return getPreferredSize().height;
    }

    @Override
    public int getWidth () {
        return getPreferredSize().width;
    }

    @Override
    public Font getFont () {
        return this.font;
    }

    @Override
    protected void paintComponent (Graphics g) {
        // Dessin du fond
        g.setColor(getBackground());
        g.fillRoundRect(1, 1, getWidth() - 2 - 1, getHeight() - 2, 2, 2);

        // Dessin des contours
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Switch.color_text);
        g2.drawRoundRect(1, 1, getWidth() - 2 - 1, getHeight() - 2 - 1, 2, 2);
        g2.setColor(Switch.color_shape);
        g2.drawRoundRect(1 + 1, 1 + 1, getWidth() - 2 - 3, getHeight() - 2 - 3, 2, 2);

        // Taille du bouton
        int x = 0;
        int lx = 0;
        if (isSelected()) {
            lx = thumbBounds.width;
        } else {
            x = thumbBounds.width;
        }
        int y = 0;
        int w = thumbBounds.width;
        int h = thumbBounds.height;

        g2.setPaint(new GradientPaint(x, (int) (y - 0.1 * h), this.colorDark, x, (int) (y + 1.2 * h), this.light));
        g2.fillRect(x, y, w, h);
        g2.setPaint(new GradientPaint(x, (int) (y + .65 * h), light, x, (int) (y + 1.3 * h), colorDark));
        g2.fillRect(x, (int) (y + .65 * h), w, (int) (h - .65 * h));

        if (w > 14) {
            int size = 10;
            g2.setColor(colorBright);
            g2.fillRect(x + w / 2 - size / 2, y + h / 2 - size / 2, size, size);
            g2.setColor(new Color(120, 120, 120));
            g2.fillRect(x + w / 2 - 4, h / 2 - 4, 2, 2);
            g2.fillRect(x + w / 2 - 1, h / 2 - 4, 2, 2);
            g2.fillRect(x + w / 2 + 2, h / 2 - 4, 2, 2);
            g2.setColor(colorDark);
            g2.fillRect(x + w / 2 - 4, h / 2 - 2, 2, 6);
            g2.fillRect(x + w / 2 - 1, h / 2 - 2, 2, 6);
            g2.fillRect(x + w / 2 + 2, h / 2 - 2, 2, 6);
            g2.setColor(new Color(170, 170, 170));
            g2.fillRect(x + w / 2 - 4, h / 2 + 2, 2, 2);
            g2.fillRect(x + w / 2 - 1, h / 2 + 2, 2, 2);
            g2.fillRect(x + w / 2 + 2, h / 2 + 2, 2, 2);
        }

        g2.setColor(color_text);
        g2.drawRoundRect(x, y, w - 1, h - 1, 2, 2);
        g2.setColor(color_shape);
        g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, 2, 2);

        g2.setColor(color_text.darker());
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(getFont());
        if (getText() != null && !getText().isEmpty()) {
            g2.drawString(getText(), lx + gap, y + h / 2 + h / 4);
        }
    }

    @Override
    public boolean isSelected () {
        return this.state;
    }
}
