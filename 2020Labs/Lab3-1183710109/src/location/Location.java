package location;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

/**
 * immutable object
 * one location, origin and terminal locations, or several locations
 */
public class Location {
    private final List<String> locations = new ArrayList<String>();
    /*
     * AF:
     * locations represent the locations in the plan
     * 
     * RI:
     * locations should be as long as arrival and leaving in class TimeSlot
     * 
     * Safety:
     * do not provide mutator
     */

    /**
     * constructor
     * @param locations
     */
    public Location(String... locations) {
        for (String str : locations)
            this.locations.add(str);
        checkRep();
    }

    /**
     * check Rep
     */
    private void checkRep() {
        for (String strLocation : locations) {
            assert (strLocation.length() > 0);
        }
    }

    /**
     * get the List of String of location
     * @return the List of String of location
     */
    public List<String> getLocations() {
        return this.locations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Location)) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(locations, location.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(locations);
    }

    @Override
    public String toString() {
        return "{" + " locations='" + getLocations() + "'" + "}";
    }

}