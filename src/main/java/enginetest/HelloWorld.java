package enginetest;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.GameListener;
import de.gurkenlabs.litiengine.graphics.RenderComponent;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HelloWorld {
    public static void main(String[] args) {
        HelloWorld hello = new HelloWorld();

//        System.out.println("main - before init");
        hello.init();
//        System.out.println("main - before run");
        hello.run();
//        System.out.println("main - end of main");
    }

    private void init() {
        Game.addGameListener(new GameListener() {
            @Override //Runs when the game starts
            public void started() {
                System.out.println("Hello event started");
            }

            @Override //Runs when the game initialize
            public void initialized(String... args) {
                System.out.println("Hello event initialized");
            }

            @Override //Runs when the program is asked to exit.
            public boolean terminating() {
                System.out.println("Hello event terminating");
                return true; //confirms it is ok to terminate
            }
            @Override //Runs when the program exits (cleanup)
            public void terminated() {
                System.out.println("Hello event terminated");
            }
        });
        Game.setInfo("gameinfo.xml");

        Game.init();
        Image cursor;
        try {

            //try loading file.
            cursor = ImageIO.read(Resources.getLocation("cursor1.png")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        } catch (IOException | IllegalArgumentException err) {
            //paint backup icon.
            System.out.println(err);
            cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics g = cursor.getGraphics();
            g.setColor(Color.red);
            g.drawOval(0, 0, 16, 16);
        }
        Game.window().getRenderComponent().setCursor(cursor);
        //Input.mouse().setGrabMouse(false);


        TestScreen testScreen = new TestScreen("TEST");
        Game.screens().add(testScreen);

        Game.screens().display("TEST");
    }

    private void run() {

        //Game start() seems to start a new thread,
        // as this function exists after starting the game.
        Game.start();
    }



}
class TestScreen extends GameScreen {
    protected TestScreen(String screenId) {
        super(screenId);

    }
    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        //g.setFont(Resources.fonts().get("customfont.ttf",32f));
        g.setColor(Color.RED);
        TextRenderer.render(g, "Test text", 100, 100);
    }
}