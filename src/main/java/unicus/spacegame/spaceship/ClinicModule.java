package unicus.spacegame.spaceship;

import unicus.spacegame.CargoCollection;
import unicus.spacegame.CargoContainer;
import unicus.spacegame.crew.AbstractJob;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.crew.Workplace;

import java.util.Collection;

public class ClinicModule extends AbstractShipModule implements Workplace {
    public final ClinicJob job;
    public ClinicModule(ShipLoc loc) {
        super(loc);
        job = new ClinicJob();
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    @Override
    public boolean useGravity() {
        return true;
    }

    @Override
    public ModuleType getModuleType() {
        return null;
    }

    @Override
    public String GetName() {
        return "Clinic module at " + this.loc.toString();
    }

    @Override
    public Collection<CargoCollection> getCargoOnDestruction() {
        return CargoContainer.Null.getCollection();
    }

    @Override
    public void onDestroy() {

        SpaceCrew.SC().removeJobs(getDependentJobs());
    }

    @Override
    public int[] getDependentJobs() {
        return new int[0];
    }

    @Override
    public int[] getAllJobs() {
        return new int[0];
    }

    class ClinicJob extends AbstractJob{

        protected ClinicJob() {
            super(SpaceCrew.SC().getJobKeys().yieldKey(), 3);
        }

        @Override
        public double getMonthlyWorkload() {
            return 0;
        }

        @Override
        public double getWorkModifierOfCrewman(int crewID) {
            return 0;
        }

        @Override
        public void endOfMonth() {
            super.endOfMonth();

        }
    }
    abstract class TreatmentTask {
        public final int patientID;
        public TreatmentTask(int crewID) {
            patientID = crewID;
        }
        /**
         * Treats a patient.
         * @param availableWork Works-points remaining that could be allocated to this task.
         * @return Work points remaining after this task.
         */
        abstract double doWork(double availableWork);
        abstract boolean isDone();
        abstract boolean onDone();
        abstract boolean onCancel();

    }
}
