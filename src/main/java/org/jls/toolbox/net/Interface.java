/*
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
 */

package org.jls.toolbox.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Network interface descriptor containing the interface's identifier, a network
 * address, the interface network address (optional, used only by multicast
 * sockets) and the connection port.
 * 
 * @author LE SAUCE Julien
 * @date Sep 15, 2015
 */
public class Interface implements Comparable<Interface> {

    private final String id;
    private final InetAddress address;
    private final int port;
    private InetAddress networkAddress;
    private NetworkInterface networkInterface;

    /**
     * Instanciates a new default network interface descriptor, address is set to
     * localhost and port to 0.
     * 
     * @param id
     *            Network interface identifier.
     * @throws UnknownHostException
     *             If an error occurred getting hostname.
     */
    public Interface(final String id) throws UnknownHostException {
        this(id, "localhost", 0);
    }

    /**
     * Instanciates a new network interface descriptor and specifies its identifier
     * and connection port.
     * 
     * @param id
     *            Network interface identifier.
     * @param port
     *            The connection port {0..99999}.
     * @throws UnknownHostException
     *             If an error occurred determining IP address by host's name.
     */
    public Interface(final String id, final int port) throws UnknownHostException {
        this(id, "localhost", port);
    }

    /**
     * Instanciates a new network interface descriptor and specifies its identifier,
     * address and connection port.
     * 
     * @param id
     *            Network interface identifier.
     * @param addr
     *            Interface address.
     * @param port
     *            The connection port {0..99999}.
     */
    public Interface(final String id, final InetAddress addr, final int port) {
        this.id = id;
        this.address = addr;
        this.networkAddress = null;
        this.networkInterface = null;
        this.port = port;
    }

    /**
     * Instanciates a new network interface descriptor and specifies its identifier,
     * address and connection port.
     * 
     * @param id
     *            Network interface identifier.
     * @param hostname
     *            Interface host's name or address as a litteral IPV4/V6 address.
     * @param port
     *            The connection port {0..99999}.
     * @throws UnknownHostException
     *             If an error occurred determining IP address by host's name.
     */
    public Interface(final String id, final String hostname, final int port) throws UnknownHostException {
        super();
        this.id = id;
        this.address = InetAddress.getByName(hostname);
        this.networkAddress = null;
        this.networkInterface = null;
        this.port = port;
    }

    /**
     * Instanciates a new network interface descriptor and specifies its identifier,
     * address, interface address and connection port.
     * 
     * @param id
     *            Network interface identifier.
     * @param hostname
     *            Interface host's name or address as a litteral IPV4/V6 address.
     * @param intAddr
     *            Ethernet interface host's name or address as a litteral IPV4/V6
     *            address.
     * @param port
     *            The connection port {0..99999}.
     * @throws UnknownHostException
     *             If an error occurred determining IP address by host's name.
     * @throws SocketException
     *             If the ethernet interface host's name or address cannot be
     *             resolved.
     */
    public Interface(final String id, final String hostname, final String intAddr, final int port)
            throws UnknownHostException, SocketException {
        this.id = id;
        this.address = InetAddress.getByName(hostname);
        setNetworkInterface(intAddr);
        this.port = port;
    }

    /**
     * Specifies the local interface to receive multicast datagram packets, or
     * <code>null</code> to defer to the interface set by
     * {@link MulticastSocket#setInterface(InetAddress)} or
     * {@link MulticastSocket#setNetworkInterface(NetworkInterface)}.
     * 
     * @param netAddr
     *            Local interface address or its identifier (for example "eth0").
     * @throws UnknownHostException
     *             If no IP address for the host could be found, or if a scope_id
     *             was specified for a global IPv6 address.
     * @throws SocketException
     *             If an I/O error occurs.
     */
    private void setNetworkInterface (String netAddr) throws UnknownHostException, SocketException {
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        InetAddress addr = InetAddress.getByName(netAddr);
        this.networkAddress = addr;
        if (pattern.matcher(netAddr).matches()) {
            this.networkInterface = NetworkInterface.getByInetAddress(addr);
        } else {
            this.networkInterface = NetworkInterface.getByName(netAddr);
        }
    }

    @Override
    public String toString () {
        return getId();
    }

    @Override
    public int compareTo (Interface o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((networkAddress == null) ? 0 : networkAddress.hashCode());
        result = prime * result + ((networkInterface == null) ? 0 : networkInterface.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Interface other = (Interface) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (networkAddress == null) {
            if (other.networkAddress != null)
                return false;
        } else if (!networkAddress.equals(other.networkAddress))
            return false;
        if (networkInterface == null) {
            if (other.networkInterface != null)
                return false;
        } else if (!networkInterface.equals(other.networkInterface))
            return false;
        if (port != other.port)
            return false;
        return true;
    }

    /**
     * Returns the network interface identifier.
     * 
     * @return Network interface identifier.
     */
    public String getId () {
        return this.id;
    }

    /**
     * Returns the network interface address.
     * 
     * @return Network interface address.
     */
    public InetAddress getAddress () {
        return this.address;
    }

    /**
     * Returns the interface network address.
     * 
     * @return Interface network address.
     */
    public InetAddress getNetworkAddress () {
        return this.networkAddress;
    }

    /**
     * Returns the interface network address.
     * 
     * @return Interface network address.
     */
    public NetworkInterface getNetworkInterface () {
        return this.networkInterface;
    }

    /**
     * Creates a new {@link SocketAddress} from this network interface descriptor.
     * 
     * @return New instance of {@link SocketAddress} using the address and port of
     *         this interface descriptor.
     */
    public SocketAddress getSocketAddress () {
        return new InetSocketAddress(this.address, this.port);
    }

    /**
     * Returns the connection port.
     * 
     * @return Connection port.
     */
    public int getPort () {
        return this.port;
    }
}
