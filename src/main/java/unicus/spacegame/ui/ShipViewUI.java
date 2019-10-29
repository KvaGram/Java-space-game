package unicus.spacegame.ui;

import unicus.spacegame.spaceship.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import static java.lang.System.out;

/**
 * ShipViewUI acts are a wrapper for SpaceshipGUI.
 * Adding to the existing functionality, this class adds a button to generating a new spaceship.
 * It also adds some dummy buttons and a background image.
 */
public class ShipViewUI extends JLayeredPane implements ActionListener {
    //The spaceshipGUI renders a workable model of the spaceship.
    private SpaceshipGUI spaceshipGUI;
    private ImageLabel spaceshipBg;
    private ImageIcon spaceshipBgFile;

    private JPanel buttonLayer;

    private JButton btnNewSpaceship;
    private JButton btnDoCrew;
    private JButton btnDoCargo;
    private JButton btnDoEngine;

    public ShipViewUI(Spaceship spaceship)
    {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));


        spaceshipBgFile = new ImageIcon( getClass().getResource("spaceship_bg.jpg"));
        spaceshipBg = new ImageLabel("");
        spaceshipBg.setIcon(spaceshipBgFile);
        spaceshipGUI = new SpaceshipGUI(spaceship);
        spaceshipGUI.setOpaque(false);
        buttonLayer = new JPanel();
        buttonLayer.setOpaque(false);

        btnNewSpaceship = new JButton("Generate new spaceship");
        btnDoCrew       = new JButton("Manage crew");
        btnDoCargo      = new JButton("Manage cargo");
        btnDoEngine     = new JButton("Visit engineering");

        btnNewSpaceship.addActionListener(this);
        btnDoCrew.addActionListener(this);
        btnDoCargo.addActionListener(this);
        btnDoEngine.addActionListener(this);

        buttonLayer.add(btnNewSpaceship);
        buttonLayer.add(btnDoCrew);
        buttonLayer.add(btnDoCargo);
        buttonLayer.add(btnDoEngine);

        updateChildBounds();

        this.add(spaceshipBg, Integer.valueOf(10));
        this.add(spaceshipGUI, Integer.valueOf(20));
        this.add(buttonLayer, Integer.valueOf(30));
    }
    public void updateChildBounds()
    {
        Rectangle b = getBounds();
        spaceshipBg.setBounds(0, 0, b.width, b.height);
        spaceshipGUI.setBounds(50, 50, b.width - 100, b.height - 100);
        buttonLayer.setBounds(0, b.height - 50, b.width, 50);
        repaint();
    }

    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        updateChildBounds();
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
        //view.setBounds(0,0,1400, 500);
        view.setOpaque(true);
        view.setBackground(Color.GRAY);
        frame.add(view);

        frame.setVisible(true);

        System.out.println(ship.toString());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Random rand = new Random();

        if (source == btnNewSpaceship){
            Spaceship ship = Spaceship.GenerateStart1(rand, 2, 10, 0.3f, 1.0f);
            spaceshipGUI.setSpaceship(ship);
        }
        else if (source == btnDoCrew){
            out.println("You knock the crew upside down");
        }
        else if (source == btnDoCargo){
            out.println("You find a alien rat-like rodent in the cargo bay");
        }
        else if (source == btnDoEngine){
            out.println("You accidentally almost caused a reactor overload.");
        }
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
