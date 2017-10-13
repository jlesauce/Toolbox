/*
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
 */

package org.jls.toolbox.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Permet d'étendre la classe {@link java.io.File} afin que la fonction
 * {@link SimpleFile#toString()} renvoie le nom du fichier sans l'extension
 * uniquement.
 * 
 * @author LE SAUCE Julien
 * @date Jun 18, 2015
 */
public class SimpleFile extends File {

    private static final long serialVersionUID = 296195692713196365L;

    /**
     * Permet d'instancier un nouveau fichier à partir de son chemin.
     * 
     * @param path
     *            Chemin du fichier.
     */
    public SimpleFile(final String path) {
        super(path);
    }

    /**
     * Permet de lire le fichier en tant que chaîne de texte.
     * 
     * @return Chaîne de texte représentant le contenu du fichier.
     * @throws IOException
     *             Si une erreur survient lors de la lecture du fichier.
     */
    public String readFile () throws IOException {
        return new String(Files.readAllBytes(Paths.get(getPath())));
    }

    @Override
    public String toString () {
        return this.getName();
    }
}
