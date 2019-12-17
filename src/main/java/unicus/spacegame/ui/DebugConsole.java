package unicus.spacegame.ui;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.gurkenlabs.litiengine.IUpdateable;
import unicus.spacegame.spaceship.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;

import static com.mojang.brigadier.arguments.IntegerArgumentType.*;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.*;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.*;

public class DebugConsole implements IUpdateable {
    public static void main(String[] args) {
        DebugConsole c = new DebugConsole(Spaceship.GenerateStart1(new Random(), 8, 10, 0.5f, 0.8f));
        c.run();

    }


    private Spaceship homeship;
    PrintStream out;
    JTextArea ta;
    JTextField tf;
    public DebugConsole(Spaceship homeship) {
        this.homeship = homeship;
        ta = new JTextArea();
        tf = new JTextField();
        ta.setBackground(Color.lightGray);
        tf.setBackground(Color.yellow);

        TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
        out = new PrintStream(taos);
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
                    process(input);
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
    }

    private void process(String rawInput) {

        String[] input = rawInput.toLowerCase().split(" ");
        //System.out.println("Processing input");
        //System.out.println("> " + input);
        if(input[0].equals("print")){
            if(input[1].equals("ship")) {
                StringBuffer b = new StringBuffer();
                homeship.getInfo(b);
                out.println(b.toString());
            }
            else
                out.println("Print what? input error");
        }
        else
            out.println("unknown command");
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