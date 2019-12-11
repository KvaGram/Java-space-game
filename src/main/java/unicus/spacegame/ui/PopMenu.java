package unicus.spacegame.ui;

import de.gurkenlabs.litiengine.gui.GuiComponent;

import javax.swing.*;

public class PopMenu extends GuiComponent {
    private BasicButton[] options;
    private boolean[] enabled;
    private MenuController controller;

    public PopMenu(double x, double y, double w, double h, String[] options, boolean[] enabled) {
        super(x, y, w, h);
        controller = null;
        setOptions(options, enabled);
    }

    public void setOptions(String[] textOptions, boolean[] enabled) {
        BasicButton[] buttons = new BasicButton[textOptions.length];
        for (int i = 0; i < textOptions.length; i++) {
            buttons[i] = new BasicButton(0, 0, 50, 20, textOptions[i]);
        }
        setOptions(buttons, enabled);
    }

    public void setOptions(BasicButton[] options, boolean[] enabled) {
        this.options = options;
        this.enabled = enabled;

        //Empty the components list.
        getComponents().removeAll(getComponents());

        for (int i = 0; i < options.length; i++) {
            //Add the button to the components list.
            getComponents().add(options[i]);
            options[i].state = (enabled[i]) ? ButtonState.normal: ButtonState.disabled;

            int index = i;
            options[i].setController(new ButtonController() {
                @Override
                public void onButtonPress(BasicButton button) {
                    if(controller != null)
                        controller.onSelect(index, button);
                }
            });
        }
        setDimension(getWidth(), getHeight());
        refreshOptions();
    }
    public void refreshOptions(){
        for (int i = 0; i < options.length; i++) {
            double h = getHeight() / options.length;
            double y = h * i + getY();
            double x = getX();

            options[i].setX(x);
            options[i].setY(y);
            options[i].setHeight(h);
            options[i].setWidth(getWidth());
        }
    }

    public void setController(MenuController controller) {
        this.controller = controller;
    }
}
