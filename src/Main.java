import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args){

        JFrame frame = new JFrame("f(x) = x * exp(a(1 - x))");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        frame.setVisible(true);

        JPanel inputPanel = new JPanel();
        //inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // first chunk
        JPanel firstChunkPanel = new JPanel();
        firstChunkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("a =");
        firstChunkPanel.add(label);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, -50, 50, 1);
        firstChunkPanel.add(slider);

        JTextField textField = new JTextField("1", 3);
        firstChunkPanel.add(textField);

        // second chunk
        JPanel secondChunkPanel = new JPanel();
        secondChunkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label2 = new JLabel("o =");
        secondChunkPanel.add(label2);

        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, -50, 50, 1);
        secondChunkPanel.add(slider2);

        JTextField orbitPoint = new JTextField("1", 3);
        secondChunkPanel.add(orbitPoint);

        JCheckBox orbitCheckBox = new JCheckBox("");
        secondChunkPanel.add(orbitCheckBox);

        // coordinate system control chunk

        JPanel coordinateSystemPanel = new JPanel();
        coordinateSystemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton buttonZoomIn = new JButton("+");
        JButton buttonZoomOut = new JButton("-");
        coordinateSystemPanel.add(buttonZoomIn);
        coordinateSystemPanel.add(buttonZoomOut);

        inputPanel.add(firstChunkPanel);
        inputPanel.add(secondChunkPanel);
        inputPanel.add(coordinateSystemPanel);
        frame.add(inputPanel, BorderLayout.NORTH);

        Painter painter = new Painter();
        frame.add(painter, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            String inputText = Double.toString((double)slider.getValue() / 10);
            textField.setText(Double.toString((double)slider.getValue() / 10));
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

        slider2.addChangeListener(e -> {
            String inputText = Double.toString((double)slider2.getValue() / 10);
            orbitPoint.setText(Double.toString((double)slider2.getValue() / 10));
            painter.setOrbitPoint(inputText);
            frame.add(painter);
            frame.update(frame.getGraphics());
        });

        orbitPoint.addActionListener(e -> {
            String inputText = orbitPoint.getText();
            painter.setOrbitPoint(inputText);
            frame.add(painter);
            frame.update(frame.getGraphics());
        });

        orbitCheckBox.addActionListener(e -> {
            painter.setDrawOrbitState(orbitCheckBox.isSelected());
            frame.add(painter);
            frame.update(frame.getGraphics());
        });

        buttonZoomIn.addActionListener(e -> {
            painter.increaseZoom();
            frame.add(painter);
            frame.update(frame.getGraphics());
        });
        buttonZoomOut.addActionListener(e -> {
            painter.decreaseZoom();
            frame.add(painter);
            frame.update(frame.getGraphics());
        });

        frame.setVisible(true);
    }
}