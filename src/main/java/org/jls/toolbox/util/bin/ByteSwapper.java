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

package org.jls.toolbox.util.bin;

public class ByteSwapper {

    private ByteSwapper() {
        throw new AssertionError();
    }

    public static short swap (final short value) {
        int b1 = value & 0xFF;
        int b2 = value >> 8 & 0xFF;
        return (short) (b1 << 8 | b2 << 0);
    }

    public static int swap (final int value) {
        int b1 = value >> 0 & 0xff;
        int b2 = value >> 8 & 0xff;
        int b3 = value >> 16 & 0xff;
        int b4 = value >> 24 & 0xff;
        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    public static long swap (final long value) {
        long b1 = value >> 0 & 0xff;
        long b2 = value >> 8 & 0xff;
        long b3 = value >> 16 & 0xff;
        long b4 = value >> 24 & 0xff;
        long b5 = value >> 32 & 0xff;
        long b6 = value >> 40 & 0xff;
        long b7 = value >> 48 & 0xff;
        long b8 = value >> 56 & 0xff;
        return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 | b5 << 24 | b6 << 16 | b7 << 8 | b8 << 0;
    }

    public static float swap (final float value) {
        int intValue = Float.floatToIntBits(value);
        intValue = swap(intValue);
        return Float.intBitsToFloat(intValue);
    }

    public static double swap (final double value) {
        long longValue = Double.doubleToLongBits(value);
        longValue = swap(longValue);
        return Double.longBitsToDouble(longValue);
    }

    public static void swap (final short[] array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("Array can't be null or empty");
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = swap(array[i]);
        }
    }

    public static void swap (final int[] array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("Array can't be null or empty");
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = swap(array[i]);
        }
    }

    public static void swap (final long[] array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("Array can't be null or empty");
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = swap(array[i]);
        }
    }

    public static void swap (final float[] array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("Array can't be null or empty");
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = swap(array[i]);
        }
    }

    public static void swap (final double[] array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("Array can't be null or empty");
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = swap(array[i]);
        }
    }
}
