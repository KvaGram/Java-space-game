package unicus.spacegame.spaceship;

public abstract class abstractShipComponent extends AbstractShipPart {

    protected abstractShipComponent(Spaceship.ShipLoc loc) {
        super(ShipPartType.Component, loc);
    }
    protected abstractShipComponent(ShipPartType partType, Spaceship.ShipLoc loc) {
        super(partType, loc);
    }
}
