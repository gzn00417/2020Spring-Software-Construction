package timeSlot;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.*;

public class TimeSlotTest {
    /*
     * Test strategy
     * test time slot used for all types of planning entry
     */
    @Test
    public void testTimeSlotOfFlightSchedule() {
        String[] timeSlotStrings = new String[] { "2020-01-01 10:10", "2020-01-01 12:12" };
        TimeSlot timeSlot = new TimeSlot(Arrays.asList(timeSlotStrings), Arrays.asList(timeSlotStrings));
        assertEquals("2020-01-01T10:10", timeSlot.getLeaving().get(0).toString());
        assertEquals("2020-01-01T12:12", timeSlot.getArrival().get(1).toString());
    }

    @Test
    public void testTimeSlotOfTrainSchedule() {
        String[] leavingStrings = new String[] { "2020-01-01 10:10", "2020-01-01 12:12", "2020-01-01 13:10" };
        String[] arrivalStrings = new String[] { "2020-01-01 10:10", "2020-01-01 12:10", "2020-01-01 13:10" };
        TimeSlot timeSlot = new TimeSlot(Arrays.asList(leavingStrings), Arrays.asList(arrivalStrings));
        assertEquals("2020-01-01T10:10", timeSlot.getLeaving().get(0).toString());
        assertEquals("2020-01-01T12:12", timeSlot.getLeaving().get(1).toString());
        assertEquals("2020-01-01T13:10", timeSlot.getArrival().get(2).toString());
    }
}