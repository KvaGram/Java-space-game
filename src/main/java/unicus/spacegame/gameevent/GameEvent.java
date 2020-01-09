package unicus.spacegame.gameevent;

import de.gurkenlabs.litiengine.IUpdateable;
import unicus.spacegame.ui.DebugConsole;

import java.util.Random;

public final class GameEvent implements IUpdateable {
    private static GameEvent INSTANCE;
    private String info = "Magic singleton class";
    //ArrayList<RandomEvent> myEvents = new ArrayList<RandomEvent>();
    private RandomEvent[] myEvents;
    private GameEvent() {
        INSTANCE = this;
        DebugConsole.getInstance().addGameEventCommands();
        loadEvents();

        //myEvents.add(ScientificDiscovery);
        //myEvents.add(MinorAirLeak);
        //myEvents.add(MetallicDeposit);
        //myEvents.add(AlienMapSellerTrue);
        //myEvents.add(AlienMapSellerTrueResult);
        //myEvents.add(AlienMapSellerFake);
        //myEvents.add(AlienMapSellerFakeResult);
        //myEvents.add(GoodGrowingSeason);
        //myEvents.add(CrewPlayingGames);
        //myEvents.add(WeaponDrillAccident);
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
        //Prerequisite verifier algorithm go here

        //Weighted random selection algorithm. Akin to picking off a D&D random table with entries like 00-22, 22-30, 31-80, 81-99.
        int sum_weights=0;
        for (RandomEvent r: myEvents) {
            sum_weights += r.baseWeight;
        }
        int i = 0;
        int r = new Random().nextInt(sum_weights); //To pick based on relative weight, we have to select from sum of weights, not number of events
        while (r > myEvents[i].getWeight()) {
            r -= myEvents[i].getWeight();
            i++;
        }
        execute_event(myEvents[i].event_id); //convert index-in-list to index-by-ID

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
        for (RandomEvent e : myEvents) {
            if (e.event_id == eventID)
                currentEvent = e;
        }
        if(currentEvent == null) {
            nextEventID = 0;
            DebugConsole.getInstance().write("Oopsie: Event "+eventID+" was not found!");
            return 0;
        }
        nextEventID = 0;

        //runs the onTriggered function.
        currentEvent.onTriggered();

        //temporary write to debug console
        DebugConsole c = DebugConsole.getInstance();
        c.write(currentEvent.getText());

        c.write("type event option # to respond:");
        String[] options = currentEvent.getChoiceTexts();
        for (int i = 0; i < options.length; i++) {
            c.write(i + " - " + options[i]);
        }

        //Pop up a UI dialog box:
        //UI.text = event_text;
        //for i in (0,event_options) : {UI.button = event_choice_text, event_choice_ID}
        //return ID of clicked button
        return eventID; //return the event ID to give the console some context on what the f- it just did - Lars
    }
    public int handle_option(int option) {
        if(!eventIsWaiting())
            return 0;
        //Note: we could move next event id to the return value from onChoice.
        currentEvent.onChoice(option);
        nextEventID = currentEvent.event_choice_ids[option];

        currentEvent = null;
        return nextEventID;
    }

    private int nextEventID = 0;
    private RandomEvent currentEvent = null;
    public boolean eventIsWaiting(){
        return currentEvent != null;
    }

    //NOTE: if testing standalone without litiengine, have the driver run this update on a loop. - Lars
    @Override
    public void update() {
        if(!eventIsWaiting() && nextEventID != 0)
            execute_event(nextEventID);

    }
    private void loadEvents() {

        myEvents = new RandomEvent[] {
                new RandomEvent(10, "Scientific discovery") {
                    @Override
                    public String getText() {
                        return "One of our crewmen has made a scientific breakthrough in his spare time!"+
                                "\n We have gained 5 research points.";
                    }

                    @Override
                    public void onTriggered() {
                        //Let's do some SCIENZE!!
                    }

                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"That's good"};
                    }
                },
                new RandomEvent(20, "Minor air-leak") {
                    @Override
                    public String getText() {
                        return "There was a minor leak in one of the airlocks."+
                                "\nWe found and patched it, but our oxygen supplies have been depleted slightly.";
                    }

                    @Override
                    public void onTriggered() {
                        //Woops, time to lose some air
                    }

                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"Unfortunate"};
                    }
                },
                new RandomEvent(30, "Metallic deposit") {
                    @Override
                    public String getText() {
                        return "We have stumbled on an asteroid with a high purity metal deposit. The metal was easy to extract and has been added to our stores.";
                    }

                    @Override
                    public void onTriggered() {
                        //Let's store the stuff we found!
                    }
                },
                new RandomEvent(40, "Alien Map-seller true", new int[]{0,41}) {
                    @Override
                    public String getText() {
                        return "An independent alien ship is hailing us, offering to trade us knowledge of galactic hyperlanes for some of our shinyum.";
                    }

                    @Override
                    public void onTriggered() {

                    }

                    @Override
                    public int getWeight() {
                        //noinspection ConstantConditions
                        if(true) //have the goods needed for trade
                            return super.getWeight();
                        return 0;
                    }
                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"No thanks","Pay them 2 Shinium."};
                    }

                    @Override
                    public void onChoice(int choice) {
                        if (choice == 1){
                            //Add
                        }

                        super.onChoice(choice);
                    }
                },
                new RandomEvent(41, "Alien map-seller true result") {
                    @Override
                    public String getText() {
                        return "We have integrated the alien coordinates into our own database."+
                                "\nWe are slightly closer to finding our way back to Earth.";
                    }

                    @Override
                    public void onTriggered() {
                        //Do some navigation data voodoo, or something...
                    }

                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"Onwards!"};
                    }

                    @Override
                    public int getWeight() {
                        return 0;
                    }
                },
                new RandomEvent(45, "Alien Map-seller fake", new int[]{0,46}) {
                    @Override
                    public String getText() {
                        return "An independent alien ship is hailing us, offering to trade us knowledge of galactic hyperlanes for some of our shinyum.";
                    }

                    @Override
                    public void onTriggered() {

                    }

                    @Override
                    public int getWeight() {
                        //noinspection ConstantConditions
                        if(true) //have the goods needed for trade
                            return super.getWeight();
                        return 0;
                    }
                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"No thanks","Pay them 2 Shinium."};
                    }

                    @Override
                    public void onChoice(int choice) {
                        if (choice == 1){
                            //Add
                        }

                        super.onChoice(choice);
                    }
                },
                new RandomEvent(46, "Alien map-seller fake result") {
                    @Override
                    public String getText() {
                        return "Sadly the alien coordinates turned out to be gibberish, but after all the time we spent trying to calculate, the scammers have fled.";
                    }

                    @Override
                    public void onTriggered() {
                        //nothing we can so...
                    }

                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"Damn them!"};
                    }

                    @Override
                    public int getWeight() {
                        return 0;
                    }
                },
                new RandomEvent(50, "Good growing season") {
                    @Override
                    public String getText() {
                        return "Our hydroponic tanks have been flourishing the past week and we are ready to harvest an unusually large crop. +4 food.";
                    }

                    @Override
                    public void onTriggered() {
                        //...plus 4 food.
                    }
                },
                new RandomEvent(60, "Crew playing games") {
                    @Override
                    public String getText() {
                        return "Your crew has been socializing happily over a lot of the games in the rec room recently."+
                                "\nMorale has improved!";
                    }

                    @Override
                    public void onTriggered() {
                        //increase morale
                    }

                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"Maybe I should join them."};
                    }
                },
                new RandomEvent(70, "Weapon drill accident") {
                    @Override
                    public String getText() {
                        return "One of your marines was injured in training during live weapons practice.";
                    }

                    @Override
                    public void onTriggered() {
                        //someone gets hurt...
                    }

                    @Override
                    public String[] getChoiceTexts() {
                        return new String[]{"Medic!"};
                    }
                }

        };
    }

    //Event texts can possibly be outsourced to external file for translation later
    //TODO: How to store prerequisites/conditionals? <- fixed for now, override getWeight.
    //TODO: What's the syntax to look out at ship state variables? (e.g. amount of resources, having a specific module)

}

/*
Data that needs to be associated an event:
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
 */
//Potentially: options that are only sometimes available?

//Draft
//class RandomEvent {
//    int e_ID;
//    String e_text;
//    //prerequisites to fire
//    int[] button_IDs;
//    String button_texts;
//    //button conditionals
//    int weight = 100;
//    private Object WeightModifiers;
//    //figure out how to store a boolean condition to evaluate later
//
//    public RandomEvent(int ID, String dialogtext, String optiontext) {
//        //Minimal simple constructor: shows text, the only option is "OK" or equivalent
//    }
//    public RandomEvent(int ID, String dialogtext, int[] option_IDs, String[] option_texts[], int starting_weight, boolean canTriggerRandomly) {
//        //Longer constructor for setting up more complicated events
//    }
//    public double GetWeight() {
//        int adjusted_weight = weight;
//        for (Modifiers m: this.WeightModifiers
//             ) {
//            if (m.condition) : adjusted_weight = adjusted_weight * m.factor;
//        }
//    }
//    public ¿MatchedPairs? GetOptions() {
//        Option[] result = new Option[];
//        for (Option o: this.DialogueOptions) {
//            if (o.condition) : result.add(o);
//        }
//    }
//}