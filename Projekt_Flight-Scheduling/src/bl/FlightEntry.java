/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl;

import java.time.LocalTime;

/**
 *
 * @author elisc
 */
public class FlightEntry {
    
    private FlightType type;
    private String airport;
    private LocalTime startTime;
    private LocalTime flightTime;
    private String machineType;
    private String airline;
    private String flightCode;
    
    public FlightEntry(FlightType type, String airport, LocalTime startTime,
            LocalTime flightTime, String machineType,
            String airline, String flightCode){
        
        this.type = type;
        this.airport = airport;
        this.startTime = startTime;
        this.flightTime = flightTime;
        this.machineType = machineType;
        this.airline = airline;
        this.flightCode = flightCode;
        
    }

    public String getFlightCode() {
        return flightCode;
    }

    public FlightType getType() {
        return type;
    }

    public String getAirport() {
        return airport;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFlightTime() {
        return flightTime;
    }

    public String getMachineType() {
        return machineType;
    }

    public String getAirline() {
        return airline;
    }
    
}
