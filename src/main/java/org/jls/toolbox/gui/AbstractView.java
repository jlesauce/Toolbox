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
import java.util.Collection;
import java.util.HashSet;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;

/**
 * Represents a view in the Model-View-Controller pattern.
 *
 * @author Julien LE SAUCE
 */
public abstract class AbstractView extends JPanel implements Observer {

    private static final long serialVersionUID = 2623093714218405490L;

    private final HashSet<Object> listeners;
    private boolean isGuiCreated;

    public AbstractView() {
        super();
        this.listeners = new HashSet<>();
        this.isGuiCreated = false;
    }

    /**
     * Create the GUI components and specify parent view.
     *
     * @param parent
     *            Specifies the parent view. By specifying the view's parent, one
     *            can ensures that the events triggered by sub-panels are
     *            transmitted to the parent's subscribers throw the parent's view.
     *            <code>null</code> can be specified to indicates that the view is
     *            the top component.
     * @return Return this view.
     */
    public AbstractView createGui (final AbstractView parent) {
        if (parent != null) {
            // Adds the parent's listeners to this view
            addAllListeners(parent.getListeners());
        }
        createGuiComponents();
        addGuiComponents();
        addGuiListeners();
        this.isGuiCreated = true;
        return this;
    }

    /**
     * Add a new listener to the events triggered by this view.
     *
     * @param o
     *            The listener.
     */
    public void addListener (final Object o) {
        this.listeners.add(o);
        LogManager.getLogger().trace("Object subscribed to {} : {}", getClass().getSimpleName(), o.toString());
    }

    /**
     * Add a new list of listeners to the events triggered by this view.
     *
     * @param listeners
     *            The listeners.
     */
    public void addAllListeners (final Collection<? extends Object> listeners) {
        this.listeners.addAll(listeners);
        for (Object o : listeners) {
            LogManager.getLogger().trace("Object subscribed to {} : {}", getClass().getSimpleName(), o.toString());
        }
    }

    /**
     * Trigger an action event that will invoke, on all the listeners, the callback
     * specified in the event.
     *
     * @param event
     *            The action event.
     */
    public void invokeCallback (final ActionEvent event) {
        for (Object object : this.listeners) {
            Invoker.invoke(object, event, this);
        }
    }

    /**
     * Create the graphical components composing this view and initializes the
     * default values.
     */
    protected abstract void createGuiComponents ();

    /**
     * This method, once the components have been created calling
     * {@link AbstractView#createGuiComponents()}, adds the graphical components to the
     * view's panel.
     */
    protected abstract void addGuiComponents ();

    /**
     * Add the listeners to the events triggered by the graphical components.
     */
    protected abstract void addGuiListeners ();

    /**
     * Return the listeners of this view.
     *
     * @return Listeners of this view.
     */
    public HashSet<Object> getListeners () {
        return this.listeners;
    }

    /**
     * Pop a small user dialog showing the specified message.
     *
     * @param title
     *            Dialog's title.
     * @param msg
     *            The message.
     * @param option
     *            Specifies the dialog option (see
     *            {@link JOptionPane#setOptionType(int)}).
     * @see JOptionPane
     */
    public void pop (final String title, final String msg, final int option) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run () {
                JOptionPane.showMessageDialog(AbstractView.this, msg, title, option);
            }
        });
    }

    /**
     * Tell whereas this view has been created/initialized or not.
     *
     * @return <code>true</code> if this view has been instanciated,
     *         <code>false</code> otherwise.
     */
    public boolean isGuiCreated () {
        return this.isGuiCreated;
    }
}
