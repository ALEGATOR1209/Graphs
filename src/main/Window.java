package main;

import graphics.Background;
import graphs.*;
import templates.*;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public class Window extends JFrame {
    private int width = 1000, height = 900;
    private int x = 0, y = 0;
    private int n1 = 8,
                n2 = 4,
                n3 = 1,
                n4 = 0;
    private boolean condensated = false;
    private boolean strong = false;
    private int matrix = 0;
    private boolean oriented = false;
    private final Font FONT = new Font("Arial", Font.PLAIN, 16);
    Window(String title) {
        super(title);
        this.init();
    }
    private void init() {
        this.setLayout(null);
        this.setBounds(this.x, this.y, this.width, this.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this
            .drawSliders()
            .drawCheckBox()
            .drawWaysButton()
            .drawStrongCheckBox()
            .drawCondensationButton()
            .createMatrix();
    }
    private void createMatrix() {
        int[][] matrix = Matrix.generateMatrix(this.n1, this.n2, this.n3, this.n4, !this.oriented);
        int[][] condensated = matrix.clone();
        if (this.condensated) condensated = Matrix.condensateMatrix(condensated);
        this
            .drawGraphSearchButton(matrix)
            .drawSpanningTreeButton(matrix)
            .drawMatrix(condensated, 670, 500)
            .drawGraph(matrix, this.oriented);
    }
    private Window drawCheckBox() {
        JCheckBox directed = new JCheckBox("Напрямлений");
        directed.setSize(140, 30);
        directed.setLocation(670, 450);
        directed.setSelected(this.oriented);
        directed.setFocusable(false);
        directed.setFont(this.FONT);
        directed.setToolTipText("Change directed graph to undirected.");
        directed.addItemListener(new GraphChanger(this));

        this.
            add(directed);
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
        button.setLocation(670, 360);
        button.setActionCommand("Show Ways Window");
        button.addActionListener(new ButtonListener(matrix, this.oriented));
        this.add(button);
        return this;
    }
    private Window drawMatrix(int[][] matrix, int x, int y) {
        HashMap<Number, int[]> strong = Matrix.getStrongComponents(Matrix.getAttainabilityMatrix(matrix.clone()));;
        Color[] colors = this.generateColors(strong.size());;
        if (this.matrix == 1) matrix = Matrix.getAttainabilityMatrix(matrix.clone());
        if (this.matrix == 2) matrix = Matrix.getStrongMatrix(matrix.clone());
        int n = matrix.length;
        String previousValency = "";
        boolean homogeneous = true;
        String letter = "A";
        for (int i = 0; i < n; i++) {
            String valency = Matrix.countConnections(matrix, i, this.oriented);
            Text horizontalNumber = new Text(i + "", 5 + x + 20 * (i + 1), y, 20);
            if (this.condensated) horizontalNumber.setText(letter);
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
            if (this.condensated) {
                verticalNumber.setText(letter);
                letter = letter.replace(letter.charAt(0), (char) (letter.codePointAt(0) + 1));
            }
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
            if (this.strong) {
                int ллл = 0;
                for (Number кек : strong.keySet()) for (int лол : strong.get(кек))
                    if (лол == i) ллл = кек.intValue();
                this.add(new Background(colors[ллл], 20, 20, x, y + 20 * (i + 1)));
                this.add(new Background(colors[ллл], 20, 20, 5 + x + 20 * (i + 1), y));
                verticalNumber.setForeground(Color.white);
                horizontalNumber.setForeground(Color.white);
            }
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
        if (this.matrix != 0) homo.setVisible(false);
        homo.setFont(this.FONT);
        homo.setSize(220, 60);
        homo.setLocation(x, (n + 4) * 20 + y);
        this.add(homo);

        String text = "Матриця суміжності";
        if (this.matrix == 1) text = "Матриця досяжності";
        if (this.matrix == 2) text = "Матриця зв'язності";
        JLabel matrixText = new Text(text, x, (n + 1) * 20 + 10 + y, 250);
        matrixText.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(matrixText);

        JSlider matrixSlider = new Slider(x, y + (n + 2) * 20 + 10, this.matrix, 0, 2);
        matrixSlider.addChangeListener(new SliderListener(this, "Matrix"));
        matrixSlider.setPaintLabels(false);
        this.add(matrixSlider);
        return this;
    }
    private void drawGraph(int[][] matrix, boolean oriented) {
        Graph
            .fromMatrix(matrix, oriented, this.strong, this.condensated)
            .circle(300, 300, 280)
            .showStrong()
            .draw(this);
        this.add(new Background(650, 650, 5, 5));
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
    public void changeStrong() { this.strong = !this.strong; }
    private Window drawStrongCheckBox() {
        JCheckBox strong = new JCheckBox("Компоненти сильної зв'язності");
        strong.setSize(300, 50);
        strong.setLocation(670, 300);
        strong.setSelected(this.strong);
        strong.setFocusable(false);
        strong.setFont(this.FONT);
        strong.setToolTipText("Show or hide strong component.");
        strong.addItemListener(new StrongChanger(this));

        if (this.condensated) {
            strong.setSelected(false);
            strong.setEnabled(false);
        }
        this.add(strong);
        return this;
    }
    private Window drawCondensationButton() {
        JButton button = new JButton(this.condensated ? "Деконденсувати" : "Конденсувати");
        button.setFont(this.FONT);
        button.setSize(180, 30);
        button.setLocation(775, 360);
        button.setActionCommand("Condensation");
        button.addActionListener(new ButtonListener(this));
        this.add(button);
        return this;
    }
    public void changeCondensation() { this.condensated = !this.condensated; }
    private Color[] generateColors(int n) {
        Color[] colors = new Color[n];
        int delta = 0xFFAD5A - 0x85FF5A;
        for (int i = 0; i < n; i++) colors[i] = new Color(0xFF5A5A + i * delta);
        return colors;
    }
    private Window drawGraphSearchButton(int[][] matrix) {
        JButton button = new JButton("Обійти");
        button.setFont(this.FONT);
        button.setSize(140, 30);
        button.setLocation(670, 400);
        button.setActionCommand("Search");
        button.addActionListener(new ButtonListener(matrix, this.oriented));
        this.add(button);
        return this;
    }
    private Window drawSpanningTreeButton(int[][] matrix) {
        JButton button = new JButton("Кістяк");
        button.setFont(FONT);
        button.setSize(140, 30);
        button.setEnabled(!oriented);
        button.setLocation(815, 400);
        button.setActionCommand("Span");
        button.addActionListener(new ButtonListener(this, matrix, oriented));
        add(button);
        return this;
    }
}
