package unicus.spacegame.ui.Homeship;

import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
enum ButtonState{normal, disabled, highlight, pressed}
interface ButtonController {void onButtonPress(BasicButton button);}
public class BasicButton extends GuiComponent {
    @Override
    public void mousePressed(final MouseEvent e) {
        if(isSuspended() || !this.getBoundingBox().contains(e.getX(), e.getY())) {
            super.mousePressed(e);
            return;
        }
        if ( controller != null ) {
            controller.onButtonPress(this);
        }
    }
    //State of the button. determines how it is rendered.
    public ButtonState state;
    //Text to display on the button
    public String text;
    //Object to call when the button is pressed.
    private ButtonController controller;

    public Color onNormalColor = Color.blue;
    public Color onDisabledColor = Color.lightGray;
    public Color onHighlightColor = Color.cyan;
    public Color onPressedColor = Color.green;

    /**
     * Instantiates a new BasicButton component at the point (x,y) with the dimension (width,height).
     *
     * @param x      the x
     * @param y      the y
     * @param w  the width
     * @param h  the height
     */
    protected BasicButton(double x, double y, double w, double h, String text) {
        super(x, y, w, h);
        state = ButtonState.normal;
        this.text = text;
    }
    @Override
    public void render(Graphics2D g){
        switch (state){
            case normal:
                g.setColor(onNormalColor);
                break;
            case disabled:s:
                g.setColor(onDisabledColor);
                break;
            case highlight:
                g.setColor(onHighlightColor);
                break;
            case pressed:
                g.setColor(onPressedColor);
                break;
        }
        g.fillRect((int)getX(), (int)getY(), (int)getHeight(), (int)getWidth());
        g.setColor(Color.black);
        g.drawString(text, (int)getX()+5, (int)getY()+5);
    }


}
