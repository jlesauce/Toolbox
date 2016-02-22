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

/**
 * Implémentation du modèle de données contenant les valeurs de la courbe à
 * afficher.
 * 
 * @see AbstractXYZDataModel
 * 
 * @author LE SAUCE Julien
 * @date Feb 13, 2015
 */
public class XYZDataModel extends AbstractXYZDataModel {

	/**
	 * Identifiant de la série de données.
	 */
	private final String key;

	/**
	 * Liste contenant tous les couples (x,y,z) de la courbe à afficher. La
	 * taille de la liste est de N*Dimension avec N le nombre d'éléments de la
	 * liste et Dimension le nombre d'axe représentés par la courbe.
	 */
	private float[][] xyzData;

	/**
	 * Permet d'instancier un modèle de données à partir d'un tableau à deux
	 * dimensions.
	 * 
	 * @param key
	 *            Identifiant de la série de données.
	 * @param data
	 *            Points (x,y,z) de la courbe.
	 */
	public XYZDataModel (final String key, final float[][] data) {
		super();
		this.key = key;
		this.xyzData = data;
	}

	@Override
	public final double getX (final int index) {
		return this.xyzData[index][0];
	}

	@Override
	public final double getY (final int index) {
		return this.xyzData[index][1];
	}

	@Override
	public final double getZ (final int index) {
		return this.xyzData[index][2];
	}

	@Override
	public final int getSize () {
		return this.xyzData == null ? 0 : this.xyzData.length;
	}

	@Override
	public final String getKey () {
		return this.key;
	}

	@Override
	public final int indexOf (double x, double y, double epsilon) {
		double da, dy;
		int index = 0;

		for (int i = 0; i < getSize(); i++) {
			da = Math.abs(this.xyzData[i][0] - x);
			dy = Math.abs(this.xyzData[i][1] - y);

			if (da < epsilon && dy < epsilon) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * Permet de remplacer la série de données par une nouvelle série. La
	 * modification des données entraîne une notification des observateurs du
	 * modèle et donc une mise à jour
	 * 
	 * @param data
	 *            Données de la courbe.
	 */
	public final void setData (float[][] data) {
		this.xyzData = data;
		notifyChanged();
	}
}