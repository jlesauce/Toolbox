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
 * Permet de représenter un nombre complexe et ses diverses fonctions génériques
 * associées.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public final class Complex {

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex I = new Complex(0, 1);

    private final double re;
    private final double im;

    /**
     * Permet d'instancier un nombre complexe à partir de sa partie réelle et
     * imaginaire.
     * 
     * @param re
     *            Partie réelle.
     * @param im
     *            Partie imaginaire.
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Permet d'instancier un nombre complexe à partir de sa partie réelle et
     * imaginaire.
     * 
     * @param re
     *            Partie réelle.
     * @param im
     *            Partie imaginaire.
     * @return Nouvelle instance de {@link Complex}.
     */
    public static Complex valueOf (double re, double im) {
        return new Complex(re, im);
    }

    /**
     * Permet d'instancier un nombre complexe représentant les coordonnées
     * cartésiennes d'un point à partir de ses coordonnées polaires.
     * 
     * @param r
     *            Distance au point.
     * @param theta
     *            Angle theta en radians.
     * @return Nombre complexe représentant les coordonnées cartésiennes du point
     *         spécifié.
     */
    public static Complex valueOfPolar (double r, double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Permet de parser une expression complexe de la forme "a + ib".
     * 
     * @param s
     *            Chaîne de texte écrite en notation complexe.
     * @return Nombre complexe extraie.
     */
    public static Complex toComplex (final String s) {
        String c_str = s.replaceAll(" ", "");
        String[] args;
        double re = 0.0;
        double im = 0.0;
        if (c_str.contains("-")) {
            args = c_str.split("-");
            re = Double.parseDouble(args[0]);
            im = -Double.parseDouble(args[1].replaceAll("i", ""));
        } else if (c_str.contains("+")) {
            args = c_str.split("+");
            re = Double.parseDouble(args[0]);
            im = Double.parseDouble(args[1].replaceAll("i", ""));
        } else {
            if (c_str.contains("i")) {
                re = 0.0;
                im = Double.parseDouble(c_str.replaceAll("i", ""));
            } else {
                re = Double.parseDouble(c_str);
                im = 0.0;
            }
        }
        return new Complex(re, im);
    }

    /**
     * Permet de calculer l'addition de ces deux nombres complexes tel que
     * a+b=(a1+b1)+(a2+b2)i avec a=a1+i*a2 et b=b1+i*b2.
     * 
     * @param b
     *            Nombre complexe tel que b=b1+i*b2.
     * @return Résultat de l'addition de ces deux nombres complexes.
     */
    public Complex add (final Complex b) {
        return new Complex(this.re + b.re, this.im + b.im);
    }

    /**
     * Permet de calculer la soustraction de ces deux nombres complexes tel que
     * a-b=(a1-b1)+(a2-b2)i avec a=a1+i*a2 et b=b1+i*b2.
     * 
     * @param b
     *            Nombre complexe tel que b=b1+i*b2.
     * @return Résultat de la soustraction de ces deux complexes.
     */
    public Complex sub (final Complex b) {
        return new Complex(this.re - b.re, this.im - b.im);
    }

    /**
     * Permet de multiplier ces deux nombres complexes tel que
     * a*b=(a1+i*a2)*(b1+i*b2).
     * 
     * @param b
     *            Nombre complexe tel que b=b1+i*b2.
     * @return Résultat de la multiplication de ces deux complexes.
     */
    public Complex times (final Complex b) {
        return new Complex(this.re * b.re - this.im * b.im, this.re * b.im + this.im * b.re);
    }

    /**
     * Permet de multiplier ce complexe par le facteur spécifié tel que
     * a*alpha=alpha*(a1+a2*i).
     * 
     * @param alpha
     *            Facteur multiplicatif.
     * @return Résultat de la multiplication de ce complexe par le facteur spécifié.
     */
    public Complex times (double alpha) {
        return new Complex(alpha * this.re, alpha * this.im);
    }

    /**
     * Permet de diviser ces deux nombres complexes tel que a/b=a*(1/b) avec
     * a=a1+i*a2 et b=b1+i*b2.
     * 
     * @param b
     *            Nombre complexe tel que b=b1+i*b2.
     * @return Résultat du calcul de la division de ces deux nombres complexes.
     */
    public Complex divides (Complex b) {
        return times(b.reciprocal());
    }

    /**
     * Permet de calculer le module (valeur absolue / magnitude) de ce nombre
     * complexe tel que module=Math.sqrt(re*re + im*im).
     * 
     * @return Résultat du calcul du module de ce nombre complexe.
     */
    public double abs () {
        return Math.hypot(this.re, this.im);
    }

    /**
     * Permet de calculer la phase (angle / argument) de ce nombre complexe tel que
     * phase=arctan(im/re) sur [-Pi;Pi].
     * 
     * @return Résultat du calcul de la phase de ce nombre complexe.
     */
    public double phase () {
        return Math.atan(this.im / this.re);
    }

    /**
     * Permet de calculer le conjugué de ce nombre complexe tel que
     * conjugate(a+ib)=a-ib.
     * 
     * @return Résultat du conjugué de ce nombre complexe.
     */
    public Complex conjugate () {
        return new Complex(this.re, -this.im);
    }

    /**
     * Permet de calculer l'inverse de ce nombre complexe tel que
     * 1/z=a/(a*a+b*b)-b/(a*a+b*b)i avec z=a+ib.
     * 
     * @return Résultat du calcul de l'inverse de ce nombre complexe.
     */
    public Complex reciprocal () {
        double denominator = this.re * this.re + this.im * this.im;
        return new Complex(this.re / denominator, -this.im / denominator);
    }

    /**
     * Permet de calculer l'exponentielle de ce nombre complexe telle que
     * exp(z)=exp(a+ib)=exp(a)[cos(b)+sin(b)*i] avec z=a+ib.
     * 
     * @return Résulat du calcul de l'exponentielle complexe de ce nombre.
     */
    public Complex exp () {
        return new Complex(Math.exp(this.re) * Math.cos(this.im), Math.exp(this.re) * Math.sin(this.im));
    }

    /**
     * Permet de calculer le sinus de ce nombre complexe tel que
     * sin(z)=sin(a+ib)=sin(a)cosh(b)+cos(a)sinh(b)*i avec z=a+ib.
     * 
     * @return Résultat du calcul du sinus complexe de ce nombre.
     */
    public Complex sin () {
        return new Complex(Math.sin(this.re) * Math.cosh(this.im), Math.cos(this.re) * Math.sinh(this.im));
    }

    /**
     * Permet de calculer le cosinus de ce nombre complexe tel que
     * cos(z)=cos(a+ib)=cos(a)cosh(b)-sin(a)sinh(b)*i avec z=a+ib.
     * 
     * @return Résultat du calcul du sinus complexe de ce nombre.
     */
    public Complex cos () {
        return new Complex(Math.cos(this.re) * Math.cosh(this.im), -Math.sin(this.re) * Math.sinh(this.im));
    }

    /**
     * Permet de calculer la tangeante de ce nombre complexe tel que
     * tan(z)=sin(z)/cos(z) avec z=a+ib.
     * 
     * @return Résultat du calcul de la tangeante complexe de ce nombre.
     */
    public Complex tan () {
        return sin().divides(cos());
    }

    @Override
    public String toString () {
        double real = MathUtils.round(this.re, 3);
        double img = MathUtils.round(this.im, 3);
        if (img == 0)
            return real + "";
        if (real == 0)
            return img + "i";
        if (img < 0)
            return real + " - " + (-img) + "i";
        return real + " + " + img + "i";
    }

    /**
     * Renvoie la partie réelle de ce nombre complexe.
     * 
     * @return Partie réelle de ce nombre complexe.
     */
    public double getReal () {
        return this.re;
    }

    /**
     * Renvoie la partie imaginaire de ce nombre complexe.
     * 
     * @return Partie imaginaire de ce nombre complexe.
     */
    public double getImaginary () {
        return this.im;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Complex)) {
            return false;
        }
        Complex c = (Complex) obj;
        return Double.compare(this.re, c.re) == 0 && Double.compare(this.im, c.im) == 0;
    }

    @Override
    public int hashCode () {
        int result = 17 + hashDouble(this.re);
        result = 31 * result + hashDouble(this.im);
        return result;
    }

    private static int hashDouble (double v) {
        long longBits = Double.doubleToLongBits(v);
        return (int) (longBits ^ (longBits >>> 32));
    }
}
