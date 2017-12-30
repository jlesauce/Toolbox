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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Observable;

import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jls.toolbox.gui.AbstractView;

public class FileTree extends AbstractView {

    private static final long serialVersionUID = -9128264990568472454L;

    private final FileSystemView fileSystemView;
    private Tree tree;
    private JScrollPane treeScroll;

    public FileTree() {
        super();
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public void update (final Observable observable, final Object object) {
    }

    public void addTreeSelectionListener (final TreeSelectionListener listener) {
        tree.addTreeSelectionListener(listener);
    };

    public void showRootFile () {
        if (tree != null) {
            tree.setSelectionInterval(0, 0);
        }
    }

    @Override
    protected void createGuiComponents () {
        createTree();
        createTreeScroll();
    }

    @Override
    protected void addGuiComponents () {
        add(treeScroll, BorderLayout.CENTER);
    }

    @Override
    protected void addGuiListeners () {
        tree.addTreeSelectionListener(new FileTreeSelectionListener());
    }

    private void createTree () {
        tree = new Tree(showSystemRootFiles());
        tree.setVisibleRowCount(15);
    }

    private void createTreeScroll () {
        treeScroll = new JScrollPane(tree);

        Dimension preferredSize = treeScroll.getPreferredSize();
        Dimension widePreferred = new Dimension(200, (int) preferredSize.getHeight());
        treeScroll.setPreferredSize(widePreferred);
    }

    private DefaultMutableTreeNode showSystemRootFiles () {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add(node);

            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }
        return root;
    }

    private void showChildren (final DefaultMutableTreeNode node) {
        ShowChildrenWorker worker = new ShowChildrenWorker(fileSystemView, tree);
        worker.setNode(node);
        worker.execute();
    }

    private class FileTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged (final TreeSelectionEvent event) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
            showChildren(node);
        }
    }
}
