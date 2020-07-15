package planningEntryAPIs;

import planningEntry.*;
import resource.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlanningEntryAPIsFirst extends PlanningEntryAPIs {
    @Override
    public boolean checkLocationConflict(List<PlanningEntry<Resource>> entries) {
        Map<String, List<ActivityCalendar<Resource>>> locationMap = new HashMap<>();
        for (int i = 0; i < entries.size(); i++) {
            ActivityCalendar<Resource> activityCalendar = (ActivityCalendar<Resource>) entries.get(i);
            String strLocation = activityCalendar.getStrLocation();
            if (locationMap.keySet().contains(strLocation)) {
                List<ActivityCalendar<Resource>> calendars = new ArrayList<>();
                calendars.addAll(locationMap.get(strLocation));
                calendars.add(activityCalendar);
                for (ActivityCalendar<Resource> c1 : calendars) {
                    for (ActivityCalendar<Resource> c2 : calendars)
                        if (!c1.equals(c2)) {
                            LocalDateTime t1b = c1.getBeginningTime(), t1e = c1.getEndingTime();
                            LocalDateTime t2b = c2.getBeginningTime(), t2e = c2.getEndingTime();
                            if ((t1b.isBefore(t2e) || t1b.isEqual(t2e)) && (t1e.isAfter(t2b) || t2e.isEqual(t2b)))
                                return true;
                        }
                }
                locationMap.remove(strLocation);
                locationMap.put(strLocation, calendars);
            } else {
                locationMap.put(strLocation, new ArrayList<ActivityCalendar<Resource>>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(activityCalendar);
                    }
                });
            }
        }
        return false;
    }
}