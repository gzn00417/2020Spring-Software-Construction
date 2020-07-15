package planningEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import location.*;
import timeSlot.*;

/**
 * a train plan containing several trains
 */
public class TrainSchedule<R> extends CommonPlanningEntry<R> {
    /**
     * list of ordered train
     */
    private final List<R> resources = new ArrayList<>();
    /**
     * origin and terminal index
     */
    public int ORIGIN = 0, TERMINAL = 0;
    /**
     * number of stations
     */
    public int LENGTH = 0;

    /*
     * AF:
     * list of resource represent several trains
     * location represents stations
     * 
     * RI:
     * list must be at least 1-length
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
    public TrainSchedule(Location location, TimeSlot timeSlot, String planningEntryNumber) {
        super(location, timeSlot, planningEntryNumber);
        this.strPlanningEntryType = "TrainSchedule";
    }

    /**
     * allocate the resource to the flight schedule
     * set the state as ALLOCATED
     * @param resources
     * @return true if the resource is set and state is ALLOCATED
     */
    public Boolean allocateResource(R... resources) {
        this.resources.addAll(Arrays.asList(resources));
        this.ORIGIN = 0;
        this.LENGTH = this.resources.size();
        this.TERMINAL = this.resources.size() - 1;
        return this.state.setNewState(strPlanningEntryType, "Allocated");
    }

    /**
     * get the Resource object of No.indexTrain train
     * @param indexTrain
     * @return the Resource object of No.indexTrain train
     */
    public R getTrainOfIndex(int indexTrain) {
        return this.resources.get(indexTrain);
    }

    /**
     * get the String of No.indexLocation location
     * @param indexLocation
     * @return the String of No.indexLocation location
     */
    public String getLocationOfIndex(int indexLocation) {
        return super.getLocation().getLocations().get(indexLocation);
    }

    /**
     * get the LocalDateTime of leaving time of No.indexLocation Location
     * @param indexLocation
     * @return the LocalDateTime of leaving time of No.indexLocation Location
     */
    public LocalDateTime getLeavingTimeOfIndex(int indexLocation) {
        assert (indexLocation != TERMINAL);
        return super.getTimeSlot().getLeaving().get(indexLocation);
    }

    /**
     * get the LocalDateTime of arrival time of No.indexLocation Location
     * @param indexLocation
     * @return the LocalDateTime of arrival time of No.indexLocation Location
     */
    public LocalDateTime getArrivalTimeOfIndex(int indexLocation) {
        assert (indexLocation != ORIGIN);
        return super.getTimeSlot().getArrival().get(indexLocation);
    }

    @Override
    public LocalDate getPlanningDate() {
        return LocalDate.parse(this.getLeavingTimeOfIndex(ORIGIN).toString().substring(0, 10),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
