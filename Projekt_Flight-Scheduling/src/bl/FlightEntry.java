/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author elisc
 */
public class FlightEntry {
    
    /**
     * variables
     */
    private FlightType type;
    private String airport;
    private LocalTime startTime;
    private LocalTime flightTime;
    private String machineType;
    private String airline;
    private String flightCode;
    private LocalTime delay;
    
    /**
     * Constructor
     * @param type
     * @param airport
     * @param startTime
     * @param flightTime
     * @param machineType
     * @param airline
     * @param flightCode 
     */
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
        this.delay = LocalTime.of(0, 0);
        
    }
    
    /**
     * calculates the aircraft's arrival time
     * @return 
     */
    public LocalTime calcArrival(){
        LocalTime arrival = startTime.plusHours(flightTime.getHour());
        arrival = arrival.plusMinutes(flightTime.getMinute());
        arrival = arrival.plusHours(delay.getHour());
        arrival = arrival.plusMinutes(delay.getMinute());
        
        return arrival;
    }

    /**
     * Getter methods below
     * @return 
     */
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
    
    public void setDelay(LocalTime delay){
        this.delay = delay;
    }
    
    public LocalTime getDelay(){
        return delay;
    }
    
}
