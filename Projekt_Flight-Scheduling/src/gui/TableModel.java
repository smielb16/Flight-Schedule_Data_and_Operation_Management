/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import bl.FlightEntry;
import db.DatabaseManagement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author elisc
 */
public class TableModel extends AbstractTableModel {

    ArrayList<FlightEntry> entries = new ArrayList<>();
    
    private final String[] COLUMNS = {"Type", "Airport", "Start",
        "Flight", "Delay", "Arrival", "Machine", "Airline", "Code"};

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FlightEntry entry = entries.get(rowIndex);
        return entry;
    }
    
    public void add(FlightEntry entry){
        entries.add(entry);
        fireTableRowsInserted(entries.size()-1, entries.size()-1);
    }

}
