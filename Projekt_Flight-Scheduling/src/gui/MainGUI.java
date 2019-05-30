/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static bl.DTFPattern.MAINPATTERN;
import bl.FlightEntry;
import bl.FlightType;
import db.DatabaseManagement;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author elisc
 */
public class MainGUI extends JFrame {

    private DatabaseManagement bl;
    private TableModel model = new TableModel();
    private JTable tbMain = new JTable();
    private JMenuItem btAddEntry;
    private JMenuItem btEditDelay;
    private JMenuItem btLoad;
    private JMenuItem btSave;
    private JMenuItem btDeleteEntries;
    private JMenu menu1;
    private JMenu menu2;
    private JMenuBar menuBar;
    private JScrollPane scrollpane;

    public static void main(String[] args) {
        new MainGUI().setVisible(true);
    }

    public MainGUI() {
        initComp();
        init();
    }

    private void init() {
        try {
            bl = DatabaseManagement.getInstance();
            if (bl.checkForExistingTable()) {
                model.setEntries(bl.getData());
            } else {
                bl.createTableSchedule();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //old initComp() which prohibited proper cell focus
    private void initCompOld() {
        tbMain.setModel(model);
        tbMain.setDefaultRenderer(Object.class, new TableRenderer());

        Container container = this.getContentPane();

        menuBar = new JMenuBar();

        scrollpane = new JScrollPane();
        scrollpane.setViewportView(tbMain);

        menu1 = new JMenu();
        menu1.setText("Edit");

        menu2 = new JMenu();
        menu2.setText("File");

        btAddEntry = new JMenuItem("Add Schedule Entry");
        btAddEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                methodAddEntry();
            }
        });

        btEditDelay = new JMenuItem("Edit Delay");
        btEditDelay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                methodEditDelay();
            }
        });

        btLoad = new JMenuItem("Load");
        btEditDelay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                methodLoad();
            }
        });

        btSave = new JMenuItem("Save");
        btEditDelay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                methodSave();
            }
        });

        menu1.add(btAddEntry);
        menu1.add(btEditDelay);

        menu2.add(btLoad);
        menu2.add(btSave);

        menuBar.add(menu1);
        menuBar.add(menu2);

        container.setLayout(new BorderLayout());
        container.add(menuBar, BorderLayout.NORTH);
        setJMenuBar(menuBar);
        container.add(scrollpane, BorderLayout.CENTER);
    }

    /**
     * opens EntryDialog and adds entry to tablemodel and database if dialog
     * signals success
     *
     * @param evt
     */
    private void methodAddEntry() {
        EntryDialog dialog = new EntryDialog(this, true);

        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            bl.addEntry(dialog.getEntry());
            model.add(dialog.getEntry());
        }
    }

    /**
     * opens EditDialog and edits selected row in tablemodel and database if
     * dialog signals success
     *
     * @param evt
     */
    private void methodEditDelay() {
        EditDialog dialog = new EditDialog(this, true);

        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            try {
                int selRow = tbMain.getSelectedRow();
                
                if (selRow < 0) {
                    throw new Exception("Please select one row!");
                }

                model.edit(selRow, dialog.getDelay());
                bl.editEntry(model.getEntry(selRow));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        ex.getMessage());
            }
        }
    }

    /**
     * loads the csv-file and clears the database table while adding the
     * contents of the file to the tablemodel and database
     *
     * @param evt
     */
    private void methodLoad() {
        try {
            bl.createTableSchedule();
            model.setEntries(new ArrayList<FlightEntry>());

            BufferedReader br = new BufferedReader(new FileReader(new File("./schedule.csv")));

            String line = null;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MAINPATTERN);

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                FlightEntry entry = new FlightEntry(
                        Enum.valueOf(FlightType.class, values[0]),
                        values[1],
                        LocalTime.parse(values[2], dtf),
                        LocalTime.parse(values[3], dtf),
                        values[6],
                        values[7],
                        values[8]
                );

                entry.setDelay(LocalTime.parse(values[4], dtf));

                bl.addEntry(entry);
                model.add(entry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * saves the contents of the table in the database onto a csv-file
     *
     * @param evt
     */
    private void methodSave() {
        try {
            ArrayList<FlightEntry> entries = bl.getData();

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./schedule.csv")));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MAINPATTERN);

            for (FlightEntry entry : entries) {
                bw.write(entry.getType().toString());
                bw.write(";");
                bw.write(entry.getAirport());
                bw.write(";");
                bw.write(entry.getStartTime().format(dtf));
                bw.write(";");
                bw.write(entry.getFlightTime().format(dtf));
                bw.write(";");
                bw.write(entry.getDelay().format(dtf));
                bw.write(";");
                bw.write(entry.calcArrival().format(dtf));
                bw.write(";");
                bw.write(entry.getMachineType());
                bw.write(";");
                bw.write(entry.getAirline());
                bw.write(";");
                bw.write(entry.getFlightCode());
                bw.write("\n");
            }
            bw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * deletes selected entries from database table and table model
     */
    private void methodDeleteEntries() {
        try {
            int[] indices = tbMain.getSelectedRows();

            model.delete(indices);

            for (int i = 0; i < indices.length; i++) {
                bl.deleteEntry(model.getEntry(indices[i]));
            }
        } catch (Exception ex) {
            //suppresses unnecessary IndexOutOfBoundsException
        }
    }

    //static MainGUI was used to fix the issue of a selected table cell not
    //remaining in focus after having clicked any button
    //no method besides using the layout the static MainGUI utilizes was found
    //in order to fix this detrimental issue
    /**
     * initializes components
     */
    private void initComp() {

        scrollpane = new JScrollPane();
        tbMain = new JTable();
        menuBar = new JMenuBar();
        menu1 = new JMenu();
        btAddEntry = new JMenuItem();
        btEditDelay = new JMenuItem();
        btDeleteEntries = new JMenuItem();
        menu2 = new JMenu();
        btLoad = new JMenuItem();
        btSave = new JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbMain.setModel(model);
        tbMain.setDefaultRenderer(Object.class, new TableRenderer());

        scrollpane.setViewportView(tbMain);

        menu1.setText("Edit");

        btAddEntry.setText("Add Schedule Entry");
        btAddEntry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                methodAddEntry();
            }
        });
        menu1.add(btAddEntry);

        btEditDelay.setText("Edit Delay");
        btEditDelay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                methodEditDelay();
            }
        });
        menu1.add(btEditDelay);

        btDeleteEntries.setText("Delete Entries");
        btDeleteEntries.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                methodDeleteEntries();
            }
        });
        menu1.add(btDeleteEntries);

        menuBar.add(menu1);

        menu2.setText("File");

        btLoad.setText("Load");
        btLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                methodLoad();
            }
        });
        menu2.add(btLoad);

        btSave.setText("Save");
        btSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                methodSave();
            }
        });
        menu2.add(btSave);

        menuBar.add(menu2);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        //this fixes the focus issue
        pack();
    }

}
