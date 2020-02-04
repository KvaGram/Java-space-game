package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.spaceship.ShipLoc;

public class RemoveModuleTask extends RefitTask{
    private static final int COST_PLACEHOLDER = 1000;

    public RemoveModuleTask(ShipLoc target) {
        super(COST_PLACEHOLDER, "Remove a module", RefitType.remove, target);
    }

    @Override
    public boolean checkPossible(StringBuffer message) {
        return HomeShip.canRemoveModule(targets[0], message);
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
        if (HomeShip.doRemoveModule(targets[0], message)) {
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
