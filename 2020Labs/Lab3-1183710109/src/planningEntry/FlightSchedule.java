package planningEntry;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import location.*;
import timeSlot.*;

/**
 * a flight plan containing information of plane, locations and time slot
 */
public class FlightSchedule<R> extends CommonPlanningEntry<R> {
    /**
     * index of origin or terminal in locations
     */
    private static final int ORIGIN = 0, TERMINAL = 1;

    /*
     * AF:
     * location represents the airports
     * resource represents the planes
     * 
     * RI:
     * location and timeSlot must be 2-length
     * 
     * Safety:
     * do not provide mutator or expose various
     */

    /**
     * constructor
     * @param location
     * @param timeSlot
     * @param planningEntryNumber
     */
    public FlightSchedule(Location location, TimeSlot timeSlot, String planningEntryNumber) {
        super(location, timeSlot, planningEntryNumber);
        this.strPlanningEntryType = "FlightSchedule";
    }

    /**
     * allocate the resource to the flight schedule
     * set the state as ALLOCATED
     * @param resource
     * @return true if the resource is set and state is ALLOCATED
     */
    public Boolean allocateResource(R resource) {
        super.resource = resource;
        return this.state.setNewState(strPlanningEntryType, "Allocated");
    }

    /**
     * get the origin location object
     * @return the origin location object
     */
    public String getLocationOrigin() {
        return super.getLocation().getLocations().get(ORIGIN);
    }

    /**
     * get the terminal location object
     * @return the terminal location object
     */
    public String getLocationTerminal() {
        return super.getLocation().getLocations().get(TERMINAL);
    }

    /**
     * get the LocalDateTime of leaving time
     * @return the LocalDateTime of leaving time
     */
    public LocalDateTime getTimeLeaving() {
        return super.getTimeSlot().getLeaving().get(ORIGIN);
    }

    /**
     * get the LocalDateTime of arrival time
     * @return the LocalDateTime of arrival time
     */
    public LocalDateTime getTimeArrival() {
        return super.getTimeSlot().getLeaving().get(TERMINAL);
    }

    @Override
    public LocalDate getPlanningDate() {
        return LocalDate.parse(this.getTimeLeaving().toString().substring(0, 10),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule<R> flightSchedule = (FlightSchedule<R>) o;
        return Objects.equals(this.getPlanningDate(), flightSchedule.getPlanningDate())
                && Objects.equals(this.getPlanningEntryNumber(), flightSchedule.getPlanningEntryNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPlanningDate(), this.getPlanningEntryNumber());
    }

}
