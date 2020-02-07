package unicus.spacegame.spaceship;

public abstract class AbstractShipSection extends AbstractShipModule{
    private final SectionType sectionType;


    public abstract AbstractShipModule[] GetModuleTypes();
    public AbstractShipSection(ShipLoc loc, SectionType sectionType) {
        super(ShipPartType.Section, loc);
        this.sectionType = sectionType;
    }
    @Deprecated
    public final int getNumModules(){return 6;};

    public abstract boolean canBuildModule(ModuleType typeToBuild, StringBuffer message);

    @Override
    public final ModuleType getModuleType() {
        return ModuleType.Empty;
    }

    public final SectionType getSectionType() {
        return sectionType;
    }
}
