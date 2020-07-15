package board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Vector;

import planningEntry.*;
import planningEntryCollection.*;
import resource.*;

public class FlightBoard extends Board {
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

    public FlightBoard(PlanningEntryCollection planningEntryCollection) {
        super(planningEntryCollection);
    }

    @Override
    public void visualize(String strCurrentTime, String strLocation, int intType) {
        // iterator
        Iterator<PlanningEntry<Resource>> iterator = super.iterator();
        // new 2D-vector
        Vector<Vector<?>> vData = new Vector<>();
        // new titles
        Vector<String> vName = new Vector<>();
        String[] columnsNames = new String[] { "Time", "Entry Number", "Origin", "", "Terminal", "State" };
        for (String name : columnsNames)
            vName.add(name);
        while (iterator.hasNext()) {
            FlightSchedule<Resource> planningEntry = (FlightSchedule<Resource>) iterator.next();
            // if the location isn't chosen, then the board be as all airports'
            if (!strLocation.isEmpty()) {
                if (intType == FlightBoard.ARRIVAL) {
                    if (!planningEntry.getLocationTerminal().toLowerCase().equals(strLocation.toLowerCase()))
                        continue;
                } else {
                    if (!planningEntry.getLocationOrigin().toLowerCase().equals(strLocation.toLowerCase()))
                        continue;
                }
            }
            // time
            LocalDateTime currentTime = LocalDateTime.parse(strCurrentTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime scheduleTime = intType == FlightBoard.ARRIVAL ? planningEntry.getTimeArrival()
                    : planningEntry.getTimeLeaving();
            // check time in range
            if (scheduleTime.isBefore(currentTime.plusHours(HOURS_RANGE))
                    && scheduleTime.isAfter(currentTime.minusHours(HOURS_RANGE))) {
                // get information
                String strScheduleTime = scheduleTime.toString().substring(11);
                String planningEntryNumber = planningEntry.getPlanningEntryNumber();
                String locationOrigin = planningEntry.getLocationOrigin();
                String locationTerminal = planningEntry.getLocationTerminal();
                String state = planningEntry.getState().getStrState();
                // load in 1D vector
                Vector<String> vRow = new Vector<>();
                vRow.add(strScheduleTime);
                vRow.add(planningEntryNumber);
                vRow.add(locationOrigin);
                vRow.add("-->");
                vRow.add(locationTerminal);
                vRow.add(state);
                // add in 2D-vector
                vData.add((Vector<?>) vRow.clone());
            }
        }
        // visualization (extends from Board.maketable)
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
            FlightSchedule<Resource> planningEntry = (FlightSchedule<Resource>) iterator.next();
            if (planningEntry.getResource() != null && !planningEntry.getResource().equals(r))
                continue;
            String strScheduleTime = planningEntry.getTimeLeaving() + " - " + planningEntry.getTimeArrival();
            String planningEntryNumber = planningEntry.getPlanningEntryNumber();
            String locationOrigin = planningEntry.getLocationOrigin();
            String locationTerminal = planningEntry.getLocationTerminal();
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