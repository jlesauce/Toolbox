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

package org.awax.toolbox.util.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.jls.toolbox.util.file.FileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TestFileFilter {

    private final TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File fakeFile;
    private File fakeFolder;

    @Before
    public void setUp () throws IOException {
        temporaryFolder.create();
        fakeFile = temporaryFolder.newFile("filename.txt");
        fakeFolder = temporaryFolder.newFolder("folder");
    }

    @After
    public void tearDown () {
        fakeFolder.delete();
        fakeFile.delete();
        temporaryFolder.delete();
    }

    @Test
    public void GivenAFileAndFilterSetToFilesOnlyThenFileShouldBeAccepted () {
        FileFilter fileFilter = new FileFilter(FileFilter.ONLY_FILES);

        boolean isAccepted = fileFilter.accept(fakeFile);

        verifyFileIsAcceptedByFilter(isAccepted);
    }

    @Test
    public void GivenAFolderAndFilterSetToFilesOnlyThenFileShouldBeRejected () {
        FileFilter fileFilter = new FileFilter(FileFilter.ONLY_FILES);

        boolean isAccepted = fileFilter.accept(fakeFolder);

        verifyFolderRejectedByFilter(isAccepted);
    }

    @Test
    public void GivenAFileAndFilterSetToDirectoriesOnlyThenFileShouldBeRejected () {
        FileFilter fileFilter = new FileFilter(FileFilter.ONLY_FOLDERS);

        boolean isAccepted = fileFilter.accept(fakeFile);

        verifyFileIsRejectedByFilter(isAccepted);
    }

    @Test
    public void GivenAFolderAndFilterSetToDirectoriesOnlyThenFileShouldBeAccepted () {
        FileFilter fileFilter = new FileFilter(FileFilter.ONLY_FOLDERS);

        boolean isAccepted = fileFilter.accept(fakeFolder);

        verifyFolderAcceptedByFilter(isAccepted);
    }

    @Test
    public void GivenFileAndFolderAndFilterSetToFilesAndDirectoriesThenFilterShouldAcceptBoth () {
        FileFilter fileFilter = new FileFilter(FileFilter.FILES_AND_FOLDERS);

        boolean isFileAccepted = fileFilter.accept(fakeFile);
        boolean isFolderAccepted = fileFilter.accept(fakeFolder);

        verifyFileIsAcceptedByFilter(isFileAccepted);
        verifyFolderAcceptedByFilter(isFolderAccepted);
    }

    @Test
    public void GivenFileWithNameWhenFilenameDoesNotMatchPatternThenFileShouldBeRejected () {
        FileFilter fileFilter = new FileFilter("wrongPattern", FileFilter.FILES_AND_FOLDERS);

        boolean isFileAccepted = fileFilter.accept(fakeFile);

        verifyFileIsRejectedByFilter(isFileAccepted);
    }

    @Test
    public void GivenFileWithNameWhenFilenameMatchPatternThenFileShouldBeAccepted () {
        FileFilter fileFilter = new FileFilter(".*", FileFilter.FILES_AND_FOLDERS);

        boolean isFileAccepted = fileFilter.accept(fakeFile);

        verifyFileIsAcceptedByFilter(isFileAccepted);
    }

    @Test
    public void GivenFileWithExtensionWhenExtensionIsListedThenFileShouldBeAccepted () {
        FileFilter fileFilter = new FileFilter(FileFilter.FILES_AND_FOLDERS, "txt", "png");

        boolean isFileAccepted = fileFilter.accept(fakeFile);

        verifyFileIsAcceptedByFilter(isFileAccepted);
    }

    @Test
    public void GivenFileWithExtensionWhenExtensionIsNotListedThenFileShouldBeRejected () {
        FileFilter fileFilter = new FileFilter(FileFilter.FILES_AND_FOLDERS, "jpg", "png");

        boolean isFileAccepted = fileFilter.accept(fakeFile);

        verifyFileIsRejectedByFilter(isFileAccepted);
    }

    private void verifyFileIsAcceptedByFilter (final boolean isAccepted) {
        assertTrue("file exists", fakeFile.exists());
        assertFalse("file is not folder", fakeFile.isDirectory());
        assertTrue("filter accept file", isAccepted);
    }

    private void verifyFileIsRejectedByFilter (final boolean isAccepted) {
        assertTrue("file exists", fakeFile.exists());
        assertFalse("file is not folder", fakeFile.isDirectory());
        assertFalse("filter reject file", isAccepted);
    }

    private void verifyFolderRejectedByFilter (final boolean isAccepted) {
        assertTrue("file exists", fakeFolder.exists());
        assertTrue("file is folder", fakeFolder.isDirectory());
        assertFalse("filter reject folder", isAccepted);
    }

    private void verifyFolderAcceptedByFilter (final boolean isAccepted) {
        assertTrue("file exists", fakeFolder.exists());
        assertTrue("file is folder", fakeFolder.isDirectory());
        assertTrue("filter accept folder", isAccepted);
    }
}
