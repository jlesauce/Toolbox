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

package org.jls.toolbox.io.serial;

import gnu.io.SerialPort;

/**
 * Represents the serial link's parameters.
 * 
 * @author Julien LE SAUCE
 * @date 20 févr. 2016
 */
public class SerialLinkParameters {

	private String comId;
	private int baudRate;
	private int flowControlIn;
	private int flowControlOut;
	private int databits;
	private int stopbits;
	private int parity;
	private int recvTimeout;

	/**
	 * Instanciates a default set of parameters ("DEFAULT", 9600 bauds, no flow
	 * control, 8 data bits, 1 stop bit, no parity).
	 */
	public SerialLinkParameters () {
		this("DEFAULT", 9600, SerialPort.FLOWCONTROL_NONE, SerialPort.FLOWCONTROL_NONE, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, 200);
	}

	/**
	 * Instanciates a new set of parameters using the specifies values.
	 * 
	 * @param id
	 *            Serial link's identifier.
	 * @param baudRate
	 *            The baud rate.
	 * @param flowControlIn
	 *            The reception's flow control type.
	 * @param flowControlOut
	 *            The transmission's flow control type.
	 * @param databits
	 *            The number of data bits.
	 * @param stopbits
	 *            The number of stop bits.
	 * @param parity
	 *            Parity type.
	 * @param recvTimeout
	 *            Reception timeout in milliseconds.
	 */
	public SerialLinkParameters (String id, int baudRate, int flowControlIn, int flowControlOut, int databits, int stopbits,
			int parity, int recvTimeout) {
		this.comId = id;
		this.baudRate = baudRate;
		this.flowControlIn = flowControlIn;
		this.flowControlOut = flowControlOut;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;
		this.recvTimeout = recvTimeout;
	}

	/**
	 * Specifies the serial link's identifier.
	 * 
	 * @param id
	 *            Serial link's identifier.
	 */
	public void setComId (String id) {
		this.comId = id;
	}

	/**
	 * Returns the serial link's identifier.
	 * 
	 * @return The serial link's identifier.
	 */
	public String getComId () {
		return this.comId;
	}

	/**
	 * Specifies the baud rate.
	 * 
	 * @param baudRate
	 *            Baud rate in bauds.
	 */
	public void setBaudRate (int baudRate) {
		this.baudRate = baudRate;
	}

	/**
	 * Returns the baud rate.
	 * 
	 * @return Baud rate in bauds.
	 */
	public int getBaudRate () {
		return this.baudRate;
	}

	/**
	 * Specifies the reception's flow control type.
	 * 
	 * @param flowControl
	 *            Flow control type.
	 */
	public void setFlowControlIn (String flowControl) {
		if ("NONE".equals(flowControl)) {
			this.flowControlIn = SerialPort.FLOWCONTROL_NONE;
		} else if ("RTSCTS_IN".equals(flowControl)) {
			this.flowControlIn = SerialPort.FLOWCONTROL_RTSCTS_IN;
		} else if ("XONXOFF_IN".equals(flowControl)) {
			this.flowControlIn = SerialPort.FLOWCONTROL_XONXOFF_IN;
		} else {
			throw new IllegalArgumentException("Invalid flowControlIn value : " + flowControl);
		}
	}

	/**
	 * Returns the reception's flow control type.
	 * 
	 * @return Flow control type.
	 */
	public int getFlowControlIn () {
		return this.flowControlIn;
	}

	/**
	 * Specifies the transmission's flow control type.
	 * 
	 * @param flowControl
	 *            Flow control type.
	 */
	public void setFlowControlOut (String flowControl) {
		if ("NONE".equals(flowControl)) {
			this.flowControlOut = SerialPort.FLOWCONTROL_NONE;
		} else if ("RTSCTS_OUT".equals(flowControl)) {
			this.flowControlOut = SerialPort.FLOWCONTROL_RTSCTS_OUT;
		} else if ("XONXOFF_OUT".equals(flowControl)) {
			this.flowControlOut = SerialPort.FLOWCONTROL_XONXOFF_OUT;
		} else {
			throw new IllegalArgumentException("Invalid flowControlOut value : " + flowControl);
		}
	}

	/**
	 * Returns the transmission's flow control type.
	 * 
	 * @return Flow control type.
	 */
	public int getFlowControlOut () {
		return this.flowControlOut;
	}

	/**
	 * Specifies the number of data bits.
	 * 
	 * @param nbBits
	 *            Number of data bits (see {@link SerialPort} to know the
	 *            available set of values).
	 */
	public void setDatabits (int nbBits) {
		switch (nbBits) {
			case 5:
				this.databits = SerialPort.DATABITS_5;
				break;
			case 6:
				this.databits = SerialPort.DATABITS_6;
				break;
			case 7:
				this.databits = SerialPort.DATABITS_7;
				break;
			case 8:
				this.databits = SerialPort.DATABITS_8;
				break;
			default:
				throw new IllegalArgumentException("Invalid databits value : " + nbBits);
		}
	}

	/**
	 * Returns the number of data bits.
	 * 
	 * @return Number of data bits.
	 */
	public int getDatabits () {
		return this.databits;
	}

	/**
	 * Specifies the number of stop bits.
	 * 
	 * @param nbBits
	 *            Number of stop bits (see {@link SerialPort} to know the
	 *            available set of values).
	 */
	public void setStopbits (String nbBits) {
		if (nbBits.equals("1")) {
			this.stopbits = SerialPort.STOPBITS_1;
		} else if (nbBits.equals("1.5") || nbBits.equals("1,5") || nbBits.equals("1_5")) {
			this.stopbits = SerialPort.STOPBITS_1_5;
		} else if (nbBits.equals("2")) {
			this.stopbits = SerialPort.STOPBITS_2;
		} else {
			throw new IllegalArgumentException("Invalid stopbits value : " + nbBits);
		}
	}

	/**
	 * Returns the number of stop bits.
	 * 
	 * @return Number of stop bits.
	 */
	public int getStopbits () {
		return this.stopbits;
	}

	/**
	 * Specifies the parity type.
	 * 
	 * @param parity
	 *            Parity type
	 */
	public void setParity (String parity) {
		if ("NONE".equals(parity)) {
			this.parity = SerialPort.PARITY_NONE;
		} else if ("EVEN".equals(parity)) {
			this.parity = SerialPort.PARITY_EVEN;
		} else if ("MARK".equals(parity)) {
			this.parity = SerialPort.PARITY_MARK;
		} else if ("ODD".equals(parity)) {
			this.parity = SerialPort.PARITY_ODD;
		} else if ("SPACE".equals(parity)) {
			this.parity = SerialPort.PARITY_SPACE;
		} else {
			throw new IllegalArgumentException("Invalid parity value");
		}
	}

	/**
	 * Returns the parity type.
	 * 
	 * @return Parity type.
	 */
	public int getParity () {
		return this.parity;
	}

	/**
	 * Returns the timeout time used to read on the serial link whatever data
	 * comes in or not.
	 * 
	 * @return Timeout in milliseconds.
	 */
	public int getRecvTimeout () {
		return this.recvTimeout;
	}

	/**
	 * Specifies the timeout time used to read on the serial link whatever data
	 * comes in or not.
	 * 
	 * @param tMillis
	 *            Timeout in milliseconds.
	 */
	public void setRecvTimeout (int tMillis) {
		this.recvTimeout = tMillis;
	}
}