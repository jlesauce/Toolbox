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

package org.jls.toolbox.math.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Permet d'afficher un simple thermomètre dont les seuils de couleurs sont
 * configurables.
 * 
 * @author LE SAUCE Julien
 * @date Feb 13, 2015
 */
public class Thermometer {

    private ChartPanel panel;
    private JFreeChart chart;
    private ThermometerPlot plot;
    private DefaultValueDataset dataset;
    private String title;

    /**
     * Permet d'instancier un thermomètre.
     * 
     * @param title
     *            Titre du thermomètre.
     */
    public Thermometer(final String title) {
        this.title = title;
        this.dataset = new DefaultValueDataset(37.5);
        createChart();
    }

    /**
     * Permet de créer et de paramètrer le thermomètre.
     */
    private void createChart () {
        this.plot = new ThermometerPlot(this.dataset);
        this.chart = new JFreeChart(this.plot);

        this.plot.setInsets(new RectangleInsets(0, 0, 0, 0));
        this.plot.setPadding(new RectangleInsets(0, 0, 0, 0));
        this.plot.setThermometerStroke(new BasicStroke(1.0f));
        this.plot.setThermometerPaint(Color.black);
        this.plot.setFollowDataInSubranges(true);
        this.plot.setRange(0, 100);
        this.plot.setForegroundAlpha(100);

        // this.chart.setBackgroundPaint(new JPanel().getBackground());
        this.chart.setAntiAlias(true);
        this.chart.setPadding(new RectangleInsets(0, 0, 0, 0));
        this.chart.setTitle(this.title);

        this.panel = new ChartPanel(this.chart);
        double coeff = 0.3;
        int width = (int) (this.panel.getPreferredSize().getWidth() * coeff);
        int height = (int) (this.panel.getPreferredSize().getHeight() * coeff);
        this.panel.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Renvoie le panneau d'affichage du thermomètre.
     * 
     * @return Panneau d'affichage du thermomètre.
     */
    public ChartPanel getPanel () {
        return this.panel;
    }
}
