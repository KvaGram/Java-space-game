package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.spaceship.ModuleType;
import unicus.spacegame.spaceship.ShipLoc;

public class BuildModuleTask extends RefitTask {
    private static final int COST_PLACEHOLDER = 1000;
    private final ModuleType targetType;

    public BuildModuleTask(ModuleType targetType, ShipLoc target) {
        super(COST_PLACEHOLDER, "Construct a " + targetType.name(), RefitType.build, target);
        this.targetType = targetType;
    }

    @Override
    public boolean checkPossible(StringBuffer message) {
        return HomeShip.canBuildModule(targets[0], targetType, message);
    }

    /**
     * TODO: move to bottom-most super-class for tasks.
     * Runs when finishing up the job, the construction job is finished.
     * Some related events could trigger.
     *
     * @param message information message on any issues or information the player needs to know.
     * @return whatever the task was successfully completed.
     */
    @Override
    public boolean onFinish(StringBuffer message) {
        //Builds the section. If it some fails, return false.
        //It if completes, remove self from the work queue,
        if (HomeShip.doBuildModule(targets[0], targetType, message)) {
            Construction.RemoveTask(this);
            return true;
        }
        return false;
    }

    /**
     * @param message information message on any issues or information the player needs to know.
     * @return whatever the task was successfully removed.
     */
    @Override
    boolean onRemove(StringBuffer message) {
        Construction.RemoveTask(this);
        return true;
    }
}
