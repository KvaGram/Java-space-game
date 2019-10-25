package unicus.spacegame.ui;


//import org.w3c.dom.events.EventListener;

import java.util.EventListener;

public interface StarEventListener extends EventListener {
    void onTravelToStar(int[] starData, int subsection, int index);
}
