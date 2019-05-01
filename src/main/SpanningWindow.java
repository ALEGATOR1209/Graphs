package main;

import graphics.Background;
import graphs.Edge;
import graphs.Graph;
import graphs.Matrix;
import graphs.Node;
import templates.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class SpanningWindow extends JFrame {
    private final Color
        CYCLE_COLOR = Color.red,
        EXPLORED_COLOR = Color.BLUE,
        UNEXPLORED_COLOR = Color.gray;
    private int[][] matrix;
    private final int
        X = 0,
        Y = 0,
        WIDTH = 1100,
        HEIGHT = 800;
    private boolean
        initialization = true,
        algorithm = true; //true = Kruskal, false = Prim
    private Graph graph;
    private ArrayList<Edge> edges = new ArrayList<>();
    private HashSet<Node> nodes = new HashSet<>();

    public SpanningWindow(int[][] matrix) {
        super("Побудова кістякового дерева");
        this.matrix = matrix;
        this.setBounds(this.X, this.Y, this.WIDTH, this.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.init();
    }

    private void init() {
        if (this.initialization) {
            drawInitComponents();
            return;
        }
        this
            .drawGraph()
            .add(new Background(Color.white, 650, 650, 5, 5))
            .drawNextButton(700, 10)
            .drawMatrix("Матриця ваг", matrix, 700, 75)
            .drawLabel("Ребра до обрацювання:", 700, 450)
            .drawEdgesList(700, 475);
    }
    private SpanningWindow drawMatrix(String title, int[][] matrix, int x, int y) {
        if (matrix.length == 0) return this;
        Text text = new Text(title, x, y);
        text.setSize(x + 10 * matrix.length, 20);
        this.add(text);
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            Text horizontalNumber = new Text(i + "", x + 5 + 25 * (i + 1), y + 25, 30);
            horizontalNumber.setForeground(Color.red);
            this.add(horizontalNumber);
            Text verticalNumber = new Text(i + "", x, y + 25 * (i + 2));
            verticalNumber.setForeground(Color.red);
            this.add(verticalNumber);

            String connections = "";
            for (int j = 0; j < matrix[i].length; j++)
                connections = connections.concat("  " + matrix[i][j]);
            connections = connections.trim();
            JLabel string = new JLabel(connections);
            string.setSize(300, 25);
            string.setLocation(x + 30, y + 25 * (i + 2));
            string.setFont(new Font("Arial", Font.PLAIN, 16));
            this.add(string);
        }
        return this;
    }

    private void drawInitComponents() {
        if (this.graph == null) {
            this.graph = Graph.fromMatrix(matrix, false, false, false)
                .circle(300, 300, 270);
            graph.setWeightMatrix(Matrix.weightsMatrix(matrix));
            this.edges = Graph.sortEdges(graph.toEdgeList());
        }

        Text text = new Text(18, "Оберіть алгоритм:", 700, 5);
        text.setSize(750, 30);
        JRadioButton kruskal = new JRadioButton("Алгоритм Крускала");
        kruskal.setLocation(700, 50);
        kruskal.setSize(200, 30);
        kruskal.setActionCommand("Kruskal");
        kruskal.addActionListener(new ButtonListener(this));
        JRadioButton prim = new JRadioButton("Алгоритм Пріма");
        prim.setLocation(700, 75);
        prim.setSize(200, 30);
        prim.setActionCommand("Prim");
        prim.addActionListener(new ButtonListener(this));
        ButtonGroup group = new ButtonGroup();
        group.add(kruskal);
        group.add(prim);
        group.setSelected(this.algorithm ? kruskal.getModel() : prim.getModel(), true);
        JButton start = new JButton("Почати побудову");
        start.setLocation(700, 120);
        start.setSize(200, 30);
        start.setFont(new Font("Arial", Font.BOLD, 16));
        start.setActionCommand("StartSpan");
        start.addActionListener(new ButtonListener(this));
        this
            .add(text)
            .add(kruskal)
            .add(prim)
            .add(start)
            .drawGraph()
            .drawMatrix("Матриця ваг", matrix, 700, 200)
            .add(new Background(Color.white, 650, 650, 5, 5));

    }
    private SpanningWindow add(JComponent comp) {
        this.getContentPane().add(comp);
        return this;
    }
    public SpanningWindow setAlgorithm(boolean algo) {
        this.algorithm = algo;
        return this;
    }
    public void redraw() {
        Container content = this.getContentPane();
        Component[] components = content.getComponents();
        for (Component component : components) component.setVisible(false);
        content.removeAll();
        this.init();
    }
    private SpanningWindow drawGraph() {
        this.graph.draw(this);
        return this;
    }
    public SpanningWindow start() {
        initialization = false;
        graph.setColorAll(UNEXPLORED_COLOR)
            .setConnectionsColorAll(UNEXPLORED_COLOR);
        return this;
    }
    private SpanningWindow drawLabel(String text, int x, int y) {
        Text label = new Text(text, x, y, text.length() * 10);
        add(label);
        return this;
    }
    private SpanningWindow drawEdgesList(int x, int y) {
        int dx = 0;
        int dy = 0;
        for (Edge edge : edges) {
            Text text = new Text(edge.toString(), x + 80 * dx, y + 25 * dy, 100);
            dy++;
            if (dy >= edges.size() / 4) {
                dx++;
                dy = 0;
            }
            add(text);
        }
        return this;
    }
    private SpanningWindow drawNextButton(int x, int y) {
        JButton button = new JButton("Далі");
        button.setLocation(x, y);
        button.setSize(250, 50);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setActionCommand("NextEdge");
        button.addActionListener(new ButtonListener(this));
        add(button);
        return this;
    }
    public SpanningWindow processNext() {
        if (algorithm) processNextKruskal();
        else processNextPrim();
        return this;
    }
    private void processNextKruskal() {

    }
    private void processNextPrim() {
        if (nodes.size() == 0) {
            Edge edge = edges.get(0);
            edges.remove(0);
            nodes.addAll(edge.getNodes());
            edge.setColor(EXPLORED_COLOR);
            edge.getNodes().forEach(node -> node
                .setColor(EXPLORED_COLOR)
                .setSpecialEdge(edge)
            );
            return;
        }
        int i = 0;
        for (Edge edge : edges) {
            ArrayList<Node> edgeNodes = edge.getNodes();
            int count = 0;
            for (Node node : edgeNodes) if (nodes.contains(node))
                count++;

            if (count == 1) {
                edge.setColor(EXPLORED_COLOR);
                edgeNodes.forEach(node -> node
                    .setColor(EXPLORED_COLOR)
                    .setSpecialEdge(edge)
                );
                edges.remove(i);
                nodes.addAll(edgeNodes);
                return;
            }
            if (count == 2) {
                edge.setColor(CYCLE_COLOR);
                edgeNodes.forEach(node -> node.setSpecialEdge(edge));
                edges.remove(i);
                return;
            }
            i++;
        }
    }
}
