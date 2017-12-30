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

package org.jls.toolbox.widget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import net.miginfocom.swing.MigLayout;

/**
 * Custom textpane console that prints user text with different colors, styles
 * and sizes.
 * 
 * @author LE SAUCE Julien
 * @date Dec 11, 2015
 */
public class Console extends JPanel {

    private static final long serialVersionUID = -6444240489722156370L;

    private static final int DEFAULT_DOCUMENT_LENGTH = 100000;

    public static final Color defaultTextColor = Color.black;
    public static final Color defaultTextBackgroundColor = Color.white;
    public static final int defaultFontStyle = Font.PLAIN;
    public static final int defaultTextSize = new JLabel().getFont().getSize();

    private Color textColor;
    private Color textBgColor;
    private Font font;
    private int style;
    private int size;

    private JScrollPane scrollpane;
    private JTextPane textPane;

    /**
     * Instantiates a new default console.
     */
    public Console() {
        super();
        this.textColor = defaultTextColor;
        this.textBgColor = defaultTextBackgroundColor;
        this.font = null;
        this.style = defaultFontStyle;
        this.size = defaultTextSize;
        createGui();
    }

    /**
     * Prints a simple text string.
     * 
     * @param text
     *            The text to print.
     */
    public void print (final String text) {
        printConsole(text, this.textColor, this.textBgColor, this.style, this.size);
    }

    /**
     * Prints a simple text string specifying its font style.
     * 
     * @param text
     *            The text to print.
     * @param fontStyle
     *            The text font style, see {@link Font}.
     */
    public void print (final String text, int fontStyle) {
        printConsole(text, this.textColor, this.textBgColor, fontStyle, this.size);
    }

    /**
     * Prints a simple text string specifying its font style and size.
     * 
     * @param text
     *            The text to print.
     * @param fontStyle
     *            The font style, see {@link Font}.
     * @param size
     *            Size of the text font.
     */
    public void print (final String text, int fontStyle, int size) {
        printConsole(text, this.textColor, this.textBgColor, fontStyle, size);
    }

    /**
     * Prints a simple text string specifying its color.
     * 
     * @param text
     *            The text to print.
     * @param textColor
     *            Color of the text.
     */
    public void print (final String text, final Color textColor) {
        printConsole(text, textColor, this.textBgColor, this.style, this.size);
    }

    /**
     * Prints a simple text string specifying its color and font style.
     * 
     * @param text
     *            The text to print.
     * @param textColor
     *            Color of the text.
     * @param fontStyle
     *            The font style, see {@link Font}.
     */
    public void print (final String text, final Color textColor, int fontStyle) {
        printConsole(text, textColor, this.textBgColor, fontStyle, this.size);
    }

    /**
     * Prints a simple text string specifying its color, font style and size.
     * 
     * @param text
     *            The text to print.
     * @param textColor
     *            Color of the text.
     * @param fontStyle
     *            The font style, see {@link Font}.
     * @param size
     *            Size of the text font.
     */
    public void print (final String text, final Color textColor, int fontStyle, int size) {
        printConsole(text, textColor, this.textBgColor, fontStyle, size);
    }

    /**
     * Prints a simple text string specifying its color, font style and background
     * color.
     * 
     * @param text
     *            The text to print.
     * @param textColor
     *            Color of the text.
     * @param bgColor
     *            Text's background color.
     * @param fontStyle
     *            The font style, see {@link Font}.
     */
    public void print (final String text, final Color textColor, final Color bgColor, int fontStyle) {
        printConsole(text, textColor, bgColor, fontStyle, this.size);
    }

    /**
     * Prints a simple text string specifying its color, font style, size and
     * background color.
     * 
     * @param text
     *            The text to print.
     * @param textColor
     *            Color of the text.
     * @param bgColor
     *            Text's background color.
     * @param fontStyle
     *            The font style, see {@link Font}.
     * @param size
     *            Size of the text font.
     */
    public void print (final String text, final Color textColor, final Color bgColor, int fontStyle, int size) {
        printConsole(text, textColor, bgColor, fontStyle, size);
    }

    /**
     * Clears the console.
     */
    public void clear () {
        this.textPane.setText("");
    }

    /**
     * Creates the Graphical User Interface.
     */
    private void createGui () {
        this.textPane = new JTextPane(new Document(DEFAULT_DOCUMENT_LENGTH));
        this.textPane.setEditable(false);
        this.scrollpane = new JScrollPane(this.textPane);
        setLayout(new MigLayout("fill, insets 0 0 0 0"));
        add(this.scrollpane, "grow");
    }

    /**
     * Adds the specified text to the console and specifies its foreground and
     * background color, its font style and size.
     * 
     * @param text
     *            The text to print.
     * @param textColor
     *            Color of the text.
     * @param bgColor
     *            Text's background color.
     * @param fontStyle
     *            The font style, see {@link Font}.
     * @param size
     *            Size of the text font.
     */
    private synchronized void printConsole (String text, Color textColor, Color bgColor, int fontStyle, int size) {
        Document doc = (Document) this.textPane.getStyledDocument();
        StyleContext sc = StyleContext.getDefaultStyleContext();
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        Style mainStyle = sc.addStyle("MainStyle", defaultStyle);
        StyleConstants.setForeground(mainStyle, textColor);
        StyleConstants.setBackground(mainStyle, bgColor);
        StyleConstants.setFontSize(mainStyle, size);
        if (fontStyle == Font.BOLD) {
            StyleConstants.setBold(mainStyle, true);
            StyleConstants.setItalic(mainStyle, false);
        } else if (fontStyle == Font.ITALIC) {
            StyleConstants.setItalic(mainStyle, true);
            StyleConstants.setBold(mainStyle, false);
        } else if (fontStyle == Font.PLAIN) {
            StyleConstants.setBold(mainStyle, false);
            StyleConstants.setItalic(mainStyle, false);
        }
        // Adds the text at the end of the current text buffer
        try {
            doc.insertString(doc.getLength(), text, mainStyle);
            this.textPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Color getBackground () {
        return this.textPane == null ? null : this.textPane.getBackground();
    }

    @Override
    public void setBackground (Color bg) {
        if (this.textPane != null) {
            // Overrides Nimbus L&F properties
            UIDefaults uiDef = new UIDefaults();
            uiDef.put("TextPane[Enabled].backgroundPainter", bg);
            this.textPane.putClientProperty("Nimbus.Overrides", uiDef);
            this.textPane.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
            // Changes the background color
            super.setBackground(bg);
            this.textPane.setBackground(bg);
        }
    }

    @Override
    public Font getFont () {
        return font;
    }

    @Override
    public void setFont (Font font) {
        if (font != null && this.textPane != null) {
            this.font = font;
            this.size = font.getSize();
            this.style = font.getStyle();
            this.textPane.setFont(font);
        }
    }

    /**
     * Returns the {@link Document} used by this console.
     * 
     * @return The {@link Document} used by this console.
     */
    public Document getDocument () {
        return (Document) this.textPane.getDocument();
    }

    public Color getDefaultTextColor () {
        return this.textColor;
    }

    public void setDefaultTextColor (Color color) {
        this.textColor = color;
    }

    public Color getDefaultTextBackgroundColor () {
        return this.textBgColor;
    }

    public void setDefaultTextBackgroundColor (Color color) {
        this.textBgColor = color;
    }

    public int getDefaultStyle () {
        return this.style;
    }

    public void setDefaultStyle (int style) {
        this.style = style;
    }

    public int getDefaultSize () {
        return this.size;
    }

    public void setDefaultSize (int size) {
        this.size = size;
    }
}
