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

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * Permet d'étendre une {@link JTable} et d'y apporter certaines fonctionnalités
 * supplémentaires.
 * 
 * @author LE SAUCE Julien
 * @date 26 mai 2015
 */
public class Table extends JTable {

    private static final long serialVersionUID = -4174786911861251037L;

    public Table() {
        super();
    }

    public Table(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public Table(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    public Table(TableModel dm) {
        super(dm);
    }

    @Override
    public Dimension getPreferredSize () {
        if (getParent() instanceof JViewport) {
            if (((JViewport) getParent()).getWidth() > super.getPreferredSize().width) {
                return getMinimumSize();
            }
        }
        return super.getPreferredSize();
    }

    @Override
    public boolean getScrollableTracksViewportWidth () {
        if (this.autoResizeMode != AUTO_RESIZE_OFF) {
            if (getParent() instanceof JViewport) {
                return (((JViewport) getParent()).getWidth() > getPreferredSize().width);
            }
            return true;
        }
        return false;
    }
}
