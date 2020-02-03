package unicus.spacegame.spaceship.cunstruction;

import unicus.spacegame.spaceship.ShipLoc;

public abstract class RefitTask extends ConstructionTask {
    //The type of refit, used to quickly identify what this task will do.
    protected RefitType refitType;
    //The location(s) involved in the construction.
    //NOTE: current options only use one location. This is for future-proofing.
    protected ShipLoc[] targets;

    public RefitTask(int labourCost, String description, RefitType refitType, ShipLoc[] targets) {
        super(labourCost, description);
        this.refitType = refitType;
        this.targets = targets;
    }
    public RefitTask(int labourCost, String description, RefitType refitType, ShipLoc target) {
        this(labourCost, description, refitType, new ShipLoc[]{target});
    }
    public abstract boolean checkPossible(StringBuffer message);


    public ShipLoc[] getTargets() {
        return targets;
    }

    public RefitType getRefitType() {
        return refitType;
    }
}
