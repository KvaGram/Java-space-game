package unicus.spacegame.spaceship;

/**
 * The type of Section a spaceship may have.
 * A SectionType have different amount of modules,
 * and some may not have gravity.
 */
public enum SectionType {
    None{
        @Override
        public int getNumModules() {
            return 0;
        }
        @Override
        public boolean getHasGravity() {
            return false;
        }
    }, Normal {
        @Override
        public int getNumModules() {
            return 5;
        }
        @Override
        public boolean getHasGravity() {
            return false;
        }
    }, Wheel {
        @Override
        public int getNumModules() {
            return 3;
        }
        @Override
        public boolean getHasGravity() {
            return true;
        }
    }, GravityPlated {
        @Override
        public int getNumModules() {
            return 4;
        }
        @Override
        public boolean getHasGravity() {
            return true;
        }
    };
    public abstract int getNumModules();
    public abstract boolean getHasGravity();
    public SectionType[] getBuildable(){
        return new SectionType[]{Normal, Wheel, GravityPlated};
    }
};
