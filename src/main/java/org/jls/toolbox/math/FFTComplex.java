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

package org.jls.toolbox.math;

/**
 * Permet de calculer la FFT ou la FFT inverse d'une séquence de N nombres
 * complexes avec une complexité de O(N*logN). La différence avec la FFT normale
 * est que cette classe manipule des données de type {@link Complex}
 * directement.
 * <P>
 * Limitations :
 * <ul>
 * <li>N doit être une puissance de 2.
 * <li>Les calculs effectués sont peu optimisés (Représentation des nombres
 * complexes, réallocation des mémoires, ...).
 * </ul>
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class FFTComplex {

    /**
     * Permet de calculer la FFT d'une séquence de N nombres complexes.
     * 
     * @param x
     *            Séquence de N nombres complexes (N doit être une puissance de 2).
     * @return Séquence de nombres complexes représentant le résultat de la FFT.
     */
    public static Complex[] fft (Complex[] x) {
        int N = x.length;

        // Cas trivial
        if (N == 1) {
            return new Complex[] { x[0] };
        }

        // Si N n'est pas une puissance de 2
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // FFT sur les termes pairs
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // FFT sur les termes impairs
        Complex[] odd = even;
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // Combinaison
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].add(wk.times(r[k]));
            y[k + N / 2] = q[k].sub(wk.times(r[k]));
        }
        return y;
    }

    /**
     * Permet de calculer la FFT inverse d'une séquence de N nombres complexes.
     * 
     * @param x
     *            Séquence de N nombres complexes (N doit être une puissance de 2).
     * @return Séquence de nombres complexes représentant le résultat de la FFT
     *         inverse.
     */
    public static Complex[] ifft (Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // On prend le conjugué de chaque complexe
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // Calcul de la FFT sur les conjugués
        y = fft(y);

        // On reprend le conjugué de chaque nombre complexe
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // Division par N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0 / N);
        }
        return y;
    }

    /**
     * Permet de calculer la convolution circulaire de x[] et y[].
     * 
     * @param x
     *            Séquence de N nombres complexes (N doit être une puissance de 2).
     * @param y
     *            Séquence de N nombres complexes (N doit être une puissance de 2).
     * @return Séquence de nombres complexes représentant le résultat de la
     *         convolution.
     */
    public static Complex[] cconvolve (Complex[] x, Complex[] y) {
        // x et y doivent être de même taille
        if (x.length != y.length) {
            throw new RuntimeException("Different sizes for x[] nd y[].");
        }
        // Si N n'est pas une puissance de 2
        if (x.length % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }
        int N = x.length;

        // Calcul de la FFT de chaque séquence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // Multiplication complexe
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // Calcul de la FFT inverse
        return ifft(c);
    }

    /**
     * Permet de calculer la convolution liéaire de x[] et y[].
     * 
     * @param x
     *            Séquence de N nombres complexes.
     * @param y
     *            Séquence de N nombres complexes.
     * @return Séquence de nombres complexes représentant le résultat de la
     *         convolution.
     */
    public static Complex[] convolve (Complex[] x, Complex[] y) {
        Complex zero = new Complex(0, 0);

        Complex[] a = new Complex[2 * x.length];
        for (int i = 0; i < x.length; i++)
            a[i] = x[i];
        for (int i = x.length; i < 2 * x.length; i++)
            a[i] = zero;

        Complex[] b = new Complex[2 * y.length];
        for (int i = 0; i < y.length; i++)
            b[i] = y[i];
        for (int i = y.length; i < 2 * y.length; i++)
            b[i] = zero;

        return cconvolve(a, b);
    }
}
