package unicus.spacegame.spaceship;

public abstract class abstractShipComponent extends AbstractShipPart {

    protected abstractShipComponent(HomeShip.ShipLoc loc) {
        super(ShipPartType.Component, loc);
    }
    protected abstractShipComponent(ShipPartType partType, HomeShip.ShipLoc loc) {
        super(partType, loc);
    }
}
