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

package org.jls.toolbox.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Permet d'exporter les données d'une {@link JTable} ou d'un {@link TableModel}
 * dans un fichier.
 * 
 * @author LE SAUCE Julien
 * @date Mar 12, 2015
 */
public class TableExporter {

	/**
	 * Permet d'exporter les données contenues dans le {@link TableModel} de la
	 * {@link JTable} spécifiée, dans le fichier spécifié.
	 * 
	 * @param table
	 *            Table contenant les données à exporter.
	 * @param file
	 *            Fichier vers lequel exporter les données.
	 * @throws IOException
	 *             Si une erreur survient lors de l'écriture des données dans le
	 *             fichier.
	 */
	public static void toFile (final JTable table, final File file) throws IOException {
		if (table == null) {
			throw new NullPointerException("Table cannot be null");
		}
		if (file == null) {
			throw new NullPointerException("File cannot be null");
		}
		// Export des données
		try (FileWriter writer = new FileWriter(file)) {
			TableModel model = table.getModel();
			// Ecriture des entêtes
			for (int col = 0; col < model.getColumnCount(); col++) {
				writer.write(model.getColumnName(col) + "\t");
			}
			writer.write("\n");
			// Ecriture des données
			for (int row = 0; row < model.getRowCount(); row++) {
				for (int col = 0; col < model.getColumnCount(); col++) {
					writer.write(model.getValueAt(row, col).toString() + "\t");
				}
				writer.write("\n");
			}
		} catch (IOException e) {
			throw e;
		}
	}
}