package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.HomeShip;

public class RemoveFrameTask extends RefitTask{
    private static final int COST_PLACEHOLDER = 1000;

    public RemoveFrameTask(HomeShip.ShipLoc target) {
        super(COST_PLACEHOLDER, "Strip a frame from a section.", RefitType.remove, target);
    }
}
