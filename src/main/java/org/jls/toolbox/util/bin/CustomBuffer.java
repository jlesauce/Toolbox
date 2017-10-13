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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Buffer allowing to iterate over a byte array and giving an easy way to decode
 * this array without specifying offsets and lengths.
 * 
 * @author LE SAUCE Julien
 * @date Aug 19, 2015
 */
public class CustomBuffer {

    private final ByteBuffer buffer;

    /**
     * Instanciates a {@link CustomBuffer} from the specified byte array.
     * 
     * @param array
     *            The array that will back the new buffer.
     */
    public CustomBuffer(final byte[] array) {
        this.buffer = ByteBuffer.wrap(array);
    }

    /**
     * Sets this buffer's position. If the mark is defined and larger than the new
     * position then it is discarded.
     * 
     * @param newPosition
     *            The new position value. Must be non-negative and no larger than
     *            the current limit
     * 
     * @return This buffer
     * 
     * @throws IllegalArgumentException
     *             If the preconditions on <tt>newPosition</tt> do not hold.
     */
    public CustomBuffer setPosition (final int newPosition) {
        this.buffer.position(newPosition);
        return this;
    }

    /**
     * Resets this buffer's position to the beginning of the backed array.
     * 
     * @return This buffer
     */
    public CustomBuffer reset () {
        this.buffer.position(0);
        return this;
    }

    /**
     * Returns the specified number of characters from the buffer and increment the
     * offset position by the specified size. A character is considered to be a
     * single byte.
     * <ul>
     * <li>If size &lt; 0 : An {@link IllegalArgumentException} is thrown,</li>
     * <li>If size = 0 : An empty string is returned.</li>
     * </ul>
     * 
     * @param size
     *            Number of characters to read. If size is negative an
     *            {@link IllegalArgumentException} is thrown.
     * @return String containing the specified number of characters from the buffer.
     */
    public String nextChars (final int size) {
        return new String(nextBytes(size));
    }

    /**
     * Returns the specified number of bytes from the buffer and increments the
     * offset position by the specified size.
     * <ul>
     * <li>If size &lt; 0 : An {@link IllegalArgumentException} is thrown,</li>
     * <li>If size = 0 : An empty byte buffer is returned.</li>
     * </ul>
     * 
     * @param size
     *            Number of bytes to read.
     * @return Byte array containing the specified number of bytes from the buffer.
     */
    public byte[] nextBytes (final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be a positive integer");
        }
        if (size == 0) {
            return new byte[0];
        }
        // Gets the specified number of bytes
        byte[] bytes = new byte[size];
        this.buffer.get(bytes);
        return bytes;
    }

    /**
     * Relative <i>get</i> method for reading a short value.
     *
     * <p>
     * Reads the next two bytes at this buffer's current position, composing them
     * into a short value according to the current byte order, and then increments
     * the position by two.
     * </p>
     *
     * @return The short value at the buffer's current position
     *
     * @throws BufferUnderflowException
     *             If there are fewer than two bytes remaining in this buffer
     */
    public short getShort () {
        return buffer.getShort();
    }

    /**
     * Returns the byte array that backs this buffer.
     * 
     * <p>
     * Modifications to this buffer's content will cause the returned array's
     * content to be modified, and vice versa.
     * </p>
     * 
     * @return The array that backs this buffer.
     */
    public byte[] getArray () {
        return this.buffer.array();
    }

    /**
     * Test.
     * 
     * @param args
     *            No arguments.
     */
    public static void main (final String[] args) {
        String str = "hereisjohnny!";
        byte[] array = str.getBytes();
        CustomBuffer buffer = new CustomBuffer(array);
        System.out.println(buffer.nextChars(4));
        buffer.setPosition(0);
        System.out.println(buffer.nextChars(2));
        buffer.setPosition(4);
        System.out.println(buffer.nextChars(4));
        buffer.reset();
        System.out.println(buffer.nextChars(4));
    }
}
