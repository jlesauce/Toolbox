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

import org.jls.toolbox.math.Sweep.SweepType;
import org.jls.toolbox.math.Window.WindowType;

/**
 * Permet de centraliser les différentes fonctions utilisées dans la génération
 * de formes d'onde.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class WaveformGenerator {

	/**
	 * Permet de générer une forme d'onde sinusoïdale modulée en fréquence et en
	 * amplitude, de fréquence centrale Fc, de largeur de bande B, de durée T et
	 * échantillonnée à la fréquence Fs.
	 * 
	 * @param win
	 *            Permet de spécifier le type d'enveloppe pour la modulation
	 *            d'amplitude.
	 * @param sw
	 *            Permet de spécifier le type de balayage de fréquence.
	 * @param fc
	 *            Fréquence centrale de la forme d'onde en hertz.
	 * @param b
	 *            Largeur de bande en hertz.
	 * @param fs
	 *            Fréquence d'échantillonnage en hertz.
	 * @param T
	 *            Durée de la forme d'onde en millisecondes.
	 * @return Tableau de taille N représentant les échantillons de la forme
	 *         d'onde générée avec <i>N=T*Fs</i>.
	 */
	public static double[] computeWaveform (WindowType win, SweepType sw, int fc, int b, int fs, long T) {
		if (sw.equals(SweepType.NONE)) {
			return Window.computeWindow(win, fs, T);
		}
		double[] wn = Sweep.computeFM(sw, fc, b, fs, T);
		double[] env = Window.computeWindow(win, fs, T);
		for (int n = 0; n < wn.length; n++) {
			wn[n] *= env[n];
		}
		return wn;
	}
}