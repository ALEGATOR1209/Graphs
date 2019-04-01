package main;

import graphs.*;
import templates.*;

import java.awt.*;
import javax.swing.*;

public class Window extends JFrame {
    private int width = 1000, height = 800;
    private int x = 0, y = 0;
    private int n1 = 8,
                n2 = 4,
                n3 = 1,
                n4 = 0;
    private int matrix = 1;
    private boolean oriented = true;
    private final Font FONT = new Font("Arial", Font.PLAIN, 16);
    Window(String title) {
        super(title);
        this.init();
    }
    private void init() {
        this.setBounds(this.x, this.y, this.width, this.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this
            .drawSliders()
            .drawCheckBox()
            .drawWaysButton()
            .createMatrix();
    }
    private void createMatrix() {
        if (this.oriented) {
            int[][] matrix = Matrix.generateMatrix(this.n1, this.n2, this.n3, this.n4, false);
            this.drawMatrix(matrix, 670, 400)
                .drawGraph(matrix, true);
        }
        else {
            int[][] matrixUndirected = Matrix.generateMatrix(this.n1, this.n2, this.n3, this.n4, true);
            this.drawMatrix(matrixUndirected, 670, 400)
                .drawGraph(matrixUndirected, false);
        }
    }
    private Window drawCheckBox() {
        JCheckBox directed = new JCheckBox("Directed");
        directed.setSize(100, 50);
        directed.setLocation(670, 350);
        directed.setSelected(this.oriented);
        directed.setFocusable(false);
        directed.setFont(this.FONT);
        directed.setToolTipText("Change directed graph to undirected.");
        directed.addItemListener(new GraphChanger(this));

        this.getContentPane().add(directed);
        return this;
    }
    private Window drawSliders() {
        JLabel n1Label = new Text("n1: ", 670, 10);
        JSlider n1Bar = new Slider(700, 10, this.n1);
        n1Bar.setEnabled(false);
        n1Bar.addChangeListener(new NumberChanger(this, 1));

        JLabel n2Label = new Text("n2: ", 670, 70);
        JSlider n2Bar = new Slider(700, 70, this.n2, 5);
        n2Bar.setEnabled(false);
        n2Bar.addChangeListener(new NumberChanger(this, 2));

        JLabel n3Label = new Text("n3: ", 670, 140);
        JSlider n3Bar = new Slider(700, 140, this.n3, 1);
        n3Bar.addChangeListener(new NumberChanger(this, 3));

        JLabel n4Label = new Text("n4: ", 670, 210);
        JSlider n4Bar = new Slider(700, 210, this.n4);
        n4Bar.addChangeListener(new NumberChanger(this, 4));

        String zalikText = "Номер залікової книжки: " + this.n1 + this.n2 + "-" + this.n3 + this.n4;
        JLabel zalikovka = new JLabel(zalikText);
        zalikovka.setLocation(670, 280);
        zalikovka.setFont(this.FONT);
        zalikovka.setSize(300, 20);

        this.add(n1Label).add(n1Bar)
            .add(n2Label).add(n2Bar)
            .add(n3Label).add(n3Bar)
            .add(n4Label).add(n4Bar)
            .add(zalikovka);

        return this;
    }
    private Window add(JComponent item) {
        this.getContentPane().add(item);
        return this;
    }
    private Window drawWaysButton() {
        int[][] matrix = Matrix.generateMatrix(this.n1, this.n2, this.n3, this.n4, !this.oriented);
        JButton button = new JButton("Шляхи");
        button.setFont(this.FONT);
        button.setSize(100, 30);
        button.setLocation(800, 360);
        button.setActionCommand("Show Ways Window");
        button.addActionListener(new ButtonListener(matrix, this.oriented));
        this.add(button);
        return this;
    }
    private Window drawMatrix(int[][] matrix, int x, int y) {
        int n = matrix.length;
        if (!this.oriented) this.matrix = 1;
        String previousValency = "";
        boolean homogeneous = true;
        for (int i = 0; i < n; i++) {
            String valency = Matrix.countConnections(matrix, i, this.oriented);
            Text horizontalNumber = new Text(i + "", 5 + x + 20 * (i + 1), y, 20);
            horizontalNumber.setForeground(Color.red);
            horizontalNumber.setToolTipText("Валентність: " + valency);
            this.add(horizontalNumber);
            if (previousValency.length() == 0) {
                previousValency = valency;
            }
            if (homogeneous && !previousValency.equals(valency)) {
                homogeneous = false;
            }
            previousValency = valency;

            Text verticalNumber = new Text(i + "", x, y + 20 * (i + 1));
            verticalNumber.setForeground(Color.red);
            verticalNumber.setToolTipText("Валентність: " + valency);
            this.add(verticalNumber);
            String connections = "";
            for (int j = 0; j < n; j++)
                connections = connections.concat("  " + matrix[i][j]);
            connections = connections.trim();
            JLabel string = new JLabel(connections);
            string.setSize(220, 20);
            string.setLocation(x + 25, y + 20 * (i + 1));
            string.setFont(this.FONT);
            this.add(string);
        }
        JLabel homo;
        if (homogeneous) {
            homo = new JLabel("ГРАФ ОДНОРІДНИЙ");
            homo.setToolTipText("Валентність: " + previousValency);
            homo.setForeground(new Color(30, 120, 10));
        }
        else {
            homo = new JLabel("ГРАФ НЕОДНОРІДНИЙ");
            homo.setForeground(Color.red);
        }
        homo.setFont(this.FONT);
        homo.setSize(220, 60);
        homo.setLocation(x, (n + 2) * 20 + y + 20);
        this
            .add(homo);
        return this;
    }
    private void drawGraph(int[][] matrix, boolean oriented) {
        Graph
            .fromMatrix(matrix, oriented)
            .circle(550, 300, 270)
            .draw(this);
    }

    public int getNumber(int n) {
        if (n == 1) return this.n1;
        if (n == 2) return this.n2;
        if (n == 3) return this.n3;
        if (n == 4) return this.n4;
        throw new Error("Wrong number");
    }
    public Window setNumber(int n, int value) {
        if (n == 1) { this.n1 = value; return this; }
        if (n == 2) { this.n2 = value; return this; }
        if (n == 3) { this.n3 = value; return this; }
        if (n == 4) { this.n4 = value; return this; }
        throw new Error("Wrong number");
    }
    public Window setMatrix(int n) {
        this.matrix = n;
        return this;
    }
    public void redraw() {
        Container content = this.getContentPane();
        Component[] components = content.getComponents();
        for (Component component : components) component.setVisible(false);
        content.removeAll();
        this.init();
    }
    public void changeOrientation() { this.oriented = !this.oriented; }
}
