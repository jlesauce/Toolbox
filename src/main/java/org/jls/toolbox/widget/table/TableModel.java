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

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Permet de créer un modèle de données pour {@link JTable} générique.
 * 
 * @author LE SAUCE Julien
 * @date Mar 17, 2015
 */
public class TableModel extends AbstractTableModel {

    private static final long serialVersionUID = -4790720129574976653L;

    private final String[] columns;
    private Object[][] data;
    private boolean isTableEditable;

    /**
     * Permet d'instancier un modèle de données.
     * 
     * @param columns
     *            Liste des noms des colonnes.
     * @param data
     *            Tableau contenant les données de la table.
     */
    public TableModel(final String[] columns, final Object[][] data) {
        super();
        this.columns = columns;
        this.data = data;
        this.isTableEditable = true;
    }

    @Override
    public int getRowCount () {
        return this.data != null ? this.data.length : 0;
    }

    @Override
    public int getColumnCount () {
        return this.columns != null ? this.columns.length : 0;
    }

    @Override
    public Object getValueAt (int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            return this.data[rowIndex][columnIndex];
        }
        return null;
    }

    @Override
    public String getColumnName (int column) {
        if (column >= 0 && column < getColumnCount()) {
            return this.columns[column];
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass (int columnIndex) {
        if (columnIndex >= 0 && columnIndex < getColumnCount()) {
            return this.columns[columnIndex].getClass();
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable (int rowIndex, int columnIndex) {
        if (!this.isTableEditable) {
            return false;
        }
        if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            this.data[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        } else {
            throw new IndexOutOfBoundsException(
                    "Index out of data table : row=" + rowIndex + ", column=" + columnIndex);
        }
    }

    /**
     * Permet de mettre à jour toutes les données de la table.
     * 
     * @param newData
     *            Nouvelle données.
     */
    public void setData (final Object[][] newData) {
        this.data = newData;
        fireTableDataChanged();
    }

    /**
     * Renvoie les données contenues dans la table.
     * 
     * @return Données contenues dans la table.
     */
    public Object[][] getData () {
        return this.data;
    }

    /**
     * Indique si la table est éditable ou non.
     * 
     * @return <code>true</code> si la table est éditable, <code>false</code> sinon.
     */
    public boolean isTableEditable () {
        return this.isTableEditable;
    }

    /**
     * Permet de spécifier si la table est éditable ou non (surpasse l'appel à la
     * méthode {@link TableModel#isCellEditable(int, int)}).
     * 
     * @param isTableEditable
     *            <code>true</code> pour la table soit éditable, <code>false</code>
     *            sinon.
     */
    public void setTableEditable (boolean isTableEditable) {
        this.isTableEditable = isTableEditable;
    }
}
