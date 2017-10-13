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

package org.jls.toolbox.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates a monothreaded UDP server between this application and local or
 * remote clients. This class implements a provider-subscriber architecture
 * type, which means that users of this class have to subscribe for the UDP
 * connection's events by calling
 * {@link UDPClient#addListener(InterfaceListener) addListener()} method. Once
 * the class has been instanciated, the socket server is created and opened by
 * calling {@link UDPClient#start() start()} method, if the socket is
 * successfully connected, the thread is started and waits for clients messages
 * until {@link UDPClient#stop() stop()} method is called. When a new client
 * sends a message to this server, its address and port are stored and the
 * client is added to the connected clients' list, so when the
 * {@link UDPServer#send(byte[])} method is called, the specified message is
 * sent to all the connected clients.
 * 
 * @author LE SAUCE Julien
 * @date Sep 15, 2015
 */
public class UDPServer implements Server, Runnable, InterfaceListener {

    private final int MAX_SIZE = 65536; // Datagram maximum size

    private final Interface com;
    private final Logger logger;
    private final HashSet<Interface> clients;
    private final HashSet<InterfaceListener> listeners;
    private final byte[] buffer;

    private DatagramSocket serverSocket;
    private Thread ownThread;

    /**
     * Instanciates a new UDP server using the specified interface descriptor. Once
     * the class has been instanciated, the socket server is created and opened by
     * calling {@link UDPServer#start() start()} method, if the socket is
     * successfully connected the thread is started and waits for clients messages
     * until {@link UDPServer#stop() stop()} method is called.
     * 
     * @param com
     *            Network interface descriptor.
     */
    public UDPServer(final Interface com) {
        this.logger = LogManager.getLogger();
        this.com = com;
        this.clients = new HashSet<>();
        this.listeners = new HashSet<>();
        this.buffer = new byte[this.MAX_SIZE];
        this.serverSocket = null;
        this.ownThread = null;
    }

    @Override
    public void run () {
        // If no socket instanciated
        if (this.serverSocket == null) {
            this.logger.error("{} server thread terminated because no socket initialized", this.com.getId());
            return;
        }
        this.logger.info("{} (port {}) now listening", this.com.getId(), this.com.getPort());
        /*
         * Reception loop
         */
        while (!Thread.currentThread().isInterrupted() && !this.serverSocket.isClosed()) {
            try {
                // Receives data
                DatagramPacket packet = read();
                byte[] msg = new byte[packet.getLength()];

                // Client interface
                Interface com = new Interface(packet.getSocketAddress().toString(), packet.getAddress(),
                        packet.getPort());

                // Adds the client to the list
                this.clients.add(com);

                // Extracts the data from the packet
                System.arraycopy(packet.getData(), 0, msg, 0, packet.getLength());

                // Notifies the subscribers
                for (InterfaceListener listener : this.listeners) {
                    InterfaceEvent event = new InterfaceEvent(this, com, msg);
                    listener.onReceive(event);
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
            }
        }
        // Closing socket
        if (this.serverSocket != null) {
            this.serverSocket.close();
            this.serverSocket = null;
        }
        this.logger.debug("{} socket closed", this.com.getId());
        this.logger.info("{} reception loop terminated", this.com.getId());
        this.ownThread = null;
    }

    @Override
    public void start () throws IOException {
        this.logger.info("Starting UDP server {}", this.com.getId());
        // Creates and opens the socket
        if (this.serverSocket == null) {
            this.logger.info("Opening socket server on port {}...", this.com.getPort());
            this.serverSocket = new DatagramSocket(this.com.getPort());
            this.logger.info("Socket server opened");
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
            this.clients.clear();
            this.ownThread = null;
            this.logger.info("{} reception thread stopped", this.com.getId());
        }
    }

    @Override
    public void send (byte[] msg) throws IOException, NoClientConnectedException {
        if (this.clients.size() > 0) {
            for (Interface client : this.clients) {
                this.logger.info("Send message to {} (length={})", client, msg.length);
                write(client, msg);
            }
        } else {
            throw new NoClientConnectedException("No client connected to server");
        }
    }

    @Override
    public boolean addListener (final InterfaceListener listener) {
        return this.listeners.add(listener);
    }

    @Override
    public boolean removeListener (final InterfaceListener listener) {
        return this.listeners.remove(listener);
    }

    @Override
    public synchronized boolean isRunning () {
        return this.ownThread != null && !this.ownThread.isInterrupted();
    }

    @Override
    public Interface getInterface () {
        return this.com;
    }

    @Override
    public Set<? extends Interface> getClients () {
        return this.clients;
    }

    @Override
    public void onException (InterfaceEvent event, Throwable t) {
        for (InterfaceListener l : this.listeners) {
            l.onException(event, t);
        }
    }

    @Override
    public void onReceive (InterfaceEvent event) {
        for (InterfaceListener l : this.listeners) {
            l.onReceive(event);
        }
    }

    @Override
    public void onTimeout (InterfaceEvent event) {
        for (InterfaceListener l : this.listeners) {
            l.onTimeout(event);
        }
    }

    @Override
    public String toString () {
        return this.com.getId();
    }

    /**
     * Sends the specified byte sequence to the server.
     * 
     * @param com
     *            Destination of the message.
     * @param msg
     *            The message to send.
     * @throws IOException
     *             If an error occurred sending the data to the server.
     */
    private void write (final Interface com, final byte[] msg) throws IOException {
        // If connection is established
        if (!this.serverSocket.isClosed()) {
            this.logger.debug("Sending packet to {} (length={})", com.getId(), msg.length);
            // Prepares the packet
            DatagramPacket packet = new DatagramPacket(msg, msg.length, com.getAddress(), com.getPort());
            // Sends the message
            this.serverSocket.send(packet);
        } else {
            throw new IOException("Socket disconnected");
        }
    }

    /**
     * Reads the incoming data from the UDP server. This method blocks until data
     * are received from the server.
     * 
     * @return The datagram packet received from the server.
     * @throws IOException
     *             If an error occurred reading from the socket.
     */
    private DatagramPacket read () throws IOException {
        DatagramPacket packet = new DatagramPacket(this.buffer, this.buffer.length);
        this.serverSocket.receive(packet);
        return packet;
    }
}
