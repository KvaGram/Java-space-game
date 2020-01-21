package unicus.spacegame.spaceship;

import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;

/**
 * The type of Module a section may have.
 * A module may require gravity to be constructed.
 * (for testing) A module has a color associated with it.
 */
public enum ModuleType {
    /**
     * The module is either an {@link NullModule} or an {@link AbstractShipSection}.
     */
    Empty{
        @Override public boolean getNeedGravity() {
            return false;
        }
        @Override
        public Color getColor() {
            return new Color(50,50,70);
        }
    },
    /**
     * The module is a {@link CargoModule}
     */
    Cargo {
        @Override
        public boolean getNeedGravity() {
            return false;
        }
        @Override
        public Color getColor() {
            return new Color(160,82,45);
        }
    },
    /**
     * The module is a {@link HabitatModule}
     */
    Habitat {
        @Override
        public boolean getNeedGravity() {
        return true;
        }
        @Override
        public Color getColor() {
            return new Color(0, 44, 241);
        }
    },
    /**
     * The module is a {@link HydroponicsModule}
     */
    Hydroponics {
        @Override
        public boolean getNeedGravity() {
            return false;
        }

        @Override
        public Color getColor() {
            return new Color(30, 168, 0);
        }
    };

    public abstract boolean getNeedGravity();
    public abstract Color getColor();
    public static final ModuleType[] buildable = new ModuleType[] {
            Empty, Cargo, Habitat, Hydroponics
    };

    public ModuleType[] getBuildable(boolean includeGravity){
        ModuleType[] ret = new ModuleType[]{Cargo};
        if(includeGravity)
            ret = ArrayUtils.addAll(ret, Habitat);
        return ret;
    }
};