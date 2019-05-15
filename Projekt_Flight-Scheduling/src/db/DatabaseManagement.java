/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author elisc
 */
public class DatabaseManagement {
    /**
     * the one and only instance of the database class
     */
    private static DatabaseManagement instance;
    private final Connection conn;
    /**
     * Constructor is private Singleton implementation
     */
    private DatabaseManagement() throws SQLException {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost/flight_scheduling",
                "postgres", "postgres");
    }
    
    public static void main(String[] args) {
        try{
            DatabaseManagement.getInstance().createTableSchedule();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * If the instance hasn't been created before, it gets created.
     * @return Returns the database instance.
     */
    public synchronized static DatabaseManagement getInstance() throws SQLException{
        if(instance == null){
            instance = new DatabaseManagement();
        }
        return instance;
    }
    
    public void createTableSchedule() throws Exception{
        String sql = "DROP TABLE IF EXISTS Schedule;"
                + "CREATE TABLE Schedule"
                + "("
                + "FLIGHT_TYPE character varying NOT NULL PRIMARY KEY,"
                + "AIRPORT character varying NOT NULL,"
                + "START_TIME character varying NOT NULL"
                + "FLIGHT_TIME character varying NOT NULL"
                + "DELAY integer"
                + "ARRIVAL_TIME character varying"
                + "MACHINE_TYPE character varying NOT NULL"
                + "AIRLINE character varying NOT NULL"
                + ")";
        
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);             
    }
    
    /*public void insertTestData() throws Exception {
        String sql = "INSERT INTO Pet VALUES(0, 'Hundi', 'Haushund');"
                + "INSERT INTO Pet VALUES(1, 'Katzi', 'Hauskatze');";
        
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
    }*/

}
