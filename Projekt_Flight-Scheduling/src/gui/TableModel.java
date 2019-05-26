/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import bl.FlightEntry;
import java.time.LocalTime;
import java.util.ArrayList;
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
    
    public void setEntries(ArrayList<FlightEntry> entries){
        this.entries = entries;
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
    
    public void edit(int selRow, LocalTime delay){
        FlightEntry entry = entries.get(selRow);
        entry.setDelay(delay);
        entries.remove(selRow);
        entries.add(selRow, entry);
        fireTableDataChanged();
    }
    
    public FlightEntry getEntry(int selRow){
        return entries.get(selRow);
    }

}
