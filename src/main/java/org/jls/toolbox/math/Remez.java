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
 * Permet de générer des filtres digitaux à réponse impulsionnelle finie (RIF) à
 * partir de certaines spécifications précisées par l'utilisateur. Ces
 * spécifications sont : le nombre de coefficients du filtre, les bandes de
 * fréquences du filtre, la réponse souhaitée sur chacune de ces bandes et le
 * poids attribué à l'erreur sur celles-ci. Pour cela on utilise la méthode
 * d'approximation de Chebyshev qui permet de réaliser une approximation
 * polynomiale de la réponse fréquentielle souhaitée. Les fonctions et
 * mécaniques utilisées ici sont décrites de façon précise dans "FIR Digital
 * Filter Design Techniques Using Weighted Chebyshev Approximation"
 * (L.R.RABINER, J.H.MCCLELLAN, T.W.PARKS) IEEE, "A Computer Program for
 * Designing Optimum FIR Linear Phase Digital Filters " (L.R.RABINER,
 * J.H.MCCLELLAN, T.W.PARKS) IEEE et "Discrete-Time Signal Processing"
 * (A.V.OPPENHEIM, R.W.SCHAFER, J.R.BUCK) Prentice Hall.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class Remez {

    public final static int BANDPASS = 1;
    public final static int DIFFERENTIATOR = 2;
    public final static int HILBERT = 3;

    private final static int POSITIVE = 0;
    private final static int NEGATIVE = 1;
    private final static int GRIDDENSITY = 16;
    private final static int MAXITERATIONS = 40;

    private double[] bands;
    private double[] des;
    private double[] weight;
    private int nbOfCoeff;
    private int type;

    /**
     * Permet d'instancier le générateur Remez.
     * 
     * @param nbOfCoeff
     *            Nombre de coefficients de la réponse impulsionnelle.
     * @param bands
     *            Tableau contenant les limites fréquentielles de chaque bande du
     *            filtre normalisées entre 0 et 0.5. Par exemple, pour une fréquence
     *            d'échantillonnage de 8KHz, une bande passante de 0 à 1.8 KHz et un
     *            bande coupée de 2.2 KHz à l'infini, on obtient [0, 0.45, 0.55,
     *            1.0] entre 0.0 et 1.0 ce qui nous donne [0, 0.225, 0.275, 0.5]
     *            entre 0.0 et 0.5.
     * @param des
     *            Tableau contenant la réponse désirée pour chaque bande de
     *            fréquence [nombre de bandes].
     * @param weight
     *            Poids attribué à l'erreur sur chaque bande du filtre [nombre de
     *            bandes / 2].
     * @param type
     *            Type du filtre (BANDPASS, HILBERT ou DIFFERENTIATOR).
     */
    public Remez(int nbOfCoeff, double bands[], double des[], double weight[], int type) {
        this.nbOfCoeff = nbOfCoeff;
        this.bands = bands;
        this.des = des;
        this.weight = weight;
        this.type = type;
    }

    /**
     * Permet de lancer la génération du filtre.
     * 
     * @return Réponse impulsionnelle calculée.
     * @throws RemezException
     *             Si l'algorithme ne converge pas vers une solution.
     */
    public double[] generateFilter () throws RemezException {
        return remez(this.nbOfCoeff, this.bands, this.des, this.weight, this.type);
    }

    /**
     * Permet de déterminer la réponse impulsionnelle optimale (au sens de
     * l'approximation de Chebyshev) d'un filtre RIF (Réponse Impulsionnelle Finie)
     * dont les caractéristiques sont données par l'utilisateur. Ces
     * caractéristiques sont le nombre de coefficients du filtre, les limites
     * fréquentielles de chaque bande, la réponse désirée sur chacune de ces bandes
     * ainsi que le poids attribué à l'erreur sur celles-ci.
     * 
     * @param nbOfCoeff
     *            Nombre de coefficients de la réponse impulsionnelle.
     * @param bands
     *            Tableau contenant les limites fréquentielles de chaque bande du
     *            filtre.
     * @param des
     *            Réponse désirée sur chaque bande de fréquences.
     * @param weight
     *            Poids attribué à l'erreur sur chaque bande du filtre.
     * @param type
     *            Type du filtre (BANDPASS, HILBERT ou DIFFERENTIATOR).
     * @return Tableau contenant les coefficients de la réponse impulsionnelle.
     * @throws RemezException
     *             Si l'algorithme ne converge pas vers une solution.
     */
    private static double[] remez (int nbOfCoeff, double bands[], double des[], double weight[], int type)
            throws RemezException {
        double[] Grid, W, D, E, h, taps, x, y, ad;
        int[] Ext;
        double c;
        int symmetry, gridsize, iter, r;

        if (type == BANDPASS) {
            symmetry = POSITIVE;
        } else {
            symmetry = NEGATIVE;
        }

        // Nombre d'extremas
        r = nbOfCoeff / 2;
        if ((nbOfCoeff % 2) > 0 && symmetry == POSITIVE) {
            r++;
        }

        // Permet de prédire la taille de la grille pour l'allocation
        gridsize = 0;
        for (int i = 0; i < (bands.length / 2); i++) {
            gridsize += (int) (2 * r * GRIDDENSITY * (bands[2 * i + 1] - bands[2 * i]) + 0.5);
        }
        if (symmetry == NEGATIVE) {
            gridsize--;
        }

        // Allocation en mémoire des tableaux utilisés
        Grid = new double[gridsize];
        D = new double[gridsize];
        W = new double[gridsize];
        E = new double[gridsize];
        Ext = new int[r + 1];
        taps = new double[r + 1];
        x = new double[r + 1];
        y = new double[r + 1];
        ad = new double[r + 1];
        h = new double[nbOfCoeff];

        // Création de la grille
        createDenseGrid(r, nbOfCoeff, bands, des, weight, Grid, D, W, symmetry);
        initialGuess(r, Ext, gridsize);

        // Pour le DIFFERENTIATOR la grille est fixe
        if (type == DIFFERENTIATOR) {
            for (int i = 0; i < gridsize; i++) {
                if (D[i] > 0.0001) {
                    W[i] = W[i] / Grid[i];
                }
            }
        }

        // Pour les symétries impaires ou négatives, modification de D[] et W[]
        // tel que précisé par Parks-McClellan
        if (symmetry == POSITIVE) {
            if (nbOfCoeff % 2 == 0) {
                for (int i = 0; i < gridsize; i++) {
                    c = Math.cos(Math.PI * Grid[i]);
                    D[i] /= c;
                    W[i] *= c;
                }
            }
        } else {
            if (nbOfCoeff % 2 > 0) {
                for (int i = 0; i < gridsize; i++) {
                    c = Math.sin(2 * Math.PI * Grid[i]);
                    D[i] /= c;
                    W[i] *= c;
                }
            } else {
                for (int i = 0; i < gridsize; i++) {
                    c = Math.sin(Math.PI * Grid[i]);
                    D[i] /= c;
                    W[i] *= c;
                }
            }
        }

        // Exécution de l'algorithme Remez Exchange
        for (iter = 0; iter < MAXITERATIONS; iter++) {
            calcParms(r, Ext, Grid, D, W, ad, x, y);
            calculateErrorFunction(r, ad, x, y, Grid, D, W, E);
            search(r, Ext, gridsize, E);
            if (isDone(r, Ext, E)) {
                break;
            }
        }
        if (iter == MAXITERATIONS) {
            // Echec de converge de la réponse
            throw new RemezException("Algorithm failed to converge.");
        }
        calcParms(r, Ext, Grid, D, W, ad, x, y);

        // Détermination des coefficients du filtre pour échantillonnage de
        // fréquence.
        // Pour les symétries impaires ou négatives, on fixe les coefficients
        // d'après Prks McClellan
        for (int i = 0; i <= nbOfCoeff / 2; i++) {
            if (symmetry == POSITIVE) {
                if ((nbOfCoeff % 2) > 0) {
                    c = 1;
                } else {
                    c = Math.cos(Math.PI * i / nbOfCoeff);
                }
            } else {
                if ((nbOfCoeff % 2) > 0) {
                    c = Math.sin(2 * Math.PI * i / nbOfCoeff);
                } else {
                    c = Math.sin(Math.PI * i / nbOfCoeff);
                }
            }
            taps[i] = computeA((double) i / nbOfCoeff, r, ad, x, y) * c;
        }

        // Echantillonnage de fréquence avec les coefficients calculés
        frequencySampling(nbOfCoeff, taps, h, symmetry);

        // Si c'est un DIFFERENTIATOR, le signe des coefficients doit être
        // inversé
        if (type == DIFFERENTIATOR) {
            for (int i = 0; i < nbOfCoeff; i++) {
                h[i] = -h[i];
            }
        }

        // On renvoie la réponse impulsionnelle calculée
        return h;
    }

    /**
     * Permet de créer la grille des fréquences à partir des bandes de fréquence
     * spécifiées, de créer le tableau de la réponse fréquentielle désirée D[] et la
     * fonction de pondération W[] sur cette grille.
     * 
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param nbOfCoeff
     *            Nombre de coefficients du filtre.
     * @param bands
     *            Bandes de fréquences spécifiées pour le filtre.
     * @param des
     *            Réponse désirée sur chaque bande de fréquences.
     * @param weight
     *            Poids attribué à l'erreur sur chaque bande du filtre.
     * @param Grid
     *            Grille des fréquences (de 0 à 0.5).
     * @param D
     *            Réponse désirée sur la grille.
     * @param W
     *            Fonction de pondération sur la grille.
     * @param symmetry
     *            Symétrie du filtre.
     */
    private static void createDenseGrid (int r, int nbOfCoeff, double bands[], double des[], double weight[],
            double Grid[], double D[], double W[], int symmetry) {
        double delf, lowf, highf;
        int j, k;

        delf = 0.5 / (GRIDDENSITY * r);

        // Pour le DIFFERENTIATOR et HILBERT, symétrie impaire et Grid[0] =
        // max(delf, band[0])
        if ((symmetry == NEGATIVE) && (delf > bands[0])) {
            bands[0] = delf;
        }

        j = 0;
        for (int band = 0; band < (bands.length / 2); band++) {
            Grid[j] = bands[2 * band];
            lowf = bands[2 * band];
            highf = bands[2 * band + 1];
            k = (int) ((highf - lowf) / delf + 0.5);
            for (int i = 0; i < k; i++) {
                D[j] = des[band];
                W[j] = weight[band];
                Grid[j] = lowf;
                lowf += delf;
                j++;
            }
            Grid[j - 1] = highf;
        }

        // De la même manière, si symétrie impaire, le dernier point ne peut pas
        // être 0.5
        // Le problème ne se pose pas pour la symétrie paire
        if ((symmetry == NEGATIVE) && (Grid[Grid.length - 1] > (0.5 - delf)) && (nbOfCoeff % 2) > 0) {
            Grid[Grid.length - 1] = 0.5 - delf;
        }
    }

    /**
     * Permet de déterminer une première approximation des extremums de fréquence
     * dans la grille.
     * 
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param Ext
     *            Tableau d'indiçage des fréquences extrêmes dans la grille.
     * @param gridsize
     *            Taille de la grille de fréquence.
     */
    private static void initialGuess (int r, int Ext[], int gridsize) {
        for (int i = 0; i <= r; i++)
            Ext[i] = i * (gridsize - 1) / r;
    }

    /**
     * CalcParms
     * 
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param Ext
     *            Tableau d'indiçage des fréquences extrêmes dans la grille.
     * @param Grid
     *            Grille des fréquences (de 0 à 0.5).
     * @param D
     *            Réponse désirée sur la grille.
     * @param W
     *            Fonction de pondération sur la grille.
     * @param ad
     *            'b' d'après Oppenheim & Schafer [r+1].
     * @param x
     *            [r+1].
     * @param y
     *            'C' d'après Oppenheim & Schafer [r+1].
     */
    private static void calcParms (int r, int Ext[], double Grid[], double D[], double W[], double ad[], double x[],
            double y[]) {
        double sign, xi, delta, denom, numer;
        int k, ld;

        for (int i = 0; i <= r; i++) {
            x[i] = Math.cos(2 * Math.PI * Grid[Ext[i]]);
        }

        // Calcul de ad[] - Oppenheim & Schafer eq 7.132
        ld = (r - 1) / 15 + 1; // Saut pour éviter les erreurs d'arrondi
        for (int i = 0; i <= r; i++) {
            denom = 1.0;
            xi = x[i];
            for (int j = 0; j < ld; j++) {
                for (k = j; k <= r; k += ld) {
                    if (k != i) {
                        denom *= 2.0 * (xi - x[k]);
                    }
                }
            }
            if (Math.abs(denom) < 0.00001) {
                denom = 0.00001;
            }
            ad[i] = 1.0 / denom;
        }

        // Calcul de delta - Oppenheim & Schafer eq 7.131
        numer = denom = 0;
        sign = 1;
        for (int i = 0; i <= r; i++) {
            numer += ad[i] * D[Ext[i]];
            denom += sign * ad[i] / W[Ext[i]];
            sign = -sign;
        }
        delta = numer / denom;
        sign = 1;

        // Calcul de y[] - Oppenheim & Schafer eq 7.133b
        for (int i = 0; i <= r; i++) {
            y[i] = D[Ext[i]] - sign * delta / W[Ext[i]];
            sign = -sign;
        }
    }

    /**
     * Utilisation des valeurs calculées par calcParms(). Permet de calculer la
     * véritable réponse fréquentielle à une fréquence donnée. Utilisation de eq
     * 7.133a d'après Oppenheim & Schafer.
     * 
     * @param freq
     *            Fréquence (de 0 à 0.5).
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param ad
     *            'b' d'après Oppenheim & Schafer [r+1].
     * @param x
     *            [r+1].
     * @param y
     *            'C' d'après Oppenheim & Schafer [r+1].
     * @return Valeur de A[freq].
     */
    private static double computeA (double freq, int r, double ad[], double x[], double y[]) {
        double xc, c, denom, numer;

        denom = numer = 0;
        xc = Math.cos(2 * Math.PI * freq);
        for (int i = 0; i <= r; i++) {
            c = xc - x[i];
            if (Math.abs(c) < 1.0e-7) {
                numer = y[i];
                denom = 1;
                break;
            }
            c = ad[i] / c;
            denom += c;
            numer += c * y[i];
        }
        return numer / denom;
    }

    /**
     * Permet de calculer la fonction d'erreur sur la grille à partir de la réponse
     * fréquentielle désirée (D[]), de la fonction de pondération (W[]), et de la
     * réponse actuelle (A[]). Le calcul de l'erreur est par définition E[exp(jw)] =
     * W[exp(jw)] * [ D[exp(jw)] - A[exp(jw)] ]
     * 
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param ad
     *            'b' d'après Oppenheim & Schafer [r+1].
     * @param x
     *            [r+1].
     * @param y
     *            'C' d'après Oppenheim & Schafer [r+1].
     * @param Grid
     *            Grille des fréquences (de 0 à 0.5).
     * @param D
     *            Réponse désirée sur la grille.
     * @param W
     *            Fonction de pondération sur la grille.
     * @param E
     *            Fonction d'erreur.
     */
    private static void calculateErrorFunction (int r, double ad[], double x[], double y[], double Grid[], double D[],
            double W[], double E[]) {
        for (int i = 0; i < Grid.length; i++) {
            double A = computeA(Grid[i], r, ad, x, y);
            E[i] = W[i] * (D[i] - A);
        }
    }

    /**
     * Permet de déterminer les extremums de la fonction d'erreur. Si plus de r+1
     * erreurs sont trouvées, on utilise l'heuristique suivante : 1) On supprime en
     * premier l'extrema adjacent de signe constant. 2) S'il y a plus d'un extrema
     * en trop, on supprime celui avec la plus petite erreur. Cela va créer une
     * alternance corrigée grâce à 1). 3) S'il y a exactement un extrema en trop, on
     * supprime le plus plus petit du premier/dernier extremum.
     * 
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param Ext
     *            Tableau d'indiçage des fréquences extrêmes dans la grille.
     * @param gridsize
     *            Taille de la grille.
     * @param E
     *            Fonction d'erreur.
     */
    private static void search (int r, int Ext[], int gridsize, double E[]) {
        int j, k, l, extra;
        int up, alt;
        int[] foundExt;

        // Allocation d'assez de mémoire pour trouver les extremums
        foundExt = new int[10 * r];
        k = 0;

        // Recherche d'un extrema en zéro
        if (((E[0] > 0.0) && (E[0] > E[1])) || ((E[0] < 0.0) && (E[0] < E[1]))) {
            foundExt[k++] = 0;
        }

        // Recherche d'extremas dans la grille
        for (int i = 1; i < gridsize - 1; i++) {
            if (((E[i] >= E[i - 1]) && (E[i] > E[i + 1]) && (E[i] > 0.0))
                    || ((E[i] <= E[i - 1]) && (E[i] < E[i + 1]) && (E[i] < 0.0))) {
                foundExt[k++] = i;
            }
        }

        // Recherche d'un extrema sur la dernière valeur
        j = gridsize - 1;
        if (((E[j] > 0.0) && (E[j] > E[j - 1])) || ((E[j] < 0.0) && (E[j] < E[j - 1]))) {
            foundExt[k++] = j;
        }

        // Suppression des extremas en trop
        extra = k - (r + 1);

        while (extra > 0) {
            // Si le premier est un maximum
            if (E[foundExt[0]] > 0.0) {
                up = 1;
            } else { // Si le premier est un minimum
                up = 0;
            }

            l = 0;
            alt = 1;
            for (j = 1; j < k; j++) {
                if (Math.abs(E[foundExt[j]]) < Math.abs(E[foundExt[l]])) {
                    l = j; // Nouvelle plus petite erreur
                }
                if ((up) > 0 && (E[foundExt[j]] < 0.0)) {
                    up = 0; // Change pour un minimum
                } else if (up <= 0 && (E[foundExt[j]] > 0.0)) {
                    up = 1; // Change pour un maximum
                } else {
                    // Deux valeurs non-alternantes trouvées
                    alt = 0;
                    break;
                }
            } // Si la boucle se termine, tous les extremums alternent

            // S'il n'y a qu'un seul extremum et qu'ils alternent tous
            if ((alt) > 0 && (extra == 1)) {
                // Alors on supprime le plus petit du premier/dernier extrema
                if (Math.abs(E[foundExt[k - 1]]) < Math.abs(E[foundExt[0]])) {
                    // Suppression du dernier extrema
                    l = foundExt[k - 1];
                } else {
                    // Suppression du premier extrema
                    l = foundExt[0];
                }
            }

            // Boucle de suppression
            for (j = l; j < k; j++) {
                foundExt[j] = foundExt[j + 1];
            }
            k--;
            extra--;
        }

        // Copie des extremums dans Ext[]
        for (int i = 0; i <= r; i++) {
            Ext[i] = foundExt[i];
        }

    }

    /**
     * Simple algorithme d'échantillonnage de fréquence pour déterminer la réponse
     * impulsionnelle h[] à partir de la réponse A[] calculée.
     * 
     * @param N
     *            Nombre de coefficients du filtre.
     * @param A
     *            Points d'échantillonnage de la réponse souhaitée [N/2].
     * @param h
     *            Réponse impulsionnelle finale du filtre.
     * @param symm
     *            Syméteie du filtre.
     */
    private static void frequencySampling (int N, double A[], double h[], int symm) {
        int n, k;
        double x, val, M;

        M = (N - 1.0) / 2.0;
        if (symm == POSITIVE) {
            if ((N % 2) > 0) {
                for (n = 0; n < N; n++) {
                    val = A[0];
                    x = 2 * Math.PI * (n - M) / N;
                    for (k = 1; k <= M; k++) {
                        val += 2.0 * A[k] * Math.cos(x * k);
                    }
                    h[n] = val / N;
                }
            } else {
                for (n = 0; n < N; n++) {
                    val = A[0];
                    x = 2 * Math.PI * (n - M) / N;
                    for (k = 1; k <= (N / 2 - 1); k++) {
                        val += 2.0 * A[k] * Math.cos(x * k);
                    }
                    h[n] = val / N;
                }
            }
        } else {
            if ((N % 2) > 0) {
                for (n = 0; n < N; n++) {
                    val = 0;
                    x = 2 * Math.PI * (n - M) / N;
                    for (k = 1; k <= M; k++) {
                        val += 2.0 * A[k] * Math.sin(x * k);
                    }
                    h[n] = val / N;
                }
            } else {
                for (n = 0; n < N; n++) {
                    val = A[N / 2] * Math.sin(Math.PI * (n - M));
                    x = 2 * Math.PI * (n - M) / N;
                    for (k = 1; k <= (N / 2 - 1); k++) {
                        val += 2.0 * A[k] * Math.sin(x * k);
                    }
                    h[n] = val / N;
                }
            }
        }
    }

    /**
     * Permet de vérifier si la fonction d'erreur est suffisamment petite pour
     * considérer que le résultat converge vers la réponse souhaitée.
     * 
     * @param r
     *            Nombre de coefficients du filtre réellement à calculer (moitié
     *            moins grâce à la parité).
     * @param Ext
     *            Tableau d'indiçage des fréquences extrêmes dans la grille.
     * @param E
     *            Fonction d'erreur.
     * @return true si le résultat a convergé, false sinon.
     */
    private static boolean isDone (int r, int Ext[], double E[]) {
        double min, max, current;

        min = max = Math.abs(E[Ext[0]]);
        for (int i = 1; i <= r; i++) {
            current = Math.abs(E[Ext[i]]);
            if (current < min) {
                min = current;
            }
            if (current > max) {
                max = current;
            }
        }
        if (((max - min) / max) < 0.0001) {
            return true;
        }
        return false;
    }
}
