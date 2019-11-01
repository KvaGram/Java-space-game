package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;

import java.awt.*;

public class SpaceObjectView {
    BasicSpaceObject model;
    SystemView master;


    public SpaceObjectView(BasicSpaceObject model, SystemView master){
        this.model = model;
        this.master = master;
    }

    public void RenderObject(Graphics2D g) {

    }
    public void RenderOrbit(Graphics2D g) {

    }

    public int getOrbitPixels(){
        return 0;
    }
    public  int getSizePixels(){
        return 0;
    }

}
