package unicus.spacegame.gameevent;

import de.gurkenlabs.litiengine.IUpdateable;
import unicus.spacegame.ui.DebugConsole;
import java.util.Random;
import java.util.ArrayList;

public final class GameEvent implements IUpdateable {
    private static GameEvent INSTANCE;
    private String info = "Magic singleton class";
    // ArrayList<RandomEvent> myEvents = new ArrayList<RandomEvent>();
    ArrayList<LargeGameEvent> gameEvents = new ArrayList<LargeGameEvent>();
    private GameEvent() {
        INSTANCE = this;
        DebugConsole.getInstance().addGameEventCommands();

        //Events should be private. External classes call event_by_ID(), not the event object itself.

        LargeGameEvent ScientificDiscovery = new LargeGameEvent(10, "One of our crewmen has made a scientific breakthrough in his spare time! We have gained 5 research points.",
                "That's good.") {
            @Override
            public void onTriggered() {
                //TODO: Award science points
            }
            @Override
            public double getWeight() {
                //TODO: Increase weight if you have crewmen with the right traits
                return 100;
            }
        };
        LargeGameEvent MinorAirLeak = new LargeGameEvent(20, "There was a minor leak in one of the airlocks. We found and patched it, but our oxygen supplies have been depleted slightly.",
                "Unfortunate.") {
            @Override
            public void onTriggered() {
                //TODO: Lose some air
            }
            @Override
            public double getWeight() {
                //TODO: Reduce weight if air is low and crew have to be more careful
                return 100;
            }
        };
        LargeGameEvent MetallicDeposit = new LargeGameEvent(30, "We have stumbled on an asteroid with a high purity metal deposit. The metal was easy to extract and has been added to our stores.",
                "OK.") {
            @Override
            public void onTriggered() {
                //TODO: add metal to ship stores
            }
            @Override
            public double getWeight() { return 100; }
        };

        LargeGameEvent AlienMapSellerTrue = new LargeGameEvent(40,
                "An independent alien ship is hailing us, offering to trade us knowledge of galactic hyperlanes for some of our shinyum.",
                new int[]{0,41}, //option_IDs
                new String[]{"No thanks","Pay them 2 Shinium"}, //option_texts
                100, true) {
            @Override
            public void onTriggered() { }
            @Override
            public double getWeight() { return 100; }
        };
        LargeGameEvent AlienMapSellerTrueResult = new LargeGameEvent(41,
                "We have integrated the alien coordinates into our own database. We are slightly closer to finding our way back to Earth.",
                new int[]{0}, //option_IDs
                new String[] {"Onwards!"}, //option_texts
                0, false ) {
            @Override
            public void onTriggered() {
                //TODO: Lose 2 Shinium
                //TODO: Directions to Earth
            }
            @Override
            public double getWeight() { return 0; }
        };

        LargeGameEvent AlienMapSellerFake = new LargeGameEvent(45,
                "An independent alien ship is hailing us, offering to trade us knowledge of galactic hyperlanes for some of our shinyum.",
                new int[]{0,46}, //option_IDs
                new String[]{"No thanks","Pay them 2 Shinium."},
                100, true) {
            @Override
            public void onTriggered() { }
            @Override
            public double getWeight() { return 100; }
        };
        LargeGameEvent AlienMapSellerFakeResult = new LargeGameEvent(46,
                "Sadly the alien coordinates turned out to be gibberish, but after all the time we spent trying to calculate, the scammers have fled.",
                new int[]{0}, //option_IDs
                new String[] {"Damn them!"}, //option_texts
                0, false ) {
            @Override
            public void onTriggered() {
                //TODO: Lose 2 Shinium
                }
            @Override
            public double getWeight() { return 0; }
        };

        LargeGameEvent GoodGrowingSeason = new LargeGameEvent(50, "Our hydroponic tanks have been flourishing the past week and we are ready to harvest an unusually large crop. +4 food.",
                "I just hope it's not broccoli.") {
            @Override
            public void onTriggered() {
                //TODO: Add food
            }
            @Override
            public double getWeight() { return 100; }
        };
        LargeGameEvent CrewPlayingGames = new LargeGameEvent(60, "Your crew has been socializing happily over a lot of the games in the rec room recently. Morale has improved.",
                "Maybe I should join them.") {
            @Override
            public void onTriggered() {
                //TODO: Raise morale
            }
            @Override
            public double getWeight() { return 100; }
        };
        LargeGameEvent WeaponDrillAccident = new LargeGameEvent(70, "One of your marines was injured in training during live weapons practice.",
                "Medic!") {
            @Override
            public void onTriggered() {
                //TODO: Injure a crewmember
            }
            @Override
            public double getWeight() { return 100; }
        };

        LargeGameEvent dummy = new LargeGameEvent(999, "dummy text", "dummy option") {
            @Override
            public void onTriggered() {
                System.out.println("Hello, space!"); //Make things happen
            }
            @Override
            public double getWeight() {
                //Calculate modifiers
                // *0.9 to *0.1 for unlikely factors. *0 for hard prerequisites failed.
                return 100;
            }
        };

        //Event texts can possibly be outsourced to external file for translation later

        //TODO: What's the syntax to look out at ship state variables? (e.g. amount of resources, having a specific module)

        gameEvents.add(ScientificDiscovery);
        gameEvents.add(MinorAirLeak);
        gameEvents.add(MetallicDeposit);
        gameEvents.add(AlienMapSellerTrue);
        gameEvents.add(AlienMapSellerTrueResult);
        gameEvents.add(AlienMapSellerFake);
        gameEvents.add(AlienMapSellerFakeResult);
        gameEvents.add(GoodGrowingSeason);
        gameEvents.add(CrewPlayingGames);
        gameEvents.add(WeaponDrillAccident);
        gameEvents.add(dummy);
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

    /** Looks over the list of events that are qualified to happen [NB: doesn't actually do this yet, docs ahead of code]
    e.g. "alien runs amok" only if you have >0 aliens aboard
    Calculate appropriate weightings for common and rare events
    Pick one of them and hand off execution to avoid code duplication.
     */
    public int event_Random() {
        //Weighted random selection algorithm. Akin to picking off a D&D random table with entries like 00-22, 22-30, 31-80, 81-99.
        //Prerequisite verifier algorithm go here
        double sum_weights=0;
        for (LargeGameEvent r: gameEvents) {
            //if event.canTriggerRandomly? or is that part of weight?
            sum_weights += r.getWeight(); //TODO: should be r.getWeight() to function after LargeGameEvent comes in place
        }
        int i = 0;
        double r = sum_weights * new Random().nextDouble(); //To pick based on relative weight, we have to select from sum of weights, not number of events
        while (r > gameEvents.get(i).getWeight()) {
            r -= gameEvents.get(i).getWeight();
            i++;
        }
        execute_event(gameEvents.get(i).e_ID); //convert index-in-list to index-by-ID

        return event_byID(0);
    }
    /** **obsolete! should merge with execute_event**
     *
     * Event picker, sanity checker
     * Call handling for event chains
     * I forget the name of the pattern but it prevents events from getting into deep stacks of chained calls
     */
    public int event_byID(int eventID) { //Also useful for debugging by console!
        return execute_event(eventID);
        //if (eventID == 0) {
        //    return; //There is no event 0!
        //}
        //int next_ID = 0;
        //do {
        //    next_ID = execute_event(eventID);
        //} while (next_ID != 0);
        //return;
    }

    /** **should merge with event_byID**
     * Returns the ID of a 'follow-up' event for when there is an event chain
    (e.g. an event asks you to make a decision between two feuding crew; you may get a second event where the loser takes matters into his own hands)
    Return 0 to signal there is no further event */
    public int execute_event(int eventID) {
        if(eventID == 0) {
            DebugConsole.getInstance().write("Oopsie: Event 0 was called");
            return 0;
        }

        if(eventIsWaiting()) {
            DebugConsole.getInstance().write("Warning: new event was called while waiting for response to an event.");
            return 0;
        }
        currentEventID = eventID;
        nextEventID = 0;

        //temporary write to debug console
        DebugConsole c = DebugConsole.getInstance();
        c.write("Hello? Yes this is event text...");
        c.write("Triggered event number "+eventID);
        //NOTE: please put the events in an easy to access list. - Lars
        c.write("type event option # to respond:");
        c.write("0 - option 1");
        c.write("1 - option 2");
        c.write("2 - option 3");

        //Pop up a UI dialog box:
        //UI.text = event_text;
        //for i in (0,event_options) : {UI.button = event_choice_text, event_choice_ID}
        //return ID of clicked button
        return currentEventID; //return current event ID to give the console some context on what the f- it just did - Lars
        //TODO: myEvents.get(eventID).onTriggered once event do-something functionality is in place
    }
    public int handle_option(int option) {
        System.out.println("Handling");
        if(!eventIsWaiting())
            return 0;
        if (nextEventID != 0) {
            execute_event(nextEventID);
        }
        //do option stuff
        nextEventID = 0; //whatever next id is, if there is one.
        currentEventID = 0;
        return nextEventID;
    }

    private int nextEventID = 0;
    private int currentEventID = 0;
    public boolean eventIsWaiting(){
        return currentEventID != 0;
    }

    //NOTE: if testing standalone without litiengine, have the driver run this update on a loop. - Lars
    @Override
    public void update() {
        if(!eventIsWaiting() && nextEventID != 0)
            execute_event(nextEventID);
    }

    /** Current base class for events. Deprecated by LargeGameEvent.
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
        public String getEvent_text() { return event_text; }
        public int getEvent_id() { return event_id; }
    }


}

/* Data that needs to be associated with an event:
-ID
-Dialog text
-whether it is an initial or a follow-up event (i.e. can it happen on its own).
-Option texts
-Option IDs
-Weight (base chance of happening)
-Weight modifiers
-Prerequisites to happening (could be folded into Weight *=0)
-What happens immediately as the event fires
-What happens when an option is selected
//Potentially: options that are only sometimes available? Complicates event construction.
 */

//Draft
abstract class LargeGameEvent {
    int e_ID;
    String e_text;
    //prerequisites to fire
    int[] button_IDs;
    String[] button_texts;
    //button conditionals
    double weight = 100.0;
    boolean isRandom;

    /**
     * Minimal simple constructor for one-option common event
     * @param ID Event number
     * @param dialogtext Text displayed to user
     * @param optiontext Text on the 'OK' or similar button
     */
    public LargeGameEvent(int ID, String dialogtext, String optiontext) {
        this.e_ID = ID;
        this.e_text = dialogtext;
        this.button_IDs = new int[]{0};
        this.button_texts = new String[]{optiontext};
        this.isRandom = true;
    }

    /**
     * Longer event constructor for events with choices.
     * @param ID Event number
     * @param dialogtext Text displayed to user
     * @param option_IDs Ordered list of subsequent events, where 0 means no further result on this choice
     * @param option_texts Ordered list of dialog button choices leading to subsequent events, matched to option_IDs
     * @param starting_weight Base likelihood weight of the event (default is 100)
     * @param canTriggerRandomly Indicates whether this is a random event on the monthly pulse; otherwise a special event
     */
    public LargeGameEvent(int ID, String dialogtext, int[] option_IDs, String[] option_texts, double starting_weight, boolean canTriggerRandomly) {
        this.e_ID = ID;
        this.e_text = dialogtext;
        this.button_IDs = option_IDs;
        this.button_texts = option_texts;
        this.weight = starting_weight;
        this.isRandom = canTriggerRandomly;
    }

    public abstract void onTriggered();
    public abstract double getWeight();

    public String getEvent_text() { return e_text; }
    public int getEvent_ID() { return e_ID; }
    public String[] getEvent_optionTexts() { return button_texts; }
    public int[] getEvent_optionIDs() { return button_IDs; }
}