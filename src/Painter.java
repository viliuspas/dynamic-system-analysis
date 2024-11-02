import javax.swing.*;
import java.awt.*;

class Painter extends JPanel {

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

        // x
        g.drawLine(0, Constants.FRAME_HEIGHT / 2, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT / 2);
        // y
        g.drawLine(Constants.FRAME_WIDTH / 2, 0, Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT);

        double a = Double.parseDouble(getInputText());
        Integer lastX = null;
        Integer lastY = null;

        g.setColor(Color.blue);
        for (double i = Constants.DOMAIN_MIN; i < Constants.DOMAIN_MAX; i += Constants.DOT_DENSITY) {
            double function = i * Math.exp(a * (1 - i));

            int x = (int)(i * Constants.ZOOM_MULTIPLIER + Constants.FRAME_WIDTH / 2);
            int y = Constants.FRAME_HEIGHT - (int)(function * Constants.ZOOM_MULTIPLIER + Constants.FRAME_HEIGHT / 2);

            if(lastX == null){
                lastX = x;
                lastY = y;
            }

            // ensures that the distance between points isn't too big
            // avoids calculation / drawing errors
            if(Math.abs(x - lastX) < Constants.MAX_JUMP && Math.abs(y - lastY) < Constants.MAX_JUMP){
                g.drawLine(lastX, lastY, x, y);
            }

            lastX = x;
            lastY = y;
        }
    }
}
