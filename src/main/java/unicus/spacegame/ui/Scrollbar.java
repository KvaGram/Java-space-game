package unicus.spacegame.ui;

import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Scrollbar extends GuiComponent {

    private final Axis2D axis;
    private Dimension viewArea;
    private final Dimension area;
    private double scroll;
    private ArrayList<ScrollbarListener> listeners;


    public Scrollbar(double x, double y, double width, double height, Axis2D axis, Dimension area, Dimension viewArea) {
        super(x, y, width, height);
        this.area = area;
        this.axis = axis;
        this.viewArea = viewArea;
        listeners = new ArrayList<>();
    }
    @Override
    public void mouseDragged(final MouseEvent e) {
        //confirm the mouse is over the scrollbar.
        if(isSuspended() || !this.getBoundingBox().contains(e.getX(), e.getY())) {
            super.mouseDragged(e);
            return;
        }
        scrollUpdate(e);
    }
    @Override
    public void mousePressed(final MouseEvent e) {
        //confirm the mouse is over the scrollbar.
        if(isSuspended() || !this.getBoundingBox().contains(e.getX(), e.getY())) {
            super.mouseDragged(e);
            return;
        }
        scrollUpdate(e);
    }

    private int scrollable() {
        if(axis == Axis2D.horizontal)
            return area.width - viewArea.width;
        else
            return area.height - viewArea.height;
    }

    public void scrollUpdate(final MouseEvent e) {
        if(axis == Axis2D.horizontal)
            scroll = ((e.getX() - getX()) / getWidth()) * scrollable();
        else //Axis2D.vertical
            scroll = ((e.getY() - getY()) / getHeight()) * scrollable();
        if(scroll < 0)
            scroll = 0;
        else if (scroll > scrollable())
            scroll = scrollable();

        //runs scrollupdate on all listeners
        for (ScrollbarListener evt : listeners) {
            evt.onScrollUpdate(axis, scroll);
        }
    }
    public void addScrollListener(ScrollbarListener listener) {
        listeners.add(listener);
    }
    public boolean removeScrollListener(ScrollbarListener listener) {
        return listeners.remove(listener);
    }
    public boolean tooSmall(){
        return scrollable() <= 0;
    }


    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if(tooSmall())
            return;
        double x = getX();
        double y = getY();

        RoundRectangle2D bar = new RoundRectangle2D.Double(x, y, getWidth(), getHeight(), 10, 10);

        //offset x or y with the current scroll value
        if(axis == Axis2D.horizontal)
            x += getWidth() * (scroll / scrollable());
        else
            y += getHeight() * (scroll / scrollable());

        Rectangle.Double knob = new Rectangle.Double(x, y, getHeight(), getHeight());

        g.setColor(Color.darkGray);
        g.fill(bar);
        g.setColor(Color.LIGHT_GRAY);
        g.fill(knob);
    }
}
