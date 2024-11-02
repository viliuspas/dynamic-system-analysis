import javax.swing.*;
import java.awt.*;

class Painter extends JPanel {
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // x
        g.drawLine(0, Constants.FRAME_HEIGHT / 2, Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT / 2);
        // y
        g.drawLine(Constants.FRAME_WIDTH / 2, 0, Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT);

        int a = 1;
        for (double i = -100; i < 100; i += Constants.DOT_DENSITY) {
            double function = a * i * Math.exp(1-i);

            int x = (int)(i * Constants.ZOOM_LEVEL + Constants.FRAME_WIDTH / 2);
            int y = Constants.FRAME_HEIGHT - (int)(function * Constants.ZOOM_LEVEL + Constants.FRAME_HEIGHT / 2) - 5;

            g.fillOval(x, y, 5, 5);
        }
    }
}
