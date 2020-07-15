package planningEntryCollection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import location.*;
import planningEntry.*;
import resource.*;
import timeSlot.*;

public class TrainScheduleCollection extends PlanningEntryCollection {
    @Override
    public FlightSchedule<Resource> addPlanningEntry(String stringInfo) {
        Pattern pattern = Pattern.compile(
                "Flight:(.*?),(.*?)\n\\{\nDepartureAirport:(.*?)\nArrivalAirport:(.*?)\nDepatureTime:(.*?)\nArrivalTime:(.*?)\nPlane:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n\\}\n");
        Matcher matcher = pattern.matcher(stringInfo);
        if (!matcher.find())
            return null;
        String planningEntryNumber = matcher.group(2);
        String departureAirport = matcher.group(3);
        String arrivalAirport = matcher.group(4);
        String departureTime = matcher.group(5);
        String arrivalTime = matcher.group(6);
        return this.addPlanningEntry(planningEntryNumber, departureAirport, arrivalAirport, departureTime, arrivalTime);
    }

    /**
     * 
     * @param planningEntryNumber
     * @param departureAirport
     * @param arrivalAirport
     * @param departureTime
     * @param arrivalTime
     * @return the added planning entry
     */
    public FlightSchedule<Resource> addPlanningEntry(String planningEntryNumber, String departureAirport,
            String arrivalAirport, String departureTime, String arrivalTime) {
        Location location = new Location(departureAirport, arrivalAirport);
        TimeSlot timeSlot = new TimeSlot(Arrays.asList(departureTime, arrivalTime),
                Arrays.asList(departureTime, arrivalTime));
        this.collectionLocation.addAll(location.getLocations());
        PlanningEntry<Resource> flightSchedule = PlanningEntry.newPlanningEntryOfFlightSchedule(location, timeSlot,
                planningEntryNumber);
        this.planningEntries.add(flightSchedule);
        return (FlightSchedule<Resource>) flightSchedule;
    }

    @Override
    public Resource allocatePlanningEntry(String planningEntryNumber, String stringInfo) {
        if (this.getPlanningEntryByStrNumber(planningEntryNumber) == null)
            return null;
        Pattern pattern1 = Pattern.compile(
                "Flight:(.*?),(.*?)\n\\{\nDepartureAirport:(.*?)\nArrivalAirport:(.*?)\nDepatureTime:(.*?)\nArrivalTime:(.*?)\nPlane:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n\\}\n");
        Pattern pattern2 = Pattern.compile("Plane:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n");
        Matcher matcher = pattern1.matcher(stringInfo);
        if (!matcher.find()) {
            matcher = pattern2.matcher(stringInfo);
            if (!matcher.find())
                return null;
        }
        String number = matcher.group(7);
        String strType = matcher.group(8);
        int intSeats = Integer.valueOf(matcher.group(9));
        double age = Double.valueOf(matcher.group(10));
        return this.allocateResource(planningEntryNumber, number, strType, intSeats, age);
    }

    /**
     * 
     * @param planningEntryNumber
     * @param number
     * @param strType
     * @param intSeats
     * @param age
     * @return the allocated resource
     */
    public Resource allocateResource(String planningEntryNumber, String number, String strType, int intSeats,
            double age) {
        Resource plane = Resource.newResourceOfPlane(number, strType, intSeats, age);
        this.collectionResource.add(plane);
        PlanningEntry<Resource> planningEntry = this.getPlanningEntryByStrNumber(planningEntryNumber);
        ((FlightSchedule<Resource>) planningEntry).allocateResource(plane);
        return plane;
    }

    /**
     * allocate existing resource
     * @param planningEntryNumber
     * @param number
     * @return the allocated resource
     */
    public Resource allocateResource(String planningEntryNumber, String number) {
        Plane plane = (Plane) this.getPlaneOfNumber(number);
        return this.allocateResource(planningEntryNumber, number, plane.getStrType(), plane.getIntSeats(),
                plane.getAge());
    }

    /**
     * get the plane of number
     * @param number
     * @return the Plane
     */
    public Resource getPlaneOfNumber(String number) {
        Set<Resource> allResource = this.getAllResource();
        for (Resource plane : allResource)
            if (((Plane) plane).getNumber().equals(number))
                return plane;
        return null;
    }

    @Override
    public void sortPlanningEntries() {
        Comparator<PlanningEntry<Resource>> comparator = new Comparator<PlanningEntry<Resource>>() {
            @Override
            public int compare(PlanningEntry<Resource> o1, PlanningEntry<Resource> o2) {
                return ((FlightSchedule<Resource>) o1).getTimeLeaving()
                        .isBefore(((FlightSchedule<Resource>) o2).getTimeArrival()) ? -1 : 1;
            }
        };
        Collections.sort(planningEntries, comparator);
    }
}