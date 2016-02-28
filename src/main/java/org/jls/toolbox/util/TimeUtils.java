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

package org.jls.toolbox.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Permet de centraliser des fonctions utiles permettant de gérer des dates et
 * des temps.
 * 
 * @author LE SAUCE Julien
 * @date Mar 12, 2015
 */
public class TimeUtils {

	private static final ResourceManager props = ResourceManager.getInstance();

	private static final SimpleDateFormat fileFormat =
			new SimpleDateFormat(props.getString("toolbox.pattern.format.date.file"));
	private static final SimpleDateFormat consoleFormat =
			new SimpleDateFormat(props.getString("toolbox.pattern.format.date.console"));
	private static final SimpleDateFormat zuluFormat =
			new SimpleDateFormat(props.getString("toolbox.pattern.format.date.zulu"));

	static {
		fileFormat.setTimeZone(TimeZone.getDefault());
		consoleFormat.setTimeZone(TimeZone.getDefault());
		zuluFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * Classe non instanciable.
	 */
	private TimeUtils () {
		throw new AssertionError();
	}

	/**
	 * Renvoie la date actuelle complète.
	 * 
	 * @return Chaîne de texte renvoyant la date actuelle complète.
	 */
	public static String getFileTimestamp () {
		return fileFormat.format(new Date());
	}

	/**
	 * Renvoie la date UTC actuelle complète.
	 * 
	 * @return Chaîne de texte renvoyant la date actuelle complète.
	 */
	public static String getZuluTimestamp () {
		return zuluFormat.format(new Date());
	}

	/**
	 * Renvoie la date au format console.
	 * 
	 * @return Date au format console.
	 */
	public static String getConsoleTimestamp () {
		return consoleFormat.format(new Date());
	}
}