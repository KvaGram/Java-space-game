package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.crew.AbstractJob;
import unicus.spacegame.crew.AdultCrewman;
import unicus.spacegame.crew.Workplace;
import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.spaceship.ModuleType;
import unicus.spacegame.spaceship.SectionType;

import java.util.ArrayList;

/**
 * Construction is a unique job and workplace.
 */
public class Construction extends AbstractJob implements Workplace {
    //The key for the Construction job is special and meant to be easily recognizable.
    static final public int CONSTRUCTION_JOB_KEY = 1;
    private static Construction instance;

    ArrayList<Object> WorkQueue;

    public Construction() {
        super(CONSTRUCTION_JOB_KEY, 9);
        if (instance == null)
            instance = this;
    }

    @Override
    public void endOfMonth() {
        super.endOfMonth();

    }

    /**
     * Returns a list of all jobs that depends on this object being active.
     * Should this object be removed, these jobs must also be removed.
     *
     * @return KeyID of the job(s) dependent on this object.
     */
    @Override
    public int[] getDependentJobs() {
        return new int[]{this.getKeyID()};
    }

    /**
     * Returns a list the job(s) associated with this object.
     * This is meant as a tool for player interface.
     * Should include all dependent jobs.
     *
     * @return
     */
    @Override
    public int[] getAllJobs() {
        return new int[]{this.getKeyID()};
    }


    /**
     * Gets the amount of workload to be put on assigned crewmembers this month.
     *
     * @return A value of workload pressure.
     */
    @Override
    public double getMonthlyWorkload() {
        return 0;
    }

    /**
     * Calculates a base efficiency for how well a crewman will do this job.
     * Used in UI to show percentage efficiency.
     * Note: implementation should include the result from {@link AdultCrewman#getGeneralWorkModifier()},
     * unless implementation has an alternative.
     *
     * @param crewID The ID of the crewman
     * @return The base efficiency of the crewman, where 1.0 equals 100%.
     */
    @Override
    public double getWorkModifierOfCrewman(int crewID) {
        return 0;
    }

    public static Construction getInstance() {
        if (instance == null)
            new Construction();
        return instance;
    }
}



