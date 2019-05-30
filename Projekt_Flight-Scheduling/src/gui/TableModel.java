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

    /**
     * variables
     */
    ArrayList<FlightEntry> entries = new ArrayList<>();    
    private final String[] COLUMNS = {"Type", "Airport", "Start",
        "Flight", "Delay", "Arrival", "Machine", "Airline", "Code"};

    
    @Override
    public int getRowCount() {
        return entries.size();
    }
    
    /**
     * replaces the entries with the parameterized entries
     * @param entries 
     */
    public void setEntries(ArrayList<FlightEntry> entries){
        this.entries = entries;
        fireTableDataChanged();
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
    
    /**
     * adds an entry to the table
     * @param entry 
     */
    public void add(FlightEntry entry){
        entries.add(entry);
        fireTableRowsInserted(entries.size()-1, entries.size()-1);
    }
    
    /**
     * edits a selected entry in the table to show
     * the newly entered value for the delay
     * @param selRow
     * @param delay 
     */
    public void edit(int selRow, LocalTime delay){
        FlightEntry entry = entries.get(selRow);
        entries.remove(selRow);
        entry.setDelay(delay);
        entries.add(selRow, entry);
        fireTableDataChanged();
    }
    
    /**
     * removes entries with given indices
     * @param indices 
     */
    public void delete(int[] indices){
        for(int i = indices.length-1; i >= 0; i--){
            entries.remove(indices[i]);
            fireTableRowsDeleted(indices[i], indices[i]);
        }
    }
    
    /**
     * returns entry for given index
     * used by DatabaseManagement
     * @param selRow
     * @return 
     */
    public FlightEntry getEntry(int selRow){
        return entries.get(selRow);
    }

}
