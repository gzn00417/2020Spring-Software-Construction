package planningEntryCollection;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.junit.Test;

import planningEntry.*;
import resource.*;

public class PlanningEntryCollectionTest {
    @Test
    public void testFlightScheduleCollection() {
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        String input = "Flight:2020-01-16,AA018\n{\nDepartureAirport:Hongkong\nArrivalAirport:Shenyang\nDepatureTime:2020-01-16 22:40\nArrivalTime:2020-01-17 03:51\nPlane:B6967\n{\nType:A340\nSeats:332\nAge:23.7\n}\n}\n";
        FlightSchedule<Resource> flightSchedule = flightScheduleCollection.addPlanningEntry(input);
        assertEquals("Hongkong", flightSchedule.getLocationOrigin());
        assertEquals("Shenyang", flightSchedule.getLocationTerminal());
        assertEquals("2020-01-16T22:40", flightSchedule.getTimeLeaving().toString());
        assertEquals("2020-01-17T03:51", flightSchedule.getTimeArrival().toString());
        assertEquals("AA018", flightSchedule.getPlanningEntryNumber());
        assertEquals(new Plane("B6967", "A340", 332, 23.7),
                flightScheduleCollection.allocatePlanningEntry("AA018", input));
        assertEquals("2020-01-16", flightSchedule.getPlanningDate().toString());
    }

    @Test
    public void testTrainScheduleCollection() {

    }

    @Test
    public void testActivityCalendarCollection() {

    }

    @Test
    public void testCommonMethods() {
        // test getALLLocation()
        FlightScheduleCollection flightScheduleCollection = new FlightScheduleCollection();
        String input1 = "Flight:2020-01-16,AA018\n{\nDepartureAirport:Hongkong\nArrivalAirport:Shenyang\nDepatureTime:2020-01-16 22:40\nArrivalTime:2020-01-17 03:51\nPlane:B6967\n{\nType:A340\nSeats:332\nAge:23.7\n}\n}\n";
        String input2 = "Flight:2021-01-16,AA018\n{\nDepartureAirport:Beijing\nArrivalAirport:Shanghai\nDepatureTime:2021-01-16 22:40\nArrivalTime:2021-01-17 03:51\nPlane:B6967\n{\nType:A340\nSeats:332\nAge:23.7\n}\n}\n";
        String input3 = "Flight:2022-01-16,AA018\n{\nDepartureAirport:Beijing\nArrivalAirport:Macau\nDepatureTime:2022-01-16 22:40\nArrivalTime:2022-01-17 03:51\nPlane:B6967\n{\nType:A340\nSeats:332\nAge:23.7\n}\n}\n";
        FlightSchedule<Resource> flightSchedule1 = flightScheduleCollection.addPlanningEntry(input1);
        FlightSchedule<Resource> flightSchedule2 = flightScheduleCollection.addPlanningEntry(input2);
        FlightSchedule<Resource> flightSchedule3 = flightScheduleCollection.addPlanningEntry(input3);
        Set<String> allLocationSet = new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("Hongkong");
                add("Shenyang");
                add("Beijing");
                add("Shanghai");
                add("Macau");
            }
        };
        assertEquals(allLocationSet, flightScheduleCollection.getAllLocation());
        List<PlanningEntry<Resource>> planningEntryList = new ArrayList<PlanningEntry<Resource>>() {
            private static final long serialVersionUID = 1L;
            {
                add(flightSchedule1);
                add(flightSchedule2);
                add(flightSchedule3);
            }
        };
        assertEquals(planningEntryList, flightScheduleCollection.getAllPlanningEntries());
    }
}