package board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Vector;

import planningEntry.*;
import planningEntryCollection.*;
import resource.*;

public class TrainBoard extends Board {
    /**
     * choose flights within HOURS_RANGE before or later
     */
    private static final int HOURS_RANGE = 1;
    /**
     * visualization label of arrival
     */
    public static final int ARRIVAL = 1;
    /**
     * visualization label of leaving
     */
    public static final int LEAVING = -1;

    public TrainBoard(PlanningEntryCollection planningEntryCollection) {
        super(planningEntryCollection);
    }

    @Override
    public void visualize(String strCurrentTime, String strLocation, int intType) {
        if (strLocation.isEmpty())
            return;
        Iterator<PlanningEntry<Resource>> iterator = super.iterator();
        Vector<Vector<?>> vData = new Vector<>();
        Vector<String> vName = new Vector<>();
        String[] columnsNames = new String[] { "Time", "Entry Number", "Origin", "", "Terminal", "State" };
        for (String name : columnsNames)
            vName.add(name);
        while (iterator.hasNext()) {
            TrainSchedule<Resource> planningEntry = (TrainSchedule<Resource>) iterator.next();
            LocalDateTime currentTime = LocalDateTime.parse(strCurrentTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime scheduleTime = null;
            for (int i = 0; i < planningEntry.LENGTH; i++) {
                if (planningEntry.getLocationOfIndex(i).equals(strLocation)) {
                    scheduleTime = intType == ARRIVAL ? planningEntry.getArrivalTimeOfIndex(i)
                            : planningEntry.getLeavingTimeOfIndex(i);
                }
            }
            if (scheduleTime == null)
                continue;
            if (scheduleTime.isBefore(currentTime.plusHours(HOURS_RANGE))
                    && scheduleTime.isAfter(currentTime.minusHours(HOURS_RANGE))) {
                String strScheduleTime = scheduleTime.toString().substring(11);
                String planningEntryNumber = planningEntry.getPlanningEntryNumber();
                String locationOrigin = planningEntry.getLocationOfIndex(planningEntry.ORIGIN);
                String locationTerminal = planningEntry.getLocationOfIndex(planningEntry.TERMINAL);
                String state = planningEntry.getState().getStrState();
                Vector<String> vRow = new Vector<>();
                vRow.add(strScheduleTime);
                vRow.add(planningEntryNumber);
                vRow.add(locationOrigin);
                vRow.add("-->");
                vRow.add(locationTerminal);
                vRow.add(state);
                vData.add((Vector<?>) vRow.clone());
            }
        }
        makeTable(vData, vName, intType == ARRIVAL ? "Arrival" : "Leaving");
    }

    @Override
    public void showEntries(Resource r) {
        Iterator<PlanningEntry<Resource>> iterator = super.iterator();
        Vector<Vector<?>> vData = new Vector<>();
        Vector<String> vName = new Vector<>();
        String[] columnsNames = new String[] { "Time", "Entry Number", "Origin", "", "Terminal", "State" };
        for (String name : columnsNames)
            vName.add(name);
        while (iterator.hasNext()) {
            TrainSchedule<Resource> planningEntry = (TrainSchedule<Resource>) iterator.next();
            if (planningEntry.getResource() != null && !planningEntry.getResource().equals(r))
                continue;
            String strScheduleTime = planningEntry.getLeavingTimeOfIndex(planningEntry.ORIGIN).toString() + " - "
                    + planningEntry.getArrivalTimeOfIndex(planningEntry.TERMINAL);
            String planningEntryNumber = planningEntry.getPlanningEntryNumber();
            String locationOrigin = planningEntry.getLocationOfIndex(planningEntry.ORIGIN);
            String locationTerminal = planningEntry.getLocationOfIndex(planningEntry.TERMINAL);
            String state = planningEntry.getState().getStrState();
            Vector<String> vRow = new Vector<>();
            vRow.add(strScheduleTime);
            vRow.add(planningEntryNumber);
            vRow.add(locationOrigin);
            vRow.add("-->");
            vRow.add(locationTerminal);
            vRow.add(state);
            vData.add((Vector<?>) vRow.clone());
        }
        super.makeTable(vData, vName, "Entries");
    }

}