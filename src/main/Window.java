package main;

import graphs.Graph;
import graphs.Matrix;
import templates.*;

import java.awt.*;
import java.util.Random;
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
            .createMatrix();
    }
    private void createMatrix() {
        if (this.oriented) {
            int[][] matrix = Window.generateMatrix(this.n1, this.n2, this.n3, this.n4, false);
            this.drawMatrix(matrix, 670, 400)
                .drawGraph(matrix, true);
        }
        else {
            int[][] matrixUndirected = Window.generateMatrix(this.n1, this.n2, this.n3, this.n4, true);
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
        directed.setFont(new Font("Arial", Font.PLAIN, 16));
        directed.setToolTipText("Change directed graph to undirected.");
        directed.addItemListener(new GraphChanger(this));

        this.getContentPane().add(directed);
        return this;
    }
    private Window drawSliders() {
        Container content = this.getContentPane();

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
        zalikovka.setFont(new Font("Arial", Font.PLAIN, 16));
        zalikovka.setSize(300, 20);

        content.add(n1Label); content.add(n1Bar);
        content.add(n2Label); content.add(n2Bar);
        content.add(n3Label); content.add(n3Bar);
        content.add(n4Label); content.add(n4Bar);
        content.add(zalikovka);

        return this;
    }
    private Window drawMatrix(int[][] matrix, int x, int y) {
        int n = matrix.length;
        int[][] startMatrix = matrix;
        for (int i = 1; i < this.matrix; i++)
            matrix = Matrix.multiplyMatrixes(matrix, startMatrix);
        Container container = this.getContentPane();
        String previousValency = "";
        boolean homogeneous = true;
        Text matrixLabel = new Text("A :", x - 30, y);
        Text matrixPower = new Text(10, this.matrix + "", x - 20, y - 10);
        for (int i = 0; i < n; i++) {
            String valency = this.countConnections(matrix, i);
            Text horizontalNumber = new Text(i + "", 5 + x + 20 * (i + 1), y, 20);
            horizontalNumber.setForeground(Color.red);
            horizontalNumber.setToolTipText("Валентність: " + valency);
            container.add(horizontalNumber);
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
            container.add(verticalNumber);
            String connections = "";
            for (int j = 0; j < n; j++)
                connections = connections.concat("  " + matrix[i][j]);
            connections = connections.trim();
            JLabel string = new JLabel(connections);
            string.setSize(220, 20);
            string.setLocation(x + 25, y + 20 * (i + 1));
            string.setFont(new Font("Arial", Font.PLAIN, 16));
            container.add(string);
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
        homo.setFont(new Font("Arial", Font.PLAIN, 16));
        homo.setSize(220, 60);
        homo.setLocation(x, (n + 2) * 20 + y + 20);
        Slider slider = new Slider(x, (n + 2) * 20 + y - 10, this.matrix,1, 3);
        slider.addChangeListener(new MatrixChanger(this));
        slider.setToolTipText("Зміна степеню матриці");
        container.add(homo); container.add(slider);
        container.add(matrixLabel); container.add(matrixPower);
        return this;
    }
    private void drawGraph(int[][] matrix, boolean oriented) {
        Graph
            .fromMatrix(matrix, oriented)
            .circle(550, 300, 270)
            .draw(this);
    }
    private static int[][] generateMatrix(int n1, int n2, int n3, int n4, boolean symetric) {
        int n = 10 + n3;
        int[][] matrix = new int[n][n];
        Random random = new Random(n1  * 1000 + n2 * 100 + n3 * 10 + n4);

        if (symetric)
            for (int i = 0; i < n; i++) for (int j = 0; j < i; j++) {
                double element = Window.getRandomElement(random, n3, n4);
                matrix[i][j] = (int) Math.floor(element);
                matrix[j][i] = matrix[i][j];
            }
        else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double element = Window.getRandomElement(random, n3, n4);
                    matrix[i][j] = (int) Math.floor(element);
                }
            }
        }
        return matrix;
    }
    private static double getRandomElement(Random random, int n3, int n4) {
        return (random.nextDouble() + random.nextDouble()) * (1 - n3 * 0.01 - n4 * 0.01 - 0.3);
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
    private String countConnections(int[][] matrix, int node) {
        String connections = "";

        if (!this.oriented) {
            int counter = 0;
            int[] line = matrix[node];
            for (int i = 0; i < line.length; i++)
                if (line[i] == 1) {
                    counter++;
                    if (i == node) counter++;
                }
            connections += counter + "";
        } else {
            int plus = 0, minus = 0;
            int[] line = matrix[node];
            for (int i : line) if (i == 1) minus++;
            for (int[] i : matrix) if (i[node] == 1) plus++;
            connections += "+" + plus + " -" + minus;
        }
        return connections;
    }
}
