import javax.swing.*;
import java.awt.*;

class Painter extends JPanel {
    final int fHeight = Constants.FRAME_HEIGHT;
    final int fWidth = Constants.FRAME_WIDTH;
    //final int functionMoveInterval = Constants.FUNCTION_MOVE_INTERVAL;
    private double domainMin = Constants.DOMAIN_MIN;
    private double domainMax = Constants.DOMAIN_MAX;
    private double domainMaxY = Constants.DOMAIN_MAX;
    private double domainMinY = Constants.DOMAIN_MIN;
    final double dotDensity = Constants.DOT_DENSITY;
    final int zoomInterval = Constants.ZOOM_INTERVAL;
    private int zoom = Constants.DEFAULT_ZOOM_MULTIPLIER;
    final int maxJump = Constants.MAX_JUMP;
    final double unitLineSize = Constants.UNIT_LINE_SIZE;
    final int orbitStepCount = Constants.ORBIT_STEP_COUNT;
    private String inputText = "1";
    private String orbitPoint = "1";
    private boolean drawOrbitState = false;
    private int functionOffsetX = 0;
    private int functionOffsetY = 0;
    private int domainOffsetX = 0;
    private int domainOffsetY = 0;
    //private int domainRange = 0;

    @FunctionalInterface
    interface Function {
        double execute(double x, double param);
    }

    public void setDrawOrbitState(boolean state) {
        drawOrbitState = state;
    }

    public boolean getDrawOrbitState() {
        return drawOrbitState;
    }

//    public void moveLeft() {
//        domainRange -= 1;
//    }
//
//    public void moveRight() {
//        domainRange += 1;
//    }

    public int getFunctionOffsetX() {
        return functionOffsetX;
    }

    public void moveFunction(int x, int y) {
        functionOffsetX += x;
        functionOffsetY -= y;
        domainOffsetX = (functionOffsetX/zoom);
        domainOffsetY = (functionOffsetY/zoom);
    }

    public void resetToStart() {
        functionOffsetX = 0;
        functionOffsetY = 0;
        domainOffsetX = 0;
        domainOffsetY = 0;
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

    public int getZoom() {
        return zoom;
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
        // x-axis
        g.drawLine(0, convertY(0) + functionOffsetY, fWidth, convertY(0) + functionOffsetY);
        // y-axis
        g.drawLine(convertX(0) - functionOffsetX, 0, convertX(0) - functionOffsetX, fHeight);

        int y1 = convertY(-unitLineSize) + functionOffsetY;
        int y2 = convertY(unitLineSize) + functionOffsetY;
        int x1 = convertX(unitLineSize) - functionOffsetX;
        int x2 = convertX(-unitLineSize) - functionOffsetX;

        // x-axis unit lines
        for (double i = domainMin + domainOffsetX; i < domainMax + domainOffsetX; i += 1) {
            int x = convertX(i) - functionOffsetX;
            g.drawLine(x, y1, x, y2);
        }
        // y-axis unit lines
        for (double i = domainMinY + domainOffsetY; i < domainMaxY + domainOffsetY; i += 1) {
            int y = convertY(i) + functionOffsetY;
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
        for(int i = 0; i<orbitStepCount ;++i) {
            double function1 = currentX * Math.exp(a * (1 - currentX));

            g.drawLine(convertX(currentX) - functionOffsetX,
                    convertY(currentY) + functionOffsetY,
                    convertX(currentX) - functionOffsetX,
                    convertY(function1) + functionOffsetY);

            double functionX = function1 / coefficient;

            g.drawLine(convertX(currentX) - functionOffsetX,
                    convertY(function1) + functionOffsetY,
                    convertX(functionX) - functionOffsetX,
                    convertY(function1) + functionOffsetY);

            currentX = functionX;
            currentY = function1;
        }
    }

    public void drawFunction(Graphics g, double a, Color color, Function function) {
        Integer lastX = null;
        Integer lastY = null;

        g.setColor(color);


        for (double i = domainMin + domainOffsetX; i < domainMax + domainOffsetX; i += dotDensity) {
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
                g.drawLine(lastX - functionOffsetX, lastY + functionOffsetY, x - functionOffsetX, y + functionOffsetY);
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
