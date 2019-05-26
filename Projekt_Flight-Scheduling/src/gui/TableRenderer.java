/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import bl.FlightEntry;
import java.awt.Color;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author elisc
 */
public class TableRenderer implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        
        FlightEntry entry = (FlightEntry) value;
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        
        switch(table.convertColumnIndexToModel(column)){
            case 0:
                label.setText(entry.getType().toString());
                break;
            case 1:
                label.setText(entry.getAirport());
                break;
            case 2:
                label.setText(entry.getStartTime().format(dtf));
                break;
            case 3:
                label.setText(entry.getFlightTime().format(dtf));
                break;
            case 4:
                label.setText(""+entry.getDelay().format(dtf));
                break;
            case 5:
                label.setText(""+entry.calcArrival().format(dtf));
                break;
            case 6:
                label.setText(entry.getMachineType());
                break;
            case 7:
                label.setText(entry.getAirline());
                break;
            case 8:
                label.setText(entry.getFlightCode());
                break;
        }
        
        label.setOpaque(true);
        label.setForeground(Color.black);
        label.setBackground(Color.white);
        
        if(isSelected){
            label.setBackground(Color.gray);
        }
        
        return label;
    }
    
}
