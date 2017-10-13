/*
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
 */

package org.jls.toolbox.math;

/**
 * Permet de calculer la transformée de Fourier discrète d'un set
 * d'échantillons.
 * 
 * @author LE SAUCE Julien
 * @date Apr 7, 2015
 */
public class DFT {

    /**
     * Permet de calculer la réponse fréquentielle d'un filtre à partir de sa
     * réponse impulsionnelle. Cela revient en fait à calculer la transformée en Z
     * de la réponse impulsionnelle du filtre, avec T(z)=Σ(k=0..N)[c(k)*z^(-k)] la
     * transformée en z du signal c (qui est en fait la suite discrète des
     * coefficients de la réponse impulsionnelle) et z l'opérateur complexe tel que
     * z=exp(-2.j.PI.f). La méthode calcule donc la réponse fréquentielle du filtre
     * entre f0 et f1 (en Hertz) à la fréquence d'échantillonnage Fs à partir d'un
     * tableau contenant la liste des coefficients de la réponse impulsionnelle du
     * filtre puis renvoie le résultat dans un tableau à deux dimensions de taille
     * Nx2 avec N le nombre de points calculés pour la réponse fréquentielle.
     * 
     * @param f0
     *            Fréquence de départ pour l'affichage de la courbe.
     * @param f1
     *            Fréquence de fin pour l'affichage de la courbe.
     * @param fs
     *            Fréquence d'échantillonnage du filtre (celle qui a été utilisée
     *            pour générer les coefficients de la réponse impulsionnelle).
     * @param N
     *            Nombre de points à calculer pour la réponse fréquentielle.
     * @param wk
     *            Tableau à une dimension de taille quelconque contenant la liste
     *            des coefficients du filtre (coefficients de la réponse
     *            impulsionnelle).
     * @return Tableau à deux dimensions de taille N*2 contenant la liste des points
     *         (x,y) de la réponse fréquentielle tels que x représente la fréquence
     *         en Hz et y le gain de la réponse en décibels.
     */
    public final static double[][] TFDGain (double f0, double f1, double fs, int N, double[] wk) {
        double[][] freqResp; // Réponse calculée
        double[][] resp; // Réponse en gain
        double dF = 0.0; // Résolution fréquentielle
        double f = 0.0; // Fréquence instantanée

        // Vérification des paramètres
        if (f0 >= f1) {
            throw new IllegalArgumentException("F0 frequency must be smaller than F1");
        }
        if (N <= 0) {
            throw new IllegalArgumentException("The number of points must be positive");
        }
        if (wk == null || wk.length == 0) {
            throw new IllegalArgumentException("Coefficient list cannot be null or empty");
        }
        // Réponse fréquentielle calculée
        // Liste des points de la courbe contenant :
        // [fréquence][partie réelle][partie imaginaire]
        freqResp = new double[N][3];
        // Réponse en gain
        // Liste des points de la courbe contenant :
        // [fréquence][gain]
        resp = new double[N][2];

        // Calcul de la résolution fréquentielle
        dF = (f1 - f0) / N;

        // Calcul des valeurs de la réponse pour chaque fréquence
        f = f0;
        for (int i = 0; i < N; i++) {
            freqResp[i][0] = f;
            freqResp[i][1] = 0.0;
            freqResp[i][2] = 0.0;
            // Calcul de la valeur pour la fréquence donnée
            // avec X(z) = Somme [ x(k).z^(-k)] et z = exp(-2.k.Pi.f)
            for (int k = 0; k < wk.length; k++) {
                // Partie réelle
                freqResp[i][1] += wk[k] * Math.cos(-2 * Math.PI * f * k / fs);
                // Partie imaginaire
                freqResp[i][2] += wk[k] * Math.sin(-2 * Math.PI * f * k / fs);
            }
            f += dF; // On passe au point suivant de la courbe
        }

        // Calcul du gain : 20*log(module)
        for (int i = 0; i < N; i++) {
            double mod = Math.sqrt(freqResp[i][1] * freqResp[i][1] + freqResp[i][2] * freqResp[i][2]);
            resp[i][0] = freqResp[i][0];
            resp[i][1] = 20 * Math.log10(mod);
        }
        return resp;
    }
}
