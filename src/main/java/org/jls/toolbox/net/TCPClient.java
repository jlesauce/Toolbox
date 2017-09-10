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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates a monothreaded TCP client between this application and a server. This
 * class implements a provider-subscriber architecture type, which means that
 * users of this class have to subscribe for the TCP connection's events by
 * calling {@link TCPClient#addListener(InterfaceListener) addListener()}
 * method. Once the class has been instanciated, the socket is created and
 * opened by calling {@link TCPClient#start() start()} method, if the socket is
 * successfully connected the thread is started and listens for server messages
 * until {@link TCPClient#stop() stop()} method is called.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class TCPClient implements Client, Runnable {

	private static final int MAX_ERRORS = 10;

	private final Logger logger;
	private final Interface com;
	private final Integer timeout;
	private final HashSet<InterfaceListener> listeners;

	private Socket socket;
	private InputStream iStream;
	private OutputStream oStream;
	private Thread ownThread;
	private int watchdog;

	/**
	 * Instanciates a new TCP client. Once the class has been instanciated, the
	 * socket is created and opened by calling {@link TCPClient#start() start()}
	 * method, if the socket is successfully connected the thread is started and
	 * listens for server messages until {@link TCPClient#stop() stop()} method
	 * is called.
	 * 
	 * @param com
	 *            The interface descriptor.
	 * @param timeout
	 *            Time out on the socket in milliseconds, if <code>null</code>
	 *            is specified then the timeout is not used.
	 */
	public TCPClient (final Interface com, final Integer timeout) {
		this.logger = LogManager.getLogger();
		this.com = com;
		this.timeout = timeout;
		this.listeners = new HashSet<>();
		this.ownThread = null;
		this.watchdog = 0;
	}

	/**
	 * Instanciates a new TCP client. Once the class has been instanciated, the
	 * specified socket is opened by calling {@link TCPClient#start() start()}
	 * method, if the socket is successfully connected the thread is started and
	 * listens for server messages until {@link TCPClient#stop() stop()} method
	 * is called.
	 * 
	 * @param com
	 *            The interface descriptor.
	 * @param socket
	 *            The client socket (when used by server side the client socket
	 *            is given by calling the socket server method
	 *            {@link ServerSocket#accept()}).
	 * @param timeout
	 *            Time out on the socket in milliseconds, if <code>null</code>
	 *            is specified then the timeout is not used.
	 * @throws IOException
	 *             If an error occurred getting input / output streams from the
	 *             specified socket.
	 */
	public TCPClient (final Interface com, final Socket socket, final Integer timeout) throws IOException {
		if (socket == null) {
			throw new NullPointerException("Socket cannot be null");
		}
		if (socket.isClosed()) {
			throw new IOException("Socket is closed");
		}
		this.com = com;
		this.logger = LogManager.getLogger();
		this.listeners = new HashSet<>();
		this.socket = socket;
		this.iStream = socket.getInputStream();
		this.oStream = socket.getOutputStream();
		this.timeout = timeout;
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
		this.watchdog = 0;
		while (!Thread.currentThread().isInterrupted() && !this.socket.isClosed()) {
			try {
				byte[] msg = read();
				if (msg != null) {
					// Notifies the subscribers
					for (InterfaceListener listener : this.listeners) {
						InterfaceEvent event = new InterfaceEvent(this, this.com, msg);
						listener.onReceive(event);
					}
					// Resets watchdog
					this.watchdog = 0;
				} else {
					this.logger.warn("An error occurred during reception");
					this.watchdog++;
				}
				// If too many errors, socket is closed
				if (this.watchdog > TCPClient.MAX_ERRORS) {
					this.logger.error("Broken Link (Max error limit reached)");
					// Notifies subscribers
					InterfaceEvent event = new InterfaceEvent(this, this.com);
					for (InterfaceListener l : this.listeners) {
						l.onException(event, new BrokenLinkException("Broken Link (Max error limit reached)"));
					}
					break;
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
				}
				break;
			} catch (ConnectionLost e) {
				InterfaceEvent event = new InterfaceEvent(this, this.com);
				for (InterfaceListener l : this.listeners) {
					l.onException(event, e);
				}
				break;
			}
		}
		// Closing socket
		try {
			if (this.socket != null) {
				this.socket.close();
				this.socket = null;
			}
			this.logger.debug("{} socket closed", this.com.getId());
		} catch (IOException e) {
			this.logger.error("An error occurred disconnecting " + this.com.toString(), e);
		}
		this.logger.info("{} reception loop terminated", this.com.getId());
		this.ownThread = null;
		this.watchdog = 0;
	}

	@Override
	public void start () throws IOException {
		this.watchdog = 0;
		this.logger.info("Starting TCP client {}", this.com.getId());
		// Creates and opens the socket
		if (this.socket == null) {
			this.logger.info("Opening socket on {}/{}...", this.com.getAddress(), this.com.getPort());
			this.socket = new Socket();
			// If a timeout is specified
			if (this.timeout != null) {
				this.socket.setSoTimeout(this.timeout.intValue());
			}
			try { // Tries to connect the socket
				this.socket.connect(this.com.getSocketAddress(), 2000);
				this.iStream = this.socket.getInputStream();
				this.oStream = this.socket.getOutputStream();
				this.logger.info("Socket opened on {}/{}", this.socket.getInetAddress(), this.socket.getPort());
			} catch (Exception e) {
				// If an error occurred then close the socket
				this.socket.close();
				this.socket = null;
				throw e;
			}
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
	 * Sends the specified byte sequence to the TCP server. Note that the size
	 * of the data payload is inserted at the beginning of the message (4 bytes
	 * in Big Endian format) to allow the receiver to pre-allocate its reception
	 * buffer.
	 * 
	 * @param msg
	 *            The message to send.
	 * @throws IOException
	 *             If an error occurred sending the data to the server.
	 */
	private void write (final byte[] msg) throws IOException {
		int sizeInt = msg.length;
		byte[] size = ByteBuffer.allocate(4).putInt(sizeInt).order(ByteOrder.BIG_ENDIAN).array();
		// If connection is established
		if (!this.socket.isClosed()) {
			this.logger.debug("Sending packet to {} (length={})", this.com.getId(), sizeInt);
			// Sends the size of the message
			this.oStream.write(size);
			// Sends the message
			this.oStream.write(msg);
			this.oStream.flush();
		} else {
			throw new IOException("Socket disconnected");
		}
	}

	/**
	 * Reads the incoming data from the TCP server. This method blocks until
	 * data are received from the server. This method expects the server to
	 * insert the data payload size at the beginning of the message (4 bytes in
	 * BigEndian format), then waits to receive the specified size before
	 * returning the received message (without the size).
	 * 
	 * @return The byte sequence received from the server.
	 * @throws IOException
	 *             If an error occurred reading the socket.
	 * @throws ConnectionLost
	 *             If the connection is declared as broken because the server
	 *             socket is unreachable.
	 */
	private byte[] read () throws IOException, ConnectionLost {
		byte[] msgSize = new byte[4];

		int nbBytes = 0;
		try {
			// Reads the message's size specified by the server
			nbBytes = this.iStream.read(msgSize);
			this.logger.debug("Reading message buffer size, received bytes = {}", nbBytes);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException("Timeout occurred on " + this.com.getId() + " link");
		} catch (Exception e) {
			throw new ConnectionLost("Connection with server " + this.com.getId() + " lost", e);
		}

		// End of Stream or Connection lost
		if (nbBytes < 0) {
			throw new ConnectionLost("Connection with server " + this.com.getId() + " lost");
		}

		// No data
		if (nbBytes == 0) {
			this.logger.warn("Receiving empty message from server {}", this.com.getId());
			return null;
		}

		// Gets the size in Big Endian
		int size = ByteBuffer.wrap(msgSize).order(ByteOrder.BIG_ENDIAN).getInt();
		this.logger.debug("Extracting buffer size : size={}", size);

		// Pre-allocates the message buffer
		byte[] recvData = new byte[size];

		// Waits until message is completely received
		nbBytes = 0;
		while (nbBytes < size) {
			int recvSize = this.iStream.read(recvData, nbBytes, size - nbBytes);
			this.logger.debug("Reading data from socket, length={}", recvSize);
			if (recvSize <= 0) {
				return null;
			}
			nbBytes += recvSize;
		}
		return recvData;
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