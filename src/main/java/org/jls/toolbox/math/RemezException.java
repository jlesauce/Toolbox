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
 * Exception pouvant être lancée par le générateur de filtres digitaux Remez si
 * une erreur survient lors de la génération.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class RemezException extends Exception {

    private static final long serialVersionUID = 4252292711161817573L;

    /**
     * Permet d'instancier une nouvelle exception sans message de détails. La cause
     * n'est pas initialisée, et doit en conséquence être initialisée par un appel à
     * {@link Exception#initCause}.
     */
    public RemezException() {
        super();
    }

    /**
     * Permet d'instancier une nouvelle exception avec le message détaillé spécifié
     * et la cause.
     * 
     * @param message
     *            Message de détail de l'exception.
     * @param cause
     *            Cause de l'exception.
     */
    public RemezException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Permet d'instancier une nouvelle exception avec le message détaillé spécifié.
     * La cause n'est pas initialisée, et doit en conséquence être initialisée par
     * un appel à {@link Exception#initCause}.
     * 
     * @param message
     *            Message de détail de l'exception.
     */
    public RemezException(String message) {
        super(message);
    }

    /**
     * Permet d'instancier une nouvelle exception avec la cause et le message
     * détaillé tel que <tt>(cause==null ? null : cause.toString())</tt> (contient
     * typiquement la classe et le message détaillé de la <tt>cause</tt>).
     * 
     * @param cause
     *            Cause de l'exception.
     */
    public RemezException(Throwable cause) {
        super(cause);
    }
}
