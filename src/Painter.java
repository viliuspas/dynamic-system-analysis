import javax.swing.*;
import java.awt.*;

class Painter extends JPanel {
    final int fHeight = Constants.FRAME_HEIGHT;
    final int fWidth = Constants.FRAME_WIDTH;
    final double domainMin = Constants.DOMAIN_MIN;
    final double domainMax = Constants.DOMAIN_MAX;
    final double dotDensity = Constants.DOT_DENSITY;
    final int zoomInterval = Constants.ZOOM_INTERVAL;
    private int zoom = Constants.DEFAULT_ZOOM_MULTIPLIER;
    final int maxJump = Constants.MAX_JUMP;
    final double unitLineSize = Constants.UNIT_LINE_SIZE;
    private String inputText = "1";
    private String orbitPoint = "1";
    private boolean drawOrbitState = false;

    @FunctionalInterface
    interface Function {
        double execute(double x, double param);
    }

    public void setDrawOrbitState(boolean state) {
        drawOrbitState = state;
    }

    public void increaseZoom() {
        zoom += zoomInterval;
    }

    public void decreaseZoom() {
        if (zoom > 10) {
            if(zoom - zoomInterval < 10) {
                zoom = 10;
            }
            else {
                zoom -= zoomInterval;
            }
        }
    }

    public void setInputText(String inputText){
        this.inputText = inputText;
    }

    public String getInputText(){
        return inputText;
    }

    public void setOrbitPoint(String orbitPoint){
        this.orbitPoint = orbitPoint;
    }

    public String getOrbitPoint(){
        return orbitPoint;
    }

    public void drawCoordinateSystem(Graphics g) {
        // x axis
        g.drawLine(0, fHeight / 2, fWidth, fHeight / 2);
        // y axis
        g.drawLine(fWidth / 2, 0, fWidth / 2, fHeight);

        int y1 = (int)(fHeight / 2 + unitLineSize * zoom);
        int y2 = (int)(fHeight / 2 - unitLineSize * zoom);
        int x1 = (int)(fWidth / 2 + unitLineSize * zoom);
        int x2 = (int)(fWidth / 2 - unitLineSize * zoom);

        for (double i = domainMin; i < domainMax; i += 1) {
            int x = (int)(i * zoom + fWidth / 2);
            int y = (int)(i * zoom + fHeight / 2);

            g.drawLine(x, y1, x, y2);
            g.drawLine(x1, y, x2, y);
        }
    }

    public int convertX(double x) {
        return (int)(fWidth/2 + x * zoom);
    }

    public int convertY(double y) {
        return (int)(fHeight/2 - y * zoom);
    }

    public void drawOrbit(Graphics g, double a) {

        double selectedX = 1;
        double selectedY = selectedX * Math.exp(a * (1 - selectedX));
        double coefficient = selectedY / selectedX;

        drawFunction(g, a, Color.red, (x, param) -> coefficient * x);

        double startX = Double.parseDouble(getOrbitPoint());

        g.setColor(Color.green);

        double currentX = startX;
        double currentY = 0;
        for(int i = 0; i<20 ;++i) {
            double function1 = currentX * Math.exp(a * (1 - currentX));

            g.drawLine(convertX(currentX), convertY(currentY), convertX(currentX), convertY(function1));

            double functionX = function1 / coefficient;

            g.drawLine(convertX(currentX), convertY(function1), convertX(functionX), convertY(function1));

            currentX = functionX;
            currentY = function1;
        }
    }

    public void drawFunction(Graphics g, double a, Color color, Function function) {
        Integer lastX = null;
        Integer lastY = null;

        g.setColor(color);

        for (double i = domainMin; i < domainMax; i += dotDensity) {
            double functionY = function.execute(i, a);

            int x = convertX(i);
            int y = convertY(functionY);

            if(lastX == null){
                lastX = x;
                lastY = y;
            }

            // ensures that the distance between points isn't too big
            // avoids calculation / drawing errors
            if(Math.abs(x - lastX) < maxJump && Math.abs(y - lastY) < maxJump){
                g.drawLine(lastX, lastY, x, y);
            }

            lastX = x;
            lastY = y;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCoordinateSystem(g);

        double a = Double.parseDouble(getInputText());
        drawFunction(g, a, Color.blue, (x, param) -> x * Math.exp(param * (1 - x)));

        //orbit

        if(drawOrbitState){ drawOrbit(g, a); }
    }
}
