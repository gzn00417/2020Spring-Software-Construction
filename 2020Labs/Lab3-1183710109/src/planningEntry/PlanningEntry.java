package planningEntry;

import entryState.EntryState;
import location.Location;
import timeSlot.TimeSlot;

/**
 * interface of 3 planning entries
 */
public interface PlanningEntry<R> {
    /**
     * a factory method for generating an instance of Flight Schedule
     * @param <R>
     * @param location
     * @param timeSlot
     * @param planningEntryNumber
     * @return a empty instance of planning entry of  flight schedule
     */
    public static <R> FlightSchedule<R> newPlanningEntryOfFlightSchedule(Location location, TimeSlot timeSlot,
            String planningEntryNumber) {
        return new FlightSchedule<R>(location, timeSlot, planningEntryNumber);
    }

    /**
     * a factory method for generating an instance of Train Schedule
     * @param <R>
     * @param location
     * @param timeSlot
     * @param planningEntryNumber
     * @return a empty instance of planning entry of train schedule
     */
    public static <R> TrainSchedule<R> newPlanningEntryOfTrainSchedule(Location location, TimeSlot timeSlot,
            String planningEntryNumber) {
        return new TrainSchedule<R>(location, timeSlot, planningEntryNumber);
    }

    /**
     *  a factory method for generating an instance of Activity Calendar
     * @param <R>
     * @param location
     * @param timeSlot
     * @param planningEntryNumber
     * @return a empty instance of planning entry of activity calendar
     */
    public static <R> ActivityCalendar<R> newPlanningEntryOfActivityCalendar(Location location, TimeSlot timeSlot,
            String planningEntryNumber) {
        return new ActivityCalendar<R>(location, timeSlot, planningEntryNumber);
    }

    /**
     * start the planning entry
     * @return true if the entry is started
     */
    public Boolean start();

    /**
     * block the planning entry
     * @return true if the entry is blocked
     */
    public Boolean block();

    /**
     * cancel the planning entry
     * @return true if the entry is cancelled
     */
    public Boolean cancel();

    /**
     * finish the planning entry
     * @return true if the entry is ended
     */
    public Boolean finish();

    /**
     * get the Location object of the planning entry
     * @return the Location object
     */
    public Location getLocation();

    /**
     * get the TimeSlot object of the planning entry
     * @return the TimeSlot object
     */
    public TimeSlot getTimeSlot();

    /**
     * get the EntryState object of the planning entry
     * @return the EntryState object
     */
    public EntryState getState();

    /**
     * get the String of planning entry type
     * @return the String of planning entry type
     */
    public String getStrPlanningEntryType();

    /**
     * get the String of planning entry number
     * @return the String of planning entry number
     */
    public String getPlanningEntryNumber();

    /**
     * get the R of resource
     * @return the R of resource
     */
    public R getResource();
}
