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

public class FFT {

    private final double[] cos;
    private final double[] sin;
    private final int N;
    private final int M;

    public FFT(final int n) {
        this.N = n;
        this.M = (int) (Math.log(n) / Math.log(2));
        // Is pow of 2
        if (n != 1 << this.M) {
            throw new IllegalArgumentException("FFT length must be power of 2");
        }
        // Precalculate sinus/cosinus tables
        this.cos = new double[n / 2];
        this.sin = new double[n / 2];
        for (int i = 0; i < n / 2; i++) {
            this.cos[i] = Math.cos(-2 * Math.PI * i / n);
            this.sin[i] = Math.sin(-2 * Math.PI * i / n);
        }
    }

    /**
     * In-place inversion of the FFT result following a central horizontal symetry.
     *
     * @param real
     *            N-sized array representing the real part of FFT.
     * @param img
     *            N-sized array representing the imaginary part of FFT.
     */
    public static void shiftFFT (final float[] real, final float[] img) {
        if (real == null || img == null) {
            throw new NullPointerException("Buffers can't be null");
        }
        if (real.length != img.length) {
            throw new IllegalArgumentException("Different buffer size");
        }
        int size = real.length;
        if (size % 2 != 0) {
            throw new IllegalArgumentException("Buffer size must be even");
        }
        for (int i = 0; i < size / 2; i++) {
            float tempReal = real[i];
            float tempImg = img[i];
            real[i] = real[i + size / 2];
            img[i] = img[i + size / 2];
            real[i + size / 2] = tempReal;
            img[i + size / 2] = tempImg;
        }
    }

    /**
     * Calculate the in-place FFT radix-2.
     *
     * @param real
     *            N-sized array representing the real part of FFT.
     * @param img
     *            N-sized array representing the imaginary part of FFT.
     */
    public void fft (final float[] real, final float[] img) {
        int i, j, k, n1, n2, a;
        double c, s, t1, t2;
        // Bit-reverse
        j = 0;
        n2 = this.N / 2;
        for (i = 1; i < this.N - 1; i++) {
            n1 = n2;
            while (j >= n1) {
                j = j - n1;
                n1 = n1 / 2;
            }
            j = j + n1;

            if (i < j) {
                t1 = real[i];
                real[i] = real[j];
                real[j] = (float) t1;
                t1 = img[i];
                img[i] = img[j];
                img[j] = (float) t1;
            }
        }
        // FFT
        n1 = 0;
        n2 = 1;
        for (i = 0; i < this.M; i++) {
            n1 = n2;
            n2 = n2 + n2;
            a = 0;

            for (j = 0; j < n1; j++) {
                c = this.cos[a];
                s = this.sin[a];
                a += 1 << this.M - i - 1;

                for (k = j; k < this.N; k = k + n2) {
                    t1 = c * real[k + n1] - s * img[k + n1];
                    t2 = s * real[k + n1] + c * img[k + n1];
                    real[k + n1] = (float) (real[k] - t1);
                    img[k + n1] = (float) (img[k] - t2);
                    real[k] = (float) (real[k] + t1);
                    img[k] = (float) (img[k] + t2);
                }
            }
        }
    }
}
