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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jls.toolbox.widget.format.Format;

import net.miginfocom.swing.MigLayout;

/**
 * Panneau permettant de sélectionner deux couleurs afin de réaliser un gradient
 * de couleur.
 * 
 * @author LE SAUCE Julien
 * @date Jul 9, 2015
 */
public class ColorScalePanel extends JPanel implements ActionListener, PropertyChangeListener {

    private static final long serialVersionUID = 7937801171092590973L;

    private float minValue;
    private float maxValue;
    private Color minColor;
    private Color maxColor;

    private JLabel lblMinValue;
    private JLabel lblMaxValue;
    private JLabel lblColor;
    private FormattedTextField tfMinValue;
    private FormattedTextField tfMaxValue;
    private JButton btnMinColor;
    private JButton btnMaxColor;

    /**
     * Permet d'instancier un sélecteir de gradient de couleur par défaut.
     */
    public ColorScalePanel() {
        this(0.0f, 1.0f, Color.white, Color.black);
    }

    /**
     * Permet d'instancier un sélecteur de gradient de couleur en spécifiant les
     * valeurs limites et leurs couleurs associées.
     * 
     * @param minValue
     *            Valeur minimale.
     * @param maxValue
     *            Valeur maximale.
     * @param minColor
     *            Couleur associée à la valeur minimale.
     * @param maxColor
     *            Couleur associée à la valeur maximale.
     */
    public ColorScalePanel(final float minValue, final float maxValue, final Color minColor, final Color maxColor) {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minColor = minColor;
        this.maxColor = maxColor;
        createComponents();
        setStyle();
        addListeners();
    }

    /**
     * Permet d'instancier les différents éléments qui composent l'interface
     * graphique.
     */
    private void createComponents () {
        this.lblMinValue = new JLabel("Min Value :");
        this.lblMaxValue = new JLabel("Max Value :");
        this.lblColor = new JLabel("Color :");

        this.tfMinValue = new FormattedTextField(Format.getDecimalFormat());
        this.tfMaxValue = new FormattedTextField(Format.getDecimalFormat());
        this.tfMinValue.setValue(this.minValue);
        this.tfMaxValue.setValue(this.maxValue);

        this.btnMinColor = new JButton("");
        this.btnMaxColor = new JButton("");
        this.btnMinColor.setBackground(this.minColor);
        this.btnMaxColor.setBackground(this.maxColor);
    }

    /**
     * Permet de créer l'interface graphique à partir des éléments qui la compose.
     */
    private void setStyle () {
        setLayout(new MigLayout("insets 10lp 10lp 10lp 10lp", "[]20lp[]", ""));
        add(this.lblMaxValue, "");
        add(this.lblColor, "wrap");
        add(this.tfMaxValue, "width 120lp, grow");
        add(this.btnMaxColor, "wrap, grow");
        add(this.lblMinValue, "wrap");
        add(this.tfMinValue, "width 120lp, grow");
        add(this.btnMinColor, "grow, wrap");
    }

    /**
     * Permet d'ajouter les différents écouteurs sur les composants de l'interface
     * graphique.
     */
    private void addListeners () {
        this.tfMinValue.addPropertyChangeListener(this);
        this.tfMaxValue.addPropertyChangeListener(this);
        this.btnMinColor.addActionListener(this);
        this.btnMaxColor.addActionListener(this);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton btn = (JButton) e.getSource();

            // ColorMin
            if (this.btnMinColor.equals(btn)) {
                Color color = JColorChooser.showDialog(this, "Pick a color", this.minColor);
                if (color != null) {
                    this.minColor = color;
                    this.btnMinColor.setBackground(color);
                }
            }
            // ColorMax
            else if (this.btnMaxColor.equals(btn)) {
                Color color = JColorChooser.showDialog(this, "Pick a color", this.maxColor);
                if (color != null) {
                    this.maxColor = color;
                    this.btnMaxColor.setBackground(color);
                }
            }
        }
    }

    @Override
    public void propertyChange (PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("value")) {
            /*
             * FormattedTextField
             */
            if (evt.getSource() instanceof FormattedTextField) {
                FormattedTextField tf = (FormattedTextField) evt.getSource();

                // Min Value
                if (this.tfMinValue.equals(tf)) {
                    this.minValue = Float.parseFloat(tf.getText());
                }
                // Max Value
                else if (this.tfMaxValue.equals(tf)) {
                    this.maxValue = Float.parseFloat(tf.getText());
                }
            }
        }
    }

    public float getMinValue () {
        return this.minValue;
    }

    public void setMinValue (float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue () {
        return this.maxValue;
    }

    public void setMaxValue (float maxValue) {
        this.maxValue = maxValue;
    }

    public Color getMinColor () {
        return this.minColor;
    }

    public void setMinColor (Color minColor) {
        this.minColor = minColor;
    }

    public Color getMaxColor () {
        return this.maxColor;
    }

    public void setMaxColor (Color maxColor) {
        this.maxColor = maxColor;
    }
}
