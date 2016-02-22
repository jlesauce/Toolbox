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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.jfree.data.xy.AbstractXYDataset;

/**
 * Classe permettant de charger les modèles de données des courbes dans le
 * graphique JFreeChart. Il est possible d'ajouter autant de courbes que
 * nécessaires sachant que chaque courbe est représentée par un modèle de
 * données distinct. Cette classe sera alors observatrice de chaque modèle de
 * données et sera donc notifiée à chaque mise à jour de l'un des modèle. Les
 * données manipulées correspondent cependant à des coordonnées dans un repère à
 * deux dimensions ou chaque point est représenté selon ses coordonnées (x,y).
 * 
 * @author LE SAUCE Julien
 * @date Feb 13, 2015
 */
public class XYDataset extends AbstractXYDataset implements Observer {

	private static final long serialVersionUID = -2492867249593419895L;

	private final ArrayList<AbstractXYZDataModel> dataModels;

	/**
	 * Permet d'instancier un set de données à partir des modèles de données des
	 * courbes.
	 * 
	 * @param xyDataModel
	 *            Modèles de données des courbes à afficher.
	 */
	public XYDataset (AbstractXYZDataModel... xyDataModel) {
		super();
		this.dataModels = new ArrayList<>();
		for (AbstractXYZDataModel model : xyDataModel) {
			this.dataModels.add(model);
			model.addObserver(this);
		}
	}

	@Override
	public void update (Observable obs, Object arg) {
		if (obs instanceof AbstractXYZDataModel) {
			fireDatasetChanged();
		}
	}

	@Override
	public int getItemCount (int series) {
		AbstractXYZDataModel model = this.dataModels.get(series);
		return model != null ? model.getSize() : 0;
	}

	@Override
	public Number getX (int series, int item) {
		AbstractXYZDataModel model = this.dataModels.get(series);
		return model != null ? (Number) model.getX(item) : null;
	}

	@Override
	public Number getY (int series, int item) {
		AbstractXYZDataModel model = this.dataModels.get(series);
		return model != null ? (Number) model.getY(item) : null;
	}

	@Override
	public int getSeriesCount () {
		return this.dataModels != null ? this.dataModels.size() : 0;
	}

	@Override
	public Comparable<?> getSeriesKey (int series) {
		return this.dataModels.get(series).getKey();
	}
}