package unicus.spacegame.crew;

/**
 * A workplace is any object that may have jobs either associated or attached to it.
 * For one example, a hydroponics bay has a hydroponics farm job depended on it.
 * For a less obvious example, a child is also a workplace, having a childcare job dependent on him/her/they/it.
 * If such a thing as a robot crewmwmber were to be added, they would also be a workplace, having a maintenance job depended on it.
 * A habitat is also a Workplace, but has no depended jobs. Instead it lists all crewmembers that are workplaces.
 */
public interface Workplace {

    /**
     * Returns a list of all jobs that depends on this object being active.
     * Should this object be removed, these jobs must also be removed.
     * @return KeyID of the job(s) dependent on this object.
     */
    int[] getDependentJobs();

    /**
     * Returns a list the job(s) associated with this object.
     * This is meant as a tool for player interface.
     * Should include all dependent jobs.
     * @return
     */
    int[] getAllJobs();
}

