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

public class ActivityCalendarCollection extends PlanningEntryCollection {
    @Override
    public ActivityCalendar<Resource> addPlanningEntry(String stringInfo) {
        //
        Pattern pattern = Pattern.compile(
                "Flight:(.*?),(.*?)\n\\{\nDepartureAirport:(.*?)\nArrivalAirport:(.*?)\nDepatureTime:(.*?)\nArrivalTime:(.*?)\ndocument:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n\\}\n");
        Matcher matcher = pattern.matcher(stringInfo);
        if (!matcher.find())
            return null;
        //
        String planningEntryNumber = matcher.group(2);
        String departureAirport = matcher.group(3);
        String arrivalAirport = matcher.group(4);
        String departureTime = matcher.group(5);
        String arrivalTime = matcher.group(6);
        return this.addPlanningEntry(planningEntryNumber, departureAirport, arrivalAirport, departureTime, arrivalTime);
    }

    public ActivityCalendar<Resource> addPlanningEntry(String planningEntryNumber, String departureAirport,
            String arrivalAirport, String departureTime, String arrivalTime) {
        Location location = new Location(departureAirport, arrivalAirport);
        TimeSlot timeSlot = new TimeSlot(Arrays.asList(departureTime, arrivalTime),
                Arrays.asList(departureTime, arrivalTime));
        this.collectionLocation.addAll(location.getLocations());
        PlanningEntry<Resource> ActivityCalendar = PlanningEntry.newPlanningEntryOfActivityCalendar(location, timeSlot,
                planningEntryNumber);
        this.planningEntries.add(ActivityCalendar);
        return (ActivityCalendar<Resource>) ActivityCalendar;
    }

    @Override
    public Resource allocatePlanningEntry(String planningEntryNumber, String stringInfo) {
        if (this.getPlanningEntryByStrNumber(planningEntryNumber) == null)
            return null;
        //
        Pattern pattern1 = Pattern.compile(
                "Flight:(.*?),(.*?)\n\\{\nDepartureAirport:(.*?)\nArrivalAirport:(.*?)\nDepatureTime:(.*?)\nArrivalTime:(.*?)\ndocument:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n\\}\n");
        Pattern pattern2 = Pattern.compile("document:(.*?)\n\\{\nType:(.*?)\nSeats:(.*?)\nAge:(.*?)\n\\}\n");
        Matcher matcher = pattern1.matcher(stringInfo);
        if (!matcher.find()) {
            matcher = pattern2.matcher(stringInfo);
            if (!matcher.find())
                return null;
        }
        //
        String docName = matcher.group(1);
        String strPublishDepartment = matcher.group(2);
        String strPublishDate = matcher.group(3);
        int docNumber = Integer.valueOf(matcher.group(4));
        return this.allocateResource(planningEntryNumber, docName, strPublishDepartment, strPublishDate, docNumber);
    }

    public Resource allocateResource(String planningEntryNumber, String docName, String strPublishDepartment,
            String strPublishDate, int docNumber) {
        Resource document = Resource.newResourceOfDocument(docName, strPublishDepartment, strPublishDate);
        this.collectionResource.add(document);
        PlanningEntry<Resource> planningEntry = this.getPlanningEntryByStrNumber(planningEntryNumber);
        ((ActivityCalendar<Resource>) planningEntry).allocateResource(document, docNumber);
        return document;
    }

    public Resource allocateResource(String planningEntryNumber, String docName, int docNumber) {
        Document document = (Document) this.getDocumentOfName(docName);
        return this.allocateResource(planningEntryNumber, docName, document.getStrPublishDepartment(),
                document.getPublishDate().toString().replace('T', ' '), docNumber);
    }

    public Resource getDocumentOfName(String docName) {
        Set<Resource> allResource = this.getAllResource();
        for (Resource document : allResource)
            if (((Document) document).getDocName().equals(docName))
                return document;
        return null;
    }

    @Override
    public void sortPlanningEntries() {
        Comparator<PlanningEntry<Resource>> comparator = new Comparator<PlanningEntry<Resource>>() {
            @Override
            public int compare(PlanningEntry<Resource> o1, PlanningEntry<Resource> o2) {
                return ((ActivityCalendar<Resource>) o1).getBeginningTime()
                        .isBefore(((ActivityCalendar<Resource>) o2).getEndingTime()) ? -1 : 1;
            }
        };
        Collections.sort(planningEntries, comparator);
    }
}