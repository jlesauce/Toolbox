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

package org.jls.toolbox.math;

/**
 * Permet de centraliser diverses fonctions mathématiques utiles.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class MathUtils {

	/**
	 * Permet d'arrondir une valeur à un nombre de décimales près.
	 * 
	 * @param value
	 *            Valeur à arrondir.
	 * @param nbDigits
	 *            Nombre de chiffres après la virgule.
	 * @return Valeur arrondie.
	 */
	public static double round (double value, int nbDigits) {
		double val = value;
		if (nbDigits > 0) {
			double p = Math.pow(10, nbDigits);
			val = Math.round(value * p) / p;
		}
		return val;
	}

	/**
	 * Renvoie l'index de la valeur maximale du tableau spécifié.
	 * 
	 * @param tab
	 *            Tableau dont on cherche le maximum.
	 * @return Index de la case correspondant à la valeur maximale du tableau
	 *         spécifié.
	 */
	public static int max (final double[] tab) {
		int index = 0;
		double max = tab[index];
		for (int i = 0; i < tab.length; i++) {
			index = tab[i] > max ? i : index;
		}
		return index;
	}

	/**
	 * Renvoie l'index de la valeur minimale du tableau spécifié.
	 * 
	 * @param tab
	 *            Tableau dont on cherche le minimum.
	 * @return Index de la case correspondant à la valeur minimale du tableau
	 *         spécifié.
	 */
	public static int min (final double[] tab) {
		if (tab == null) {
			throw new NullPointerException("Array cannot be null");
		}
		if (tab.length == 0) {
			throw new IllegalArgumentException("Array has zero length");
		}
		int index = 0;
		double max = tab[index];
		for (int i = 0; i < tab.length; i++) {
			index = tab[i] < max ? i : index;
		}
		return index;
	}

	/**
	 * Renvoie l'index de la valeur maximale, en valeur absolue, du tableau
	 * spécifié. C'est-à-dire l'index de la valeur minimale du tableau si
	 * <i>|min| > |max|</i> ou la valeur maximale si <i>|max| > |min|</i>.
	 * 
	 * @param tab
	 *            Tableau dont on cherche le maximum en valeur absolue.
	 * @return Index de la case correspondant à la valeur maximale en valeur
	 *         absolue du tableau spécifié.
	 */
	public static int maxAbs (final double[] tab) {
		if (tab == null) {
			throw new NullPointerException("Array cannot be null");
		}
		if (tab.length == 0) {
			throw new IllegalArgumentException("Array has zero length");
		}
		int index = 0;
		double max = tab[0];
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] > max) {
				index = i;
				max = tab[i];
			}
		}
		return index;
	}

	/**
	 * Permet de calculer la valeur moyenne d'un tableau de taille quelconque.
	 * 
	 * @param an
	 *            Liste des valeurs à moyenner.
	 * @return Valeur moyenne calculée.
	 */
	public static double mean (double[] an) {
		if (an == null) {
			throw new NullPointerException("Array cannot be null");
		}
		if (an.length == 0) {
			throw new IllegalArgumentException("Array has zero length");
		}
		double sum = 0.0;
		for (int n = 0; n < an.length; n++) {
			sum += an[n];
		}
		return sum / an.length;
	}

	/**
	 * Permet de calculer la moyenne quadratique d'un tableau d'échantillons de
	 * taille quelconque. La formule appliquée est sqrt[Sum(k=0..N-1, wk*wk)/N],
	 * avec wk les échantillons et N le nombre de points.
	 * 
	 * @param an
	 *            Liste des valeurs à moyenner.
	 * @return Valeur moyenne quadratique calculée.
	 */
	public static double rootMeanSquare (double[] an) {
		if (an == null) {
			throw new NullPointerException("Array cannot be null");
		}
		if (an.length == 0) {
			throw new IllegalArgumentException("Array has zero length");
		}
		double sum = 0.0;
		for (int n = 0; n < an.length; n++) {
			sum += an[n] * an[n];
		}
		return Math.sqrt(sum / an.length);
	}

	/**
	 * Permet de normaliser les valeurs du tableau par le maximum en valeur
	 * absolue. Cela a pour effet de normaliser toutes les valeurs entre -1.0 et
	 * 1.0. Pour cela on vient rechercher le maximum en valeur absolue puis on
	 * divise toutes les valeurs du tableau par cette valeur.
	 * 
	 * @param tab
	 *            Tableau à normaliser.
	 * @return Renvoie la liste des valeurs normalisées.
	 */
	public static double[] normalize (final double[] tab) {
		if (tab == null) {
			throw new NullPointerException("Array cannot be null");
		}
		if (tab.length == 0) {
			throw new IllegalArgumentException("Array has zero length");
		}
		// Recherche de la valeur absolue du max
		double max = Math.abs(tab[maxAbs(tab)]);
		// On divise toutes les valeurs par le maximum
		for (int i = 0; i < tab.length; i++) {
			tab[i] /= max;
		}
		return tab;
	}

	/**
	 * Permet de calculer le module et la phase à partir d'une liste de nombres
	 * complexes.
	 * 
	 * @param real
	 *            Liste de N valeurs réelles.
	 * @param img
	 *            Liste de N valeurs imaginaires.
	 * @param magnitude
	 *            Tableau pré-alloué de taille N représentant les modules
	 *            calculés.
	 * @param phase
	 *            Tableau pré-alloué de taille N représentant les phases
	 *            calculées en radians.
	 */
	public static void magnitudePhase (float[] real, float[] img, float[] magnitude, float[] phase) {
		for (int i = 0; i < real.length; i++) {
			// Module
			magnitude[i] = (float) Math.hypot(real[i], img[i]);
			// Phase
			// Cas d'une limite infinie (division par 0)
			if (real[i] == 0) {
				if (img[i] >= 0) {
					phase[i] = (float) (4 * Math.atan(1.0) / 2.0f);
				} else {
					phase[i] = (float) (-4 * Math.atan(1.0) / 2.0f);
				}
			} else {
				phase[i] = (float) Math.atan(img[i] / real[i]);
			}
		}
	}

	/**
	 * Permet de calculer le gain en décibels à partir du module tel que
	 * <i>gain(n)=20*log10(mod(n))</i>.
	 * 
	 * @param mod
	 *            Tableau de taille quelconque contenant les valeurs de module
	 *            dont on souhaite calculer le gain en dB.
	 */
	public static void gain (float[] mod) {
		for (int i = 0; i < mod.length; i++) {
			mod[i] = (float) (20 * Math.log10(mod[i]));
		}
	}
}