/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author elisc
 */
@RunWith(Parameterized.class)
public class FlightEntryTest {
    
    @Parameter(0)
    public LocalTime start;
    
    @Parameter(1)
    public LocalTime flightTime;
    
    @Parameter(2)
    public LocalTime delay;
    
    @Parameter(3)
    public LocalTime arrival;
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {LocalTime.of(16, 55), LocalTime.of(2, 30), LocalTime.of(0, 15), LocalTime.of(19, 40)},
            {LocalTime.of(6, 0), LocalTime.of(18, 30), LocalTime.of(0, 0), LocalTime.of(0, 30)},
            {LocalTime.of(12, 15), LocalTime.of(10, 45), LocalTime.of(1, 45), LocalTime.of(0, 45)}
           });
    }  

    
    public FlightEntryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calcArrival method, of class FlightEntry.
     */
    @Test
    public void testCalcArrival() {
        FlightEntry entry = new FlightEntry(
                FlightType.ARRIVAL,
                "Chicago O'Hare",
                start,
                flightTime,
                "Boeing 787",
                "Delta Airlines",
                "DALXX"
        );
        
        entry.setDelay(delay);
        LocalTime result = entry.calcArrival();
        assertEquals(arrival, result);
    }

}
