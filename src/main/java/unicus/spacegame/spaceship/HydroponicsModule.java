package unicus.spacegame.spaceship;

import unicus.spacegame.crew.AbstractJob;
import unicus.spacegame.crew.AdultCrewman;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.crew.Workplace;

public class HydroponicsModule extends AbstractShipModule implements Workplace {
    private HydroponicsJob job;
    public HydroponicsModule(HomeShip.ShipLoc loc) {
        super(loc);
        int jobKey = SpaceCrew.getInstance().getJobKeys().yieldKey();
        job = new HydroponicsJob(jobKey)
    }
    public class HydroponicsJob extends AbstractJob {

        protected HydroponicsJob(int keyID) {
            super(keyID, 3);
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
         * Calculates how much work this worker will normally produce.
         * This is used to:
         * show efficiency percentage (work divided by workload)
         * used as first step to calculate how muc work this assigned crewman will do.
         * NOTE: Implementation must be calculation only, and not alter any data, as this may be called multiple times.
         *
         * @param crewman  The assigned crewman
         * @param workload The amount of workload assigned to this crewman for this job.
         * @return An estimated amount of work a crewman will do on the job.
         * <p>
         * NOTE: class AdultCrewman may be replaced with a more general class of all crewmen who can take jobs.
         */
        @Override
        public double evaluateWorker(AdultCrewman crewman, double workload) {
            return 0;
        }

        /**
         * Completes work required for the month.
         * Completes task list and or operations.
         * May triggers events related to what has been worked on.
         *
         * @param workDone how much work has been done in total
         */
        @Override
        public void endOfMonth(double workDone) {

        }
    }

    /**
     * Returns a list of all jobs that depends on this object being active.
     * Should this object be removed, these jobs must also be removed.
     *
     * @return KeyID of the job(s) dependent on this object.
     */
    @Override
    public int[] getDependentJobs() {
        return new int[0];
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
        return new int[0];
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    /**
     * For modules:
     * Whatever this module requires gravity to be constructed.
     * For section-frames:
     * Whatever this section-frame provides gravity.
     *
     * @return
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public String GetName() {
        return null;
    }

    @Override
    public CargoPlaceholder[] getCargoOnDestruction() {
        return new CargoPlaceholder[0];
    }

    /**
     * This function runs when the ship-part is removed or dismantled from the ship.
     * This function only deals with this object itself, any ship-part depended on this,
     * will be taken care of from HomeShip. .
     */
    @Override
    public void onDestroy() {

    }
}
