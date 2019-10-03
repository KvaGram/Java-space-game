package unicus.spacegame;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.tilemap.MapRenderer;
import de.gurkenlabs.litiengine.environment.tilemap.xml.MapObject;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;

public class SpaceGameProto extends GameScreen
{

    public SpaceGameProto()
    {
        super("SPACE_GAME_MAIN");
    }
    @Override
    public void render(final Graphics2D g)
    {
        super.render(g);
        //g.setFont(Resources.fonts().get("customfont.ttf", 32f));
        g.setColor(Color.RED);
        TextRenderer.render(g, "Test text", 100, 100);
        Game.  loadEnvironment(new Environment("tiledtest.tmx"));
    }

    public static void main(String[] args)
    {
        SpaceGameProto spacegame = new SpaceGameProto();

        Game.setInfo("gameinfo.xml");
        Game.init();
        Game.start();

        Game.screens().add(spacegame);
        Game.screens().display(spacegame);

        System.out.println("Game version is " + Game.info().getVersion());

    }

    private class Foo extends GameScreen
    {

    }
}
