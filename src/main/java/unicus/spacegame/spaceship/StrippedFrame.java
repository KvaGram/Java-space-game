package unicus.spacegame.spaceship;

import unicus.spacegame.CargoCollection;
import unicus.spacegame.CargoContainer;

import java.util.Collection;

public class StrippedFrame extends AbstractShipSection {

    public StrippedFrame(HomeShip.ShipLoc loc) {
        super(loc, SectionType.None);
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    /**
     * Whatever this section-frame provides gravity.
     *
     * @return false
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public AbstractShipModule[] GetModuleTypes() {
        return new AbstractShipModule[0];
    }

    @Override
    public boolean canBuildModule(ModuleType typeToBuild, StringBuffer message) {
        message.append("This section is stripped");
        return false;
    }

    @Override
    public String GetName() {
        return "Stripped Section at " +  loc.toString();
    }

    @Override
    public Collection<CargoCollection> getCargoOnDestruction() {
        return CargoContainer.Null.getCollection();
    }

    /**
     * This function runs when the ship-part is removed or dismantled from the ship.
     * This function only deals with this object itself, any ship-part depended on this,
     * will be taken care of from HomeShip. .
     */
    @Override
    public void onDestroy() {
        
    }
}
