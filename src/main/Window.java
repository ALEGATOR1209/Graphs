package main;

import graphs.Graph;

import java.awt.*;
import javax.swing.*;

class Window extends JFrame {
    private int width = 700, height = 500;
    private int x = 0, y = 0;
    private GraphChanger listener;
    Window(String title) {
        super(title);
        this.init();
    }

    private void init() {
        this.setBounds(this.x, this.y, this.width, this.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.listener = new GraphChanger();

        JCheckBox directed = new JCheckBox("Directed");
        directed.setSize(100, 100);
        directed.setBounds(100, 100, 100, 20);
        directed.setLocation(600, 0);
        directed.setSelected(false);
        directed.setFocusable(false);
        directed.setFont(new Font("Arial", Font.PLAIN, 16));
        directed.setBackground(Color.gray);
        directed.setForeground(Color.white);
        directed.setToolTipText("Change directed graph to undirected.");
        directed.addItemListener(this.listener);
        this.getContentPane().add(directed);
    }
    Window drawGraph(int[][] matrix, boolean oriented) {
        this.listener.addGraph(Graph
            .fromMatrix(matrix, oriented)
            .circle(425, 205, 200)
            .draw(this)
            .hide());
        return this;
    }
}
