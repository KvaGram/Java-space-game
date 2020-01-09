package unicus.spacegame.gameevent;

/** Current base class for events.
 * Contains its own ID and text for a dialog box, text on dialog choice options, ID of each dialog choice.
 * If the ID of a dialog choice is nonzero, that will be the followup event triggered.
 * Initial events should be numbered like BASIC: ID 10, 20, 30, 40...
 * so that follow-up events in a chain can be easily inserted at 31,32,33 for the results of event 30.
 * Argument form is e.g. (10, "A thing happened!", new int[]{0}, new String[]{"OK"}.
 * ID Weight makes things happen more or less often. Baseline is 100.
 * Weight is not included in the usual constructor because it should only rarely be altered.
 *
 * --
 * moved class out of GameEvent.
 * Access is set to package-private, so only classes in unicus.spacegame.gameevent can make one.
 * converted to an abstract, because java, we can do that.
 *  - Lars
 */
abstract class RandomEvent {
    private static final int DEFAULT_WEIGHT = 100;
    final int event_id; //Event IDs should start at 10 and be spaced apart, like BASIC line numbers, for much the same reason.
    final String event_title; //The title is a short descriptive text. May be used for debug. May be visible for player.
    final int[] event_choice_ids;
    final int baseWeight; //Each random event should have a chance of happening based on (this.weight)/(sum:weights). Most should stay at 100.

    public RandomEvent(int id, String title) {
        this(id, title, new int[]{0}, DEFAULT_WEIGHT);
    }
    public RandomEvent(int id, String title, int[] choice_ids) {
        this(id, title, choice_ids, DEFAULT_WEIGHT);
    }
    public RandomEvent(int id, String title, int[] choice_ids, int weight) {
        assert (id != 0);
        this.event_id = id;
        this.event_title = title;
        this.event_choice_ids = choice_ids;
        this.baseWeight = weight;
    }


    /** Calculates weight (chance of happening).
     *  overrides may include checks for criteria.
     *   - Defaults to returning baseWeight.
     * @return weighted chance of happening
     */
    public int getWeight(){return baseWeight;};


    /** Gets body-text of the event
     * @return A test to display
     */
    public abstract String getText();

    /**
     * Gets a list of texts to display as the choice options in the event.
     *  - Defaults to a single option 'close'.
     * @return An array of short texts
     */
    public String[] getChoiceTexts() {
        return new String[]{"close"};
    }
    /**
     * Function to run when event is triggered.
     */
    public abstract void onTriggered();

    /** Function to run once an option has been selected,
     *  - Defaults: does nothing.
     * @param choice index of choice made by player
     */
    public void onChoice(int choice) {}

    /*
    Idea: add in more optional functions, allowing for more customizing on how an event appear or function.
    Idea: have onChoice return next event to run (by default: 0). This eliminates the event_choice_ids property.
        If we also add a random seed or Random instance as a parameter for onTriggered and/or onChoice, we can get random outcomes in the events.
        This would allow merging event 40 and 45
     - Lars

     */

}
