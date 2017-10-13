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

package org.jls.toolbox.widget;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jls.toolbox.widget.spinner.WrappingNumberModel;

import net.miginfocom.swing.MigLayout;

/**
 * Panneau permettant à l'utilisateur de sélectionner une date complète ainsi
 * qu'une heure de récupérer cette date sous forme d'une instance de
 * {@link Date}.
 * 
 * @author LE SAUCE Julien
 * @date Mar 31, 2015
 */
public class TimePicker implements ChangeListener, ActionListener {

    private final Calendar calendar;
    private boolean update;

    private JPanel panel;
    private JLabel lblTime;
    private JSpinner spDay;
    private JSpinner spMonth;
    private JSpinner spYear;
    private JSpinner spHour;
    private JSpinner spMin;
    private JSpinner spSec;
    private JSpinner spTimeSec;
    private JButton btnNow;

    /**
     * Permet d'instancier un panneau de sélection de la date et de l'heure
     * initialisé au temps UTC.
     */
    public TimePicker() {
        this(new Date(0L));
    }

    /**
     * Permet d'instancier un panneau de sélection de la date et de l'heure
     * initialisé au temps spécifié.
     * 
     * @param date
     *            Date affichée à la création de la fenêtre.
     */
    public TimePicker(final Date date) {
        this.calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
        this.update = true;
        createView();
        setStyle();
        setOnListeners();
        setDate(date);
    }

    /**
     * Permet d'afficher la fenêtre de sélection de la date et de l'heure.
     * 
     * @param parent
     *            Permet de spécifier le composant à partir duquel sera affichée la
     *            fenêtre.
     * @return Entier indiquant quelle option a été sélectionnée par l'utilisateur
     *         ({@link JOptionPane#OK_OPTION} ou {@link JOptionPane#CANCEL_OPTION}).
     */
    public int showGui (final Component parent) {
        return JOptionPane.showConfirmDialog(parent, this.panel, "Time Picker", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Permet de créer les différents éléments qui composent la vue.
     */
    private void createView () {
        this.lblTime = new JLabel("Time (s) :");
        this.spDay = new JSpinner(new WrappingNumberModel(1, 1, 31, 1));
        this.spMonth = new JSpinner(new WrappingNumberModel(1, 1, 12, 1));
        this.spYear = new JSpinner(new WrappingNumberModel(1970, 1970, 9999, 1));
        this.spHour = new JSpinner(new WrappingNumberModel(0, 0, 23, 1));
        this.spMin = new JSpinner(new WrappingNumberModel(0, 0, 59, 1));
        this.spSec = new JSpinner(new WrappingNumberModel(0, 0, 59, 1));
        this.spTimeSec = new JSpinner(new WrappingNumberModel(0l, 0l, null, 1l));
        this.btnNow = new JButton("Now");

        NumberEditor editorDay = new NumberEditor(this.spDay, "00");
        NumberEditor editorMonth = new NumberEditor(this.spMonth, "00");
        NumberEditor editorYear = new NumberEditor(this.spYear, "0000");
        NumberEditor editorHour = new NumberEditor(this.spHour, "00");
        NumberEditor editorMin = new NumberEditor(this.spMin, "00");
        NumberEditor editorSec = new NumberEditor(this.spSec, "00");
        NumberEditor editorTimeSec = new NumberEditor(this.spTimeSec, "#0");

        this.spDay.setEditor(editorDay);
        this.spMonth.setEditor(editorMonth);
        this.spYear.setEditor(editorYear);
        this.spHour.setEditor(editorHour);
        this.spMin.setEditor(editorMin);
        this.spSec.setEditor(editorSec);
        this.spTimeSec.setEditor(editorTimeSec);
    }

    /**
     * Permet, une fois les composants créés, de placer correctement les éléments
     * qui composent la vue.
     */
    private void setStyle () {
        this.spDay.setBorder(new TitledBorder("Day"));
        this.spMonth.setBorder(new TitledBorder("Month"));
        this.spYear.setBorder(new TitledBorder("Year"));
        this.spHour.setBorder(new TitledBorder("Hours"));
        this.spMin.setBorder(new TitledBorder("Minutes"));
        this.spSec.setBorder(new TitledBorder("Seconds"));

        this.panel = new JPanel(new MigLayout("", "[80]"));
        this.panel.add(this.spDay, "grow");
        this.panel.add(this.spMonth, "grow");
        this.panel.add(this.spYear, "grow, wrap");
        this.panel.add(this.spHour, "grow");
        this.panel.add(this.spMin, "grow");
        this.panel.add(this.spSec, "grow, wrap");
        this.panel.add(this.lblTime, "gap top 15px, split 3, span");
        this.panel.add(this.spTimeSec, "width 150px");
        this.panel.add(this.btnNow, "");
    }

    /**
     * Permet d'ajouter les différents écouteurs de la vue.
     */
    private void setOnListeners () {
        this.spDay.addChangeListener(this);
        this.spMonth.addChangeListener(this);
        this.spYear.addChangeListener(this);
        this.spHour.addChangeListener(this);
        this.spMin.addChangeListener(this);
        this.spSec.addChangeListener(this);
        this.spTimeSec.addChangeListener(this);
        this.btnNow.addActionListener(this);
    }

    /**
     * Permet de mettre à jour les champs du panneau en fonction de la date
     * courante.
     */
    private void updateFields () {
        this.update = false;
        this.spDay.setValue(this.calendar.get(Calendar.DAY_OF_MONTH));
        this.spMonth.setValue(this.calendar.get(Calendar.MONTH) + 1);
        this.spYear.setValue(this.calendar.get(Calendar.YEAR));
        this.spHour.setValue(this.calendar.get(Calendar.HOUR_OF_DAY));
        this.spMin.setValue(this.calendar.get(Calendar.MINUTE));
        this.spSec.setValue(this.calendar.get(Calendar.SECOND));
        this.spTimeSec.setValue(this.calendar.getTimeInMillis() / 1000);
        this.update = true;
    }

    @Override
    public void stateChanged (ChangeEvent e) {
        /*
         * JSpinner
         */
        if (e.getSource() instanceof JSpinner) {
            JSpinner spinner = (JSpinner) e.getSource();

            // Mise à jour du temps en secondes
            if (this.spTimeSec.equals(spinner)) {
                if (this.update) {
                    long timeMillis = Long.parseLong(spinner.getValue().toString()) * 1000;
                    this.calendar.setTimeInMillis(timeMillis);
                }
            }
            // Mise à jour des champs d'ajustement
            else {
                if (this.update) {
                    this.calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(this.spDay.getValue().toString()));
                    this.calendar.set(Calendar.MONTH, Integer.parseInt(this.spMonth.getValue().toString()) - 1);
                    this.calendar.set(Calendar.YEAR, Integer.parseInt(this.spYear.getValue().toString()));
                    this.calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.spHour.getValue().toString()));
                    this.calendar.set(Calendar.MINUTE, Integer.parseInt(this.spMin.getValue().toString()));
                    this.calendar.set(Calendar.SECOND, Integer.parseInt(this.spSec.getValue().toString()));
                }
            }
            updateFields();
        }
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        /*
         * JButton
         */
        if (e.getSource() instanceof JButton) {
            JButton btn = (JButton) e.getSource();

            // Now
            if (this.btnNow.equals(btn)) {
                setTime(System.currentTimeMillis());
            }
        }
    }

    /**
     * Permet de mettre à jour la date affichée par le panneau à partir de la date
     * spécifiée.
     * 
     * @param date
     *            Date à afficher.
     */
    public void setDate (Date date) {
        this.calendar.setTime(date);
        updateFields();
    }

    /**
     * Renvoie la date affichée par le panneau.
     * 
     * @return Date affichée par le panneau.
     */
    public Date getDate () {
        return this.calendar.getTime();
    }

    /**
     * Permet de mettre à jour le temps affiché à partir d'un temps en millisecondes
     * UTC.
     * 
     * @param timeMillis
     *            Temps en millisecondes UTC.
     */
    public void setTime (long timeMillis) {
        this.calendar.setTimeInMillis(timeMillis);
        updateFields();
    }

    /**
     * Renvoie le temps affiché par le panneau en millisecondes UTC.
     * 
     * @return Temps en millisecondes UTC.
     */
    public long getTime () {
        return this.calendar.getTimeInMillis();
    }

    /**
     * Renvoie la timeZone utilisée pour afficher la date.
     * 
     * @return {@link TimeZone} utilisée.
     */
    public TimeZone getTimeZone () {
        return this.calendar.getTimeZone();
    }

    /**
     * Permet de spécifier la timeZone utilisée pour afficher la date.
     * 
     * @param timeZone
     *            TimeZone utilisée pour afficher la date.
     */
    public void setTimeZone (TimeZone timeZone) {
        this.calendar.setTimeZone(timeZone);
        updateFields();
    }
}
