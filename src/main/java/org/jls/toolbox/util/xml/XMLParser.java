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

package org.jls.toolbox.util.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Wrapper de la librairie JDom permettant de parser des chaînes en XML.
 * 
 * @author LE SAUCE Julien
 * @version 1.0
 */
public class XMLParser {

    /**
     * Permet à partir d'un document XML généré par JDom de récupérer la chaîne
     * formatée en XML.
     * 
     * @param xmlDocument
     *            Fichier XML généré avec JDom.
     * @return Chaîne formatée en XML.
     */
    public static String getStream (Document xmlDocument) {
        Format format = Format.getPrettyFormat();
        format.setIndent("\t");
        XMLOutputter output = new XMLOutputter(format);
        return output.outputString(xmlDocument);
    }

    /**
     * Permet de parser un fichier XML avec JDom.
     * 
     * @param file
     *            Fichier XML à parser.
     * @return Document XML généré par JDom.
     * @throws JDOMException
     *             Si un problème au niveau du format XML est détecté, une exception
     *             est levée.
     * @throws IOException
     *             Si un problème au niveau du fichier est détecté, une exception
     *             est levée.
     */
    public static Document parseXML (File file) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        document = builder.build(file);
        return document;
    }

    /**
     * Permet de parser un fichier XML avec JDom.
     * 
     * @param url
     *            URL du fichier XML à parser.
     * @return Document XML généré par JDom.
     * @throws JDOMException
     *             Si un problème au niveau du format XML est détecté, une exception
     *             est levée.
     * @throws IOException
     *             Si un problème au niveau du fichier est détecté, une exception
     *             est levée.
     */
    public static Document parseXML (final URL url) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        document = builder.build(url.openStream());
        return document;
    }

    /**
     * Permet de parser une chaîne de texte XML avec JDom.
     * 
     * @param str
     *            Chaîne de texte XML à parser.
     * @return Document XML généré par JDom.
     * @throws JDOMException
     *             Si un problème au niveau du format XML est détecté, une exception
     *             est levée.
     * @throws IOException
     *             Si un problème au niveau du fichier est détecté, une exception
     *             est levée.
     */
    public static Document parseXML (String str) throws JDOMException, IOException {
        InputStream stream = new ByteArrayInputStream(str.getBytes());
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        document = builder.build(stream);
        return document;
    }

    /**
     * Permet d'exporter un {@link Document} vers un fichier XML.
     * 
     * @param doc
     *            Document à exporter dans un fichier.
     * @param file
     *            Fichier vers lequel exporter le fichier.
     * @throws FileNotFoundException
     *             Le fichier est créé donc on s'en fout.
     * @throws IOException
     *             Si une erreur survient pendant l'écriture, une exception est
     *             levée.
     */
    public static void exportToFile (final Document doc, final File file) throws FileNotFoundException, IOException {
        if (doc != null && file != null) {
            try (FileOutputStream os = new FileOutputStream(file)) {
                XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                out.output(doc, os);
            }
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Gets the specified attribute in the element but throws an exception if the
     * attribute does not exist.
     * 
     * @param elmt
     *            Element to look into.
     * @param attrId
     *            Attribute value to get.
     * @return The specified attribute value.
     */
    public static String getAttributeValue (final Element elmt, final String attrId) {
        String value = elmt.getAttributeValue(attrId);
        if (value != null) {
            return value;
        } else {
            throw new IllegalArgumentException(
                    "Attribute ID " + attrId + " does not exist in element " + elmt.getName());
        }
    }
}
