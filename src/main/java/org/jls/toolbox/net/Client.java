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

import java.io.IOException;

/**
 * Interface representing a network client.
 * 
 * @author LE SAUCE Julien
 * @date Sep 15, 2015
 */
public interface Client {

	/**
	 * Starts the reception thread of the client.
	 * 
	 * @throws IOException
	 *             If an error occurred starting the client thread.
	 */
	public void start () throws IOException;

	/**
	 * Stops the reception thread of the client.
	 * 
	 * @throws IOException
	 *             If an error occurred stoping the client thread.
	 */
	public void stop () throws IOException;

	/**
	 * Sends the specified message to the local or remote server.
	 * 
	 * @param msg
	 *            The message to send.
	 * @throws IOException
	 *             If an error occurred sending the message.
	 */
	public void send (final byte[] msg) throws IOException;

	/**
	 * Subscribes the specified listener to the notifications of the network
	 * interface.
	 * 
	 * @param listener
	 *            The listener to the notifications of the network interface.
	 * @return <code>true</code> if the listener was added to the list,
	 *         <code>false</code> if it was already contained.
	 */
	public boolean addListener (final InterfaceListener listener);

	/**
	 * Unsubscribes the specified listener to the notifications of the network
	 * interface.
	 * 
	 * @param listener
	 *            The listener to the notifications of the network interface.
	 * @return <code>true</code> if the listener was removed from the list,
	 *         <code>false</code> if it was not contained.
	 */
	public boolean removeListener (final InterfaceListener listener);

	/**
	 * Returns <code>true</code> if the client reception thread is running,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the client reception thread is running,
	 *         <code>false</code> otherwise.
	 */
	public boolean isRunning ();

	/**
	 * Returns the network interface descriptor associated with this client.
	 * 
	 * @return {@link Interface} associated with this client.
	 */
	public Interface getInterface ();
}