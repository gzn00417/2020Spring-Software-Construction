package planningEntryAPIs;

import planningEntry.*;
import resource.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class PlanningEntryAPIs {
    /**
     * For Activity Calendar
     * check locations of planning entry in entries if they are conflicted
     * @param entries
     * @return true if there are locations conflict
     */
    public abstract boolean checkLocationConflict(List<PlanningEntry<Resource>> entries);

    /**
     * For Flight Schedule and Train Schedule
     * check the resource exclusive conflict
     * @param entries
     * @return true if there are conflicts
     */
    public static boolean checkResourceExclusiveConflict(List<PlanningEntry<Resource>> entries) {
        Map<Resource, List<PlanningEntry<Resource>>> resourceMap = new HashMap<>();
        for (int i = 0; i < entries.size(); i++) {
            PlanningEntry<Resource> planningEntry = entries.get(i);
            Resource resource = planningEntry.getResource();
            if (resourceMap.keySet().contains(resource)) {
                List<PlanningEntry<Resource>> planningEntries = new ArrayList<>();
                planningEntries.addAll(resourceMap.get(resource));
                planningEntries.add(planningEntry);
                for (PlanningEntry<Resource> p1 : planningEntries) {
                    for (PlanningEntry<Resource> p2 : planningEntries)
                        if (!p1.equals(p2)) {
                            LocalDateTime t1b = ((FlightSchedule<Resource>) p1).getTimeLeaving(),
                                    t1e = ((FlightSchedule<Resource>) p1).getTimeArrival(),
                                    t2b = ((FlightSchedule<Resource>) p2).getTimeLeaving(),
                                    t2e = ((FlightSchedule<Resource>) p2).getTimeArrival();
                            if ((t1b.isBefore(t2e) || t1b.isEqual(t2e)) && (t1e.isAfter(t2b) || t2e.isEqual(t2b)))
                                return true;
                        }
                }
                resourceMap.remove(resource);
                resourceMap.put(resource, planningEntries);
            } else {
                resourceMap.put(resource, new ArrayList<PlanningEntry<Resource>>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(planningEntry);
                    }
                });
            }
        }
        return false;
    }

    /**
     * find the closest entry using the same resource with e
     * @param r
     * @param e
     * @param entries
     * @return the pre entry
     */
    public static PlanningEntry<Resource> findPreEntryPerResource(Resource r, PlanningEntry<Resource> e,
            List<PlanningEntry<Resource>> entries) {
        LocalDateTime latestDateTime = LocalDateTime.MIN;
        PlanningEntry<Resource> prePlanningEntry = null;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getResource().equals(e.getResource())) {
                PlanningEntry<Resource> planningEntry = entries.get(i);
                LocalDateTime endingTime = ((FlightSchedule<Resource>) planningEntry).getTimeArrival();
                if (endingTime.isAfter(latestDateTime)
                        && endingTime.isBefore(((FlightSchedule<Resource>) e).getTimeLeaving())) {
                    latestDateTime = endingTime;
                    prePlanningEntry = planningEntry;
                }
            }
        }
        return prePlanningEntry;
    }
}