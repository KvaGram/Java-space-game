package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.spaceship.ModuleType;

public class BuildModuleTask extends RefitTask {
    private static final int COST_PLACEHOLDER = 1000;
    private final ModuleType targetType;

    public BuildModuleTask(ModuleType targetType, HomeShip.ShipLoc target) {
        super(COST_PLACEHOLDER, "Construct a " + targetType.name(), RefitType.build, target);
        this.targetType = targetType;
    }
}
