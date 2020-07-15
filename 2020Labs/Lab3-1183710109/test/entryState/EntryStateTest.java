package entryState;

import static org.junit.Assert.*;

import org.junit.*;

public class EntryStateTest {
    @Test
    public void testEntryState() {
        EntryState entryState = new EntryState("Waiting");
        assertEquals("WAITING", entryState.getState().name());
        assertTrue(entryState.setNewState("FlightSchedule", "allocated"));
        assertEquals("ALLOCATED", entryState.getState().name());
        assertTrue(entryState.setNewState("ActivityCalendar", "running"));
        assertEquals("RUNNING", entryState.getState().name());
        // only train can blocked
        assertFalse(entryState.setNewState("FlightSchedule", "blocked"));
        assertTrue(entryState.setNewState("TrainSchedule", "blocked"));
        assertTrue(entryState.setNewState("TrainSchedule", "running"));
        assertTrue(entryState.setNewState("ActivityCalendar", "ended"));
    }
}