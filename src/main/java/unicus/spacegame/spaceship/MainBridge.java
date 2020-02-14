package unicus.spacegame.spaceship;

import unicus.spacegame.crew.*;

import static unicus.spacegame.utilities.Constants.*;

public class MainBridge extends SpecialModule implements Workplace {
    private static MainBridge MB;
    public static MainBridge MB(){
        if(MB == null)
            new MainBridge();
        return MB;
    }

    public CaptainJob captainJob;
    public BridgeDuty bridgeDuty;

    protected MainBridge() {
        super(ShipLoc.get(0,1));
        captainJob = new CaptainJob();
        bridgeDuty = new BridgeDuty();
        MB = this; //Set instance
    }

    @Override
    public String GetName() {
        return "(STUB) Main bridge";
    }

    /**
     * Returns a list of all jobs that depends on this object being active.
     * Should this object be removed, these jobs must also be removed.
     *
     * @return KeyID of the job(s) dependent on this object.
     */
    @Override
    public int[] getDependentJobs() {
        return new int[]{CAPTAIN_JOB_KEY, BRIDGE_JOB_KEY};
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
        return new int[]{CAPTAIN_JOB_KEY, BRIDGE_JOB_KEY};
    }

    class CaptainJob extends AbstractJob{
        protected CaptainJob() {
            super(CAPTAIN_JOB_KEY, 1);
        }

        /**
         * Gets the amount of workload to be put on assigned crewmembers this month.
         *
         * @return A value of workload pressure.
         */
        @Override
        public double getMonthlyWorkload() {
            //TODO: scale workload with number of living crewmen
            return 1000;
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
            return 1.0;
        }

        @Override
        public void endOfMonth() {
            super.endOfMonth();
            //TODO: Calculate consequences for captain job
        }

        @Override
        public String getName() {
            return "Captain";
        }

    }

    class BridgeDuty extends AbstractJob{
        protected BridgeDuty() {
            super(BRIDGE_JOB_KEY, 12);
        }

        /**
         * Gets the amount of workload to be put on assigned crewmembers this month.
         *
         * @return A value of workload pressure.
         */
        @Override
        public double getMonthlyWorkload() {
            int numShifts = getNumShifts();

            switch (numShifts) {
                case 1:
                    //With so few people, the bridge cannot be manned at all time.
                    //Planning to neglect the bridge saves on much workload, but has consequences.
                    return 6000;
                case 2:
                    //With reduced crew manning the bridge, some tasks on the bridge may end up neglected.
                    //Planning to delay or neglect some tasks saves a bit workload, but has consequences.
                    return 8000;
                //case 3:
                //    return 10000;
                //case 4:
                //    return 10000;
                default:
                    //Making sure the bridge stays fully operational is a tough and often boring job. It takes a toll
                    // on the crew.
                    return 10000;
            }
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
            double cNavigation = crewman.getSkill(SkillType.navigation);

            double score = 1.0;

            //crewmen with too low artifice score gets penalty.
            double minArtifice = 20;
            //crewmen with too low navigation gets a penalty.
            double minNavigation = 30;

            //crewmen with a high navigation gets a boost
            double maxNavigation = 70;

            if(cArtifice < minArtifice)
                score *= (cArtifice / minArtifice);
            if(cNavigation < minNavigation)
                score *= (cNavigation / minNavigation);
            else if(cNavigation > maxNavigation)
                score *= (cNavigation / maxNavigation);

            double crewmanBonus = crewman.getGeneralWorkModifier();
            return score + crewmanBonus;
        }

        @Override
        public String getName() {
            return "Bridge duty";
        }

        private int getNumShifts() {
            int numAssignments =  SpaceCrew.SC().getJobAssignmentsByJob(BRIDGE_JOB_KEY).length;

            if (numAssignments < 4)
                return 1;
            if(numAssignments < 9)
                return 2;
            if(numAssignments < 12)
                return 3;
            else
                return 4;
        }

    }
}
