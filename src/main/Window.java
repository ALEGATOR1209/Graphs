package main;

import graphs.Graph;

import java.awt.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Window extends JFrame {
    private int width = 1000, height = 500;
    private int x = 0, y = 0;
    private int n1 = 8,
                n2 = 4,
                n3 = 1,
                n4 = 0;
    private GraphChanger listener;
    Window(String title) {
        super(title);
        this.init();
    }

    private void init() {
        this.setBounds(this.x, this.y, this.width, this.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = this.getContentPane();
        this.listener = new GraphChanger();

        JLabel n1Label = new Text("n1: ", 570, 10);
        JSlider n1Bar = new Slider(600, 10, this.n1);
        n1Bar.setEnabled(false);
        n1Bar.addChangeListener(new NumberChanger(this, 1));

        JLabel n2Label = new Text("n2: ", 570, 70);
        JSlider n2Bar = new Slider(600, 70, this.n2);
        n2Bar.setEnabled(false);
        n2Bar.addChangeListener(new NumberChanger(this, 2));

        JLabel n3Label = new Text("n3: ", 570, 140);
        JSlider n3Bar = new Slider(600, 140, this.n3);
        n3Bar.addChangeListener(new NumberChanger(this, 3));

        JLabel n4Label = new Text("n4: ", 570, 210);
        JSlider n4Bar = new Slider(600, 210, this.n4);
        n4Bar.addChangeListener(new NumberChanger(this, 4));

        String zalikText = "Номер залікової книжки: " + this.n1 + this.n2 + "-" + this.n3 + this.n4;
        JLabel zalikovka = new JLabel(zalikText);
        zalikovka.setLocation(570, 280);
        zalikovka.setFont(new Font("Arial", Font.PLAIN, 16));
        zalikovka.setSize(300, 20);

        JCheckBox directed = new JCheckBox("Directed");
        directed.setSize(100, 50);
        directed.setLocation(570, 350);
        directed.setSelected(true);
        directed.setFocusable(false);
        directed.setFont(new Font("Arial", Font.PLAIN, 16));
        directed.setToolTipText("Change directed graph to undirected.");
        directed.addItemListener(this.listener);

        content.add(directed); content.add(zalikovka);
        content.add(n1Label); content.add(n1Bar);
        content.add(n2Label); content.add(n2Bar);
        content.add(n3Label); content.add(n3Bar);
        content.add(n4Label); content.add(n4Bar);

        int[][] matrixDirected = Window.generateMatrix(this.n1, this.n2, this.n3, this.n4, false);
        int[][] matrixUndirected = Window.generateMatrix(this.n1, this.n2, this.n3, this.n4, true);
        this.drawGraph(matrixDirected, true)
            .drawGraph(matrixUndirected, false);
    }
    private Window drawGraph(int[][] matrix, boolean oriented) {
        this.listener.addGraph(Graph
            .fromMatrix(matrix, oriented)
            .circle(425, 205, 200)
            .draw(this)
            .hide());
        return this;
    }
    private static int[][] generateMatrix(int n1, int n2, int n3, int n4, boolean symetric) {
        int n = 10 + n3;
        int[][] matrix = new int[n][n];
        Random random = new Random(n1  * 1000 + n2 * 100 + n3 * 10 + n4);

        if (symetric)
            for (int i = n - 1; i >= 0; i--) {
                for (int j = i; j >= 0; j--) {
                    double element = Window.getRandomElement(random, n3, n4);
                    matrix[i][j] = (int) Math.floor(element);
                    matrix[j][i] = matrix[i][j];
                }
            }
        else for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double element = Window.getRandomElement(random, n3, n4);
                matrix[i][j] = (int) Math.floor(element);
            }
        }
        return matrix;
    }
    private static double getRandomElement(Random random, int n3, int n4) {
        return (random.nextDouble() + random.nextDouble()) * (1 - n3 * 0.005 - n4 * 0.005 - 0.25);
    }
    int getNumber(int n) {
        if (n == 1) return this.n1;
        if (n == 2) return this.n2;
        if (n == 3) return this.n3;
        if (n == 4) return this.n4;
        throw new Error("Wrong number");
    }
    Window setNumber(int n, int value) {
        if (n == 1) { this.n1 = value; return this; }
        if (n == 2) { this.n2 = value; return this; }
        if (n == 3) { this.n3 = value; return this; }
        if (n == 4) { this.n4 = value; return this; }
        throw new Error("Wrong number");
    }
    void redraw() {
        Container content = this.getContentPane();
        Component[] components = content.getComponents();
        for (Component component : components) component.setVisible(false);
        content.removeAll();
        this.init();
    }
}

class NumberChanger implements ChangeListener {
    private Window window;
    private int number;
    NumberChanger (Window window, int number) {
        this.window = window;
        this.number = number;
    }
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (this.window.getNumber(this.number) == source.getValue()) return;
        this.window.setNumber(this.number, source.getValue())
            .redraw();
    }
}

class Slider extends JSlider {
    Slider(int x, int y, int val) {
        super(0, 9, 1);
        this.setLocation(x, y);
        this.setSize(200, 50);
        this.setValue(val);
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.setMajorTickSpacing(1);
    }
}
class Text extends JLabel {
    Text(String text, int x, int y) {
        super(text);
        this.setSize(50, 20);
        this.setLocation(x, y);
        this.setFont(new Font("Arial", Font.PLAIN, 16));
    }
}