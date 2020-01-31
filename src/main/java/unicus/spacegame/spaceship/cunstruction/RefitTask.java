package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.HomeShip;

public abstract class RefitTask extends ConstructionTask {
    protected RefitType refitType;
    protected HomeShip.ShipLoc[] targets;

    public RefitTask(int labourCost, String description, RefitType refitType, HomeShip.ShipLoc[] targets) {
        super(labourCost, description);
        this.refitType = refitType;
        this.targets = targets;
    }
    public RefitTask(int labourCost, String description, RefitType refitType, HomeShip.ShipLoc target) {
        super(labourCost, description);
        this.refitType = refitType;
        this.targets = new HomeShip.ShipLoc[]{target};
    }
    public abstract boolean checkPossible(StringBuffer message);


    public HomeShip.ShipLoc[] getTargets() {
        return targets;
    }

    public RefitType getRefitType() {
        return refitType;
    }
}
