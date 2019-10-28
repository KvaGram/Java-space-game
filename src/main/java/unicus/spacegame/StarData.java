package unicus.spacegame;


import java.awt.*;
import java.util.ArrayList;

public class StarData {
    public int subsector;
    public int index;
    public long seed;
    public Point location;
    public ArrayList<StarData> connections;
    public StarData(int subsector, int index, long seed, Point location){
        this.subsector = subsector;
        this.index = index;
        this.seed = seed;
        this.location = location;
        connections = new ArrayList<>();
    }
    /* public StarData(int subsector, int index, Sectormaps map) {
        this(subsector, index, map.getStarSeed(subsector, index), map.getStarPoint(subsector, index))
    } */
}