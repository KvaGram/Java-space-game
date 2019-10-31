package unicus.spacegame.structures.starsystem;

public class BasicSpaceObject {
    ObjectType type;
    ObjectSize size;

    BasicSpaceObject parent;
    int orbitIndex;
    float orbitRotation;

    public BasicSpaceObject(ObjectType type, ObjectSize size) {
        this.type = type;
        this.size = size;

        parent = this;
        orbitIndex = 0;
        orbitRotation = 0.0f;
    }

    public BasicSpaceObject(ObjectType type, ObjectSize size, BasicSpaceObject parent, int orbit, float rot) {
        this.type = type;
        this.size = size;

        this.parent = parent;
        this.orbitIndex = orbit;
        this.orbitRotation = rot;
    }


    public String getTooltip(){
        String str = "Basic space object\n for development only!\n type " + type.name() + "\nsize " + size.name();
        if(parent == null || parent == this || parent.type == ObjectType.none){
            str += "\nIt is not orbiting anything.";
        } else {
            str += "\nIt is orbiting a  " + parent.type + ".";
            str += "\nIt is orbiting at " + orbitIndex + " index";
            str += "\nIt is currently at " + orbitRotation + " radians around the " + parent.type;
        }
        return str;
    }
    public BasicSpaceObject getParent(){
        return parent;
    }

    public int getOrbitIndex() {
        return orbitIndex;
    }

    public float getOrbitRotation() {
        return orbitRotation;
    }

    public ObjectSize getSize() {
        return size;
    }

    public ObjectType getType() {
        return type;
    }
}
