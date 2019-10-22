package unicus.spacegame.ui;

import unicus.spacegame.spaceship.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ShipViewUI extends JLayeredPane {
    //The spaceshipGUI renders a workable model of the spaceship.
    private SpaceshipGUI spaceshipGUI;
    private ImageLabel spaceshipBg;
    private ImageIcon spaceshipBgFile;

    public ShipViewUI(Spaceship spaceship)
    {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        spaceshipBgFile = new ImageIcon("src/main/resources/spaceship_bg.jpg");
        spaceshipBg = new ImageLabel("");
        spaceshipBg.setIcon(spaceshipBgFile);
        spaceshipGUI = new SpaceshipGUI(spaceship);
        spaceshipGUI.setOpaque(false);

        spaceshipGUI.setBounds(100,100,1200, 400);
        spaceshipBg.setBounds(this.getBounds());
        this.add(spaceshipBg, 100);
        this.add(spaceshipGUI, 0);

    }

    public void setBounds(int x, int y, int width, int height){
        spaceshipBg.setBounds(x, y, width, height);
        super.setBounds(x, y, width, height);
    }
    public void setBounds(Rectangle r){
        spaceshipBg.setBounds(r);
        super.setBounds(r);
    }
    public static void main(String[] args) {
        Random rand;
        if(args.length > 1){
            try{
                rand = new Random(Integer.parseInt(args[0]));
            } catch (NumberFormatException err) {
                System.err.println(err);
                rand = new Random();
            }
        } else {
            rand = new Random(0);
        }

        Spaceship ship = Spaceship.GenerateStart1(rand, 2, 10, 0.3f, 1.0f);
        ShipViewUI view = new ShipViewUI(ship);

        JFrame frame = new JFrame("Ship view proto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 600);
        view.setBounds(0,0,1400, 600);
        view.setOpaque(true);
        view.setBackground(Color.GRAY);
        frame.add(view);

        frame.setVisible(true);

        System.out.println(ship.toString());

    }
    //from https://stackoverflow.com/questions/10634417/image-resize-to-fit-on-jpanel
    private class ImageLabel extends JLabel{
        private Image _myimage;

        public ImageLabel(String text){
            super(text);
        }

        public void setIcon(Icon icon) {
            super.setIcon(icon);
            if (icon instanceof ImageIcon)
            {
                _myimage = ((ImageIcon) icon).getImage();
            }
        }

        @Override
        public void paint(Graphics g){
            g.drawImage(_myimage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
}
