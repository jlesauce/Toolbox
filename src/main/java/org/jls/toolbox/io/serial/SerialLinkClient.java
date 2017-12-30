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

package org.jls.toolbox.io.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.CommPortOwnershipListener;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates a serial link client, that can receive and send messages.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public class SerialLinkClient implements CommPortOwnershipListener, SerialPortEventListener {

    private final Logger logger;
    private final String appName;
    private final ArrayList<SerialLinkEventListener> listeners;

    private CommPortIdentifier portId;
    private SerialPort serialPort;
    private SerialLinkParameters serialParams;
    private OutputStream output;
    private InputStream input;
    private boolean isOpen;

    /**
     * Instantiates a new serial link client. The serial link is initialized using
     * the parameters specified by {@link SerialLinkParameters}. Once the serial
     * link has been instanciated, it has to be started calling the method
     * {@link SerialLinkClient#open()}. The received messages will be notified to
     * the subscribers of this class using {@link SerialLinkEventListener}
     * interface.
     * 
     * @param appName
     *            Application's name used for the port reservation.
     * @param params
     *            Specifies the serial link parameters.
     */
    public SerialLinkClient(final String appName, final SerialLinkParameters params) {
        this.logger = LogManager.getLogger();
        this.appName = appName;
        this.serialParams = params;
        this.listeners = new ArrayList<>();
        this.portId = null;
        this.serialPort = null;
        this.output = null;
        this.input = null;
        this.isOpen = false;
    }

    /**
     * Opens the connection using the specified parameters.
     * 
     * @throws SerialLinkException
     *             If an error occurred opening the serial link.
     */
    public void open () throws SerialLinkException {
        if (this.serialPort == null) {
            boolean error = false;
            try {
                this.logger.debug("Getting serial port identifier {}", this.serialParams.getComId());
                this.portId = CommPortIdentifier.getPortIdentifier(this.serialParams.getComId());
                if (this.portId.getPortType() != CommPortIdentifier.PORT_SERIAL) {
                    throw new SerialLinkException(this.portId.getName() + " is not a serial port");
                }
                /*
                 * Opens the connection with a timeout, allowing other applications to free the
                 * used port if necessary
                 */
                this.logger.info("Opening serial port {}", this.serialParams.getComId());
                this.serialPort = (SerialPort) this.portId.open(this.appName, 2000);

                // Apply the serial link parameters
                setParameters(this.serialParams);

                // Retrieve input/output streams
                this.input = this.serialPort.getInputStream();
                this.output = this.serialPort.getOutputStream();

                this.serialPort.notifyOnBreakInterrupt(true);
                this.serialPort.notifyOnDataAvailable(true);
                this.serialPort.enableReceiveTimeout(this.serialParams.getRecvTimeout());
                this.portId.addPortOwnershipListener(this);
                this.serialPort.addEventListener(this);
                this.isOpen = true;
            } catch (NoSuchPortException e) {
                error = true;
                throw new SerialLinkException(e.getMessage(), e);
            } catch (PortInUseException e) {
                error = true;
                throw new SerialLinkException(e.getMessage(), e);
            } catch (IOException e) {
                error = true;
                throw new SerialLinkException(e.getMessage(), e);
            } catch (UnsupportedCommOperationException e) {
                error = true;
                throw new SerialLinkException(e.getMessage(), e);
            } catch (TooManyListenersException e) {
                error = true;
                throw new SerialLinkException(e.getMessage(), e);
            } catch (Exception e) {
                error = true;
                throw new SerialLinkException(e.getMessage(), e);
            } finally {
                if (error && this.serialPort != null) {
                    this.serialPort.close();
                    this.serialPort = null;
                }
            }
        }
    }

    /**
     * Closes the serial link connection and associated elements.
     */
    public void close () {
        if (this.serialPort != null) {
            try {
                this.output.close();
                this.input.close();
                this.logger.info("Serial port {} closed", this.serialParams.getComId());
            } catch (IOException e) {
                this.logger.error("An error occured while closing the serial link {}", this.serialParams.getComId(), e);
            }
            this.serialPort.close();
            this.serialPort = null;
            this.portId.removePortOwnershipListener(this);
        }
        this.isOpen = false;
    }

    /**
     * Sends a message through the serial link.
     * 
     * @param msg
     *            The message to send.
     * @throws SerialLinkException
     *             If serial link is closed or an error occurred sending the
     *             message.
     */
    public void write (final String msg) throws SerialLinkException {
        if (msg != null && !msg.isEmpty()) {
            if (this.serialPort != null && this.isOpen) {
                try {
                    this.logger.info("Sending packet on serial port {} (length={})", this.serialParams.getComId(),
                            msg.getBytes().length);
                    this.output.write(msg.getBytes());
                } catch (IOException e) {
                    throw new SerialLinkException(e.getMessage(), e);
                }
            } else {
                throw new SerialLinkException("Serial port is closed");
            }
        } else {
            throw new NullPointerException("Message cannot be null or empty");
        }
    }

    /**
     * Sends a <code>break</code> signal for the specified delay.
     * 
     * @param millis
     *            Break's delay in milliseconds.
     * @throws SerialLinkException
     *             If serial link is closed.
     */
    public void sendBreak (int millis) throws SerialLinkException {
        if (this.serialPort != null && this.isOpen) {
            this.serialPort.sendBreak(millis);
        } else {
            throw new SerialLinkException("Serial port is closed");
        }
    }

    /**
     * Applies the specified parameters to the serial link. If the update fails,
     * then the default parameters are applied and an exception is thrown.
     * 
     * @param params
     *            The serial link parameters.
     * @throws SerialLinkException
     *             If an error occurred applying the specified parameters.
     */
    private void setParameters (final SerialLinkParameters params) throws SerialLinkException {
        try {
            this.serialPort.setSerialPortParams(params.getBaudRate(), params.getDatabits(), params.getStopbits(),
                    params.getParity());
            int flowControl = params.getFlowControlIn() | params.getFlowControlOut();
            this.serialPort.setFlowControlMode(flowControl);
            if (this.serialPort.getFlowControlMode() != flowControl) {
                throw new SerialLinkException("Failed to update flow control mode");
            }
        } catch (UnsupportedCommOperationException e) {
            throw new SerialLinkException("Unsupported parameters for serial link", e);
        }
    }

    /**
     * Reads the messages received from the serial link connection.
     * 
     * @return Received message.
     */
    private String read () {
        StringBuffer buffer = new StringBuffer();
        int newData = 0;
        while (newData != -1) {
            try {
                newData = this.input.read();
                if (newData == -1) {
                    break;
                }
                // Substitutes '\r' chars
                if ('\r' == (char) newData) {
                    buffer.append('\n');
                } else {
                    buffer.append((char) newData);
                }
            } catch (IOException e1) {
                this.logger.error("An error occured while reading the serial input stream", e1);
                return null;
            }
        }
        return buffer.toString();
    }

    @Override
    public void ownershipChange (int type) {
        this.logger.warn("{} ownership changed", this.serialParams.getComId());
    }

    public SerialLinkParameters getSerialParams () {
        return this.serialParams;
    }

    public void setSerialParams (SerialLinkParameters serialParams) {
        this.serialParams = serialParams;
    }

    /**
     * Adds a listener to the notifications triggered by the serial link.
     * 
     * @param listener
     *            Listener to the notifications.
     */
    public void addSerialEventListener (final SerialLinkEventListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    /**
     * Removes the specified listener of the notifications of the serial link.
     * 
     * @param listener
     *            Listener to the notifications.
     * @return <code>true</code> if the listener has been removed and was contained
     *         in the listeners' list.
     */
    public boolean removeSerialEventListener (final SerialLinkEventListener listener) {
        return this.listeners.remove(listener);
    }

    @Override
    public void serialEvent (SerialPortEvent e) {
        String msg = "";
        if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            // Reads the incoming message
            msg = read();
        }
        // Notifies subscribers
        if (msg != null && !msg.isEmpty()) {
            for (SerialLinkEventListener listener : this.listeners) {
                listener.onNotify(new SerialLinkEvent(this, this.serialParams, msg, e.getEventType()));
            }
        }
    }

    /**
     * Returns the state of the serial link.
     * 
     * @return <code>true</code> if the serial link is connected, <code>false</code>
     *         otherwise.
     */
    public boolean isConnected () {
        return this.isOpen;
    }
}
