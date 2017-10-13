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

package org.jls.toolbox.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Permet de représenter une date avec une précision de la microseconde.
 * 
 * @author LE SAUCE Julien
 * @date Jun 5, 2015
 */
public class Timestamp {

    private final double time;
    private final java.sql.Timestamp timestamp;

    /**
     * Permet de construire un objet <code>Timestamp</code> à partir du nombre de
     * secondes écoulées depuis le temps UTC (Epoch).
     * 
     * @param time
     *            Temps écoulé en secondes depuis le temps UTC.
     */
    public Timestamp(double time) {
        this.timestamp = new java.sql.Timestamp((long) (time * 1000));
        this.time = time;
        long dateSec = (long) time;
        double diff = time - dateSec;
        long dateNanos = (long) (diff * 1000000000 + 0.5);
        this.timestamp.setNanos((int) dateNanos);
    }

    /**
     * Permet de dire si ce timestamp est plus ancien que le timestamp spécifié.
     * 
     * @param timestamp
     *            Timestamp à comparer.
     * @return <code>true</code> si le timestamp est plus ancien que le timestamp
     *         spécifié (i.e. le timestamp spécifié est le plus récent),
     *         <code>false</code> sinon.
     */
    public boolean after (Timestamp timestamp) {
        return this.timestamp.after(timestamp.getTimestamp());
    }

    /**
     * Renvoie une chaîne représentant la date complète associé à cet objet avec une
     * précision de la nanoseconde.
     * 
     * @return Chaîne représentant la date complète de cet objet.
     */
    public String toUTCString () {
        Date date = new Date(this.timestamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String str = formatter.format(date) + "." + String.format("%06d", getMicros());
        return str;
    }

    /**
     * Renvoie une chaîne représentant le jour, le mois et l'année représentés par
     * cet objet.
     * 
     * @return Chaîne au format jj/mm/aaaa.
     */
    public String toUTCDate () {
        Date date = new Date(this.timestamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    /**
     * Renvoie une chaîne représentant le temps représenté par cet objet avec une
     * précision de la nanoseconde.
     * 
     * @return Chaîne au format hh/mm/ss.SSSSSSSSS.
     */
    public String toUTCTime () {
        Date date = new Date(this.timestamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String str = formatter.format(date) + "." + String.format("%06d", getMicros());
        return str;
    }

    /**
     * Renvoie la partie fractionnelle en microsecondes du temps en secondes.
     * 
     * @return Temps fractionnel en microsecondes.
     */
    public long getMicros () {
        long dateSec = (long) this.time;
        double diff = this.time - dateSec;
        return (long) (diff * 1000000 + 0.5);
    }

    /**
     * Renvoie le temps en microsecondes.
     * 
     * @return Temps en microsecondes.
     */
    public long getMicroTime () {
        long milliTime = this.timestamp.getTime();
        long secTime = milliTime / 1000;
        long microTime = secTime * 1000000 + (long) (this.timestamp.getNanos() / 1000.0 + 0.5);
        return microTime;
    }

    /**
     * Renvoie le timestamp de cet objet.
     * 
     * @return Timestamp de cet objet.
     */
    private java.sql.Timestamp getTimestamp () {
        return this.timestamp;
    }
}
