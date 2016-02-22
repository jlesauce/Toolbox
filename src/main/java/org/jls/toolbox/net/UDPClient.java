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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates a monothreaded UDP client between this application and a server. This
 * class implements a provider-subscriber architecture type, which means that
 * users of this class have to subscribe for the UDP connection's events by
 * calling {@link UDPClient#addListener(InterfaceListener) addListener()}
 * method. Once the class has been instanciated, the socket is created and
 * opened by calling {@link UDPClient#start() start()} method, if the socket is
 * successfully connected the thread is started and listens for server messages
 * until {@link UDPClient#stop() stop()} method is called.
 * 
 * @author LE SAUCE Julien
 * @date Sep 15, 2015
 */
public class UDPClient implements Client, Runnable {

	private final int MAX_SIZE = 65536; // Datagram maximum size

	private final Logger logger;
	private final Interface com;
	private final HashSet<InterfaceListener> listeners;
	private final int timeout;
	private final byte[] buffer;

	private DatagramSocket socket;
	private Thread ownThread;

	/**
	 * Instanciates a new UDP client. Once the class has been instanciated, the
	 * socket is created and opened by calling {@link UDPClient#start() start()}
	 * method, if the socket is successfully connected the thread is started and
	 * listens for server messages until {@link UDPClient#stop() stop()} method
	 * is called.
	 * 
	 * @param com
	 *            The interface descriptor.
	 * @param timeout
	 *            Time out on the socket in milliseconds.
	 */
	public UDPClient (final Interface com, final Integer timeout) {
		this.logger = LogManager.getLogger();
		this.com = com;
		this.listeners = new HashSet<>();
		this.timeout = timeout;
		this.buffer = new byte[this.MAX_SIZE];
		this.socket = null;
		this.ownThread = null;
	}

	@Override
	public void run () {
		// If no socket instanciated
		if (this.socket == null) {
			this.logger.error("{} thread terminated because no socket initialized", this.com.getId());
			return;
		}
		this.logger.info("{} ({}/{}) now listening", this.com.getId(), this.com.getAddress().getHostAddress(),
				this.com.getPort());
		/*
		 * Reception loop
		 */
		while (!Thread.currentThread().isInterrupted() && !this.socket.isClosed()) {
			try {
				byte[] msg = read();
				if (msg != null) {
					// Notifies the subscribers
					for (InterfaceListener listener : this.listeners) {
						InterfaceEvent event = new InterfaceEvent(this, this.com, msg);
						listener.onReceive(event);
					}
				} else {
					this.logger.warn("An error occurred during reception");
				}
			} catch (IOException e) {
				// If a timeout occurred
				if (e instanceof SocketTimeoutException) {
					InterfaceEvent event = new InterfaceEvent(this, this.com);
					for (InterfaceListener l : this.listeners) {
						l.onTimeout(event);
					}
				} else {
					this.logger.error("An error occurred receiving a message from {}", this.com.getId(), e);
					InterfaceEvent event = new InterfaceEvent(this, this.com);
					for (InterfaceListener l : this.listeners) {
						l.onException(event, e);
					}
					break;
				}
			}
		}
		// Closing socket
		if (this.socket != null) {
			this.socket.close();
			this.socket = null;
		}
		this.logger.debug("{} socket closed", this.com.getId());
		this.logger.info("{} reception loop terminated", this.com.getId());
		this.ownThread = null;
	}

	@Override
	public void start () throws IOException {
		this.logger.info("Starting UDP client {}", this.com.getId());
		// Creates and opens the socket
		if (this.socket == null) {
			this.logger.info("Opening datagram socket...");
			this.socket = new DatagramSocket();
			this.socket.setSoTimeout(this.timeout);
			this.logger.info("Socket opened on {}/{}", this.socket.getInetAddress(), this.socket.getPort());
		}
		// Starts the reception loop
		if (this.ownThread == null) {
			this.ownThread = new Thread(this);
			this.ownThread.start();
		}
	}

	@Override
	public void stop () {
		// If thread exists
		if (this.ownThread != null) {
			this.ownThread.interrupt();
			this.ownThread = null;
			this.logger.info("{} reception thread stopped", this.com.getId());
		}
	}

	@Override
	public void send (byte[] msg) throws IOException {
		write(msg);
	}

	@Override
	public boolean addListener (final InterfaceListener listener) {
		return this.listeners.add(listener);
	}

	@Override
	public boolean removeListener (final InterfaceListener listener) {
		return this.listeners.remove(listener);
	}

	/**
	 * Sends the specified byte sequence to the server.
	 * 
	 * @param msg
	 *            The message to send.
	 * @throws IOException
	 *             If an error occurred sending the data to the server.
	 */
	private void write (final byte[] msg) throws IOException {
		// If connection is established
		if (!this.socket.isClosed()) {
			this.logger.debug("Sending packet to {} (length={})", this.com.getId(), msg.length);
			// Prepares the packet
			DatagramPacket packet = new DatagramPacket(msg, msg.length, this.com.getAddress(), this.com.getPort());
			// Sends the message
			this.socket.send(packet);
		} else {
			throw new IOException("Socket disconnected");
		}
	}

	/**
	 * Reads the incoming data from the UDP server. This method blocks until
	 * data are received from the server.
	 * 
	 * @return The byte sequence received from the server.
	 * @throws IOException
	 *             If an error occurred reading from the socket.
	 */
	private byte[] read () throws IOException {
		DatagramPacket packet = new DatagramPacket(this.buffer, this.buffer.length);
		this.socket.receive(packet);
		byte[] msg = new byte[packet.getLength()];
		// Extracts the data from the packet
		System.arraycopy(packet.getData(), 0, msg, 0, packet.getLength());
		return msg;
	}

	@Override
	public boolean isRunning () {
		return this.ownThread != null && !this.ownThread.isInterrupted();
	}

	@Override
	public Interface getInterface () {
		return this.com;
	}
}