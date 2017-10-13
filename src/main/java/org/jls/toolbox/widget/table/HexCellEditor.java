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

package org.jls.toolbox.widget.table;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

import org.jls.toolbox.widget.format.HexFormatter;

/**
 * Permet de créer un éditeur de cellule hexadécimal implémentant
 * {@link DefaultCellEditor}.
 * 
 * @author LE SAUCE Julien
 * @date Mar 3, 2015
 */
public class HexCellEditor extends DefaultCellEditor {

    private static final long serialVersionUID = -8627558633251070164L;

    /**
     * Permet d'instancier un éditeur de cellule pour {@link JFormattedTextField}.
     */
    public HexCellEditor() {
        super(new JFormattedTextField());
    }

    @Override
    public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField editor = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected,
                row, column);
        if (value instanceof String) {
            HexFormatter formatter = new HexFormatter(0L, 0xFFFFFFFFL, false, false, false);
            editor.setFormatterFactory(new DefaultFormatterFactory(formatter));
            editor.setHorizontalAlignment(SwingConstants.CENTER);
            editor.setBorder(null);
            editor.setValue(value);
        }
        return editor;
    }

    @Override
    public boolean stopCellEditing () {
        try {
            // On récupère la valeur du champ de texte avant de stopper
            // l'édition
            getCellEditorValue();
            return super.stopCellEditing();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object getCellEditorValue () {
        // Récupération de la valeur du champ de texte
        String str = (String) super.getCellEditorValue();
        if (str == null || str.isEmpty()) {
            return null;
        }
        // On parse la valeur en hexadécimal
        if (str.matches("^[0-9a-fA-F]+$")) {
            return Long.parseLong(str, 16);
        } else {
            return null;
        }
    }
}
