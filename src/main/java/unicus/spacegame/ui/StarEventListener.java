package unicus.spacegame.ui;


//import org.w3c.dom.events.EventListener;

import unicus.spacegame.StarData;

import java.util.EventListener;

public interface StarEventListener extends EventListener {
    void onTravelToStar(StarData starData, int subsection, int index);
}
