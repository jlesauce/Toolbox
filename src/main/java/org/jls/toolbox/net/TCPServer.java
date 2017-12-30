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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates a multithreaded TCP server between this application and local or
 * remote clients. This class implements a provider-subscriber architecture
 * type, which means that users of this class have to subscribe for the TCP
 * connection's events by calling
 * {@link TCPClient#addListener(InterfaceListener) addListener()} method. Once
 * the class has been instanciated, the socket server is created and opened by
 * calling {@link TCPClient#start() start()} method, if the socket is
 * successfully connected the thread is started and waits for clients to connect
 * until {@link TCPClient#stop() stop()} method is called. When a new client
 * connects to this server a new {@link TCPClient} is created and used as an
 * interface between this server and the local or remote client in its own
 * thread.
 * 
 * @author LE SAUCE Julien
 * @date Sep 15, 2015
 */
public class TCPServer implements Server, Runnable, InterfaceListener {

    private final Interface com;
    private final Logger logger;
    private final HashSet<Interface> clientsDesc;
    private final HashSet<TCPClient> clients;
    private final HashSet<InterfaceListener> listeners;

    private ServerSocket serverSocket;
    private Thread ownThread;

    /**
     * Instantiates a new TCP server using the specified interface descriptor. Once
     * the class has been instanciated, the socket server is created and opened by
     * calling {@link TCPServer#start() start()} method, if the socket is
     * successfully connected the thread is started and waits for clients to connect
     * until {@link TCPServer#stop() stop()} method is called.
     * 
     * @param com
     *            Network interface descriptor.
     */
    public TCPServer(final Interface com) {
        this.logger = LogManager.getLogger();
        this.com = com;
        this.clientsDesc = new HashSet<>();
        this.clients = new HashSet<>();
        this.listeners = new HashSet<>();
        this.serverSocket = null;
        this.ownThread = null;
    }

    @Override
    public void run () {
        try {
            // Open the server's socket
            this.logger.info("Opening server socket on port {}", this.com.getPort());
            this.serverSocket = new ServerSocket(this.com.getPort());

            // Waits for new clients to connect
            while (isRunning() && !this.serverSocket.isClosed()) {
                try {
                    Socket socket = this.serverSocket.accept();
                    Interface com = new Interface(socket.getInetAddress().toString(), socket.getInetAddress(),
                            socket.getPort());
                    TCPClient client = new TCPClient(com, socket, 0);
                    this.logger.info("New connection to {} server from {} ({} client(s) connected)", this.com.getId(),
                            client.toString(), this.clients.size() + 1);
                    client.addListener(this);
                    client.start();
                    this.clientsDesc.add(com);
                    this.clients.add(client);
                } catch (IOException e) {
                    this.logger.error("An error occurred connecting a new client", e);
                    break;
                }
            }
        } catch (IOException e) {
            this.logger.error("An error occurred receiving a message from {}", this.com.getId(), e);
            InterfaceEvent event = new InterfaceEvent(this, this.com);
            for (InterfaceListener l : this.listeners) {
                l.onException(event, e);
            }
        }
        this.logger.info("{} server thread terminated", this.com.getId());
    }

    @Override
    public synchronized void start () {
        // If thread doesn't exist yet
        if (this.ownThread == null) {
            this.logger.info("Starting {} server", this.com.getId());
            this.ownThread = new Thread(this);
            this.ownThread.start();
        }
    }

    @Override
    public synchronized void stop () throws IOException {
        // If threads exists
        if (this.ownThread != null) {
            // Tells the thread to interrupt itself
            this.ownThread.interrupt();
            this.ownThread = null;
            // Disconnects the connected clients
            this.logger.info("Disconnecting clients");
            for (TCPClient client : this.clients) {
                if (client.isRunning()) {
                    client.stop();
                }
            }
            this.clientsDesc.clear();
            this.clients.clear();
            this.serverSocket.close();
            this.serverSocket = null;
            this.logger.info("Server {} stopped", this.com.getId());
        }
    }

    @Override
    public void send (byte[] msg) throws IOException, NoClientConnectedException {
        if (this.clients.size() > 0) {
            for (TCPClient client : this.clients) {
                this.logger.info("Send message to {} (length={})", client.getInterface(), msg.length);
                client.send(msg);
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
        return this.clientsDesc;
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
}
