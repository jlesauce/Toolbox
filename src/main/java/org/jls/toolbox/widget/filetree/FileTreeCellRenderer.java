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

package org.jls.toolbox.widget.filetree;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -6640854181102423787L;

    private final FileSystemView fileSystemView;
    private final JLabel label;

    FileTreeCellRenderer() {
        fileSystemView = FileSystemView.getFileSystemView();
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent (final JTree tree,
                                                   final Object value,
                                                   final boolean selected,
                                                   final boolean expanded,
                                                   final boolean leaf,
                                                   final int row,
                                                   final boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        File file = (File) node.getUserObject();

        label.setIcon(fileSystemView.getSystemIcon(file));
        label.setText(fileSystemView.getSystemDisplayName(file));
        label.setToolTipText(file.getPath());

        setLabelColors(selected);

        return label;
    }

    void setLabelColors (final boolean isSelected) {
        if (isSelected) {
            setLabelColorsIfSelected();
        } else {
            setLabelColorsIfNotSelected();
        }
    }

    void setLabelColorsIfSelected () {
        label.setBackground(backgroundSelectionColor);
        label.setForeground(textSelectionColor);
    }

    void setLabelColorsIfNotSelected () {
        label.setBackground(backgroundNonSelectionColor);
        label.setForeground(textNonSelectionColor);
    }
}
