package board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Vector;

import planningEntry.*;
import planningEntryCollection.*;
import resource.*;

public class ActivityBoard extends Board {
    /**
     * choose flights within HOURS_RANGE before or later
     */
    private static final int HOURS_RANGE = 1;
    /**
     * visualization label of arrival
     */
    public static final int BEGINNING = 1;
    /**
     * visualization label of leaving
     */
    public static final int ENDING = -1;

    public ActivityBoard(PlanningEntryCollection planningEntryCollection) {
        super(planningEntryCollection);
    }

    @Override
    public void visualize(String strCurrentTime, String strLocation, int intType) {
        Iterator<PlanningEntry<Resource>> iterator = super.iterator();
        Vector<Vector<?>> vData = new Vector<>();
        Vector<String> vName = new Vector<>();
        String[] columnsNames = new String[] { "Time", "Entry Number", "Location", "State" };
        for (String name : columnsNames)
            vName.add(name);
        while (iterator.hasNext()) {
            ActivityCalendar<Resource> planningEntry = (ActivityCalendar<Resource>) iterator.next();
            if (!strLocation.isEmpty()) {
                if (!planningEntry.getStrLocation().toLowerCase().equals(strLocation.toLowerCase()))
                    continue;
            }
            LocalDateTime currentTime = LocalDateTime.parse(strCurrentTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime scheduleTime = intType == BEGINNING ? planningEntry.getBeginningTime()
                    : planningEntry.getEndingTime();
            if (scheduleTime.isBefore(currentTime.plusHours(HOURS_RANGE))
                    && scheduleTime.isAfter(currentTime.minusHours(HOURS_RANGE))) {
                String strScheduleTime = scheduleTime.toString().substring(11);
                String planningEntryNumber = planningEntry.getPlanningEntryNumber();
                String location = planningEntry.getStrLocation();
                String state = planningEntry.getState().getStrState();
                Vector<String> vRow = new Vector<>();
                vRow.add(strScheduleTime);
                vRow.add(planningEntryNumber);
                vRow.add(location);
                vRow.add(state);
                vData.add((Vector<?>) vRow.clone());
            }
        }
        makeTable(vData, vName, intType == BEGINNING ? "Beginning" : "Ending");
    }

    @Override
    public void showEntries(Resource r) {
        Iterator<PlanningEntry<Resource>> iterator = super.iterator();
        Vector<Vector<?>> vData = new Vector<>();
        Vector<String> vName = new Vector<>();
        String[] columnsNames = new String[] { "Time", "Entry Number", "Location", "State" };
        for (String name : columnsNames)
            vName.add(name);
        while (iterator.hasNext()) {
            ActivityCalendar<Resource> planningEntry = (ActivityCalendar<Resource>) iterator.next();
            if (planningEntry.getResource() != null && !planningEntry.getResource().equals(r))
                continue;
            String strScheduleTime = planningEntry.getBeginningTime().toString();
            String planningEntryNumber = planningEntry.getPlanningEntryNumber();
            String location = planningEntry.getStrLocation();
            String state = planningEntry.getState().getStrState();
            Vector<String> vRow = new Vector<>();
            vRow.add(strScheduleTime);
            vRow.add(planningEntryNumber);
            vRow.add(location);
            vRow.add(state);
            vData.add((Vector<?>) vRow.clone());
        }
        super.makeTable(vData, vName, "Entries");
    }
}