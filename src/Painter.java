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
    private String inputText = "1";

    public void setInputText(String inputText){
        this.inputText = inputText;
    }

    public String getInputText(){
        return inputText;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // x axis
        g.drawLine(0, fHeight / 2, fWidth, fHeight / 2);
        // y axis
        g.drawLine(fWidth / 2, 0, fWidth / 2, fHeight);

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
    }
}
