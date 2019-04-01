package main;

import graphs.Matrix;
import templates.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class WaysWindow extends JFrame {
    private int[][] matrix;
    private final int
        X = 1000,
        Y = 200,
        WIDTH = 750,
        HEIGHT = 500,
        MAX_LENGTH = 3;
    private int
        length = 1,
        vortex = 0;
    private boolean oriented;
    private final Font FONT = new Font("Arial", Font.PLAIN, 16);

    public WaysWindow(int[][] matrix, boolean oriented) {
        super("Ways Explorer");
        this.matrix = matrix;
        this.oriented = oriented;
        this.setBounds(this.X, this.Y, this.WIDTH, this.HEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.init();
    }
    private void init() {
        this
            .drawFields()
            .drawWays()
            .drawMatrixLabel()
            .drawMatrix(50, 200);
    }
    private WaysWindow add(JComponent item) {
        this.getContentPane().add(item);
        return this;
    }
    private WaysWindow drawFields() {
        Text vorText = new Text("Вершина: ", 10, 10);
        vorText.setSize(100, 50);
        JSlider vortexChooser = new Slider(100, 25, this.vortex, 0, this.matrix.length - 1);
        vortexChooser.setSize(250, 50);
        vortexChooser.addChangeListener(new SliderListener(this, "vortex"));

        Text lengthText = new Text("Довжина: ", 10, 60);
        lengthText.setSize(100, 50);
        JSlider lengthChooser = new Slider(100, 75, this.length, 1, this.MAX_LENGTH);
        lengthChooser.setSize(250, 50);
        lengthChooser.addChangeListener(new SliderListener(this, "length"));

        this
            .add(vorText)
            .add(vortexChooser)
            .add(lengthText)
            .add(lengthChooser);
        return this;
    }
    public WaysWindow setVortex(int vortex) {
        this.vortex = vortex;
        return this;
    }
    public WaysWindow setLength(int length) {
        this.length = length;
        return this;
    }
    public int getVortex() { return this.vortex; }
    public int getLength() { return this.length; }
    public void redraw() {
        Container content = this.getContentPane();
        Component[] components = content.getComponents();
        for (Component component : components) component.setVisible(false);
        content.removeAll();
        this.init();
    }
    private WaysWindow drawWays() {
        Text title = new Text("Шляхи з вершини " + this.vortex + " довжиною " + this.length, 375, 10);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setSize(500, 50);
        this.add(title);

        HashMap<Number, int[]> ways = Matrix.findWays(this.matrix, this.vortex, this.length);
        int j = 1;
        for (Number key : ways.keySet()) {
            int[] way = ways.get(key);
            String wayText = "";
            for (int i = 0; i < way.length; i++) {
                wayText = wayText.concat(way[i] + "");
                if (i == way.length - 1) continue;
                wayText = wayText.concat(" -> ");
            }
            Text wayComponent = new Text(wayText, 500, 20 * (j + 2));
            wayComponent.setSize(200, 20);
            this.add(wayComponent);
            j++;
        }
        return this;
    }
    private WaysWindow drawMatrixLabel() {
        Text matrixLabel = new Text("Матриця суміжності степеня " + this.length, 50, 150);
        matrixLabel.setSize(300, 50);
        matrixLabel.setFont(new Font("Arial", Font.BOLD , 16));
        this.add(matrixLabel);
        return this;
    }
    private WaysWindow drawMatrix(int x, int y) {
        int n = this.matrix.length;
        int[][] startMatrix = this.matrix;
        int[][] matrix = this.matrix;
        for (int i = 1; i < this.length; i++)
            matrix = Matrix.multiplyMatrixes(matrix, startMatrix);
        for (int i = 0; i < n; i++) {
            String valency = Matrix.countConnections(matrix, i, this.oriented);
            Text horizontalNumber = new Text(i + "", 5 + x + 20 * (i + 1), y, 20);
            horizontalNumber.setForeground(Color.red);
            horizontalNumber.setToolTipText("Валентність: " + valency);
            this.add(horizontalNumber);

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
        return this;
    }
}
