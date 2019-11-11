package unicus.spacegame.structures.starsystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class BasicSpaceObject {
    ObjectType type;
    ObjectSize size;

    //generated values, set in update internal
    protected int generatedLocalSize;
    protected int generatedFullSize;
    protected Point generatedLocation;
    protected int orbitDistance;

    Point offset;
    long planetSeed;
    ArrayList<BasicSpaceObject> children;

    BasicSpaceObject parent;
    int   orbitIndex;
    float orbitRotation;


    public BasicSpaceObject(ObjectType type, ObjectSize size, long seed) {
        this.type = type;
        this.size = size;
        this.planetSeed = seed;
        //TODO: use seed to generate properties, THEN generate renderSeed.
        this.children = new ArrayList<>();

        parent = this;
        orbitIndex = 0;
        orbitRotation = 0.0f;
        offset = new Point();

        generatedLocalSize = 0;
        generatedFullSize = 0;
        generatedLocation = new Point();
    }

    public BasicSpaceObject(ObjectType type, ObjectSize size, long seed, BasicSpaceObject parent, int orbit, float rot) {
        this.type = type;
        this.size = size;
        this.planetSeed = seed;
        //TODO: use seed to generate properties, THEN generate renderSeed.
        this.children = new ArrayList<>();

        this.parent = parent;
        this.orbitIndex = orbit;
        this.orbitRotation = rot;

        //Make sure the parent know this child exist.
        if(!isRoot())
            parent.addChild(this);
        offset = new Point();

        generatedLocalSize = 0;
        generatedFullSize = 0;
        generatedLocation = new Point();
    }
    public int getGeneratedLocalSize(){
        return  generatedLocalSize;
    }
    public int getGeneratedFullSize(){
        return generatedFullSize;
    }
    public Point getGeneratedLocation(){
        return generatedLocation;
    }
    public Point getParentLocation(){
        return parent.getGeneratedLocation();
    }
    public int getOrbitDistance() { return orbitDistance; }

    public Boolean isRoot() {
        return (parent == this || parent == null);
    }

    /**
     * Update generated local size, fullsize and location of this element and its children
     * Call only from root objects.
     */
    public void update() {
        if(isRoot())
            updateInternal();
    }
    protected void updateInternal() {
        Random r = new Random(planetSeed);
        //Local size is fetched using getLocalSize.
        //In this base class, getLocalSize is a 2D lookup-table on ObjectType and ObjectSize.
        generatedLocalSize = getLocalSize();

        //If this object has no children, then the full size equals the local size.
        if(children.isEmpty())
            generatedFullSize = generatedLocalSize;
        else {
            for (BasicSpaceObject c : children) {
                c.updateInternal();
            }
            /**
             * Sort child objects first by orbit index (closest to furthest)
             * then sort by generated full size (largest to smallest)
             */
            Comparator<BasicSpaceObject> childsorter = Comparator.comparingInt(o -> o.orbitIndex);
            childsorter.thenComparingInt(o -> o.generatedFullSize).reversed();
            children.sort(childsorter);

            int highest = children.get(children.size() - 1).orbitIndex;


            generatedFullSize = (int)(generatedLocalSize * 1.2);

            BasicSpaceObject prevChild = null;
            BasicSpaceObject currentChild;

            int currentOrbit = 0;
            int orbitD = 0;
            for (int i = 0; i < children.size(); i++) {
                currentChild = children.get(i);
                //In the case of co-orbiting objects, set the co-orbiting object's orbit distance to be the same.
                //Since the list of children are sorted by size, any co-orbiting object(s) is of same size or smaller,
                //So the orbit distance does not need to be re-adjusted.
                if(prevChild != null && (prevChild.orbitIndex == currentChild.orbitIndex)) {
                    currentChild.orbitDistance = orbitD;
                    prevChild = currentChild;
                    continue;
                }
                //The child orbit distance is the current full size of parent object, plus a random distance based on the size of the child object
                orbitD = generatedFullSize + r.nextInt(currentChild.generatedFullSize/3);
                currentChild.orbitDistance = orbitD;
                //The generated full size is set to the current child's orbit distance plus size
                generatedFullSize = orbitD + currentChild.generatedFullSize;
                prevChild = currentChild;
            }
        }
        //Set the world location of the object
        if(isRoot()) {
            //For the root object, the location is just the offset (typically not set, thus (0,0)).
            generatedLocation = offset;
        } else {
            //For a child object, the location is a cos/sin on the orbit rotation multiplied by orbit distance,
            //added to the parent location.
            int x = parent.generatedLocation.x;
            int y = parent.generatedLocation.y;
            x += (int)(Math.cos(orbitRotation) * orbitDistance );
            y += (int)(Math.sin(orbitRotation) * orbitDistance );
            generatedLocation.x = x;
            generatedLocation.y = y;
        }
    }


    public boolean addChild(BasicSpaceObject child)
    {
        return children.add(child);
    }
    public boolean removeChild(BasicSpaceObject child)
    {
        return children.remove(child);
    }

    public String getTooltip(){
        String str = "Basic space object\n for development only!\n type " + type.name() + "\nsize " + size.name();
        if(isRoot() || parent.type == ObjectType.NONE){
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

    public long getPlanetSeed(){
        return planetSeed;
    }

    /**
     * Get the world-unit radius size of this object.
     * @return radius in world-units
     */
    public int getLocalSize() {
        switch (type) {
            case STAR:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 250;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 300;
                else
                    return 500;
            case GAS_PLANET:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 100;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 200;
                else
                    return 300;
            case LIFE_PLANET:
            case PLANET:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 40;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 100;
                else
                    return 150;
            case JUNK_FIELD:
                switch (size) {
                    case NONE:
                        return 0;
                    case TINY:
                        return 10;
                    case SMALL:
                        return 30;
                    case MODERATE:
                        //noinspection DuplicateBranchesInSwitch
                        return 50;
                    case LARGE:
                        return 80;
                    case GIANT:
                        return 120;
                    case XLARGE:
                        return 300;
                    default:
                        return 50;
                }
            case ROUGE_ASTEROID:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 10;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 30;
                else
                    return 50;
            case SPACE_STATION:
            case SATELLITE:
            case NONE:
            default:
                return 0;
        }
    }
}
