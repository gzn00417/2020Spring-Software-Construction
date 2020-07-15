package entryState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * enum of entry state
 */
public enum EntryStateEnum {
    /**
     * they represent 6 states of the planning entry
     */
    WAITING, ALLOCATED, RUNNING, BLOCKED, ENDED, CANCELLED;

    /**
     * achievable states map for entries able to be blocked
     */
    public static final Map<EntryStateEnum, EntryStateEnum[]> newStateAchievableBlockedAble = new HashMap<EntryStateEnum, EntryStateEnum[]>() {
        private static final long serialVersionUID = 1L;
        {
            put(WAITING, new EntryStateEnum[] { ALLOCATED, CANCELLED });
            put(ALLOCATED, new EntryStateEnum[] { RUNNING, CANCELLED });
            put(RUNNING, new EntryStateEnum[] { BLOCKED, ENDED });
            put(BLOCKED, new EntryStateEnum[] { RUNNING, CANCELLED });
            put(CANCELLED, new EntryStateEnum[] {});
            put(ENDED, new EntryStateEnum[] {});
        }
    };

    /**
     * achievable states map for entries not able to be blocked
     */
    public static final Map<EntryStateEnum, EntryStateEnum[]> newStateAchievableBlockedDisable = new HashMap<EntryStateEnum, EntryStateEnum[]>() {
        private static final long serialVersionUID = 1L;
        {
            put(WAITING, new EntryStateEnum[] { ALLOCATED, CANCELLED });
            put(ALLOCATED, new EntryStateEnum[] { RUNNING, CANCELLED });
            put(RUNNING, new EntryStateEnum[] { ENDED });
            put(CANCELLED, new EntryStateEnum[] {});
            put(ENDED, new EntryStateEnum[] {});
        }
    };

    /**
     * define which is able to be blocked
     */
    public static final List<String> BlockAbleKeyWords = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("Train");
        }
    };

    /**
     * get all states achievable
     * @param strPlanningEntryType
     * @return array of the states
     */
    public EntryStateEnum[] newStateAchievable(String strPlanningEntryType) {
        for (String str : BlockAbleKeyWords)
            if (strPlanningEntryType.contains(str))
                return EntryStateEnum.newStateAchievableBlockedAble.get(this);
        return EntryStateEnum.newStateAchievableBlockedDisable.get(this);
    }
}