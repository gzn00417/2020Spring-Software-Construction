package planningEntryCollection;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.*;
import location.*;
import planningEntry.*;
import resource.*;
import timeSlot.*;

public class FlightScheduleCollection extends PlanningEntryCollection {
    @Override
    public FlightSchedule<Resource> addPlanningEntry(String stringInfo) throws Exception {
        Pattern pattern = Pattern.compile(
                "Flight:(.*?),(.*?)\n\\{\nDepartureAirport:(.*?)\nArrivalAirport:(.*?)\nDepatureTime:(.*?)\nArrivalTime:(.*?)\nPlane:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n\\}\n");
        Matcher matcher = pattern.matcher(stringInfo);
        if (!matcher.find()) {
            throw new DataPatternException("Data: " + stringInfo + " mismatch Pattern.");
        }
        String planningEntryNumber = matcher.group(2);
        checkEntryNumber(planningEntryNumber);
        String departureAirport = matcher.group(3);
        String arrivalAirport = matcher.group(4);
        checkDiffAirport(departureAirport, arrivalAirport);
        String departureTime = matcher.group(5);
        String arrivalTime = matcher.group(6);
        checkTime(departureTime, arrivalTime);
        return this.addPlanningEntry(planningEntryNumber, departureAirport, arrivalAirport, departureTime, arrivalTime);
    }

    /**
     * generate a planning entry by given params
     * @param planningEntryNumber
     * @param departureAirport
     * @param arrivalAirport
     * @param departureTime
     * @param arrivalTime
     * @return the flight schedule
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
    public Resource allocatePlanningEntry(String planningEntryNumber, String stringInfo) throws Exception {
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
        checkPlaneNumber(number);
        String strType = matcher.group(8);
        checkPlaneType(strType);
        String strSeats = matcher.group(9);
        checkPlaneSeat(strSeats);
        String strAge = matcher.group(10);
        checkPlaneAge(strAge);
        return this.allocateResource(planningEntryNumber, number, strType, Integer.valueOf(strSeats),
                Double.valueOf(strAge));
    }

    /**
     * allocate resource with concrete params
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
     * allocate one plan available resource
     * @param flightSchedule
     * @param stringInfo the input string array containing resource or whole planning entry
     * @return the resource allocated
     * @throws Exception
     */
    public Resource allocatePlanningEntry(FlightSchedule<Resource> flightSchedule, String stringInfo) throws Exception {
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
        checkPlaneNumber(number);
        String strType = matcher.group(8);
        checkPlaneType(strType);
        String strSeats = matcher.group(9);
        checkPlaneSeat(strSeats);
        String strAge = matcher.group(10);
        checkPlaneAge(strAge);
        Resource plane = Resource.newResourceOfPlane(number, strType, Integer.valueOf(strSeats),
                Double.valueOf(strAge));
        this.collectionResource.add(plane);
        flightSchedule.allocateResource(plane);
        return plane;
    }

    /**
     * get the plane of number
     * @param number
     * @return the Plane
     */
    public Resource getPlaneOfNumber(String number) {
        List<Resource> allResource = this.getAllResource();
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

    /**
     * check entry number
     * @param planningEntryNumber
     * @throws EntryNumberFormatException
     */
    public static void checkEntryNumber(String planningEntryNumber) throws EntryNumberFormatException {
        if (Character.isUpperCase(planningEntryNumber.charAt(0))
                && Character.isUpperCase(planningEntryNumber.charAt(1))) {
            for (int i = 2; i < planningEntryNumber.length(); i++) {
                if (!Character.isDigit(planningEntryNumber.charAt(i)))
                    throw new EntryNumberFormatException(planningEntryNumber + " has incorrect format.");
            }
        } else
            throw new EntryNumberFormatException(planningEntryNumber + " has incorrect format.");
    }

    /**
     * check airports are different
     * @param departureAirport
     * @param arrivalAirport
     * @throws SameAirportException
     */
    public static void checkDiffAirport(String departureAirport, String arrivalAirport) throws SameAirportException {
        if (departureAirport.equals(arrivalAirport))
            throw new SameAirportException(departureAirport + " is the same with " + arrivalAirport + " .");
    }

    /**
     * check time format and departure is before arrival
     * @param departureTime
     * @param arrivalTime
     * @throws TimeOrderException
     * @throws DateTimeParseException
     */
    public static void checkTime(String departureTime, String arrivalTime)
            throws TimeOrderException, DateTimeParseException {
        LocalDateTime dt = null, at = null;
        try {
            dt = LocalDateTime.parse(departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            at = LocalDateTime.parse(arrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            throw new DateTimeParseException("The date time is not matched.", departureTime + arrivalTime, 0);
        } finally {
            if (dt != null && at != null) {
                if (!dt.isBefore(at))
                    throw new TimeOrderException(
                            "Departure time " + departureTime + " is not before arrival time " + arrivalTime + " .");
            }
        }
    }

    /**
     * check plane number
     * @param planeNumber
     * @throws PlaneNumberFormatException
     */
    public static void checkPlaneNumber(String planeNumber) throws PlaneNumberFormatException {
        if (planeNumber.length() == 5 && (planeNumber.charAt(0) == 'N' || planeNumber.charAt(0) == 'B')) {
            for (int i = 1; i < planeNumber.length(); i++) {
                if (!Character.isDigit(planeNumber.charAt(i)))
                    throw new PlaneNumberFormatException(planeNumber + " has incorrect format.");
            }
        } else
            throw new PlaneNumberFormatException(planeNumber + " has incorrect format.");
    }

    /**
     * check plane type
     * @param strType
     * @throws PlaneTypeException
     */
    public static void checkPlaneType(String strType) throws PlaneTypeException {
        for (int i = 0; i < strType.length(); i++) {
            char ch = strType.charAt(i);
            if (!(Character.isAlphabetic(ch) || Character.isDigit(ch)))
                throw new PlaneTypeException(strType + " has incorrect format.");
        }
    }

    /**
     * check plane seat range
     * @param strSeats
     * @throws PlaneSeatRangeException
     */
    public static void checkPlaneSeat(String strSeats) throws PlaneSeatRangeException {
        int intSeats = Integer.valueOf(strSeats);
        if (intSeats < 50 || intSeats > 600)
            throw new PlaneSeatRangeException(intSeats + " is not in [50, 600].");
    }

    /**
     * check plane age format
     * @param strAge
     * @throws PlaneAgeFormatException
     */
    public static void checkPlaneAge(String strAge) throws PlaneAgeFormatException {
        double age = Double.valueOf(strAge);
        if (strAge.indexOf(".") < strAge.length() - 2 || age < 0 || age > 30)
            throw new PlaneAgeFormatException(strAge + " has incorrect format.");
    }

    /**
    * check dates and numbers conflict
    * @throws SameEntryException
    */
    public void checkDateNumberConflict() throws SameEntryException {
        List<PlanningEntry<Resource>> entries = this.getAllPlanningEntries();
        int n = entries.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    FlightSchedule<Resource> e1 = (FlightSchedule<Resource>) entries.get(i),
                            e2 = (FlightSchedule<Resource>) entries.get(j);
                    if (e1.getPlanningEntryNumber().equals(e2.getPlanningEntryNumber())) {
                        if (((Plane) e1.getResource()).equals((Plane) e2.getResource()))
                            throw new SameEntryException(e1.getPlanningEntryNumber() + " and "
                                    + e2.getPlanningEntryNumber() + " are the same entries.");
                    }
                }
            }
        }
    }

    /**
    * check gap between leaving and arrival
    * @throws HugeTimeGapException
    */
    public void checkTimeGap() throws HugeTimeGapException {
        List<PlanningEntry<Resource>> entries = this.getAllPlanningEntries();
        int n = entries.size();
        for (int i = 0; i < n; i++) {
            FlightSchedule<Resource> e = (FlightSchedule<Resource>) entries.get(i);
            LocalDateTime t1 = e.getTimeLeaving(), t2 = e.getTimeArrival();
            if (t1.plusDays(1).isBefore(t2))
                throw new HugeTimeGapException(t1.toString() + " is too early than " + t2.toString());
        }
    }

    /**
     * check entry information consistent
     * @throws EntryInconsistentInfoException
     */
    public void checkEntryConsistentInfo() throws EntryInconsistentInfoException {
        List<PlanningEntry<Resource>> entries = this.getAllPlanningEntries();
        int n = entries.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    FlightSchedule<Resource> e1 = (FlightSchedule<Resource>) entries.get(i),
                            e2 = (FlightSchedule<Resource>) entries.get(j);
                    if (e1.getPlanningEntryNumber().equals(e2.getPlanningEntryNumber())) {
                        LocalTime t11 = e1.getTimeLeaving().toLocalTime(), t12 = e1.getTimeArrival().toLocalTime(),
                                t21 = e2.getTimeLeaving().toLocalTime(), t22 = e2.getTimeArrival().toLocalTime();
                        if (!(t11.equals(t21) && t12.equals(t22)) || !e1.getLocation().equals(e2.getLocation()))
                            throw new EntryInconsistentInfoException(e1.getPlanningEntryNumber() + " is inconsistent.");
                    }
                }
            }
        }
    }
}