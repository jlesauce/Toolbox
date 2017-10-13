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

package org.jls.toolbox.widget;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * {@link FileChooser} fournit un mécanisme simple permettant à l'utilisateur de
 * sélectionner un fichier que ce soit en lecture ou en écriture et, en fonction
 * du choix, d'afficher différents messages d'avertissement s'il y a lieu (comme
 * par exemple en cas d'écrire d'un fichier qui existe déjà).
 * 
 * @see JFileChooser
 */
public class FileChooser extends JFileChooser {

    private static final long serialVersionUID = -1512438428145987687L;

    private String[] formats;

    /**
     * Permet d'instancier un {@link FileChooser} en spécifiant le dossier parent.
     * 
     * @param parentDir
     *            Dossier parent.
     */
    public FileChooser(final File parentDir) {
        this(parentDir, (String[]) null);
    }

    /**
     * Permet d'instancier un {@link FileChooser} en spécifiant le dossier parent.
     * 
     * @param parentPath
     *            Chemin du dossier parent.
     */
    public FileChooser(final String parentPath) {
        this(parentPath, (String[]) null);
    }

    /**
     * Permet d'instancier un {@link FileChooser} en spécifiant le dossier parent
     * ainsi que la liste des formats autorisés.
     * 
     * @param parentDir
     *            Dossier parent.
     * @param formats
     *            Liste des extensions de fichier autorisées.
     *            <p>
     *            Par exemple :
     *            <code>FileChooser fc = new FileChooser(file, "bin", "dat", "txt");</code>
     */
    public FileChooser(final File parentDir, String... formats) {
        this(parentDir.getAbsolutePath(), formats);
    }

    /**
     * Permet d'instancier un {@link FileChooser} en spécifiant le dossier parent
     * ainsi que la liste des formats autorisés.
     * 
     * @param parentPath
     *            Chemin du dossier parent.
     * @param formats
     *            Liste des extensions de fichier autorisées.
     *            <p>
     *            Par exemple :
     *            <code>FileChooser fc = new FileChooser(file, "bin", "dat", "txt");</code>
     */
    public FileChooser(final String parentPath, String... formats) {
        super(parentPath);
        this.formats = formats;
    }

    @Override
    public void approveSelection () {
        File file = getSelectedFile();

        if (SAVE_DIALOG == getDialogType()) {
            if (this.formats != null && this.formats.length > 0) {
                boolean valid = true;
                // On balaye les formats acceptés
                for (String ext : this.formats) {
                    // Si l'extension n'est pas la liste
                    if (!file.getName().endsWith(ext)) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    String acceptedFormats = "";
                    // Création de la liste des formats acceptés
                    for (String ext : this.formats) {
                        acceptedFormats += ", " + ext;
                    }
                    // On supprime la 1ère virgule
                    acceptedFormats = acceptedFormats.substring(2);
                    JOptionPane.showMessageDialog(this,
                            "Invalid file extension type. Accepted extension(s) :\n\n" + acceptedFormats,
                            "File type error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            // Si le fichier sélectionné existe
            if (file.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "The selected file exists, overwrite it ?",
                        "Overwrite File", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        super.cancelSelection();
                        return;
                    default:
                        throw new IllegalArgumentException("Invalid argument : " + result);
                }
            } else { // Sinon on s'en fout
                super.approveSelection();
            }
        } else if (OPEN_DIALOG == getDialogType()) {
            // Vérification du format
            if (this.formats != null && this.formats.length > 0) {
                boolean valid = true;
                // On balaye les formats acceptés
                for (String ext : this.formats) {
                    // Si l'extension n'est pas la liste
                    if (!file.getName().endsWith(ext)) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    String acceptedFormats = "";
                    // Création de la liste des formats acceptés
                    for (String ext : this.formats) {
                        acceptedFormats += ", " + ext;
                    }
                    // On supprime la 1ère virgule
                    acceptedFormats = acceptedFormats.substring(2);
                    JOptionPane.showMessageDialog(this,
                            "Invalid file extension type. Accepted extension(s) :\n\n" + acceptedFormats,
                            "File type error", JOptionPane.ERROR_MESSAGE);
                } else {
                    super.approveSelection();
                }
            } else {
                super.approveSelection();
            }
        }
    }

    /**
     * Permet d'afficher un dialogue "Save File" standard mais en présélectionnant
     * le fichier.
     * 
     * @param filename
     *            Nom du fichier présélectionné.
     * @param parent
     *            Composant parent à partir duquel afficher la fenêtre de dialogue.
     * @return Renvoie l'état du {@link FileChooser} (voir
     *         {@link JFileChooser#showSaveDialog(Component)} ).
     * @throws HeadlessException
     *             Si GraphicsEnvironment.isHeadless() renvoie <code>true</code> .
     */
    public int showSaveDialog (final String filename, Component parent) throws HeadlessException {
        File preFile = new File(getCurrentDirectory().getAbsolutePath() + File.separator + filename);
        setSelectedFile(preFile);
        return super.showSaveDialog(parent);
    }
}
