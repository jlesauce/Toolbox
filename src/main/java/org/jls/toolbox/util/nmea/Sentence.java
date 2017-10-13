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

package org.jls.toolbox.util.nmea;

import java.util.ArrayList;

/**
 * Permet de représenter une chaîne NMEA.
 * 
 * @author LE SAUCE Julien
 * @date Feb 12, 2015
 */
public class Sentence {

    private final String dataType;
    private final ArrayList<String> fields;
    private long checkSum;

    /**
     * Permet de créer une chaîne NMEA à partir de son identifiant.
     * 
     * @param dataType
     *            Identifiant de la chaîne.
     */
    public Sentence(String dataType) {
        this.dataType = dataType;
        this.fields = new ArrayList<>();
        this.checkSum = 0;
    }

    /**
     * Permet d'ajouter une donnée.
     * 
     * @param value
     *            Valeur à ajouter.
     */
    public void put (String value) {
        this.fields.add(value);
    }

    /**
     * Permet de calculer le checksum de cette chaîne NMEA. Le checksum est calculé
     * sur la chaîne de texte NMEA en omettant les caractères $ et *. Si on a la
     * chaîne $GPA,a,b,c,1,2,3*0 alors le checksum sera calculé sur la chaîne
     * GPA,a,b,c,1,2,3. Le checksum est calculé en effectuant un XOR caractère par
     * caractère sur la chaîne obtenue.
     * 
     * @return Valeur du checksum obtenu sur cette chaîne NMEA.
     */
    public long computeCheckSum () {
        long checksum = 0;
        // Création de la chaîne sans les $ et *
        String str = this.dataType;
        for (String value : this.fields) {
            str += "," + value;
        }
        for (char c : str.toCharArray()) {
            checksum = checksum ^ c;
        }
        return checksum;
    }

    /**
     * Permet de vérifier que le checksum fourni avec la chaîne NMEA est correct.
     * Pour cela on recalcule le checksum avec les données stockées puis on compare
     * les deux checksums.
     * 
     * @return <code>true</code> si le checksum recalculé correspond au checksum
     *         fourni avec la chaîne NMEA, <code>false</code> sinon.
     */
    public boolean isValidChecksum () {
        long newChecksum = computeCheckSum();
        return newChecksum == this.checkSum ? true : false;
    }

    public String getDataType () {
        return this.dataType;
    }

    public ArrayList<String> getFields () {
        return this.fields;
    }

    public long getCheckSum () {
        return this.checkSum;
    }

    public void setCheckSum (long checkSum) {
        this.checkSum = checkSum;
    }
}
