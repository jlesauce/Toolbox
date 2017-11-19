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

import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

public class ShowChildrenWorker extends SwingWorker<Void, File> {

    private final FileSystemView fileSystemView;
    private final Tree tree;
    private DefaultMutableTreeNode node;

    public ShowChildrenWorker(final FileSystemView fileSystemView, final Tree tree) {
        super();
        this.fileSystemView = fileSystemView;
        this.tree = tree;
    }

    @Override
    protected Void doInBackground () throws Exception {
        tree.setEnabled(false);

        File file = (File) node.getUserObject();
        if (file.isDirectory()) {
            File[] files = fileSystemView.getFiles(file, true);

            if (node.isLeaf()) {
                for (File child : files) {
                    if (child.isDirectory()) {
                        publish(child);
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void process (final List<File> chunks) {
        for (File child : chunks) {
            node.add(new DefaultMutableTreeNode(child));
        }
    }

    @Override
    protected void done () {
        tree.setEnabled(true);
    }

    public void setNode (final DefaultMutableTreeNode node) {
        this.node = node;
    }
}
