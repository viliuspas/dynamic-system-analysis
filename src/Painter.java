import javax.swing.*;
import java.awt.*;

class Painter extends JPanel {
    final int fHeight = Constants.FRAME_HEIGHT;
    final int fWidth = Constants.FRAME_WIDTH;
    final double domainMin = Constants.DOMAIN_MIN;
    final double domainMax = Constants.DOMAIN_MAX;
    final double dotDensity = Constants.DOT_DENSITY;
    final int zoom = Constants.ZOOM_MULTIPLIER;
    final int maxJump = Constants.MAX_JUMP;
    final double unitLineSize = Constants.UNIT_LINE_SIZE;
    private String inputText = "1";
    private String orbitPoint = "1";

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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCoordinateSystem(g);

        double a = Double.parseDouble(getInputText());
        Integer lastX = null;
        Integer lastY = null;

        g.setColor(Color.blue);

        for (double i = domainMin; i < domainMax; i += dotDensity) {
            double function = i * Math.exp(a * (1 - i));

            int x = (int)(i * zoom + fWidth / 2);
            int y = fHeight - (int)(function * zoom + fHeight / 2);

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
        //second function

        lastX = null;
        lastY = null;
        double selectedX = 1;
        double selectedY = selectedX * Math.exp(a * (1 - selectedX));
        double coefficient = selectedY / selectedX;

        g.setColor(Color.red);

        for (double i = domainMin; i < domainMax; i += dotDensity) {
            double function = coefficient * i;

            int x = (int)(i * zoom + fWidth / 2);
            int y = fHeight - (int)(function * zoom + fHeight / 2);

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
}
