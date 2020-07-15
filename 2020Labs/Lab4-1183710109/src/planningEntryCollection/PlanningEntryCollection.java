package planningEntryCollection;

import java.util.ArrayList;
import java.util.List;

import exceptions.*;
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
    protected List<Resource> collectionResource = new ArrayList<>();
    protected List<String> collectionLocation = new ArrayList<>();

    /**
     * generate an instance of planning entry
     * @param stringInfo the input string array of planning entry information
     * @return a new instance
     * @throws Exception
     */
    public abstract PlanningEntry<Resource> addPlanningEntry(String stringInfo) throws Exception;

    /**
     * search for a planning entry whose number matches the given
     * @param planningEntryNumber
     * @return the planning entry
     */
    public PlanningEntry<Resource> getPlanningEntryByStrNumber(String planningEntryNumber) {
        assert (!planningEntryNumber.isBlank());
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
     * @throws Exception
     */
    public abstract Resource allocatePlanningEntry(String planningEntryNumber, String stringInfo) throws Exception;

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
    public List<Resource> getAllResource() {
        return this.collectionResource;
    }

    /**
     * get the set of locations
     * @return the set of String of locations
     */
    public List<String> getAllLocation() {
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

    /**
     * check plane information consistent
     * @throws PlaneInconsistentInfoException
     */
    public void checkPlaneConsistentInfo() throws PlaneInconsistentInfoException {
        List<Resource> planes = this.getAllResource();
        for (Resource r1 : planes) {
            for (Resource r2 : planes) {
                if (r1 != r2) {
                    Plane p1 = (Plane) r1, p2 = (Plane) r2;
                    if (p1.getNumber().equals(p2.getNumber()) && !p1.equals(p2))
                        throw new PlaneInconsistentInfoException(p1.getNumber() + " is inconsistent.");
                }
            }
        }
    }

    /**
     * check same entry in different days
     * @throws SameEntrySameDayException
     */
    public void checkSameEntryDiffDay() throws SameEntrySameDayException {
        List<PlanningEntry<Resource>> entries = this.getAllPlanningEntries();
        int n = entries.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    PlanningEntry<Resource> e1 = entries.get(i), e2 = entries.get(j);
                    if (e1.getPlanningEntryNumber().equals(e2.getPlanningEntryNumber())) {
                        if (((CommonPlanningEntry<Resource>) e1).getPlanningDate()
                                .isEqual(((CommonPlanningEntry<Resource>) e2).getPlanningDate()))
                            throw new SameEntrySameDayException(
                                    e1.getPlanningEntryNumber() + " has two entries in one day.");
                    }
                }
            }
        }
    }
}