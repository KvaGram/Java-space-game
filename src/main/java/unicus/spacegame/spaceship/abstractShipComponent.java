package unicus.spacegame.spaceship;

public abstract class abstractShipComponent extends AbstractShipPart {

    protected abstractShipComponent(ShipLoc loc) {
        super(ShipPartType.Component, loc);
    }
    protected abstractShipComponent(ShipPartType partType, ShipLoc loc) {
        super(partType, loc);
    }
}
