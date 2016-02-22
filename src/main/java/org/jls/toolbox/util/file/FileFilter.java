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
 * Filtre permettant de filtrer des fichiers selon leur type (fichier ou
 * dossier) ainsi que selon leur nom à l'aide d'expressions régulières.
 * 
 * @author LE SAUCE Julien
 * @date Feb 24, 2015
 */
public class FileFilter implements java.io.FileFilter {

	public static final int ONLY_FILES = 1;
	public static final int ONLY_DIRECTORIES = 2;
	public static final int FILES_AND_DIRECTORIES = 3;

	private final int option;
	private final String pattern;
	private final String[] fileExts;

	/**
	 * Permet d'instancier un filtre selon le type de fichiers souhaités.
	 * 
	 * @param option
	 *            Permet de sélectionner le mode de filtrage {ONLY_FILES,
	 *            ONLY_DIRECTORIES, FILES_AND_DIRECTORIES}.
	 */
	public FileFilter (int option) {
		this(null, option);
	}

	/**
	 * Permet d'instancier un filtre selon le type de fichiers souhaités. Il est
	 * également possible de spécifier un pattern afin de filtrer les fichiers
	 * selon leur nom.
	 * 
	 * @param pattern
	 *            Permet de filtrer les fichiers par leur nom, le pattern est
	 *            alors utilisé avec la fonction {@link String#matches(String)}.
	 *            Si <code>null</code> est spécifié alors aucun filtrage selon
	 *            le nom n'est effectué.
	 * @param option
	 *            Permet de sélectionner le mode de filtrage {ONLY_FILES,
	 *            ONLY_DIRECTORIES, FILES_AND_DIRECTORIES}.
	 */
	public FileFilter (String pattern, int option) {
		super();
		this.option = option;
		this.pattern = pattern;
		this.fileExts = null;
	}

	/**
	 * Permet d'instancier un filtre selon le type de fichiers souhaités. Il est
	 * également possible de spécifier une liste d'extensions permettant de
	 * filtrer les fichiers dont le nom ne se termine pas par l'une des
	 * extensions spécifiées.
	 * 
	 * @param option
	 *            Permet de sélectionner le mode de filtrage (
	 *            {@link #ONLY_FILES}, {@link #ONLY_DIRECTORIES} ou
	 *            {@link #FILES_AND_DIRECTORIES}).
	 * @param fileExt
	 *            Liste d'extensions de fichiers permettant de filtrer les
	 *            fichiers selon leur nom, on ne gardera ainsi que les fichiers
	 *            dont le nom se termine par l'une des extensions spécifiées.Si
	 *            <code>null</code> est spécifié alors aucun filtrage selon le
	 *            nom n'est effectué.
	 *            <p>
	 *            Par exemple :
	 *            <code>FileFilter filter = new FileFilter(FILES_ONLY, "xml", "dat", "bin");</code>
	 */
	public FileFilter (int option, String... fileExt) {
		super();
		this.option = option;
		this.pattern = null;
		this.fileExts = fileExt;
	}

	@Override
	public boolean accept (File file) {
		// Filtrage selon le type de fichier
		switch (this.option) {
			case ONLY_FILES:
				if (file.isDirectory()) {
					return false;
				}
				break;
			case ONLY_DIRECTORIES:
				if (file.isFile()) {
					return false;
				}
				break;
			case FILES_AND_DIRECTORIES:
				break;
			default:
				throw new IllegalArgumentException("Invalid option : " + this.option);
		}
		// Filtrage du nom
		if (this.pattern != null) {
			String filename = file.getName();
			return filename.matches(this.pattern);
		}
		// Filtrage selon l'extension
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