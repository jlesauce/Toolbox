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

package org.jls.toolbox.gui;

import java.util.Observable;

/**
 * Represents a data model in the Model-View-Controller pattern.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public abstract class AbstractModel extends Observable {

    /**
     * Instanciates a new data model.
     */
    public AbstractModel() {
        super();
    }

    /**
     * Notifies the observers of this model that a modication has been made.
     * 
     * @param arg
     *            It's possible to specify an object to the observer.
     */
    public final void notifyChanged (Object... arg) {
        setChanged();
        if (arg.length == 0) {
            notifyObservers();
        } else if (arg.length == 1 && arg[0] != null) {
            notifyObservers(arg[0]);
        } else {
            throw new IllegalArgumentException("Only one argument allowed");
        }
        clearChanged();
    }
}
