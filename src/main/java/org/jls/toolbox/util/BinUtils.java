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

package org.jls.toolbox.util;

import java.nio.ByteBuffer;

/**
 * Permet de centraliser des fonctions utiles permettant d'effectuer des
 * opérations sur des nombres binaires.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class BinUtils {

    private static ByteBuffer byteBuffer = ByteBuffer.allocate(Long.SIZE);

    /**
     * Classe non instanciable.
     */
    private BinUtils() {
        throw new AssertionError();
    }

    /**
     * Permet de quantifier une valeur flottante inférieure à 1.0 sur le nombre de
     * bits spécifié. Si la valeur est non signée, soit l'ensemble [0.0;1.0], cela
     * revient à multiplier la valeur par 2^n avec n le nombre de bits. Si en
     * revanche la valeur est négative, soit l'ensemble [-1.0;1.0], on doit diviser
     * la dynamique par deux, on multiplie donc la valeur par 2^n-1.
     * 
     * @param value
     *            Valeur à quantifier.
     * @param nbBits
     *            Nombre de bits utilisés pour représenter la valeur.
     * @param isSigned
     *            Permet de spécifier si la valeur est signée ou non. En effet si la
     *            valeur est signée alors la dynamique est divisée par deux.
     *            <code>true</code> pour une valeur signée, <code>false</code> pour
     *            une valeur non signée.
     * @return Valeur quantifiée sur le nombre de bits spécifié.
     */
    public static long quantize (double value, int nbBits, boolean isSigned) {
        if (isSigned) {
            double q = Math.pow(2, nbBits - 1);
            double lup = q - 1;
            double ldwn = -q;
            long v = Math.round(value * q);
            v = v > lup ? (long) lup : v;
            v = v < ldwn ? (long) ldwn : v;
            return v;
        }
        double q = Math.pow(2, nbBits);
        double lup = q - 1;
        long v = Math.round(value * q);
        v = v > lup ? (long) lup : v;
        return v;
    }

    /**
     * Permet de quantifier un tableau de valeurs flottantes inférieures à 1.0 sur
     * le nombre de bits spécifié. Si les valeurs sont non signées, soit l'ensemble
     * [0.0;1.0], cela revient à multiplier chacune des valeurs par 2^n avec n le
     * nombre de bits. Si en revanche les valeurs sont négatives, soit l'ensemble
     * [-1.0;1.0], on doit diviser la dynamique par deux, on multiplie donc chacune
     * des valeurs par 2^n-1.
     * 
     * @param tab
     *            Tableau de valeurs de taille quelconque à quantifier.
     * @param nbBits
     *            Nombre de bits utilisés pour représenter les valeurs.
     * @param isSigned
     *            Permet de spécifier si les valeurs sont signées ou non. En effet
     *            si elles sont signées alors la dynamique est divisée par deux.
     *            <code>true</code> pour des valeurs signées, <code>false</code>
     *            pour des valeurs non signées.
     * @return Renvoie la liste des valeurs quantifiées.
     */
    public static long[] quantize (double[] tab, int nbBits, boolean isSigned) {
        long[] result = new long[tab.length];
        for (int i = 0; i < tab.length; i++) {
            result[i] = quantize(tab[i], nbBits, isSigned);
        }
        return result;
    }

    /**
     * Permet de convertir une valeur de type long en un tableau d'octets.
     * 
     * @param x
     *            Valeur de type long.
     * @return Tableau d'octets de taille <i>Long.SIZE</i> représentant la valeur
     *         spécifiée.
     */
    public static byte[] longToBytes (long x) {
        BinUtils.byteBuffer.putLong(0, x);
        return BinUtils.byteBuffer.array();
    }

    /**
     * Permet de convertir un tableau d'octets en une valeur de type long.
     * 
     * @param bytes
     *            Tableau d'octets à convertir.
     * @return Valeur de type long convertie.
     */
    public static long bytesToLong (final byte[] bytes) {
        BinUtils.byteBuffer.put(bytes, 0, bytes.length);
        BinUtils.byteBuffer.flip();
        return BinUtils.byteBuffer.getLong();
    }
}
