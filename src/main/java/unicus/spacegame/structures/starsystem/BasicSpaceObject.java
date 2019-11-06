package unicus.spacegame.structures.starsystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BasicSpaceObject {
    ObjectType type;
    ObjectSize size;

    //generated values, set in update internal
    int generatedLocalSize;
    int generatedFullSize;
    Point generatedLocation;
    int orbitDistance;

    Point offset;
    long renderSeed;
    ArrayList<BasicSpaceObject> children;

    BasicSpaceObject parent;
    int   orbitIndex;
    float orbitRotation;


    public BasicSpaceObject(ObjectType type, ObjectSize size, long seed) {
        this.type = type;
        this.size = size;
        this.renderSeed = seed;
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
        this.renderSeed = seed;
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
        //Local size is fetched using getLocalSize.
        //In this base class, getLocalSize is a 2D lookup-table on ObjectType and ObjectSize.
        generatedLocalSize = getLocalSize();

        if(isRoot()) {
            //If this object is a root, then the location equals the offset.
            //The offset is normally 0,0.
            generatedLocation = offset;
        }
        else {
            //If the object is not a root, then the location is based on the
            //orbital distance to its parent, and its current rotation around it.
            //Relative to the parent location.

            int x = parent.generatedLocation.x;
            int y = parent.generatedLocation.y;
            x += (int)(Math.cos(orbitRotation) * orbitDistance );
            y += (int)(Math.sin(orbitRotation) * orbitDistance );
            generatedLocation.x = x;
            generatedLocation.y = y;
        }
        //In order to generate the full size,
        //the full size of the child objects needs to be generated first.
        //So this is where this function is called for the children


        if (children.size() == 0) {
            //If the object has no children, then full size equals local size.
            generatedFullSize = generatedLocalSize;
        }
        else {
            /***
             * TODO: FIX ORBIT DISTANCE SYSTEM!!!
             * The problem lies in finding the appropriate distance between objects, when the size is not yet known.
             * I think I need to have the full size of the previous orbit objects, the current and the next in order
             * to make a proper calculation.
             *
             * This conflicts with the function that sets location, as it requires orbit distance.
             * I think a rather large change in the logic and structure are needed.
             *
             */


            //The object has children.
            //Make sure the children are sorted by orbit index.
            children.sort(Comparator.comparingInt(o -> o.orbitIndex));
            //Calculate the distance between each orbit.
            int highestOrbit = getHighestOrbit();
            int currentOrbitDistance = (int) (generatedLocalSize * 1.5);
            for (int i = 1; i <= highestOrbit; i++) {

                ArrayList<BasicSpaceObject> cInOrbit = getChildrenInOrbit(i);
                Collections.max(cInOrbit, Comparator.comparingInt(c -> c.generatedLocalSize));
                getLargestChildByOrbit(i);
            }


            //The object has children.
            //The full size of the children must be calculated first.
            //So, the child objects are updated here.
            for(BasicSpaceObject c : children)
                c.updateInternal();

            //gets the largest child of the highest orbit.
            BasicSpaceObject topChild = getLargestChildByOrbit(getHighestOrbit());
            //the full size of this object is the local size, plus the top child's full size plus the distance between.
            generatedFullSize = generatedLocalSize + topChild.generatedFullSize + getChildOrbitDistance(topChild.orbitIndex);
        }
    }

    /**
     * Generates the orbit-distance of each object orbiting this object.
     * @param orbitIndex The orbit to get distance for.
     * @return A radius of game-units.
     */
    public int getChildOrbitDistance(int orbitIndex) {
        return 1000 * orbitIndex;

//        //at the index of 0, we are at this object itself.
//        if (orbitIndex == 0)
//            return generatedLocalSize;
//        //There could be more than one object that share the orbit.
//        //The orbit distance is based on the largest one.
//        BasicSpaceObject prevLargest = getLargestChildByOrbit(orbitIndex - 1);
//
//        BasicSpaceObject thisLargest = getLargestChildByOrbit(orbitIndex);
//        //return the full size of the largest object in the orbit, plus 20% as safe space
//        //added to the distance of the previous orbit index (recursively towards index 0).
//
//        int extraSpace = 0;
//        if(prevLargest != null ){
//        }
//
//        return (int)(thisLargest.generatedFullSize * 3) + getChildOrbitDistance(orbitIndex -1);
//

    }
    public int getOrbitDistance()
    {
        return orbitDistance;
    }

    /**
     *
     * @param orbitIndex
     * @return The chile with largest full size in given orbit.
     */
    public BasicSpaceObject getLargestChildByOrbit(int orbitIndex) {
        ArrayList<BasicSpaceObject> children = getChildrenInOrbit(orbitIndex);
        if(children.isEmpty())
            return null;
        BasicSpaceObject largest = children.get(0);
        for(int i = 1; i < children.size(); i++) {
            BasicSpaceObject other = children.get(i);
            if(other.generatedFullSize > largest.generatedFullSize)
                largest = other;
        }
        return largest;
    }


    public boolean addChild(BasicSpaceObject child)
    {
        return children.add(child);
    }
    public boolean removeChild(BasicSpaceObject child)
    {
        return children.remove(child);
    }
    public ArrayList<BasicSpaceObject> getChildrenInOrbit(int orbitIndex){
        return children.stream().filter(c -> c.orbitIndex == orbitIndex).collect(Collectors.toCollection(ArrayList::new));
    }
    public int getHighestOrbit() {
        int result = 0;
        for(BasicSpaceObject c : children)
            result = Math.max(result, c.orbitIndex);
        return result;
    }




    public String getTooltip(){
        String str = "Basic space object\n for development only!\n type " + type.name() + "\nsize " + size.name();
        if(isRoot() || parent.type == ObjectType.none){
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

    public long getRenderSeed(){
        return renderSeed;
    }

    public BasicSpaceObject[] getChildren(){
        children.sort(Comparator.comparingInt(o -> o.orbitIndex));
        return children.toArray(new BasicSpaceObject[children.size()]);
    }


    /**
     * Get the world-unit radius size of this object.
     * @return radius in world-units
     */
    public int getLocalSize() {
        switch (type) {
            case star:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 250;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 300;
                else
                    return 500;
            case gasPlanet:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 100;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 200;
                else
                    return 300;
            case lifePlanet:
            case planet:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 40;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 100;
                else
                    return 150;
            case asteroid:
                if (size.smallerThan(ObjectSize.MODERATE))
                    return 10;
                else if (size.smallerThan(ObjectSize.XLARGE))
                    return 30;
                else
                    return 50;
            case spaceStation:
            case satellite:
            case debris:
                return 10;
            case none:
            default:
                return 0;
        }
    }
}
