package unicus.spacegame.ui.System;

import unicus.spacegame.structures.starsystem.BasicSpaceObject;

import java.awt.*;

public class SpaceObjectView {
    BasicSpaceObject model;
    SystemView master;
    int[] pixelRadiusOrbits;
    int pixelSize;
    BasicSpaceObject[] modelChildren;

    public SpaceObjectView(BasicSpaceObject model, SystemView master){
        this.model = model;
        this.master = master;
        update();
    }
    public void update()
    {
        modelChildren = model.getChildren();


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
