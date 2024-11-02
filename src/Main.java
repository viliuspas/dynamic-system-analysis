import javax.swing.*;

public class Main{
    public static void main(String[] args){


        JFrame frame = new JFrame("Function");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.setVisible(true);

        Painter painter = new Painter();

        frame.add(painter);
        frame.update(frame.getGraphics());
    }
}

