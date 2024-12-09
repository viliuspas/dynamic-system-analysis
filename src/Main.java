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

        JCheckBox aToNCheckbox = new JCheckBox("Enable xn to n schema");
        firstChunkPanel.add(aToNCheckbox);

        // second chunk
        JPanel secondChunkPanel = new JPanel();
        secondChunkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label2 = new JLabel("x =");
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
        JButton buttonReset = new JButton("0");
        coordinateSystemPanel.add(buttonZoomIn);
        coordinateSystemPanel.add(buttonZoomOut);
        coordinateSystemPanel.add(buttonReset);

        JCheckBox feigenbaumCheckBox = new JCheckBox("Enable Feigenbaum schema");
        coordinateSystemPanel.add(feigenbaumCheckBox);

        JCheckBox drawExtraStatsBox = new JCheckBox("Enable extra stats");
        coordinateSystemPanel.add(drawExtraStatsBox);

        inputPanel.add(firstChunkPanel);
        inputPanel.add(secondChunkPanel);
        inputPanel.add(coordinateSystemPanel);
        frame.add(inputPanel, BorderLayout.NORTH);

        Painter painter = new Painter();

        MouseControl mouseControl = new MouseControl(painter);
        painter.addMouseListener(mouseControl);
        painter.addMouseMotionListener(mouseControl);
        painter.addMouseWheelListener(mouseControl);

        frame.add(painter, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            String inputText = Double.toString((double)slider.getValue() / 10);
            textField.setText(Double.toString((double)slider.getValue() / 10));
            painter.setInputText(inputText);
            painter.repaint();
        });

        textField.addActionListener(e -> {
            String inputText = textField.getText();
            painter.setInputText(inputText);
            painter.repaint();
        });

        slider2.addChangeListener(e -> {
            String inputText = Double.toString((double)slider2.getValue() / 10);
            orbitPoint.setText(Double.toString((double)slider2.getValue() / 10));
            painter.setOrbitPoint(inputText);
            painter.repaint();
        });

        orbitPoint.addActionListener(e -> {
            String inputText = orbitPoint.getText();
            painter.setOrbitPoint(inputText);
            painter.repaint();
        });

        orbitCheckBox.addActionListener(e -> {
            painter.setDrawOrbitState(orbitCheckBox.isSelected());
            painter.repaint();
        });

        buttonZoomIn.addActionListener(e -> {
            painter.increaseZoom();
            painter.repaint();
        });
        buttonZoomOut.addActionListener(e -> {
            painter.decreaseZoom();
            painter.repaint();
        });
        buttonReset.addActionListener(e -> {
            painter.resetToStart();
            painter.repaint();
        });

        feigenbaumCheckBox.addActionListener(e ->{
            painter.setDrawFeigenbaumState(feigenbaumCheckBox.isSelected());
            painter.repaint();
        });

        drawExtraStatsBox.addActionListener(e ->{
            painter.setDrawExtraStats(drawExtraStatsBox.isSelected());
            painter.repaint();
        });

        aToNCheckbox.addActionListener(e ->{
            painter.setDrawAToNState(aToNCheckbox.isSelected());
            painter.repaint();
        });

        frame.setVisible(true);
    }
}