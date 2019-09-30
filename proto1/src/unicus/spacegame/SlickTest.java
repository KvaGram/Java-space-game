package unicus.spacegame;
import org.newdawn.slick.*;

public class SlickTest extends BasicGame
{
    /**
     * Create a new basic game
     *
     * @param title The title for the game
     */
    public SlickTest(String title) {
        super(title);
    }
    public static void main(String[] args)
    {
        SlickTest test = new SlickTest("TÆSTGAIM");
        AppGameContainer container;
        try
        {
            container = new AppGameContainer(test, 500, 500, false);
            container.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void init(GameContainer container) throws SlickException
    {

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {

    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException
    {

    }
}