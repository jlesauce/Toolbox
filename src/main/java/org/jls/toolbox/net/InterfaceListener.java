/*#
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
 #*/

package org.jls.toolbox.net;

/**
 * Listener to the notifications of a network interface.
 * 
 * @see Client
 * @see Server
 * @author LE SAUCE Julien
 * @date Sep 15, 2015
 */
public interface InterfaceListener {

	/**
	 * This method is called when a message is received from the network
	 * interface.
	 * 
	 * @param event
	 *            {@link InterfaceEvent} associated with this event.
	 */
	public void onReceive (final InterfaceEvent event);

	/**
	 * This method is called when a timeout occurred on the network interface.
	 * 
	 * @param event
	 *            {@link InterfaceEvent} associated with this event.
	 */
	public void onTimeout (final InterfaceEvent event);

	/**
	 * This method is called when an exception occurred on the network
	 * interface.
	 * 
	 * @param event
	 *            {@link InterfaceEvent} associated with this event.
	 * @param t
	 *            The thrown exception.
	 */
	public void onException (final InterfaceEvent event, final Throwable t);
}