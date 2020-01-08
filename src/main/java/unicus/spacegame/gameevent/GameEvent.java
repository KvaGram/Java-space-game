package unicus.spacegame.gameevent;

import de.gurkenlabs.litiengine.IUpdateable;

import java.util.ArrayList;
import java.util.Random;

public final class GameEvent implements IUpdateable {
    private static GameEvent INSTANCE;
    private String info = "Magic singleton class";
    private GameEvent() {
    }

    /** Data structure for the event list is in flux. Possibly an external file, possibly a series of constructors.
     */
    public static GameEvent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameEvent();
            //Set up event database.
        }
        return INSTANCE;
    }

    ArrayList<RandomEvent> el = new ArrayList<RandomEvent>();

    /** Look over the list of events that are qualified to happen
    (e.g. "alien runs amok" only if you have aliens aboard)
    Calculate appropriate weightings for common and rare events
    Pick one of them and hand off execution to avoid code duplication.
     */
    public void event_Random() {
        //Prerequisite verifier algorithm go here
        int sum_weights=0;
        sum_weights += ScientificDiscovery.weight;
        sum_weights += MinorAirLeak.weight;
        //Random selection algorithm go here
        event_byID(0);
    }
    /** Event picker, sanity checker
     * Call handling for event chains
     * I forget the name of the pattern but it prevents events from getting into deep stacks of chained calls
     */
    public void event_byID(int eventID) { //Also useful for debugging by console!
        if (eventID == 0) {
            return; //There is no event 0!
        }
        int next_ID = 0;
        do {
            next_ID = execute_event(eventID);
        } while (next_ID != 0);
        return;
    }

    /** Returns the ID of a 'follow-up' event for when there is an event chain
    (e.g. an event asks you to make a decision between two feuding crew; you may get a second event where the loser takes matters into his own hands)
    Return 0 to signal there is no further event */
    public int execute_event(int eventID) {

        //Pop up a UI dialog box:
        //UI.text = event_text;
        //for i in (0,event_options) : {UI.button = event_choice_text, event_choice_ID}
        //return ID of clicked button
        return 0;  //placeholder
    }

    @Override
    public void update() {

    }

    /** Current base class for events.
     * Contains its own ID and text for a dialog box, text on dialog choice options, ID of each dialog choice.
     * If the ID of a dialog choice is nonzero, that will be the followup event triggered.
     * Initial events should be numbered like BASIC: ID 10, 20, 30, 40...
     * so that follow-up events in a chain can be easily inserted at 31,32,33 for the results of event 30.
     * Argument form is e.g. (10, "A thing happened!", new int[]{0}, new String[]{"OK"}.
     * ID Weight makes things happen more or less often. Baseline is 100.
     * Weight is not included in the usual constructor because it should only rarely be altered.
     */
    private class RandomEvent {
        int event_id; //Event IDs should start at 10 and be spaced apart, like BASIC line numbers, for much the same reason.
        String event_text;
        int[] event_choice_ids;
        String[] event_choice_texts;
        int weight=100; //Each random event should have a chance of happening based on (this.weight)/(sum:weights). Most should stay at 100.
        RandomEvent(int id, String text, int[] choice_ids, String[] choice_texts) {
            assert (id != 0);
            this.event_id = id;
            this.event_text = text;
            this.event_choice_ids = choice_ids;
            this.event_choice_texts = choice_texts;
        }
    }

    //Events should be private. External classes call event_by_ID(), not the event object itself.
    private RandomEvent ScientificDiscovery = new RandomEvent(10, "One of our crewmen has made a scientific breakthrough in his spare time! We have gained 5 research points.",
            new int[]{0}, new String[]{"That's good."});

    private RandomEvent MinorAirLeak = new RandomEvent(20, "There was a minor leak in one of the airlocks. We found and patched it, but our oxygen supplies have been depleted slightly.",
            new int[]{0}, new String[]{"Unfortunate."});
    //Event texts can possibly be outsourced to external file for translation later
    //TODO: How to store prerequisites/conditionals?
    //TODO: What's the syntax to look out at ship state variables? (e.g. amount of resources, having a specific module)

}

/*
Data that needs to be in an event:
-ID
-Dialog text
-whether it is an initial or a follow-up event (i.e. can it happen on its own).
-Option texts
-Option IDs
-Weight (base chance of happening)
-Weight modifiers
-Prerequisites to happening (could be folded into Weight *=0)
-What happens immediately as the event fires
-What happens when a result is selected
 */