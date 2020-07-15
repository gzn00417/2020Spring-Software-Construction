package planningEntryCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import planningEntry.*;
import resource.*;

/**
 * planning entry collection is used to:
 * manage resource, locations;
 * generate / cancel / allocate / start / block / finish a planning entry;
 * ask the current state
 * search the conflict in the set of planning entry ( location / resource )
 * present all the plan that one chosen resource has been used (Waiting, Running, Ended)
 * show the board
 */
public abstract class PlanningEntryCollection {
    protected List<PlanningEntry<Resource>> planningEntries = new ArrayList<>();
    protected Set<Resource> collectionResource = new HashSet<>();
    protected Set<String> collectionLocation = new HashSet<>();

    /**
     * generate an instance of planning entry
     * @param stringInfo the input string array of planning entry information
     * @return a new instance
     */
    public abstract PlanningEntry<Resource> addPlanningEntry(String stringInfo);

    /**
     * search for a planning entry whose number matches the given
     * @param planningEntryNumber
     * @return the planning entry
     */
    public PlanningEntry<Resource> getPlanningEntryByStrNumber(String planningEntryNumber) {
        for (PlanningEntry<Resource> planningEntry : planningEntries)
            if (planningEntry.getPlanningEntryNumber().equals(planningEntryNumber))
                return planningEntry;
        return null;
    }

    /**
     * cancel a plan
     * @param planningEntryNumber
     * @return true if cancelled successfully
     */
    public Boolean cancelPlanningEntry(String planningEntryNumber) {
        PlanningEntry<Resource> planningEntry = this.getPlanningEntryByStrNumber(planningEntryNumber);
        return planningEntry == null ? false : planningEntry.cancel();
    }

    /**
     * allocate one plan available resource
     * @param planningEntryNumber
     * @param stringInfo the input string array containing resource or whole planning entry
     * @return the resource allocated
     */
    public abstract Resource allocatePlanningEntry(String planningEntryNumber, String stringInfo);

    /**
     * start the planning entry
     * @param planningEntryNumber
     * @return true if the start is successful
     */
    public Boolean startPlanningEntry(String planningEntryNumber) {
        PlanningEntry<Resource> planningEntry = this.getPlanningEntryByStrNumber(planningEntryNumber);
        return planningEntry == null ? false : planningEntry.start();
    }

    /**
     * block the planning entry
     * @param planningEntryNumber
     * @return true if it is blocked
     */
    public Boolean blockPlanningEntry(String planningEntryNumber) {
        PlanningEntry<Resource> planningEntry = this.getPlanningEntryByStrNumber(planningEntryNumber);
        return planningEntry == null ? false : planningEntry.block();
    }

    /**
     * finish the planning entry
     * @param planningEntryNumber
     * @return true if the planning entry is ended
     */
    public Boolean finishPlanningEntry(String planningEntryNumber) {
        PlanningEntry<Resource> planningEntry = this.getPlanningEntryByStrNumber(planningEntryNumber);
        return planningEntry == null ? false : planningEntry.finish();
    }

    /**
     * get the list of planning entries
     * @return the list of planning entries
     */
    public List<PlanningEntry<Resource>> getAllPlanningEntries() {
        return this.planningEntries;
    }

    /**
     * get the set of resources
     * @return the set of resources
     */
    public Set<Resource> getAllResource() {
        return this.collectionResource;
    }

    /**
     * get the set of locations
     * @return the set of String of locations
     */
    public Set<String> getAllLocation() {
        return collectionLocation;
    }

    /**
     * delete chosen resource
     * @param resource
     * @return true if delete successfully
     */
    public boolean deleteResource(Resource resource) {
        return collectionResource.remove(resource);
    }

    /**
     * delete chosen location
     * @param location
     * @return true if delete successfully
     */
    public boolean deleteLocation(String location) {
        return collectionLocation.remove(location);
    }

    /**
     * sort planning entries
     */
    public abstract void sortPlanningEntries();
}