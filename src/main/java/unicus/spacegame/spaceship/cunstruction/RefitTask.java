package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.ShipLoc;

public abstract class RefitTask extends ConstructionTask {
    protected RefitType refitType;
    protected ShipLoc[] targets;

    public RefitTask(int labourCost, String description, RefitType refitType, ShipLoc[] targets) {
        super(labourCost, description);
        this.refitType = refitType;
        this.targets = targets;
    }
    public RefitTask(int labourCost, String description, RefitType refitType, ShipLoc target) {
        super(labourCost, description);
        this.refitType = refitType;
        this.targets = new ShipLoc[]{target};
    }
    public abstract boolean checkPossible(StringBuffer message);


    public ShipLoc[] getTargets() {
        return targets;
    }

    public RefitType getRefitType() {
        return refitType;
    }
}
