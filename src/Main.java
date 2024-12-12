import jdk.jshell.Diag;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

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

        JLabel label2 = new JLabel("x =");
        secondChunkPanel.add(label2);

        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, -50, 50, 1);
        secondChunkPanel.add(slider2);

        JTextField orbitPoint = new JTextField("1", 3);
        secondChunkPanel.add(orbitPoint);

        // coordinate system control chunk

        JPanel coordinateSystemPanel = new JPanel();
        coordinateSystemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton buttonZoomIn = new JButton("+");
        JButton buttonZoomOut = new JButton("-");
        JButton buttonReset = new JButton("0");
        coordinateSystemPanel.add(buttonZoomIn);
        coordinateSystemPanel.add(buttonZoomOut);
        coordinateSystemPanel.add(buttonReset);

        JComboBox<Diagrams> diagramChoice = new JComboBox<>(Diagrams.values());
        coordinateSystemPanel.add(diagramChoice);

        JCheckBox extraStatsBox = new JCheckBox();
        coordinateSystemPanel.add(extraStatsBox);
        JLabel intervalLabel = new JLabel("Orbit");
        coordinateSystemPanel.add(intervalLabel);

        Painter painter = new Painter(intervalLabel);

        JPanel densityPanel = new JPanel();

        JLabel xDensityText = new JLabel("   |     a density =");

        String initialXDensity = String.valueOf(painter.getXDensity());
        String initialYDensity = String.valueOf(painter.getYDensity());

        JTextField xDensity = new JTextField(initialXDensity,5);
        JLabel yDensityText = new JLabel("xn density =");
        JTextField yDensity = new JTextField(initialYDensity, 5);
        densityPanel.add(xDensityText);
        densityPanel.add(xDensity);
        densityPanel.add(yDensityText);
        densityPanel.add(yDensity);

        densityPanel.setVisible(false);
        coordinateSystemPanel.add(densityPanel);

        inputPanel.add(firstChunkPanel);
        inputPanel.add(secondChunkPanel);
        inputPanel.add(coordinateSystemPanel);
        frame.add(inputPanel, BorderLayout.NORTH);

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

        orbitPoint.addActionListener(e -> {
            String inputText = orbitPoint.getText();
            painter.setOrbitPoint(inputText);
            painter.repaint();
        });

        slider2.addChangeListener(e -> {
            String inputText = Double.toString((double)slider2.getValue() / 10);
            orbitPoint.setText(Double.toString((double)slider2.getValue() / 10));
            painter.setOrbitPoint(inputText);
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

        diagramChoice.addActionListener(e -> {
            Diagrams selectedDiagram = (Diagrams) diagramChoice.getSelectedItem();

            extraStatsBox.setVisible(true);
            intervalLabel.setVisible(true);
            if (selectedDiagram == Diagrams.Time){
                extraStatsBox.setVisible(false);
                intervalLabel.setVisible(false);
            }

            densityPanel.setVisible(false);
            if (selectedDiagram == Diagrams.Feigenbaum){
                densityPanel.setVisible(true);
            }

            painter.setCurrentDiagram(selectedDiagram);
            painter.repaint();
        });

        extraStatsBox.addActionListener(e -> {
            painter.setDrawExtraStats(extraStatsBox.isSelected());
            painter.repaint();
        });

        xDensity.addActionListener(e -> {
            String inputText = xDensity.getText();
            int density = Integer.parseInt(inputText);
            painter.setXDensity(density);
            painter.repaint();
        });

        yDensity.addActionListener(e -> {
            String inputText = yDensity.getText();
            int density = Integer.parseInt(inputText);
            painter.setYDensity(density);
            painter.repaint();
        });

        frame.setVisible(true);
    }
}