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

package org.jls.toolbox.util.file;

import java.io.File;

/**
 * File filter class allowing to filter files using their type (file or folder)
 * and their names using regular expressions.
 *
 * @author Julien LE SAUCE
 */
public class FileFilter implements java.io.FileFilter {

    public static final int ONLY_FILES = 1;
    public static final int ONLY_FOLDERS = 2;
    public static final int FILES_AND_FOLDERS = 3;

    private final int option;
    private final String pattern;
    private final String[] fileExts;

    /**
     * Create a new filter by file type.
     *
     * @param option
     *            Filter mode {ONLY_FILES, ONLY_DIRECTORIES, FILES_AND_DIRECTORIES}.
     */
    public FileFilter(final int option) {
        this(null, option);
    }

    /**
     * Create a new filter by file type and name.
     *
     * @param pattern
     *            Pattern used to filter filenames using
     *            {@link String#matches(String)}. If <code>null</code> specified,
     *            the pattern won't be applied.
     * @param option
     *            Filter mode {ONLY_FILES, ONLY_DIRECTORIES, FILES_AND_DIRECTORIES}.
     */
    public FileFilter(final String pattern, final int option) {
        super();
        this.option = option;
        this.pattern = pattern;
        this.fileExts = null;
    }

    /**
     * Create a new filter by file type and file extension.
     *
     * @param option
     *            Filter mode {ONLY_FILES, ONLY_DIRECTORIES, FILES_AND_DIRECTORIES}.
     * @param fileExt
     *            List of allowed file extensions. If <code>null</code> specified,
     *            pattern won't be applied.
     *            <p>
     *            For example:
     *            <code>FileFilter filter = new FileFilter(FILES_ONLY, "xml", "dat", "bin");</code>
     *            </p>
     */
    public FileFilter(final int option, final String... fileExt) {
        super();
        this.option = option;
        this.pattern = null;
        this.fileExts = fileExt;
    }

    @Override
    public boolean accept (final File file) {
        if (!isFileTypeAccepted(file, this.option)) {
            return false;
        }
        if (!isFileMatchPattern(file)) {
            return false;
        }
        if (!isFileExtensionAccepted(file)) {
            return false;
        }
        return true;
    }

    private boolean isFileTypeAccepted (final File file, final int option) {
        switch (option) {
            case ONLY_FILES:
                if (file.isDirectory()) {
                    return false;
                }
                break;
            case ONLY_FOLDERS:
                if (file.isFile()) {
                    return false;
                }
                break;
            case FILES_AND_FOLDERS:
                return true;
            default:
                throw new IllegalArgumentException("Invalid option: " + this.option);
        }
        return true;
    }

    private boolean isFileMatchPattern (final File file) {
        if (this.pattern != null) {
            String filename = file.getName();
            return filename.matches(this.pattern);
        }
        return true;
    }

    private boolean isFileExtensionAccepted (final File file) {
        if (this.fileExts != null) {
            String filename = file.getName();
            for (String ext : this.fileExts) {
                if (filename.endsWith(ext)) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}