package unicus.spacegame.gameevent;

import de.gurkenlabs.litiengine.Game; // wut. IntelliJ is being eager.
import de.gurkenlabs.litiengine.IUpdateable;

public class GameEvent implements IUpdateable {

    public GameEvent() {
        Game.loop().attach(this);
    }

    public void event_Random() {
        /* Look over the list of events that are qualified to happen
        (e.g. "alien runs amok" only if you have aliens aboard)
        Calculate appropriate weightings for common and rare events
        Pick one of them and then call event_byID to avoid code duplication.
         */
    }
    public void event_byID(int eventID) {
        int next_ID = 0;
        do {
            next_ID = execute_event(eventID);
        } while (next_ID != 0);
        return;
    }
    public int execute_event(int eventID) {
        /* Returns the ID of a 'follow-up' event for when there is an event chain
        (e.g. an event asks you to make a decision between two feuding crewmembers; you may get a second event where the loser takes matters into his own hands)
         */
        return 0;
    }

    @Override
    public void update() {

    }
}
