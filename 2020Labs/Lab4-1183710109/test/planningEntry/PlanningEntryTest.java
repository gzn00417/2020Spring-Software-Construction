package planningEntry;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.Arrays;
import resource.*;
import location.*;
import timeSlot.*;

public class PlanningEntryTest {
        /**
         * testing strategy:
         * test the planning entry instance:
         * generate an instance
         * change from states to states: to ended or cancelled
         */
        @Test
        public void testFlightScheduleInstance() {
                Plane plane = Resource.newResourceOfPlane("SB250", "A320", 1000, 2.5);
                Location location = new Location("Harbin", "Beijing");
                TimeSlot timeSlot = new TimeSlot(Arrays.asList("2020-01-01 10:00", "2020-02-02 12:00"),
                                Arrays.asList("2020-01-01 10:00", "2020-02-02 12:00"));

                // run a plan
                FlightSchedule<Plane> planningEntry = PlanningEntry.newPlanningEntryOfFlightSchedule(location, timeSlot,
                                "CA001");
                assertEquals("WAITING", planningEntry.getState().getStrState());
                assertTrue(planningEntry.allocateResource(plane));
                assertEquals("ALLOCATED", planningEntry.getState().getStrState());
                assertTrue(planningEntry.start());
                assertEquals("RUNNING", planningEntry.getState().getStrState());
                assertFalse(planningEntry.cancel());
                assertEquals("RUNNING", planningEntry.getState().getStrState());
                assertTrue(planningEntry.finish());
                assertEquals("ENDED", planningEntry.getState().getStrState());

                // cancel a plan
                FlightSchedule<Plane> planningEntry_ = PlanningEntry.newPlanningEntryOfFlightSchedule(location,
                                timeSlot, "CA001");
                assertTrue(planningEntry_.allocateResource(plane));
                assertEquals("ALLOCATED", planningEntry_.getState().getStrState());
                assertTrue(planningEntry_.cancel());
                assertEquals("CANCELLED", planningEntry_.getState().getStrState());
        }

        /**
         * testing strategy:
         * test the planning entry instance:
         * generate an instance
         * change from states to states: to ended or cancelled
         */
        @Test
        public void testTrainScheduleInstance() {
                Plane plane = Resource.newResourceOfPlane("SB250", "A320", 1000, 2.5);
                Location location = new Location("Harbin", "Beijing");
                TimeSlot timeSlot = new TimeSlot(Arrays.asList("2020-01-01 10:00", "2020-02-02 12:00"),
                                Arrays.asList("2020-01-01 10:00", "2020-02-02 12:00"));

                // run a plan
                FlightSchedule<Plane> planningEntry = PlanningEntry.newPlanningEntryOfFlightSchedule(location, timeSlot,
                                "CA001");
                assertEquals("WAITING", planningEntry.getState().getStrState());
                assertTrue(planningEntry.allocateResource(plane));
                assertEquals("ALLOCATED", planningEntry.getState().getStrState());
                assertTrue(planningEntry.start());
                assertEquals("RUNNING", planningEntry.getState().getStrState());
                assertFalse(planningEntry.cancel());
                assertEquals("RUNNING", planningEntry.getState().getStrState());
                assertTrue(planningEntry.finish());
                assertEquals("ENDED", planningEntry.getState().getStrState());

                // cancel a plan
                FlightSchedule<Plane> planningEntry_ = PlanningEntry.newPlanningEntryOfFlightSchedule(location,
                                timeSlot, "CA001");
                assertTrue(planningEntry_.allocateResource(plane));
                assertEquals("ALLOCATED", planningEntry_.getState().getStrState());
                assertTrue(planningEntry_.cancel());
                assertEquals("CANCELLED", planningEntry_.getState().getStrState());
        }

        /**
        * testing strategy:
        * test the planning entry instance:
        * generate an instance
        * change from states to states: to ended or cancelled
        */
        @Test
        public void testActivityCalendarInstance() {
                Plane plane = Resource.newResourceOfPlane("SB250", "A320", 1000, 2.5);
                Location location = new Location("Harbin", "Beijing");
                TimeSlot timeSlot = new TimeSlot(Arrays.asList("2020-01-01 10:00", "2020-02-02 12:00"),
                                Arrays.asList("2020-01-01 10:00", "2020-02-02 12:00"));

                // run a plan
                FlightSchedule<Plane> planningEntry = PlanningEntry.newPlanningEntryOfFlightSchedule(location, timeSlot,
                                "CA001");
                assertEquals("WAITING", planningEntry.getState().getStrState());
                assertTrue(planningEntry.allocateResource(plane));
                assertEquals("ALLOCATED", planningEntry.getState().getStrState());
                assertTrue(planningEntry.start());
                assertEquals("RUNNING", planningEntry.getState().getStrState());
                assertFalse(planningEntry.cancel());
                assertEquals("RUNNING", planningEntry.getState().getStrState());
                assertTrue(planningEntry.finish());
                assertEquals("ENDED", planningEntry.getState().getStrState());

                // cancel a plan
                FlightSchedule<Plane> planningEntry_ = PlanningEntry.newPlanningEntryOfFlightSchedule(location,
                                timeSlot, "CA001");
                assertTrue(planningEntry_.allocateResource(plane));
                assertEquals("ALLOCATED", planningEntry_.getState().getStrState());
                assertTrue(planningEntry_.cancel());
                assertEquals("CANCELLED", planningEntry_.getState().getStrState());
        }
}