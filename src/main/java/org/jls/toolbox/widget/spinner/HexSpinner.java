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

package org.jls.toolbox.widget.spinner;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * Permet de créer un {@link JSpinner} affichant des nombres au format
 * hexadécimal.
 * 
 * @author LE SAUCE Julien
 * @date Feb 24, 2015
 */
public class HexSpinner extends JSpinner {

    private static final long serialVersionUID = 1152947943927384286L;

    /**
     * Permet d'instancier un spinner hexadécimal à partir de son modèle de données.
     * 
     * @param model
     *            Modèle de données du spinner.
     */
    public HexSpinner(HexSpinnerModel model) {
        super(model);
    }

    @Override
    protected JComponent createEditor (SpinnerModel model) {
        return new HexSpinnerEditor(this);
    }

    @Override
    public HexSpinnerModel getModel () {
        return (HexSpinnerModel) super.getModel();
    }

    @Override
    public void setValue (Object value) {
        HexSpinnerModel model = getModel();
        model.setValue(value);
    }

    /**
     * Renvoie la valeur décimale stockée dans le modèle de données du spinner.
     * 
     * @return Valeur décimale du modèle de données du spinner.
     */
    public long getNumber () {
        return getModel().getNumber();
    }
}
