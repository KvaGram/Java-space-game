package unicus.spacegame.structures.starsystem;

import java.util.ArrayList;

public abstract class AbstractParentObject extends AbstractSpaceObject {
    private ArrayList<Orbiter> children;

    public ArrayList<Orbiter> getChildren() {
        return children;
    }
    public boolean addChild(Orbiter child) {
        return children.add(child);
    }

    public boolean removeChild(Orbiter child) {
        return children.remove(child);
    }
}
