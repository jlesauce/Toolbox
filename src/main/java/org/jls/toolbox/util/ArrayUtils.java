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

package org.jls.toolbox.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Permet de centraliser des fonctions utiles permettant d'effectuer des
 * opérations sur des tableaux.
 * 
 * @author LE SAUCE Julien
 * @date Mar 5, 2015
 */
public class ArrayUtils {

    /**
     * Classe non instanciable.
     */
    private ArrayUtils() {
        throw new AssertionError();
    }

    /**
     * Converts a list into an array.
     * 
     * @param <T>
     *            Type of the objects contained in the list.
     * @param list
     *            List of objects to be cast into an array.
     * @param clazz
     *            Class of the objects contained in the list.
     * @return Array containing all the elements of the list.
     */
    public static <T> T[] toArray (final List<?> list, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, list.size());
        array = list.toArray(array);
        return array;
    }

    /**
     * Permet de convertir une liste de {@link Long} en un tableau de son type
     * primitif <code>long</code>.
     * 
     * @param list
     *            Liste de {@link Long}.
     * @return Table de <code>long</code>.
     */
    public static long[] toLongArray (final List<Long> list) {
        long[] array = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Permet de convertir un tableau de type <i>double</i> en un tableau de type
     * <i>float</i>.
     * 
     * @param array
     *            Tableau à convertir.
     * @return Tableau de type <i>float</i>.
     */
    public static float[][] toFloatArray (double[][] array) {
        float[][] floatArray = new float[array.length][];
        for (int i = 0; i < floatArray.length; i++) {
            floatArray[i] = toFloatArray(array[i]);
        }
        return floatArray;
    }

    /**
     * Permet de convertir un tableau de type <i>double</i> en un tableau de type
     * <i>float</i>.
     * 
     * @param array
     *            Tableau à convertir.
     * @return Tableau de type <i>float</i>.
     */
    public static float[] toFloatArray (double[] array) {
        float[] floatArray = new float[array.length];
        for (int i = 0; i < floatArray.length; i++) {
            floatArray[i] = (float) array[i];
        }
        return floatArray;
    }

    /**
     * Permet de trier une collection de {@link String}.
     * 
     * @param strs
     *            Collection de {@link String} à trier.
     * @return Résultat renvoyé sous la forme d'un tableau de {@link String}.
     */
    public static String[] sort (Collection<String> strs) {
        String[] array = new String[strs.size()];
        strs.toArray(array);
        Arrays.sort(array);
        return array;
    }
}
