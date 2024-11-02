import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args){

        JFrame frame = new JFrame("f(x) = x * exp(a(1 - x))");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.setVisible(true);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("a =");
        inputPanel.add(label);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, -20, 20, 1);
        inputPanel.add(slider);

        JTextField textField = new JTextField("1", 3);
        inputPanel.add(textField);

        frame.add(inputPanel, BorderLayout.NORTH);

        Painter painter = new Painter();
        frame.add(painter, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            String inputText = Integer.toString(slider.getValue());
            textField.setText(Integer.toString(slider.getValue()));
            painter.setInputText(inputText);
            frame.add(painter);
            frame.update(frame.getGraphics());
        });

        textField.addActionListener(e -> {
            String inputText = textField.getText();
            painter.setInputText(inputText);
            frame.add(painter);
            frame.update(frame.getGraphics());
        });

        frame.setVisible(true);
    }
}