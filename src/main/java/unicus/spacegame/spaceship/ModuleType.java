package unicus.spacegame.spaceship;

import java.awt.*;

/**
 * The type of Module a section may have.
 * A module may require gravity to be constructed.
 * A module has a color associated with it.
 */
public enum ModuleType {
    Empty{
        @Override public boolean getNeedGravity() {
            return false;
        }
        @Override
        public Color getPaintColor() {
            return new Color(50,50,70);
        }
    },Cargo {
        @Override
        public boolean getNeedGravity() {
            return false;
        }
        @Override
        public Color getPaintColor() {
            return new Color(160,82,45);
        }
    }, Habitat {
        @Override
        public boolean getNeedGravity() {
        return true;
        }
        @Override
        public Color getPaintColor() {
            return new Color(100,200,0);
        }
    };

    public abstract boolean getNeedGravity();
    public abstract Color getPaintColor();
};