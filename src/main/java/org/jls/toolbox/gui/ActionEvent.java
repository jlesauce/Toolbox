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

/**
 * Action triggered by an {@link AbstractView} that can be used by a subscriber
 * using an {@link ActionCallback}. This event extends the functionalities
 * provided by {@link java.awt.event.ActionEvent} and can be transmitted to the
 * view's subscribers by the {@link CommandListener}.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public class ActionEvent extends java.awt.event.ActionEvent {

    private static final long serialVersionUID = 9148498099084955285L;

    private final AbstractView view;

    /**
     * Constructs an <code>ActionEvent</code> object with modifier keys.
     * <p>
     * This method throws an <code>IllegalArgumentException</code> if
     * <code>source</code> is <code>null</code>. A <code>null</code>
     * <code>command</code> string is legal, but not recommended.
     * 
     * @param source
     *            The object that originated the event
     * @param id
     *            An integer that identifies the event. For information on allowable
     *            values, see the class description for {@link ActionEvent}
     * @param command
     *            A string that may specify a command (possibly one of several)
     *            associated with the event
     * @param modifiers
     *            The modifier keys down during event (shift, ctrl, alt, meta).
     *            Passing negative parameter is not recommended. Zero value means
     *            that no modifiers were passed
     * @throws IllegalArgumentException
     *             if <code>source</code> is null
     * @see #getSource()
     * @see #getID()
     * @see #getActionCommand()
     * @see #getModifiers()
     */
    public ActionEvent(Object source, int id, String command, int modifiers) {
        super(source, id, command, modifiers);
        this.view = null;
    }

    /**
     * Constructs an <code>ActionEvent</code> object with the specified modifier
     * keys and timestamp.
     * <p>
     * This method throws an <code>IllegalArgumentException</code> if
     * <code>source</code> is <code>null</code>. A <code>null</code>
     * <code>command</code> string is legal, but not recommended.
     * 
     * @param source
     *            The object that originated the event
     * @param id
     *            An integer that identifies the event. For information on allowable
     *            values, see the class description for {@link ActionEvent}
     * @param command
     *            A string that may specify a command (possibly one of several)
     *            associated with the event
     * @param modifiers
     *            The modifier keys down during event (shift, ctrl, alt, meta).
     *            Passing negative parameter is not recommended. Zero value means
     *            that no modifiers were passed
     * @param when
     *            A long that gives the time the event occurred. Passing negative or
     *            zero value is not recommended
     * @throws IllegalArgumentException
     *             if <code>source</code> is null
     * @see #getSource()
     * @see #getID()
     * @see #getActionCommand()
     * @see #getModifiers()
     * @see #getWhen()
     * 
     * @since 1.4
     */
    public ActionEvent(Object source, int id, String command, long when, int modifiers) {
        super(source, id, command, when, modifiers);
        this.view = null;
    }

    /**
     * Constructs an <code>ActionEvent</code> object.
     * <p>
     * This method throws an <code>IllegalArgumentException</code> if
     * <code>source</code> is <code>null</code>. A <code>null</code>
     * <code>command</code> string is legal, but not recommended.
     * 
     * @param source
     *            The object that originated the event
     * @param id
     *            An integer that identifies the event. For information on allowable
     *            values, see the class description for {@link ActionEvent}
     * @param command
     *            A string that may specify a command (possibly one of several)
     *            associated with the event
     * @throws IllegalArgumentException
     *             if <code>source</code> is null
     * @see #getSource()
     * @see #getID()
     * @see #getActionCommand()
     */
    public ActionEvent(Object source, int id, String command) {
        super(source, id, command);
        this.view = null;
    }

    /**
     * Instantiates an <code>ActionEvent</code> object from an
     * {@link java.awt.event.ActionEvent} and specifies the graphical interface that
     * triggered the event.
     * 
     * @param e
     *            The event triggered by the component.
     * @param view
     *            The graphical interface.
     */
    public ActionEvent(final java.awt.event.ActionEvent e, final AbstractView view) {
        super(e.getSource(), e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers());
        this.view = view;
    }

    /**
     * Returns the graphical interface that triggered the event.
     * 
     * @return Graphical interface that triggered the event.
     */
    public AbstractView getView () {
        return this.view;
    }
}
