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

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jls.toolbox.util.file.SimpleFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SimpleFileTest {

    private final TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File fakeFile;

    @Before
    public void setUp () throws IOException {
        temporaryFolder.create();
        fakeFile = temporaryFolder.newFile("filename.txt");
    }

    @After
    public void tearDown () {
        fakeFile.delete();
        temporaryFolder.delete();
    }

    @Test
    public void GivenAFileWithContentWhenReadingContentThenReadContentShouldBeEqualToActualContent ()
            throws IOException {
        SimpleFile simpleFile = new SimpleFile(fakeFile);
        String content = "This is the content of the file\non several lines";
        writeContentToFile(simpleFile, content);

        String readContent = simpleFile.readFileContentAsString();

        assertEquals(readContent, content);
    }

    @Test
    public void GivenAFileWhenPrintingNameThenOnlyShortNameIsPrinted () {
        SimpleFile simpleFile = new SimpleFile(fakeFile);

        String name = simpleFile.toString();

        assertEquals(name, "filename.txt");
    }

    private void writeContentToFile (final File file, final String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }
}
