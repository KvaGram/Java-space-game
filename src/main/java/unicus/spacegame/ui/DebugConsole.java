package unicus.spacegame.ui;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.gurkenlabs.litiengine.IUpdateable;
import unicus.spacegame.SpaceGame;
import unicus.spacegame.crew.SpaceCrew;
import unicus.spacegame.gameevent.GameEvent;
import unicus.spacegame.crew.*;
import unicus.spacegame.spaceship.AbstractShipModule;
import unicus.spacegame.spaceship.HomeShip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.CommandDispatcher;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.*;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.*;
import com.mojang.brigadier.arguments.*;
import unicus.spacegame.spaceship.ShipLoc;


public class DebugConsole implements IUpdateable {
    private static DebugConsole instance;

    public static void main(String[] args) {
        DebugConsole c = new DebugConsole();
        HomeShip.GenerateStart1(new Random(), 8, 10, 0.5f, 0.8f);
        c.run();
    }


    PrintStream out;
    JTextArea ta;
    JTextField tf;
    TextAreaOutputStream outStream;

    Random random;

    CommandDispatcher<Object> dispatcher;
    private static Object dummySender = new Object();

    public DebugConsole() {
        ta = new JTextArea();
        tf = new JTextField();
        ta.setBackground(Color.lightGray);
        tf.setBackground(Color.yellow);

        outStream = new TextAreaOutputStream(ta, 60);
        out = new PrintStream(outStream);

        random = new Random();

        dispatcher = new CommandDispatcher<Object>();
        instance = this;
    }

    public void write(String text) {
        out.println(text);
    }

    public static DebugConsole getInstance() {
        if (instance == null)
            instance = new DebugConsole();
        return instance;
    }

    public void addShipCommands() {
        //SpaceCrew crew = SpaceCrew.getInstance();
        HomeShip ship = HomeShip.getInstance();

        /*
        Print - prints information about the homeship (todo: the crew, cargo etc)

        print ship: print a summary of the homeship
        print [ShipLoc] print a summary of a module or section.


        refit - change the ship configuration

        refit remove [ShipLoc] - Attempts to remove a module or section by normal means.
        refit remove [ShipLoc] check - checks if a module or section can be removed.
        refit remove [ShipLoc] force - instantly removes a module or section ignoring requirements, tasks and resources.

        refit remove component [ShipLoc] [index]
        refit remove component [ShipLoc] [index] check
        refit remove component [ShipLoc] [index] force

        refit build [ShipLoc] [partType] - Attempts to build the partType module or section-frame by normal means.
        refit build [ShipLoc] [partType] check - checks if the partType module or section-frame can be built.
        refit build [ShipLoc] [partType] force - instantly builds the module or section, ignoring illegal combinations, requirements, tasks and resources.

        refit build component [ShipLoc] [index] [partType]
        refit build component [ShipLoc] [index] [partType] check
        refit build component [ShipLoc] [index] [partType] force
         */
        dispatcher.register(
                literal("print").then(
                        literal("ship")
                                .executes( context -> {
                                            printShip();
                                            return 1;
                                        }
                                )
                ).then(
                        argument("loc", shipLocArgument())
                                .executes( context -> {
                                    printShipLoc(context.getArgument("loc", ShipLoc.class));
                                    return 1;
                                })

                )
        );
    }

    public void addGameEventCommands() {
        GameEvent gameEvent = GameEvent.getInstance();
        /*
        event run [event_id] - forcefully runs the event of this id, if it exist. event_id must be above 0, fails if an event is waiting
        event option [option_choice] - selects an option as response to an event, fails if no event is waiting.
        event random - runs a randomly selected event, fails if an event is waiting

         */


        dispatcher.register(literal("event").then(
                literal("run").then(
                    argument("event_id", integer(1)).executes(context -> {
                        int eventID = context.getArgument("event_id", int.class);
                        gameEvent.event_byID(eventID);
                        return eventID;
                    })
                )
            ).then(
                literal("option").then(
                    argument("option_choice", integer(0)).executes(context -> {
                        int optionChoice = context.getArgument("option_choice", int.class);
                        return gameEvent.handle_option(optionChoice);

                    })
                )
            ).then(
                literal("random").executes(context -> {
                    int event_id = gameEvent.event_Random();
                    return event_id;
                })
            )
        );


    }
    public void addGameCommands() {
        dispatcher.register(
            literal("game").then(
                literal("date").then(
                    literal("advance").executes( context -> {
                        SpaceGame.NextMonth();
                        return SpaceGame.getGameMonth();
                    })
                ).then(
                    literal("print").executes(context -> {
                        out.println(SpaceGame.getDate());
                        return SpaceGame.getGameMonth();
                    })
                )
            )
        );
    }
    public void addCrewCommands() {
        /*
        crew add [crewmanstate] [birthdate] (creates a new crewman, with random characteristics)
        crew remove [crewID] (Converts a crewman to a forgotten memorial, functionally dead and removed from the game)
        crew print all (prints out a detailed list of all crewmembers on the homeship, living or dead.)
        crew print [crewID] (prints details of the selected crewman.)

        job assign [jobID] [crewID] (attempts to assign the job to the crewman)
        job unassign [jobID] [crewID] (unassigns the job from the crewman)
        job print all (prints a detailed list of all jobs)
        job print [jobID] (prints detailed info of the job)
         */
        dispatcher.register(
            literal("crew").then(
                literal("add").then(
                    literal("adult").then(
                        argument("birthdate", integer()).executes(
                            context -> {
                                SpaceCrew spaceCrew = SpaceCrew.getInstance();
                                int key = spaceCrew.getCrewKeys().yieldKey();
                                int birthday = context.getArgument("birthdate", int.class);
                                //String name = context.getArgument("name", String.class);
                                AbleCrewman c = new AdultCrewman(key, birthday, random.nextLong(), new int[0]);
                                spaceCrew.addReplaceCrewmen(c);
                                out.println("Created a new adult crewman. ID: " + key);
                                return key;
                            }
                        )
                    )
                )
            ).then(
                literal("remove").then(
                    argument("crewID", integer()).executes(
                        context -> {
                            SpaceCrew spaceCrew = SpaceCrew.getInstance();
                            int key = context.getArgument("crewID", int.class);
                            AbstractCrewman crewman = spaceCrew.getCrew(key);
                            if(crewman == null) {
                                out.println("CrewID " + key + " was not found!");
                                return 0;
                            }
                            out.println(crewman.getSelfID().name + " will be turned into a memorial, and considered dead!");
                            out.println("function not implemented yet!");
                            return key;
                        }
                    )
                )
            ).then(
                literal("print").then(
                    literal("all").executes(
                        context -> {
                            AbstractCrewman[] crewlist =  SpaceCrew.getInstance().getCrewmen();
                            String separator = "\n--------------\n";
                            StringBuffer text = new StringBuffer();
                            text.append(separator);
                            for (AbstractCrewman c : crewlist) {
                                c.toString(text);
                                text.append(separator);
                            }
                            out.print(text);
                            return crewlist.length;
                        }
                    )
                ).then(
                    argument("crewID", integer()).executes(
                        context -> {
                            SpaceCrew spaceCrew = SpaceCrew.getInstance();
                            int key = context.getArgument("crewID", int.class);
                            AbstractCrewman crewman = spaceCrew.getCrew(key);
                            if(crewman == null) {
                                out.println("CrewID " + key + " was not found!");
                                return 0;
                            }
                            else {
                                out.println(crewman.toString());
                                return key;
                            }

                        }
                    )
                )
            )
        );
        dispatcher.register(
            literal("job").then(
                literal("print").then(
                    literal("all").executes(
                        context -> {
                            AbstractJob[] joblist =  SpaceCrew.getInstance().getJobs();
                            String separator = "\n--------------\n";
                            StringBuffer text = new StringBuffer();
                            text.append(separator);
                            for (AbstractJob j : joblist) {
                                j.toString(text);
                                text.append(separator);
                            }
                            out.print(text);
                            return joblist.length;
                        }
                    )
                ).then(
                    argument("jobID", integer()).executes(
                        context -> {
                            SpaceCrew spaceCrew = SpaceCrew.getInstance();
                            int key = context.getArgument("jobID", int.class);
                            AbstractJob job = spaceCrew.getJob(key);
                            if(job == null) {
                                out.println("jobID " + key + " was not found!");
                                return 0;
                            }
                            else {
                                out.println(job.toString());
                                return key;
                            }
                        }
                    )
                )
            ).then(
                literal("assign").then(
                    argument("jobID", integer()).then(
                        argument("crewID", integer()).executes(
                            context -> {
                                SpaceCrew spaceCrew = SpaceCrew.getInstance();
                                int jobID = context.getArgument("jobID", int.class);
                                int crewID = context.getArgument("crewID", int.class);
                                StringBuffer message = new StringBuffer();
                                if (spaceCrew.canAssignJobCrew(jobID, crewID, message)) {
                                    spaceCrew.assignJobCrew(jobID, crewID);
                                    out.println("Job has been assigned!");
                                    return jobID;
                                }
                                else {
                                    out.println(message);
                                    return 0;
                                }
                            }
                        )
                    )
                )
            ).then(
                literal("unassign").then(
                    argument("jobID", integer()).then(
                        argument("crewID", integer()).executes(
                            context -> {
                                SpaceCrew spaceCrew = SpaceCrew.getInstance();
                                int jobID = context.getArgument("jobID", int.class);
                                int crewID = context.getArgument("crewID", int.class);
                                JobAssignment ja = spaceCrew.getJobAssignment(jobID, crewID);
                                if(ja == null) {
                                    out.println("This job assignment don't exist.");
                                    return 0;
                                }
                                else {
                                    out.println("Job assignment removed!");
                                    spaceCrew.unassignJobCrew(jobID, crewID);
                                    return jobID;
                                }
                            }
                        )
                    )
                )
            )
        );
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.add(new JLabel("debug terminal"), BorderLayout.NORTH);
        System.setOut(out);

        frame.add(new JScrollPane(ta), BorderLayout.CENTER);
        frame.add(new JScrollPane(tf), BorderLayout.SOUTH);

        tf.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been released.
             *
             * @param e
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    String input = tf.getText();
                    tf.setText("");
                    try {
                        dispatcher.execute(input, new Object());
                    } catch (CommandSyntaxException ex) {
                        out.println(ex.getMessage());
                    }
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private ShipLocArgument shipLocArgument(){
        return new ShipLocArgument(HomeShip.getInstance());
    }

    private void printShipLoc(ShipLoc loc){
        StringBuffer b = new StringBuffer();
        loc.getModule().getInfo(b);
        out.println(b.toString());
    }

    private void printShip(){
        StringBuffer b = new StringBuffer();
        String name = "The Homeship";
        int numCrew = 0;
        int numJobs = 0;
        b.append("\nSummary of " + name);
        b.append("\nNumber of crew - " + numCrew);
        b.append("\nNumber of jobs - " + numJobs);
        b.append("\nNumber of buildable sections - " + HomeShip.getMiddleLength());
        b.append("\n------------------------------------");
        for (AbstractShipModule m : HomeShip.getInstance().modules.values()) {
            m.getInfo(b);
        }
        out.println(b.toString());
    }



    /**
     * This method is called by the game loop on all objects that need to update
     * their attributes. It is called on every tick, means, it is called
     * Game.GameLoop.TICKS_PER_SECOND times per second.
     */
    @Override
    public void update() {

    }
}

class InvalidShipLocMessage implements Message{
    private String message;

    public InvalidShipLocMessage(String message) {
        this.message = message;
    }

    @Override
    public String getString() {
        return message;
    }
}

class ShipLocArgument implements ArgumentType<ShipLoc> {
    private HomeShip homeship;
    private ShipLocTarget target;

    ShipLocArgument(HomeShip homeship, ShipLocTarget target) {
        this.homeship = homeship;
        this.target = target;
    }
    ShipLocArgument(HomeShip homeship) {
        this(homeship, ShipLocTarget.either);
    }

    @Override
    public ShipLoc parse(StringReader reader) throws CommandSyntaxException {
        reader.expect('(');
        int section = reader.readInt();
        reader.expect(':');
        int module = reader.readInt();
        reader.expect(')');

        CommandExceptionType exceptionType = new CommandExceptionType() {};

        ShipLoc loc = new ShipLoc(section, module);
        if (!loc.isValidModule()) {
            Message msg = new Message() {
                @Override public String getString() {return loc.toString() + " does not point to a valid module or section."; }
            };
            throw new CommandSyntaxException(exceptionType, msg);
        }
        if(target != ShipLocTarget.either) {
            if(target == ShipLocTarget.module && loc.getM() == 0) {
                Message msg = new Message() {
                    @Override public String getString() {return loc.toString() + " points to a section, but a module was expected"; }
                };
                throw new CommandSyntaxException(exceptionType, msg);
            }
            else if (target == ShipLocTarget.section && loc.getM() != 0) {
                Message msg = new Message() {
                    @Override public String getString() {return loc.toString() + " points to a module, but a section was expected"; }
                };
                throw new CommandSyntaxException(exceptionType, msg);
            }
        }
        return loc;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return new CompletableFuture<>();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("(3:2)","(0,0)","9:5");
    }
}
enum ShipLocTarget{either, module, section}

class TextAreaOutputStream extends OutputStream {

// *************************************************************************************************
// INSTANCE MEMBERS
// *************************************************************************************************

    private byte[]                          oneByte;                                                    // array for write(int val);
    private Appender                        appender;                                                   // most recent action

    public TextAreaOutputStream(JTextArea txtara) {
        this(txtara,1000);
    }

    public TextAreaOutputStream(JTextArea txtara, int maxlin) {
        if(maxlin<1) { throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value="+maxlin+")"); }
        oneByte=new byte[1];
        appender=new Appender(txtara,maxlin);
    }

    /** Clear the current console text area. */
    public synchronized void clear() {
        if(appender!=null) { appender.clear(); }
    }

    public synchronized void close() {
        appender=null;
    }

    public synchronized void flush() {
    }

    public synchronized void write(int val) {
        oneByte[0]=(byte)val;
        write(oneByte,0,1);
    }

    public synchronized void write(byte[] ba) {
        write(ba,0,ba.length);
    }

    public synchronized void write(byte[] ba,int str,int len) {
        if(appender!=null) { appender.append(bytesToString(ba,str,len)); }
    }

    static private String bytesToString(byte[] ba, int str, int len) {
        try { return new String(ba,str,len,"UTF-8"); } catch(UnsupportedEncodingException thr) { return new String(ba,str,len); } // all JVMs are required to support UTF-8
    }

// *************************************************************************************************
// STATIC MEMBERS
// *************************************************************************************************

    static class Appender
            implements Runnable
    {
        private final JTextArea             textArea;
        private final int                   maxLines;                                                   // maximum lines allowed in text area
        private final LinkedList<Integer> lengths;                                                    // length of lines within text area
        private final ArrayList<String> values;                                                     // values waiting to be appended

        private int                         curLength;                                                  // length of current line
        private boolean                     clear;
        private boolean                     queue;

        Appender(JTextArea txtara, int maxlin) {
            textArea =txtara;
            maxLines =maxlin;
            lengths  =new LinkedList<Integer>();
            values   =new ArrayList<String>();

            curLength=0;
            clear    =false;
            queue    =true;
        }

        synchronized void append(String val) {
            values.add(val);
            if(queue) { queue=false; EventQueue.invokeLater(this); }
        }

        synchronized void clear() {
            clear=true;
            curLength=0;
            lengths.clear();
            values.clear();
            if(queue) { queue=false; EventQueue.invokeLater(this); }
        }

        // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
        public synchronized void run() {
            if(clear) { textArea.setText(""); }
            for(String val: values) {
                curLength+=val.length();
                if(val.endsWith(EOL1) || val.endsWith(EOL2)) {
                    if(lengths.size()>=maxLines) { textArea.replaceRange("",0,lengths.removeFirst()); }
                    lengths.addLast(curLength);
                    curLength=0;
                }
                textArea.append(val);
            }
            values.clear();
            clear =false;
            queue =true;
        }

        static private final String         EOL1="\n";
        static private final String         EOL2=System.getProperty("line.separator",EOL1);
    }

}