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

package org.jls.toolbox.util.nmea;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * Parser de messages au format NMEA.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class NMEAParser {

	/**
	 * Permet de vérifier qu'une chaîne respecte le format NMEA.
	 * 
	 * @param str
	 *            Chaîne de texte à contrôler.
	 * @return <code>true</code> si la chaîne respecte la forme d'une chaîne
	 *         NMEA, <code>false</code> sinon.
	 */
	public static boolean isNMEAString (String str) {
		if (str == null) {
			throw new NullPointerException("String cannot be null");
		}
		// Description du regex :
		// ----------------------
		// \$ : Commence par $
		// [^*$]+ : Suivi de tout sauf * ou $ (multiples caractères)
		// (,[^*$]+) : Suivi par au moins un groupe de (tout sauf * ou $)
		// précédé d'une virgule
		// \*[0-9A-Fa-f]{1,2} : Fini par *checksum avec checksum sur deux
		// caractères hexa
		// ()+ : Et le tout doit être présent au moins une fois
		return str.matches("^(\\$[^*$]+(,[^*$]+)\\*[0-9A-Fa-f]{1,2})+$");
	}

	/**
	 * Permet de parser une chaîne de texte au format NMEA.
	 * 
	 * @param str
	 *            Chaîne de texte au format NMEA.
	 * @return Renvoie la liste des valeurs extraites depuis la chaîne NMEA.
	 * @throws NMEAParsingException
	 *             Si une erreur survient durant le parsing, une exception est
	 *             levée.
	 */
	public static HashMap<String, Sentence> parseString (final String str) throws NMEAParsingException {
		HashMap<String, Sentence> nmeaSentences = new HashMap<>();
		StringTokenizer nmeaStrs = new StringTokenizer(str, "$");

		// Extraction des phrases
		if (nmeaStrs.countTokens() > 0) {
			while (nmeaStrs.hasMoreTokens()) {
				String sentence = nmeaStrs.nextToken();
				StringTokenizer sentences = new StringTokenizer(sentence, ",");

				// Détection d'un champ vide
				if (sentence.contains(",,") || sentence.contains(",*") || sentence.startsWith(",")) {
					throw new NMEAParsingException("Empty value forbidden : $" + sentence);
				}

				// Extraction des champs
				if (sentences.countTokens() > 0) {
					// Détection de présence de données
					if (sentences.countTokens() == 1) {
						throw new NMEAParsingException("The type $" + sentences.nextToken() + " contains no data");
					}
					// DataType
					String dataType = sentences.nextToken();
					String checksum;
					boolean isChecksum;
					Sentence newSentence = new Sentence(dataType);
					nmeaSentences.put(dataType, newSentence);

					// Balayage des données
					while (sentences.hasMoreTokens()) {
						String value = sentences.nextToken();
						checksum = "";
						isChecksum = false;
						// Détection du caractère * en milieu de phrase
						if (value.contains("*") && sentences.hasMoreTokens()) {
							throw new NMEAParsingException("Invalid use of * special character in type $" + dataType
									+ " (* must be only used to end sentence before checksum).");
						}
						// Détection du checksum
						if (value.contains("*")) {
							StringTokenizer tokens = new StringTokenizer(value, "*");
							if (value.startsWith("*")) {
								throw new NMEAParsingException("Empty value forbidden : $" + sentence);
							} else if (value.endsWith("*")) {
								throw new NMEAParsingException("Sentence must contains a checksum : $" + sentence);
							} else if (tokens.countTokens() > 2) {
								throw new NMEAParsingException("Invalid use of * special character in type $" + dataType
										+ " (Correct checksum format is *<Checksum>).");
							} else if (tokens.countTokens() < 2) {
								throw new NMEAParsingException("Invalid checksum format : $" + sentence);
							} else {
								isChecksum = true;
								value = tokens.nextToken();
								checksum = tokens.nextToken();
							}
						}
						newSentence.put(value);
						if (isChecksum) {
							newSentence.setCheckSum(Long.parseLong(checksum, 16));
						}
					}
				} else {
					throw new NMEAParsingException(
							"Invalid sentence, format is $<dataType>,<data0>,<data1>,<data2>,...*<Checksum>");
				}
			}
		} else {
			throw new NMEAParsingException("No sentence");
		}
		return nmeaSentences;
	}

	/**
	 * Permet à partir d'une instance de {@link Sentence} de construire une
	 * chaîne de texte au format NMEA. Attention si spécifié on rajoute \r\n à
	 * la fin de la chaîne.
	 * 
	 * @param sentence
	 *            Objet à convertir en chaîne NMEA.
	 * @param endString
	 *            Si <code>true</code> alors on rajoute \r\n à la fin de la
	 *            chaîne.
	 * @return Chaîne de texte au format NMEA.
	 */
	public static String toNMEAString (Sentence sentence, boolean endString) {
		String nmeaStr = "$" + sentence.getDataType();
		for (String str : sentence.getFields()) {
			nmeaStr += "," + str;
		}
		nmeaStr += "*" + String.format("%x", sentence.computeCheckSum());
		if (endString) {
			nmeaStr += "\r\n";
		}
		return nmeaStr;
	}

	/**
	 * Permet à partir d'une liste d'instances de {@link Sentence} de construire
	 * une chaîne de texte au format NMEA. Attention si spécifié on rajoute \r\n
	 * à la fin de la chaîne.
	 * 
	 * @param sentences
	 *            Objets à convertir en une seule chaîne NMEA.
	 * @param endString
	 *            Si <code>true</code> alors on rajoute \r\n à la fin de la
	 *            chaîne.
	 * @return Chaîne de texte au format NMEA.
	 */
	public static String toNMEAString (Collection<? extends Sentence> sentences, boolean endString) {
		String nmeaString = "";
		for (Sentence s : sentences) {
			nmeaString += toNMEAString(s, false);
		}
		if (endString) {
			nmeaString += "\r\n";
		}
		return nmeaString;
	}

	/**
	 * Permet de générer un timestamp NMEA au format YYYYMMDDhhmmss.zzz à partir
	 * du temps UTC de la machine.
	 * 
	 * @return Timestamp généré au temps UTC de la machine.
	 */
	public static String computeTimestamp () {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		cal.setTimeInMillis(System.currentTimeMillis());
		String timestamp = "";
		timestamp += String.format("%04d", cal.get(Calendar.YEAR));
		timestamp += String.format("%02d", cal.get(Calendar.MONTH) + 1);
		timestamp += String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		timestamp += String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
		timestamp += String.format("%02d", cal.get(Calendar.MINUTE));
		timestamp += String.format("%02d", cal.get(Calendar.SECOND));
		timestamp += "." + String.format("%03d", cal.get(Calendar.MILLISECOND));
		return timestamp;
	}
}