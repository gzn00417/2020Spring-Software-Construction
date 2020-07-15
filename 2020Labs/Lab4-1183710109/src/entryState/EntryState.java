package entryState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * state of one planning entry
 */
public class EntryState {
    private EntryStateEnum state;

    /*
     * AF:
     * the state enum's name represents the state 
     * RI:
     * state must be in enums
     * Safety:
     * it's a mutable object, but do not let the outside modify state directly
     */

    /**
     * constructor
     * @param stateName
     */
    public EntryState(String stateName) {
        this.state = EntryStateEnum.valueOf(stateName.toUpperCase());
        checkRep();
    }

    /**
     * check Rep
     */
    private void checkRep() {
        assert (state != null);
    }

    /**
     * set the new state
     * @param strPlanningEntryType in {"FlightSchedule", "TrainSchedule", "ActivityCalendar"}
     * @param strNewState
     * @return true if the setting is successful, false if not
     */
    public Boolean setNewState(String strPlanningEntryType, String strNewState) {
        assert (strPlanningEntryType.toLowerCase().contains("train")
                || !this.getStrState().toLowerCase().equals("blocked"));
        if (this.setAvailability(strPlanningEntryType, strNewState.toUpperCase())) {
            this.state = EntryStateEnum.valueOf(strNewState.toUpperCase());
            assert (strPlanningEntryType.toLowerCase().contains("train")
                    || !this.getStrState().toLowerCase().equals("blocked"));
            return true;
        }
        return false;
    }

    /**
     * judge whether this state can be transferred to the new state
     * @param strPlanningEntryType in {"FlightSchedule", "TrainSchedule", "ActivityCalendar"}
     * @param strNewState
     * @return true if the current state can be transferred to the new state, false if not
     */
    private Boolean setAvailability(String strPlanningEntryType, String strNewState) {
        List<EntryStateEnum> availableStatesList = new ArrayList<EntryStateEnum>(
                Arrays.asList(this.getState().newStateAchievable(strPlanningEntryType)));
        return availableStatesList.contains(EntryStateEnum.valueOf(strNewState.toUpperCase()));
    }

    public String getStrState() {
        return state.name();
    }

    public EntryStateEnum getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EntryState)) {
            return false;
        }
        EntryState entryState = (EntryState) o;
        return Objects.equals(state, entryState.state);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(state);
    }

    @Override
    public String toString() {
        return "{" + " state='" + getStrState() + "'" + "}";
    }
}
