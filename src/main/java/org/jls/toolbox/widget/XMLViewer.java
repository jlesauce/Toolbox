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

package org.jls.toolbox.widget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.PlainDocument;

import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLStyleConstants;

/**
 * Editeur permettant d'afficher une chaîne de texte au format XML.
 * 
 * @author LE SAUCE Julien
 * @date 15 mai 2015
 */
public class XMLViewer extends JScrollPane {

	private static final long serialVersionUID = 338031754473083441L;

	private JEditorPane editor;
	private XMLEditorKit editorKit;
	private String xmlStream;

	/**
	 * Permet d'instancier l'éditeur XML.
	 * 
	 * @param xmlStream
	 *            Chaîne de texte au format XML.
	 */
	public XMLViewer (String xmlStream) {
		super();
		this.xmlStream = xmlStream;
		this.editorKit = new XMLEditorKit();
		this.editorKit.setAutoIndentation(true);
		this.editorKit.setTagCompletion(true);
		createView();
		setStyle();
	}

	/**
	 * Permet de créer les différents éléments qui composent la vue.
	 */
	private void createView () {
		this.editor = new JEditorPane();
		this.editor.setEditorKit(this.editorKit);
		this.editor.setText(this.xmlStream);
		this.editor.setEditable(false);
	}

	/**
	 * Permet, une fois les composants créés, de placer correctement les
	 * éléments qui composent la vue.
	 */
	private void setStyle () {
		setViewportView(this.editor);
		setAutoscrolls(false);

		DefaultCaret caret = (DefaultCaret) this.editor.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		this.editor.setFont(new Font("Courrier", Font.PLAIN, 13));
		this.editor.getDocument().putProperty(PlainDocument.tabSizeAttribute, 2);
		this.editor.getDocument().putProperty(XMLEditorKit.ERROR_HIGHLIGHTING_ATTRIBUTE, true);
		this.editorKit.setAutoIndentation(true);
		this.editorKit.setTagCompletion(true);

		this.editorKit.setStyle(XMLStyleConstants.ATTRIBUTE_NAME, Color.RED, Font.PLAIN);
		this.editorKit.setStyle(XMLStyleConstants.ATTRIBUTE_VALUE, Color.BLUE, Font.PLAIN);
		this.editorKit.setStyle(XMLStyleConstants.SPECIAL, Color.BLACK, Font.PLAIN);
		this.editorKit.setStyle(XMLStyleConstants.ENTITY, Color.BLACK, Font.PLAIN);
	}

	/**
	 * Permet de mettre à jour la chaîne de texte XML affichée par l'éditeur.
	 * 
	 * @param xmlString
	 *            Chaîne de texte au format XML.
	 */
	public void setText (String xmlString) {
		this.editor.setText(xmlString);
	}

	/**
	 * Permet de spécifier si la zone d'écriture est éditable ou non.
	 * 
	 * @param isEditable
	 *            <code>true</code> pour que la zone soit éditable,
	 *            <code>false</code> sinon.
	 */
	public void setEditable (boolean isEditable) {
		this.editor.setEditable(isEditable);
	}
}