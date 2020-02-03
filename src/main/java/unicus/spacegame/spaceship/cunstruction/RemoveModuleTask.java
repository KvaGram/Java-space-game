package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.ShipLoc;

public class RemoveModuleTask extends RefitTask{
    private static final int COST_PLACEHOLDER = 1000;

    public RemoveModuleTask(ShipLoc target) {
        super(COST_PLACEHOLDER, "Remove a module", RefitType.remove, target);
    }
}
