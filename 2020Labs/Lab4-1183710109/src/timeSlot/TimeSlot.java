package timeSlot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * immutable object
 * time slot
 */
public class TimeSlot {
    private final List<LocalDateTime> arrival = new ArrayList<>();
    private final List<LocalDateTime> leaving = new ArrayList<>();
    /*
     * AF:
     * arrival[i] represent the time it arrives locations[i]
     * leaving[i] represent the time it leaves locations[i]
     * 
     * when Flight Schedule:
     * length == 2, arrival[0] == leaving[0], arrival[1] == leaving[1]
     * 
     * when Activity Schedule:
     * length == 1, arrival[0] is ending time, leaving[0] is beginning time
     * 
     * RI:
     * the length of arrival and leaving should be equal
     * leaving[i] should be later than arrival[i]
     * when i<length arrival[i] and leaving[i] should be non-null
     * 
     * Safety:
     * do not provide mutator
     */

    /**
     * constructor
     * @param arrival
     * @param leaving
     */
    public TimeSlot(List<String> leaving, List<String> arrival) {
        for (String strDateTime : leaving)
            this.leaving.add(LocalDateTime.parse(strDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        for (String strDateTime : arrival)
            this.arrival.add(LocalDateTime.parse(strDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        checkRep();
    }

    /**
     * check Rep
     */
    private void checkRep() {
        assert (arrival.size() == leaving.size());
        for (int i = 0; i < arrival.size(); i++) {
            assert (arrival.get(i) != null);
            assert (leaving.get(i) != null);
        }
    }

    /**
     * get the list of LocalDateTime of arrival
     * @return the list of LocalDateTime
     */
    public List<LocalDateTime> getArrival() {
        return this.arrival;
    }

    /**
     * get the list of LocalDateTime of leaving
     * @return the list of LocalDateTime
     */
    public List<LocalDateTime> getLeaving() {
        return this.leaving;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TimeSlot)) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(arrival, timeSlot.arrival) && Objects.equals(leaving, timeSlot.leaving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrival, leaving);
    }

    @Override
    public String toString() {
        return "{" + " arrival='" + getArrival() + "'" + ", leaving='" + getLeaving() + "'" + "}";
    }

}