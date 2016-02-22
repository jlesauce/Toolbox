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
 * Permet de générer différents types de fenêtrages.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class Window {

	private static final double PI = Math.PI;

	/**
	 * Permet de représenter les différents types de fenêtrages utilisés et qui
	 * ne nécessitent aucun paramètres particuliers.
	 * 
	 * @author LE SAUCE Julien
	 * @date Feb 12, 2015
	 */
	public enum WindowType {
		RECTANGULAR, COS2, SIN2, TUKEY, FLAT_TOP, BLACKMAN_HARRIS, BLACKMAN_NUTTALL, HANN;
	}

	/**
	 * Permet de générer une fenêtre du type spécifié sur N points.
	 * 
	 * @param win
	 *            Fenêtre souhaité de type {@link WindowType}.
	 * @param f
	 *            Fréquence d'échantillonnage en hertz utilisée pour la
	 *            génération de la fenêtre.
	 * @param T
	 *            Longueur de la fenêtre en millisecondes.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] computeWindow (WindowType win, int f, long T) {
		int N = (int) (f * T / 1000);
		return computeWindow(win, N);
	}

	/**
	 * Permet de générer une fenêtre du type spécifié sur N points.
	 * 
	 * @param win
	 *            Fenêtre souhaité de type {@link WindowType}.
	 * @param N
	 *            Nombre de points du fenêtrage.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] computeWindow (WindowType win, int N) {
		switch (win) {
			case RECTANGULAR:
				return rectangular(N);
			case COS2:
				return cos2(N);
			case SIN2:
				return sin2(N);
			case TUKEY:
				return tukey(N);
			case FLAT_TOP:
				return flatTop(N);
			case BLACKMAN_HARRIS:
				return blackmanHarris(N);
			case BLACKMAN_NUTTALL:
				return blackmanNuttall(N);
			case HANN:
				return hann(N);
			default:
				throw new IllegalArgumentException("Invalid window type");
		}
	}

	/**
	 * Permet de générer une fenêtre rectangulaire sur N points, c'est-à-dire
	 * tous les coefficients à 1.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] rectangular (int N) {
		double[] wn = new double[N];
		for (int n = 0; n < N; n++) {
			wn[n] = 1.0;
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de type cosinus carré sur N points.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] cos2 (int N) {
		double[] cos2 = new double[N];
		// Formule Custom
		for (int n = 0; n < N; n++) {
			cos2[n] = Math.cos((PI / 2.0) * (n - (N / 2.0 - 0.5)) / (N / 1.6));
			cos2[n] *= cos2[n];
		}
		return cos2;
	}

	/**
	 * Permet de générer une fenêtre de type sinus carré sur N points.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] sin2 (int N) {
		double[] wn = new double[N];
		for (int n = 0; n < N; n++) {
			wn[n] = Math.sin((PI * n) / N);
			wn[n] *= wn[n];
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre en puissance de cosinus sur N points tel
	 * que <i>w(n)=cos<sup>a</sup>((PI.n)/(N-1) - PI/2)</i> avec <i>a</i> un
	 * nombre entier positif.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @param a
	 *            Valeur de puissance du cosinus tel que
	 *            <i>w(n)=cos<sup>a</sup>(x)</i>.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] cosine (int N, int a) {
		double[] wn = new double[N];
		for (int n = 0; n < N; n++) {
			double cos = Math.cos((PI * n) / (N - 1.0) - PI / 2.0);
			wn[n] = Math.pow(cos, a);
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre en puissance de sinus sur N points tel que
	 * <i>w(n)=sin<sup>a</sup>((PI.n)/(N-1) - PI/2)</i> avec <i>a</i> un nombre
	 * entier positif.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @param a
	 *            Valeur de puissance du sinus tel que
	 *            <i>w(n)=sin<sup>a</sup>(x)</i>.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] sine (int N, int a) {
		double[] wn = new double[N];
		for (int n = 0; n < N; n++) {
			double sin = Math.sin((PI * n) / (N - 1.0) - PI / 2.0);
			wn[n] = Math.pow(sin, a);
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de Tukey sur N points. La fenêtre de Tukey
	 * (cosinus applati) permet de diminuer l'amplitude des valeurs aux
	 * extrémités et d'attribuer le coefficient maximal au centre de la fenêtre.
	 * Pour cela on divise l'intervalle en trois parties en appliquant les
	 * formules suivantes :
	 * <p>
	 * w(n) =
	 * </p>
	 * <ul>
	 * <li><i>1/2 * [ 1+cos(PI*(2n/alpha(N-1))- 1) ]</i>, n appartenant à {0;
	 * alpha*(N-1)/2}
	 * 
	 * <li><i>1</i>, n appartenant à {alpha*(N-1)/2; (N-1)*(1-alpha/2)}
	 * 
	 * <li><i>1/2 * [ 1+cos(PI*(2n/alpha(N-1))-2/alpha+1) ]</i>, n appartenant à
	 * {(N-1)*(1-alpha/2); (N-1)}
	 * </ul>
	 * 
	 * avec N le nombre de points de la fenêtre et alpha le coefficient entre 0
	 * et 1 permettant de paramètrer la largeur du centre de la fenêtre.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @param alpha
	 *            Coefficient permettant de paramètrer la largeur du centre de
	 *            la fenêtre (0 correspond à une fenêtre rectangulaire et 1
	 *            correspond à une fenêtre de Hann).
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] tukey (int N, float alpha) {
		double[] wn = new double[N];
		double borneInf = (alpha * (N - 1) / 2.0);
		double borneSup = (N - 1.0) * (1.0 - alpha / 2.0);
		for (int n = 0; n < N; n++) {
			// 0 <= n <= borneInf
			if (n >= 0 && n <= borneInf) {
				// 1/2 * [ 1 + cos( PI * (2n / alpha(N-1)) - 1 ) ]
				wn[n] = 0.5 * (1 + Math.cos(PI * ((2.0 * n) / (alpha * (N - 1)) - 1)));
			}
			// borneInf < n < borneSup
			else if (n > borneInf && n < borneSup) {
				wn[n] = 1.0;
			}
			// borneSup <= n <= (N-1)
			else {
				// 1/2 * [ 1 + cos( PI * (2n / alpha(N-1)) - 2/alpha + 1 ) ]
				wn[n] = 0.5 * (1 + Math.cos(PI * ((2.0 * n) / (alpha * (N - 1)) - 2.0 / alpha + 1)));
			}
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de Tukey sur N points. La fenêtre de Tukey
	 * (cosinus applati) permet de diminuer l'amplitude des valeurs aux
	 * extrémités et d'attribuer le coefficient maximal au centre de la fenêtre.
	 * Attention la formule utilisée est plus simple que la formule officielle,
	 * elle consiste à diviser l'intervalle en trois parties de respectivement
	 * 1/8, 6/8 et 1/8 de l'intervalle. La partie centrale est une simple
	 * fenêtre carrée et les parties extrèmes sont calculées en utilisant la
	 * formule suivante :
	 * 
	 * <p>
	 * <i>w(n) = cos(PI/2 * (j - ((N/8 - 0.5) / (N/8))))</i>
	 * </p>
	 * 
	 * avec j indice variant uniquement sur les intervalles aux extrémités (en
	 * ignorant la bande centrale rectangulaire).
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] tukey (int N) {
		double[] wn = new double[N];
		int j = 0; // j balaye uniquement les intervalles extrèmes
		for (int n = 0; n < N; n++) {
			if ((n >= N / 8) && (n < N - N / 8)) {
				wn[n] = 1.0;
			} else {
				wn[n] = Math.cos((PI / 2.0) * (j - ((N / 8.0) - 0.5)) / (N / 8.0));
				j++;
			}
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de Blackman généralisée sur N points. La
	 * fenêtre de Blackman généralisée est de la forme
	 * <i>w(n)=Sum[0..k]{(-1)<sup>k</sup>ak.cos((2.PI.k.n)/(N-1))}</i> avec k le
	 * nombre de degrés du fenêtrage.
	 * 
	 * @param N
	 *            Nombre de points du fenêtrage.
	 * @param ak
	 *            Tableau représentant les valeurs <i>a(k)</i> dont la taille
	 *            permet de spécifier le degré du fenêtrage.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] blackman (int N, double[] ak) {
		double wn[] = new double[N];
		for (int n = 0; n < N; n++) {
			double w = 0;
			for (int k = 0; k < ak.length; k++) {
				w += Math.pow(-1, k) * ak[k] * Math.cos((2 * PI * k * n) / (N - 1.0));
			}
			wn[n] = w;
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de Blackman-Harris sur N Points. Cette
	 * fenêtre est un cas particulier de la fenêtre de Blackman généralisée avec
	 * a0=0.35875, a1=0.48829, a2=0.14128, a3=0.01168.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] blackmanHarris (int N) {
		double[] ak = {0.35875, 0.48829, 0.14128, 0.01168};
		return blackman(N, ak);
	}

	/**
	 * Permet de générer une fenêtre de Blackman-Nuttall sur N Points. Cette
	 * fenêtre est un cas particulier de la fenêtre de Blackman généralisée avec
	 * a0=0.3635819, a1=0.4891775, a2=0.1365995, a3=0.0106411.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] blackmanNuttall (int N) {
		double[] ak = {0.3635819, 0.4891775, 0.1365995, 0.0106411};
		return blackman(N, ak);
	}

	/**
	 * Permet de générer une fenêtre "flat top" normalisée sur N points
	 * (c'est-à-dire variant entre 0.0 et 1.0).
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] flatTop (int N) {
		double[] wn = new double[N];
		double[] a = {0.21557895, -0.41663158, 0.277263158, -0.083578947, 0.006947368};
		for (int n = 0; n < N; n++) {
			wn[n] = 0.0;
			for (int i = 0; i < a.length; i++) {
				wn[n] += a[i] * Math.cos((2 * i * n * PI) / (N - 1));
			}
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de Hamming généralisée sur N points de la
	 * forme <i>w(n)=alpha - beta*cos((2.PI.n) / (N-1))</i>.
	 * 
	 * @param alpha
	 *            Permet de spécifier la valeur alpha.
	 * @param beta
	 *            Permet de spécifier la valeur beta.
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] hamming (double alpha, double beta, int N) {
		double wn[] = new double[N];
		for (int n = 0; n < N; n++) {
			wn[n] = alpha - beta * Math.cos((2 * PI * n) / (N - 1));
		}
		return wn;
	}

	/**
	 * Permet de générer une fenêtre de Hann (ou Hanning par abus de langage)
	 * sur N points. La fenêtre de Hann est un cas particulier de la fenêtre de
	 * Hamming avec alpha=1.5 et beta=1.5.
	 * 
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] hann (int N) {
		return hamming(1.5, 1.5, N);
	}

	/**
	 * Permet de générer une fenêtre gaussienne de paramètre sigma sur N points.
	 * 
	 * @param sigma
	 *            Paramètre sigma tel que sigma < 0.5.
	 * @param N
	 *            Nombre de points de la fenêtre.
	 * @return Tableau de taille N représentant les coefficients de la fenêtre.
	 */
	public static double[] gaussian (double sigma, int N) {
		if (sigma > 0.5) {
			throw new IllegalArgumentException("Sigma must be fewer than 0.5");
		}
		double wn[] = new double[N];
		for (int n = 0; n < N; n++) {
			wn[n] = Math.exp(-0.5 * Math.pow((n - 0.5 * (N - 1)) / (0.5 * sigma * (N - 1)), 2));
		}
		return wn;
	}
}