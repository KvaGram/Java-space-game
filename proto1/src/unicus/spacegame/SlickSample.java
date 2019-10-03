package unicus.spacegame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SlickSample extends BasicGame {

    public SlickSample(String title) {
        super(title);
    }

    @Override
    public void render(GameContainer arg0, Graphics arg1) throws SlickException {
    }

    @Override
    public void init(GameContainer arg0) throws SlickException {
    }

    @Override
    public void update(GameContainer arg0, int arg1) throws SlickException {
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new SlickSample("test"));
        app.setDisplayMode(800, 600, false);
        app.start();
    }
}