/*#
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
 #*/

package org.jls.toolbox.math.chart;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Permet d'afficher une simple courbe à deux dimensions y = f(x).
 * 
 * @author LE SAUCE Julien
 * @date Feb 13, 2015
 */
public class XYLineChart {

	private final Color CHART_BACKGROUND_COLOR = new JPanel().getBackground();
	private final Color CHART_FOREGROUND_COLOR = new JLabel().getForeground();
	private final Color CROSSHAIR_COLOR = Color.BLUE;

	private final String title; // Titre du graphique
	private final String xTitle; // Titre de l'axe des abscisses
	private final String yTitle; // Titre de l'axe des ordonnées
	private final XYDataset dataset; // Les données affichées dans le graphique
	private final boolean isLegendVisible; // Affichage de la légende
	private final boolean isTooltipsVisible; // Affichge des tooltips
	private final boolean isUrlVisible; // Affichage des urls (???)
	private final boolean isGridXVisible; // Affichage de la grille verticale
	private final boolean isGridYVisible; // Affichage de la grille horizontale

	private JFreeChart chart; // Le graphique
	private XYPlot plot; // La zone de dessin des courbes

	/**
	 * Permet d'instancier une courbe à deux dimensions.
	 * 
	 * @param title
	 *            Titre du graphique.
	 * @param xAxisLabel
	 *            Titre de l'axe des abscisses.
	 * @param yAxisLabel
	 *            Titre de l'axe des ordonnées.
	 * @param dataset
	 *            Série de données de la courbe.
	 * @param legend
	 *            Affichage de la légende.
	 * @param tooltip
	 *            Affichage des tooltips.
	 * @param url
	 *            Affichage des urls.
	 */
	public XYLineChart (String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, boolean legend,
			boolean tooltip, boolean url) {
		this.title = title;
		this.xTitle = xAxisLabel;
		this.yTitle = yAxisLabel;
		this.isLegendVisible = legend;
		this.isTooltipsVisible = tooltip;
		this.isUrlVisible = url;
		this.dataset = dataset;
		this.isGridXVisible = true;
		this.isGridYVisible = true;

		createChart();
		setChartStyle();
	}

	/**
	 * Permet d'élargir les lignes du curseur de la courbe.
	 */
	public void setCrossHairBold () {
		this.plot.setDomainCrosshairStroke(new BasicStroke(1f));
		this.plot.setRangeCrosshairStroke(new BasicStroke(1f));
	}

	/**
	 * Permet de faire en sorte que le curseur de la courbe se localise
	 * automatiquement sur les points de la courbe.
	 * 
	 * @param flag
	 *            Le flag.
	 */
	public void setCrosshairLockedOnData (boolean flag) {
		this.plot.setDomainCrosshairLockedOnData(flag);
		this.plot.setRangeCrosshairLockedOnData(flag);
	}

	/**
	 * Permet de créer le graphique à partir des paramètres spécifiés.
	 */
	private void createChart () {
		this.chart = ChartFactory.createXYLineChart(this.title, // Titre du
																// graphique
				this.xTitle, // Titre de l'axe des abscisses
				this.yTitle, // Titre de l'axe des ordonnées
				this.dataset, // Valeurs de la courbe
				PlotOrientation.VERTICAL, // Orientation du graphique
				this.isLegendVisible, // Affichage de la légende
				this.isTooltipsVisible, // Affichage des tooltips
				this.isUrlVisible); // Affichage des urls
		this.plot = (XYPlot) this.chart.getPlot();

		if (this.chart.getLegend() != null) {
			this.chart.getLegend().setBackgroundPaint(new JPanel().getBackground());
			this.chart.getLegend().setItemPaint(new JLabel().getForeground());
		}
	}

	/**
	 * Permet de paramètrer le graphique une fois créé.
	 */
	private void setChartStyle () {
		// Paramètrage des courbes
		this.plot.setBackgroundAlpha((float) 0.0);
		this.plot.setDomainCrosshairVisible(this.isGridXVisible);
		this.plot.setDomainCrosshairLockedOnData(true);
		this.plot.setRangeCrosshairVisible(this.isGridYVisible);
		this.plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
		this.plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

		NumberAxis xAxis = (NumberAxis) this.plot.getDomainAxis();
		NumberAxis yAxis = (NumberAxis) this.plot.getRangeAxis();

		this.chart.setBackgroundPaint(this.CHART_BACKGROUND_COLOR);
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

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) this.plot.getRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setBaseShapesFilled(false);
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