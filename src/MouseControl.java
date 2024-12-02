import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseControl extends MouseAdapter {

    Painter painter;
    int activeButton = MouseEvent.NOBUTTON;

    private int lastX, lastY;

    public double convertPosX(int x) {
        return (double) (x + painter.getFunctionOffsetX() - painter.fWidth / 2) /painter.getZoom();
    }

    public MouseControl(Painter painter) {
        this.painter = painter;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        activeButton = e.getButton();
        Point p = e.getPoint();

        if (activeButton == MouseEvent.BUTTON1) {
            lastX = p.x;
            lastY = p.y;
        } else if (activeButton == MouseEvent.BUTTON3 && painter.getDrawOrbitState()) {
            painter.setOrbitPoint(Double.toString(convertPosX(p.x)));
            painter.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        Point p = e.getPoint();

        if(activeButton == MouseEvent.BUTTON1) {
            int offsetX = lastX - p.x;
            int offsetY = lastY - p.y;

            //System.out.println("offsetX: " + offsetX + " offsetY: " + offsetY);

            painter.moveFunction(offsetX, offsetY);

            lastX = p.x;
            lastY = p.y;

            painter.repaint();
        } else if (activeButton == MouseEvent.BUTTON3 && painter.getDrawOrbitState()) {
            painter.setOrbitPoint(Double.toString(convertPosX(p.x)));
            painter.repaint();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int r = e.getWheelRotation();

        if(r > 0) {
            painter.decreaseZoom();
        }
        else if(r < 0) {
            painter.increaseZoom();
        }

        painter.repaint();
    }

}
