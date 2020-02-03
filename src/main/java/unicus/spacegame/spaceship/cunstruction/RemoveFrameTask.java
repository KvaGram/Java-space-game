package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.ShipLoc;

public class RemoveFrameTask extends RefitTask{
    private static final int COST_PLACEHOLDER = 1000;

    public RemoveFrameTask(ShipLoc target) {
        super(COST_PLACEHOLDER, "Strip a frame from a section.", RefitType.remove, target);
    }
}
