/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import bl.DTFPattern;
import bl.FlightEntry;
import bl.FlightType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elisc
 */
public class DatabaseManagement implements DTFPattern{

    /**
     * the one and only instance of the database class
     */
    private static DatabaseManagement instance;
    private final Connection conn;
    private ArrayList<FlightEntry> entries;

    /**
     * Constructor is private Singleton implementation
     */
    private DatabaseManagement() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/flight_scheduling",
                "postgres", "postgres"
        );
    }

    /**
     * If the instance hasn't been created before, it gets created.
     *
     * @return Returns the database instance.
     */
    public synchronized static DatabaseManagement getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManagement();
        }
        return instance;
    }

    public boolean checkForExistingTable() throws SQLException{
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet res = meta.getTables(null, null, "Schedule", null);
        
        return res.next();
    }

    public void createTableSchedule() throws Exception {
        String sql = "DROP TABLE IF EXISTS Schedule;"
                + "CREATE TABLE IF NOT EXISTS Schedule"
                + "("
                + "FLIGHT_TYPE character varying NOT NULL,"
                + "AIRPORT character varying NOT NULL,"
                + "START_TIME character varying NOT NULL,"
                + "FLIGHT_TIME character varying NOT NULL,"
                + "DELAY character varying,"
                + "ARRIVAL_TIME character varying,"
                + "MACHINE_TYPE character varying NOT NULL,"
                + "AIRLINE character varying NOT NULL,"
                + "FLIGHT_CODE character varying NOT NULL PRIMARY KEY"
                + ")";

        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
    }

    public void addEntry(FlightEntry entry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MAINPATTERN);

        String[] values = new String[9];

        values[0] = entry.getType().toString();
        values[1] = entry.getAirport();
        values[2] = entry.getStartTime().format(dtf);
        values[3] = entry.getFlightTime().format(dtf);
        values[4] = entry.getDelay().format(dtf);
        values[5] = entry.calcArrival().format(dtf);
        values[6] = entry.getMachineType();
        values[7] = entry.getAirline();
        values[8] = entry.getFlightCode();

        String sql = "INSERT INTO Schedule VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stat = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                stat.setString(i + 1, values[i]);
            }
            stat.executeUpdate();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    public void editEntry(FlightEntry entry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MAINPATTERN);
        try {
            String sql = "UPDATE Schedule"
                    + "SET delay=?, arrival_time=?"
                    + "WHERE flight_code=?";

            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, entry.getDelay().format(dtf));
            stat.setString(2, entry.calcArrival().format(dtf));
            stat.setString(3, entry.getFlightCode());
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Queries the whole table*
     */
    public ArrayList<FlightEntry> getData() throws SQLException {
        String sql = "SELECT * FROM Schedule";
        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(MAINPATTERN);

        ArrayList<FlightEntry> entries = new ArrayList<>();

        while (rs.next()) {
            FlightEntry entry = new FlightEntry(
                    Enum.valueOf(FlightType.class, rs.getString(1)),
                    rs.getString(2),
                    LocalTime.parse(rs.getString(3), dtf),
                    LocalTime.parse(rs.getString(4), dtf),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9)
            );

            entry.setDelay(LocalTime.parse(rs.getString(5), dtf));
            
            entries.add(entry);
        }

        return entries;
    }

    public boolean checkDbForEntry(String flightcode) {
        boolean exists = false;
        try {
            String sql = "SELECT * FROM Schedule WHERE flight_code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, flightcode);

            ResultSet rs = ps.executeQuery();
            exists = rs.next();

        } catch (SQLException ex) {
        }
        return exists;
    }

}
