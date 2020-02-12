package unicus.spacegame.spaceship;

import unicus.spacegame.crew.*;

import static unicus.spacegame.utilities.Constants.*;

public class Engineering extends SpecialModule implements Workplace {
    MainEngineeringJob job;


    protected Engineering(ShipLoc loc) {
        super(loc);
        job = new MainEngineeringJob();
    }

    @Override
    public String GetName() {
        return "(STUB) Engineering";
    }

    /**
     * Returns a list of all jobs that depends on this object being active.
     * Should this object be removed, these jobs must also be removed.
     *
     * @return KeyID of the job(s) dependent on this object.
     */
    @Override
    public int[] getDependentJobs() {
        return new int[]{MAIN_ENGINEER_JOB_KEY};
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
        return new int[]{MAIN_ENGINEER_JOB_KEY};
    }

    private class MainEngineeringJob extends AbstractJob {
        protected MainEngineeringJob() {
            super(MAIN_ENGINEER_JOB_KEY, 6);
        }

        /**
         * Gets the amount of workload to be put on assigned crewmembers this month.
         *
         * @return A value of workload pressure.
         */
        @Override
        public double getMonthlyWorkload() {
            return 5000;
        }

        /**
         * Calculates a base efficiency for how well a crewman will do this job.
         * Used in UI to show percentage efficiency.
         * Note: implementation should include the result from {@link AbleCrewman#getGeneralWorkModifier()},
         * unless implementation has an alternative.
         *
         * @param crewID The ID of the crewman
         * @return The base efficiency of the crewman, where 1.0 equals 100%.
         */
        @Override
        public double getWorkModifierOfCrewman(int crewID) {
            AbleCrewman crewman = (AbleCrewman) SpaceCrew.SC().getCrew(crewID);
            double cArtifice = crewman.getSkill(SkillType.artifice);

            double score = 1.0;

            //crewmen with too low artifice score gets penalty.
            double minArtifice = 50;
            //crewmen with a high artifice gets a boost
            double maxArtifice = 80;

            if(cArtifice < minArtifice)
                score *= (cArtifice / minArtifice);
            else if(cArtifice > maxArtifice)
                score *= (cArtifice / maxArtifice);

            double crewmanBonus = crewman.getGeneralWorkModifier();
            return score + crewmanBonus;
        }

        @Override
        public void endOfMonth() {
            super.endOfMonth();
        }
    }
}
