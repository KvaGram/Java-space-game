package unicus.spacegame.crew;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The CrewmanState defines the state of life of crewman is.
 * Every implementation of AbstractCrewman must set its crewman state value.
 * CrewmanState can be considered as an age-group.
 * Typically there is one CrewState value per implemented AbstractCrewman class.
 * Used internally to determine the type of crewman it is, and to ensure safe casting.
 *
 * Note that the crewman state is guided by the age of the crewman, but not locked to it.
 * The state represents the physical apperance and abilities of a crewman, their phase in life, not their actual age.
 * A child could be a late bloomer, or an early one, changing when then become a youth.
 * An adult could stay active and healthy way longer than what is normal, delaying when they become seniors.
 *
 *
 * NOTE: To change CrewState you must use a conversion function to replace the object.
 *  - when converting a Crewman object, the old object MUST be deleted afterwards
 */
public enum CrewmanState {
    infant, toddler, child, youth, adult, senior, corpse, memorial;

    /**
     * Whatever this crewman state can be assigned a job
     * @return
     */
    public boolean isWorkAble() {
        return ArrayUtils.contains(WORK_ABLE, this);
    }

    /**
     * Types of crewmen who are able to be assigned to a job.
     */
    public static final CrewmanState[] WORK_ABLE = new CrewmanState[]{child, youth, adult, senior};
}
