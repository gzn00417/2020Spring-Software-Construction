package board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

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
    /**
     * 
     */
    public static final String INFO = "INFO", WARNING = "WARNING", SEVERE = "SEVERE";

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

    /**
     * show logs
     * @param askedTime
     * @param logType
     * @param appType
     * @throws IOException
     */
    public void showLog(String askedTime, String logType, String appType) throws IOException {
        long WITHIN_MINUTE = 1;
        LocalDateTime askingTime = null;
        if (!askedTime.isBlank()) {
            askingTime = LocalDateTime.parse(askedTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        }
        Vector<Vector<?>> vData = new Vector<>();
        Vector<String> vName = new Vector<>();
        String[] columnsNames = new String[] { "Time", "Log Type", "AppType", "Action", "Message" };
        for (String name : columnsNames)
            vName.add(name);
        BufferedReader bReader = new BufferedReader(new FileReader(new File("log/FlightScheduleLog.txt")));
        String line = "";
        Pattern pattern1 = Pattern.compile("(.*?) apps\\.(.*?)App (.*?)\\.");
        Pattern pattern2 = Pattern.compile("([A-Z]*?): (.*?)\\.+");
        while ((line = bReader.readLine()) != null) {
            if (line.isBlank())
                break;
            Matcher matcher1 = pattern1.matcher(line);
            if (!matcher1.find())
                break;
            line = bReader.readLine();
            Matcher matcher2 = pattern2.matcher(line);
            if (!matcher2.find())
                break;
            String timeString = matcher1.group(1);
            String logType1 = matcher2.group(1);
            String appType1 = matcher1.group(2);
            String action = matcher1.group(3);
            String message = matcher2.group(2);
            LocalDateTime timeFormatted = LocalDateTime.parse(timeString,
                    DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss a"));
            System.out.println(timeFormatted.toString());
            if (askingTime == null || (askingTime.plusMinutes(WITHIN_MINUTE).isAfter(timeFormatted)
                    && askingTime.minusMinutes(WITHIN_MINUTE).isBefore(timeFormatted))) {
                if (logType.isBlank() || logType.equals(logType1)) {
                    if (appType.isBlank() || appType.equals(appType1)) {
                        Vector<String> vRow = new Vector<>();
                        vRow.add(timeString);
                        vRow.add(logType1);
                        vRow.add(appType1);
                        vRow.add(action);
                        vRow.add(message);
                        vData.add((Vector<?>) vRow.clone());
                    }
                }
            }
        }
        bReader.close();
        super.makeTable(vData, vName, "Log");
    }
}