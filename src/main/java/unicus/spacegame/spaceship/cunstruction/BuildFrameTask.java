package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.HomeShip;
import unicus.spacegame.spaceship.SectionType;

public class BuildFrameTask extends RefitTask{
    private static final int COST_PLACEHOLDER = 1000;
    private final SectionType targetType;

    public BuildFrameTask(SectionType targetType, HomeShip.ShipLoc target) {
        super(COST_PLACEHOLDER, "Construct a " + targetType.name(), RefitType.build, target);
        this.targetType = targetType;
    }
}
