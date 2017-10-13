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

package org.jls.toolbox.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class that performs operations on files and directories.
 * 
 * @author LE SAUCE Julien
 * @date Sep 3, 2015
 */
public class FileUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Delete a file or a directory, allowing recursive directory deletion. Improved
     * version of the {@link File#delete()} method.
     * 
     * @param file
     *            The file or directory to delete.
     * @param recursive
     *            <code>true</code> for a recursive deletion, <code>false</code> to
     *            only delete the specified file or directory.
     * @return <code>true</code> if the specified file or directory has been
     *         correctly deleted, <code>false</code> otherwise.
     */
    public static boolean delete (final File file, final boolean recursive) {
        if (file.exists()) {
            // If it's a file or recursive option is not selected
            if (!file.isDirectory() || !recursive) {
                return file.delete();
            }
            File[] subFiles = file.listFiles();
            // Iterates over the existing files
            for (File subFile : subFiles) {
                boolean deleted = delete(subFile, recursive);
                if (!deleted) {
                    return false;
                }
            }
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * Copies recursively the specified source folder to the specified destination.
     * If the destination does not exist then the destination directory is created.
     * 
     * @param src
     *            Source folder to copy and paste.
     * @param dst
     *            Destination folder.
     * @throws IOException
     *             If an error occurred copying the files to the specified
     *             destination.
     */
    public static void copyFolder (final File src, final File dst) throws IOException {
        // Checks input
        if (src == null) {
            throw new NullPointerException("Source cannot be null");
        }
        if (dst == null) {
            throw new NullPointerException("Destination cannot be null");
        }
        if (!src.exists()) {
            throw new FileNotFoundException("Source file not found : " + src.getAbsolutePath());
        }

        // If the source is a directory
        if (src.isDirectory()) {
            // If destination does not exist
            if (!dst.exists()) {
                createDirectory(dst);
                LOGGER.debug("Directory copied from {} to {}", src, dst);
            }
            // Lists the content
            String[] files = src.list();
            // Construct the src and dst file structure
            for (String filename : files) {
                // Construct src and dst files
                File srcFile = new File(src, filename);
                File dstFile = new File(dst, filename);
                // Recursive copy
                copyFolder(srcFile, dstFile);
            }
        } else { // src is a file
            // then copy it
            try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
                byte[] buffer = new byte[1024];
                int length;
                // Copy the content
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            LOGGER.debug("File copied from {} to {}", src, dst);
        }
    }

    /**
     * Create the specified directory (if the parent folders doesn't exist, then
     * those folders will be created).
     * 
     * @param dst
     *            The directory to create.
     * @throws FileNotFoundException
     *             If folder creation failed.
     */
    private static void createDirectory (final File dst) throws FileNotFoundException {
        if (!dst.mkdirs()) {
            throw new FileNotFoundException("Failed to create folder " + dst.getAbsolutePath());
        }
    }
}
