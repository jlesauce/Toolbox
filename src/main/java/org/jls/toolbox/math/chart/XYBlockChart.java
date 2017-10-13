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
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jls.toolbox.math.Point;

/**
 * Permet de créer un graphique en deux dimensions mais à 3 variables. En effet,
 * le graphique se présente comme un simple graphique en deux dimensions mais la
 * troisème dimension est représentée par une échelle de couleur.
 * 
 * @author LE SAUCE Julien
 * @date Feb 13, 2015
 */
public class XYBlockChart {

    private final Color CHART_BACKGROUND_COLOR = new JPanel().getBackground();
    private final Color CHART_FOREGROUND_COLOR = new JLabel().getForeground();
    private final Color CROSSHAIR_COLOR = Color.BLUE;

    private final String title; // Titre du graphique
    private final String xTitle; // Titre de l'axe des abscisses
    private final String yTitle; // Titre de l'axe des ordonnées
    private final String zTitle; // Titre de l'axe des z
    private final XYZDataset dataset; // Les données affichées dans le graphique

    private JFreeChart chart; // Le graphique
    private XYPlot plot; // La zone de dessin des courbes
    private XYBlockRenderer renderer; // Renderer de la courbe
    private PaintScaleLegend scaleLegend; // Echelle des couleurs du graphique

    /**
     * Permet d'instancier un graphique en blocs.
     * 
     * @param title
     *            Titre du graphique.
     * @param xAxisLabel
     *            Titre de l'axe des abcisses.
     * @param yAxisLabel
     *            Titre de l'axe des ordonnées.
     * @param zAxisLabel
     *            Titre de l'échelle de couleurs.
     * @param dataset
     *            Données associées au graphique.
     */
    public XYBlockChart(String title, String xAxisLabel, String yAxisLabel, String zAxisLabel, XYZDataset dataset) {
        this.title = title;
        this.xTitle = xAxisLabel;
        this.yTitle = yAxisLabel;
        this.zTitle = zAxisLabel;
        this.dataset = dataset;

        createChart();
        setChartStyle();
    }

    /**
     * Permet de modifier l'échelle de couleur utilisée par le graphique. Cela va
     * créer un gradient de couleur entre les deux couleurs spécifiées, la première
     * couleur correspondant aux valeurs les plus fortes de l'échelle des données à
     * représenter. Les bornes inférieure et supérieure permettent de préciser
     * l'étendue des données représentées par cette échelle de couleur.
     * 
     * @param colorMin
     *            Couleur d'arrivée du gradient représentant les plus faibles
     *            valeurs de l'échelle.
     * @param colorMax
     *            Couleur de départ du gradient représentant les plus fortes valeurs
     *            de l'échelle.
     * @param lowerBound
     *            Borne inférieure de l'échelle des données représentées.
     * @param upperBound
     *            Bornes supérieure de l'échelle des données représentées.
     */
    public void setColorGradient (Color colorMin, Color colorMax, double lowerBound, double upperBound) {
        LookupPaintScale scale = new LookupPaintScale(lowerBound, upperBound, Color.lightGray);
        double r1, r2, dr, g1, g2, dg, b1, b2, db;
        int nbVal = (int) (upperBound - lowerBound + 0.5);

        // Acquisition des composantes
        r1 = colorMax.getRed();
        g1 = colorMax.getGreen();
        b1 = colorMax.getBlue();
        r2 = colorMin.getRed();
        g2 = colorMin.getGreen();
        b2 = colorMin.getBlue();

        // Calcul du delta entre les composantes
        dr = (r2 - r1) / nbVal;
        dg = (g2 - g1) / nbVal;
        db = (b2 - b1) / nbVal;

        // Création du gradient
        for (int i = 0; i < nbVal; i++) {
            scale.add(lowerBound + i, new Color((int) r2, (int) g2, (int) b2));
            r2 = r2 - dr;
            g2 = g2 - dg;
            b2 = b2 - db;
        }

        // Mise à jour du graphique
        this.renderer.setPaintScale(scale);
        this.scaleLegend.setScale(scale);

        // Du fait que la légende n'est pas liée aux modifications de la courbe,
        // il faut récréer la légende à la main.
        this.chart.removeSubtitle(this.scaleLegend);
        createPaintScaleLegend(scale);
    }

    /**
     * Permet d'ajuster automatiquement l'échelle de couleur de la courbe en
     * fonction des données affichées.
     * 
     * @param colorMin
     *            Permet de spécifier la couleur associée à la valeur maximale.
     * @param colorMax
     *            Permet de spécifier la couleur associée à la valeur minimale.
     * @return Renvoie un Point contenant la valeur du minimum et du maximum
     *         détectés.
     */
    public Point adjustColorScale (Color colorMin, Color colorMax) {
        double min = 0, max = 0;
        // S'il y a bien des données
        if (this.dataset.getItemCount(0) > 0) {
            min = this.dataset.getZValue(0, 0);
            max = this.dataset.getZValue(0, 0);
            // Recherche du min/max
            for (int i = 0; i < this.dataset.getItemCount(0); i++) {
                double value = this.dataset.getZValue(0, i);
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
            // Mise à jour de l'échelle
            if (max - min > 2000000) {
                throw new IllegalArgumentException("Too much values");
            }
            setColorGradient(colorMin, colorMax, min, max);
        }
        return new Point(min, max);
    }

    /**
     * Permet de modifier la largeur des blocs affichés par le renderer.
     * 
     * @param width
     *            Largeur en data/axis units.
     */
    public void setBlockWidth (double width) {
        this.renderer.setBlockWidth(width);
    }

    /**
     * Permet de créer le graphique à partir des paramètres spécifiés.
     */
    private void createChart () {
        // Création des axes
        NumberAxis xAxis = new NumberAxis(this.xTitle);
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLabelFont(new Font("Dialog", Font.BOLD, 14));
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);

        NumberAxis yAxis = new NumberAxis(this.yTitle);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLabelFont(new Font("Dialog", Font.BOLD, 14));
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);

        this.renderer = new XYBlockRenderer();
        PaintScale scale = new GrayPaintScale(0, 10);
        this.renderer.setPaintScale(scale);

        // Création de la courbe
        this.plot = new XYPlot(this.dataset, xAxis, yAxis, this.renderer);

        // Création du graphique
        this.chart = new JFreeChart(this.title, this.plot);
        this.chart.removeLegend();

        createPaintScaleLegend(scale);
        setColorGradient(Color.yellow, Color.red, 0, 1);
    }

    /**
     * Permet de paramètrer le graphique une fois créé.
     */
    private void setChartStyle () {
        this.plot.setBackgroundAlpha((float) 0.0);
        this.plot.setDomainCrosshairLockedOnData(false);
        this.plot.setRangeCrosshairLockedOnData(false);
        this.plot.setDomainCrosshairVisible(true);
        this.plot.setRangeCrosshairVisible(true);
        this.plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        this.plot.setDomainGridlinesVisible(true);
        this.plot.setRangeGridlinesVisible(true);
        this.plot.setRangeGridlinePaint(Color.white);

        this.plot.setDomainCrosshairStroke(new BasicStroke(1f));
        this.plot.setRangeCrosshairStroke(new BasicStroke(1f));
        this.chart.setBackgroundPaint(this.CHART_BACKGROUND_COLOR);

        NumberAxis xAxis = (NumberAxis) this.plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) this.plot.getRangeAxis();

        xAxis.setAxisLinePaint(this.CHART_FOREGROUND_COLOR);
        xAxis.setLabelPaint(this.CHART_FOREGROUND_COLOR);
        xAxis.setTickLabelPaint(this.CHART_FOREGROUND_COLOR);
        xAxis.setTickMarkPaint(this.CHART_FOREGROUND_COLOR);
        xAxis.setAutoRange(true);
        xAxis.setAutoRangeIncludesZero(false);

        yAxis.setAxisLinePaint(this.CHART_FOREGROUND_COLOR);
        yAxis.setLabelPaint(this.CHART_FOREGROUND_COLOR);
        yAxis.setTickLabelPaint(this.CHART_FOREGROUND_COLOR);
        yAxis.setTickMarkPaint(this.CHART_FOREGROUND_COLOR);
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(false);

        this.plot.setBackgroundPaint(this.CHART_FOREGROUND_COLOR);
        this.plot.setDomainGridlinePaint(this.CHART_FOREGROUND_COLOR);
        this.plot.setRangeGridlinePaint(this.CHART_FOREGROUND_COLOR);
        this.plot.setDomainCrosshairPaint(this.CROSSHAIR_COLOR);
        this.plot.setRangeCrosshairPaint(this.CROSSHAIR_COLOR);
    }

    /**
     * Permet de créer l'échelle de couleur du graphique.
     * 
     * @param scale
     *            Echelle de couleur utilisée pour représenter les données.
     */
    private void createPaintScaleLegend (PaintScale scale) {
        NumberAxis axis = new NumberAxis(this.zTitle);
        axis.setLabelFont(new Font("Dialog", Font.BOLD, 14));
        this.scaleLegend = new PaintScaleLegend(scale, axis);
        this.scaleLegend.setPosition(RectangleEdge.RIGHT);
        this.scaleLegend.setAxisLocation(AxisLocation.TOP_OR_RIGHT);
        this.scaleLegend.setMargin(new RectangleInsets(0.0, 10.0, 25.0, 0.0));
        this.scaleLegend.setBackgroundPaint(this.CHART_BACKGROUND_COLOR);
        this.chart.addSubtitle(this.scaleLegend);
    }

    /**
     * Renvoie la courbe {@link JFreeChart} permettant d'être ajoutée à un
     * {@link ChartPanel}.
     * 
     * @return Courbe {@link JFreeChart}.
     */
    public JFreeChart getChart () {
        return this.chart;
    }
}
