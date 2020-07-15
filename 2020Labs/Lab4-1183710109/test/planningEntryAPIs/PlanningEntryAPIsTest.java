package planningEntryAPIs;

import org.junit.Test;

import planningEntry.*;
import resource.*;
import timeSlot.*;
import location.*;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanningEntryAPIsTest {
        /*
         * Test strategy
         * 
         * Location Conflict:
         * Add entries from without conflict to with conflict.
         * call method when they're added.
         * 
         * Resource conflict:
         * Add entries from without conflict to with conflict.
         * call method when they're added.
         * 
         * Find Pre Entry:
         * Add entries from non-pre-entry to has it, and to new pre-entry.
         * call method when they're added, and update the new entry closer to the asking.
         */
        @Test
        public void testCheckLocationConflictWithFirst() {
                List<PlanningEntry<Resource>> entries = new ArrayList<>();
                Location location1 = new Location("A");
                TimeSlot timeSlot1 = new TimeSlot(Arrays.asList("2020-01-01 10:00"), Arrays.asList("2020-01-01 12:00"));
                ActivityCalendar<Resource> entry1 = PlanningEntry.newPlanningEntryOfActivityCalendar(location1,
                                timeSlot1, "1");
                Location location2 = new Location("A");
                TimeSlot timeSlot2 = new TimeSlot(Arrays.asList("2020-01-02 10:00"), Arrays.asList("2020-01-02 12:00"));
                ActivityCalendar<Resource> entry2 = PlanningEntry.newPlanningEntryOfActivityCalendar(location2,
                                timeSlot2, "2");
                Location location3 = new Location("B");
                TimeSlot timeSlot3 = new TimeSlot(Arrays.asList("2020-01-01 10:00"), Arrays.asList("2020-01-01 12:00"));
                ActivityCalendar<Resource> entry3 = PlanningEntry.newPlanningEntryOfActivityCalendar(location3,
                                timeSlot3, "3");
                entries.add(entry1);
                entries.add(entry2);
                entries.add(entry3);
                assertFalse((new PlanningEntryAPIsFirst()).checkLocationConflict(entries));
                Location location4 = new Location("A");
                TimeSlot timeSlot4 = new TimeSlot(Arrays.asList("2020-01-01 11:00"), Arrays.asList("2020-01-01 13:00"));
                ActivityCalendar<Resource> entry4 = PlanningEntry.newPlanningEntryOfActivityCalendar(location4,
                                timeSlot4, "4");
                entries.add(entry4);
                assertTrue(entry4.getBeginningTime().isBefore(entry1.getEndingTime())
                                && entry4.getEndingTime().isAfter(entry1.getBeginningTime()));
                //assertTrue(PlanningEntryAPIs.checkLocationConflict(entries, PlanningEntryAPIs.ITERATOR_SEARCH));
        }

        @Test
        public void testCheckLocationConflictWithSecond() {
                List<PlanningEntry<Resource>> entries = new ArrayList<>();
                Location location1 = new Location("A");
                TimeSlot timeSlot1 = new TimeSlot(Arrays.asList("2020-01-01 10:00"), Arrays.asList("2020-01-01 12:00"));
                ActivityCalendar<Resource> entry1 = PlanningEntry.newPlanningEntryOfActivityCalendar(location1,
                                timeSlot1, "1");
                Location location2 = new Location("A");
                TimeSlot timeSlot2 = new TimeSlot(Arrays.asList("2020-01-02 10:00"), Arrays.asList("2020-01-02 12:00"));
                ActivityCalendar<Resource> entry2 = PlanningEntry.newPlanningEntryOfActivityCalendar(location2,
                                timeSlot2, "2");
                Location location3 = new Location("B");
                TimeSlot timeSlot3 = new TimeSlot(Arrays.asList("2020-01-01 10:00"), Arrays.asList("2020-01-01 12:00"));
                ActivityCalendar<Resource> entry3 = PlanningEntry.newPlanningEntryOfActivityCalendar(location3,
                                timeSlot3, "3");
                entries.add(entry1);
                entries.add(entry2);
                entries.add(entry3);
                assertFalse((new PlanningEntryAPIsSecond()).checkLocationConflict(entries));
                Location location4 = new Location("A");
                TimeSlot timeSlot4 = new TimeSlot(Arrays.asList("2020-01-01 11:00"), Arrays.asList("2020-01-01 13:00"));
                ActivityCalendar<Resource> entry4 = PlanningEntry.newPlanningEntryOfActivityCalendar(location4,
                                timeSlot4, "4");
                entries.add(entry4);
                assertTrue(entry4.getBeginningTime().isBefore(entry1.getEndingTime())
                                && entry4.getEndingTime().isAfter(entry1.getBeginningTime()));
                //assertTrue(PlanningEntryAPIs.checkLocationConflict(entries, PlanningEntryAPIs.ITERATOR_SEARCH));
        }

        @Test
        public void testCheckResourceExclusiveConflict() {
                List<PlanningEntry<Resource>> entries = new ArrayList<>();
                FlightSchedule<Resource> entry1 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 10:00", "2020-01-01 12:00"),
                                                                Arrays.asList("2020-01-01 10:00", "2020-01-01 12:00")),
                                                "1");
                entry1.allocateResource(Resource.newResourceOfPlane("A1", "A320", 1000, 2.5));
                entries.add(entry1);
                FlightSchedule<Resource> entry2 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 14:00", "2020-01-01 16:00"),
                                                                Arrays.asList("2020-01-01 14:00", "2020-01-01 16:00")),
                                                "2");
                entry2.allocateResource(Resource.newResourceOfPlane("A1", "A320", 1000, 2.5));
                entries.add(entry2);
                assertFalse(PlanningEntryAPIs.checkResourceExclusiveConflict(entries));
                FlightSchedule<Resource> entry3 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 11:00", "2020-01-01 13:00"),
                                                                Arrays.asList("2020-01-01 11:00", "2020-01-01 13:00")),
                                                "3");
                entry3.allocateResource(Resource.newResourceOfPlane("A1", "A320", 1000, 2.5));
                entries.add(entry3);
                assertTrue(PlanningEntryAPIs.checkResourceExclusiveConflict(entries));
        }

        @Test
        public void testFindPreEntryPerResource() {
                List<PlanningEntry<Resource>> entries = new ArrayList<>();
                Resource r = Resource.newResourceOfPlane("A1", "A320", 1000, 2.5);
                FlightSchedule<Resource> entry1 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 10:00", "2020-01-01 12:00"),
                                                                Arrays.asList("2020-01-01 10:00", "2020-01-01 12:00")),
                                                "1");
                entry1.allocateResource(r);
                entries.add(entry1);
                FlightSchedule<Resource> entry2 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 14:00", "2020-01-01 16:00"),
                                                                Arrays.asList("2020-01-01 14:00", "2020-01-01 16:00")),
                                                "2");
                entry2.allocateResource(r);
                entries.add(entry2);
                assertNull(PlanningEntryAPIs.findPreEntryPerResource(r, entry1, entries));
                FlightSchedule<Resource> entry3 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 07:00", "2020-01-01 08:00"),
                                                                Arrays.asList("2020-01-01 07:00", "2020-01-01 08:00")),
                                                "3");
                entry3.allocateResource(r);
                entries.add(entry3);
                assertTrue(entry3.getTimeArrival().isBefore(entry1.getTimeLeaving()));
                assertTrue(entry3.getTimeArrival().isAfter(LocalDateTime.MIN));
                assertEquals(entry3, PlanningEntryAPIs.findPreEntryPerResource(r, entry1, entries));
                FlightSchedule<Resource> entry4 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 07:30", "2020-01-01 08:30"),
                                                                Arrays.asList("2020-01-01 07:30", "2020-01-01 08:30")),
                                                "4");
                entry4.allocateResource(r);
                entries.add(entry4);
                assertEquals(entry4, PlanningEntryAPIs.findPreEntryPerResource(r, entry1, entries));
                FlightSchedule<Resource> entry5 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 10:30", "2020-01-01 11:30"),
                                                                Arrays.asList("2020-01-01 10:30", "2020-01-01 11:30")),
                                                "5");
                entry5.allocateResource(r);
                entries.add(entry5);
                assertEquals(entry4, PlanningEntryAPIs.findPreEntryPerResource(r, entry1, entries));
                FlightSchedule<Resource> entry6 = PlanningEntry
                                .newPlanningEntryOfFlightSchedule(new Location("A", "B"),
                                                new TimeSlot(Arrays.asList("2020-01-01 09:30", "2020-01-01 09:45"),
                                                                Arrays.asList("2020-01-01 09:30", "2020-01-01 09:45")),
                                                "6");
                entry6.allocateResource(r);
                entries.add(entry6);
                assertEquals(entry6, PlanningEntryAPIs.findPreEntryPerResource(r, entry1, entries));
        }
}