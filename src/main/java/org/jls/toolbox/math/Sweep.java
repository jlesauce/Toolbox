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
 * Permet de générer différents types de balayages de fréquence.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class Sweep {

    /**
     * Permet de représenter les différents types de balayage de fréquence.
     * 
     * @author LE SAUCE Julien
     * @date Feb 12, 2015
     */
    public enum SweepType {
        NONE, CW, LINEAR_UP, LINEAR_DOWN, HYPERBOLIC_UP, HYPERBOLIC_DOWN;
    }

    /**
     * Permet de générer une forme d'onde sinusoïdale, modulée en fréquence selon le
     * type de balayage spécifié, de fréquence centrale Fc, sur la bande B, de durée
     * T et échantillonnée à la fréquence Fs.
     * 
     * @param sw
     *            Type de balayage de fréquence.
     * @param fc
     *            Fréquence centrale de la modulation en hertz.
     * @param b
     *            Largeur de bande de la modulation en hertz.
     * @param fs
     *            Fréquence d'échantillonnage en hertz.
     * @param T
     *            Durée de la forme d'onde en millisecondes.
     * @return Tableau de taille N représentant les valeurs des fréquences
     *         instantanées de la forme d'onde avec N=T*Fs.
     */
    public static double[] computeFM (SweepType sw, double fc, double b, double fs, long T) {
        double[] wk = null;
        switch (sw) {
            case CW:
                wk = continuousWave(fc, T, fs);
                break;
            case LINEAR_UP:
                wk = linearChirp(fc, b, T, fs, true);
                break;
            case LINEAR_DOWN:
                wk = linearChirp(fc, b, T, fs, false);
                break;
            case HYPERBOLIC_UP:
                wk = hyperbolicChirp(fc, b, T, fs, true);
                break;
            case HYPERBOLIC_DOWN:
                wk = hyperbolicChirp(fc, b, T, fs, false);
                break;
            case NONE:
                throw new IllegalArgumentException("Invalid use of NONE case");
            default:
                throw new IllegalArgumentException("Incorrect modulation type");
        }
        return wk;
    }

    /**
     * Permet de générer un set de N valeurs de fréquences modulées selon le type de
     * modulation spécifié.
     * 
     * @param sw
     *            Type de balayage de fréquence.
     * @param fc
     *            Fréquence centrale en hertz.
     * @param b
     *            Largeur de bande en hertz.
     * @param T
     *            Durée de la forme d'onde en millisecondes.
     * @return Liste de N valeurs de fréquences modulées selon le type de modulation
     *         spécifié.
     */
    public static double[] computeFrequencySet (SweepType sw, double fc, double b, long T) {
        int N = (int) (fc * T / 1000);
        return computeFrequencySet(sw, fc, b, N);
    }

    /**
     * Permet de générer un set de N valeurs de fréquences modulées selon le type de
     * modulation spécifié.
     * 
     * @param sw
     *            Type de balayage de fréquence.
     * @param fc
     *            Fréquence centrale en hertz.
     * @param b
     *            Largeur de bande en hertz.
     * @param N
     *            Nombre de valeurs de fréquences générées
     * @return Liste de N valeurs de fréquences en hertz modulées selon le type de
     *         modulation spécifié.
     */
    public static double[] computeFrequencySet (SweepType sw, double fc, double b, int N) {
        double[] fk = null;
        switch (sw) {
            case CW:
                fk = continuousWaveSet(fc, N);
                break;
            case LINEAR_UP:
                fk = linearFrequencySet(fc, b, N, true);
                break;
            case LINEAR_DOWN:
                fk = linearFrequencySet(fc, b, N, false);
                break;
            case HYPERBOLIC_UP:
                fk = hyperbolicFrequencySet(fc, b, N, true);
                break;
            case HYPERBOLIC_DOWN:
                fk = hyperbolicFrequencySet(fc, b, N, false);
                break;
            case NONE:
                throw new IllegalArgumentException("Invalid use of NONE case");
            default:
                throw new IllegalArgumentException("Incorrect modulation type");
        }
        return fk;
    }

    /**
     * Permet de générer une forme d'onde sinusoïdale à fréquence constante Fc, de
     * durée T et échantillonnée à la fréquence Fs. Il est cependant possible de
     * spécifier des fréquences additionnelles, on vient alors calculer la somme des
     * sinusoïdes pour chacunes de ces fréquences.
     * 
     * 
     * @param f
     *            Fréquence de la forme d'onde en hertz.
     * @param T
     *            Durée de la forme d'onde en millisecondes.
     * @param fs
     *            Fréquence d'échantillonnage en hertz.
     * @param adds
     *            Fréquence(s) additionnelle(s) en hertz permettant d'ajouter des
     *            sinusoïdes à la forme d'onde tel que <i>w(n)=sin(2.PI.fc.t) +
     *            sin(2.PI.fa.t)</i> avec fc la fréquence de la première sinusoïde
     *            et fa la fréquence additionnelle.
     * @return Tableau de taille N représentant les échantillons de la forme d'onde
     *         avec N=T*Fs.
     */
    public static double[] continuousWave (double f, long T, double fs, double... adds) {
        double d = T / 1000.0;
        int N = (int) Math.round(d * fs);
        double[] wf = new double[N];
        for (int n = 0; n < N; n++) {
            double t = n / fs;
            wf[n] = Math.sin(2 * Math.PI * f * t);
            // Fréquences supplémentaires
            if (adds != null && adds.length > 0) {
                int i = 0;
                while (i < adds.length) {
                    wf[n] += Math.sin(2.0 * Math.PI * adds[i] * t);
                    i++;
                }
                wf[n] /= adds.length + 1;
            }
        }
        return wf;
    }

    /**
     * Permet de générer une forme d'onde sinusoïdale, modulée en fréquence selon un
     * type de balayage linéaire, de fréquence centrale Fc, de largeur de bande B,
     * de durée T et échantillonnée à la fréquence Fs.
     * 
     * @param fc
     *            Fréquence centrale de la forme d'onde en hertz.
     * @param b
     *            Largeur de bande en hertz.
     * @param T
     *            Durée de la forme d'onde en millisecondes.
     * @param fs
     *            Fréquence d'échantillonnage en hertz.
     * @param up
     *            <code>true</code> pour un balayage de fréquence ascendant,
     *            <code>false</code> pour un balayage de fréquence descendant.
     * @return Tableau de taille N représentant les échantillons de la forme d'onde
     *         avec N=T*Fs.
     */
    public static double[] linearChirp (double fc, double b, long T, double fs, boolean up) {
        if (fc - b / 2.0 < 0) {
            throw new IllegalArgumentException("Half band Fc-B/2 must be positive");
        }
        double d = T / 1000.0;
        int N = (int) Math.round(d * fs);
        double[] wf = new double[N];
        double f0 = fc - b / 2.0;
        double f1 = fc + b / 2.0;
        if (!up) {
            double tmp = f0;
            f0 = f1;
            f1 = tmp;
        }
        for (int i = 0; i < N; i++) {
            double delta = i / (double) N;
            double t = d * delta;
            double phase = 2 * Math.PI * t * (f0 + (f1 - f0) * delta / 2.0);
            while (phase > 2 * Math.PI) {
                phase -= 2 * Math.PI;
            }
            wf[i] = Math.sin(phase);
        }
        return wf;
    }

    /**
     * Permet de générer une forme d'onde sinusoïdale, modulée en fréquence selon un
     * type de balayage hyperbolique, de fréquence centrale Fc, de largeur de bande
     * B, de durée T et échantillonnée à la fréquence Fs.
     * 
     * @param fc
     *            Fréquence centrale de la forme d'onde en hertz.
     * @param b
     *            Largeur de bande en hertz.
     * @param T
     *            Durée de la forme d'onde en millisecondes.
     * @param fs
     *            Fréquence d'échantillonnage en hertz.
     * @param up
     *            <code>true</code> pour un balayage de fréquence ascendant,
     *            <code>false</code> pour un balayage de fréquence descendant.
     * @return Tableau de taille N représentant les échantillons de la forme d'onde
     *         avec N=T*Fs.
     */
    public static double[] hyperbolicChirp (double fc, double b, long T, double fs, boolean up) {
        double d = T / 1000.0;
        int N = (int) Math.round(d * fs);
        double[] wf = new double[N];
        double fStart, fStop, k;
        if (up) {
            fStart = fc - b / 2.0;
            fStop = fc + b / 2.0;
        } else {
            fStart = fc + b / 2.0;
            fStop = fc - b / 2.0;
        }
        k = (fStart - fStop) / (fStop * d);
        for (int n = 0; n < N; n++) {
            wf[n] = Math.sin(2 * Math.PI * fStart * Math.log(1 + k * n / fs) / k);
        }
        return wf;
    }

    /**
     * Permet de générer un set de N valeurs de fréquences modulées linéairement.
     * Les fréquences haute et basse sont déterminées à partir de la fréquence
     * centrale (fc) et de la bande passante (B) telles que fbasse = fc - B/2 et
     * fhaute = fc + B/2. Le paramètre N permet de déterminer le nombre de valeurs
     * de fréquences et le paramètre up permet de spécifier si la modulation de
     * fréquence se fait de manière croissante ou décroissante.
     * 
     * @param fc
     *            Fréquence centrale en hertz.
     * @param b
     *            Largeur de bande en hertz.
     * @param N
     *            Nombre de valeurs de fréquences générées.
     * @param up
     *            <code>true</code> pour une modulation croissante (de la fréquence
     *            la plus basse à la fréquence la plus haute), <code>false</code>
     *            pour une modulation décroissante.
     * @return Liste de N valeurs de fréquences modulées linéairement.
     */
    public static double[] linearFrequencySet (double fc, double b, int N, boolean up) {
        double[] lfmup = new double[N];
        double[] lfmdown = new double[N];
        for (int n = 0; n < N; n++) {
            lfmup[n] = fc + b * (2 * n - N + 1) / (2.0 * N);
            lfmdown[n] = fc + b * (N - 1 - 2 * n) / (2.0 * N);
        }
        return up ? lfmup : lfmdown;
    }

    /**
     * Permet de générer un set de N valeurs de fréquences modulées de manière
     * hyperbolique. Les fréquences haute et basse sont déterminées à partir de la
     * fréquence centrale (fc) et de la bande passante (B) telles que fbasse = fc -
     * B/2 et fhaute = fc + B/2. Le paramètre N permet de déterminer le nombre de
     * valeurs de fréquences et le paramètre up permet de spécifier si la modulation
     * de fréquence se fait de manière croissante ou décroissante.
     * 
     * @param fc
     *            Fréquence centrale en hertz.
     * @param b
     *            Largeur de bande en hertz.
     * @param N
     *            Nombre de valeurs de fréquences générées.
     * @param up
     *            <code>true</code> pour une modulation croissante (de la fréquence
     *            la plus basse à la fréquence la plus haute), <code>false</code>
     *            pour une modulation décroissante.
     * @return Liste de N valeurs de fréquences modulées de manière hyperbolique.
     */
    public static double[] hyperbolicFrequencySet (double fc, double b, int N, boolean up) {
        double[] hfmup = new double[N];
        double[] hfmdown = new double[N];
        double fdwn = fc - b / 2.0;
        double fup = fc + b / 2.0;
        for (int n = 0; n < N; n++) {
            hfmup[n] = fdwn * fup * N / ((N - n - 0.5) * fup + fdwn * (n + 0.5));
            hfmdown[n] = fdwn * fup * N / ((N - n - 0.5) * fdwn + fup * (n + 0.5));
        }
        return up ? hfmup : hfmdown;
    }

    /**
     * Permet de générer un set de N valeurs de fréquences identiques.
     * 
     * @param fc
     *            Valeur de fréquence en hertz.
     * @param N
     *            Nombre de valeurs de fréquences générées.
     * @return Liste de N valeurs de fréquences identiques.
     */
    public static double[] continuousWaveSet (double fc, int N) {
        double[] fk = new double[N];
        for (int n = 0; n < N; n++) {
            fk[n] = fc;
        }
        return fk;
    }
}
