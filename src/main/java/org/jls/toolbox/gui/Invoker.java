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

import java.awt.event.ActionEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to invoke code on a controller from a view.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public class Invoker {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Non-instanciable class.
     */
    private Invoker() {
        super();
    }

    /**
     * Invokes a method from the specified object. The called method must be annoted
     * using the syntax :
     * <p>
     * <code>@ActionCallback ("theCallbackID")</code>
     * </p>
     * 
     * @param object
     *            The object containing the method to call.
     * @param event
     *            The event triggered by the {@link AbstractView} object.
     * @param caller
     *            The object which triggered the event.
     */
    public static void invoke (final Object object, final ActionEvent event, final AbstractView caller) {
        for (Method m : object.getClass().getMethods()) {
            for (Annotation a : m.getAnnotations()) {
                if (a instanceof ActionCallback) {
                    ActionCallback callback = (ActionCallback) a;
                    if (callback.value().equals(event.getActionCommand())) {
                        try {
                            // Calls the method
                            org.jls.toolbox.gui.ActionEvent action = new org.jls.toolbox.gui.ActionEvent(event, caller);
                            m.invoke(object, action);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                            LOGGER.error("Action callback error : {}.{}#{}", object.getClass().getName(), m.getName(),
                                    callback.value(), e1.getCause());
                        }
                    }
                }
            }
        }
    }
}
