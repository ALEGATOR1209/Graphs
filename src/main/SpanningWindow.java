package main;

import graphics.Background;
import graphs.*;
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
        WIDTH = 1700,
        HEIGHT = 1000;
    private int n1, n2, n3, n4;
    private boolean
        initialization = true,
        finish = false,
        algorithm = true; //true = Kruskal, false = Prim
    private Graph graph;
    private ArrayList<Edge> edges = new ArrayList<>();
    private HashSet<Node> nodes = new HashSet<>();
    private ArrayList<Edge> spanEdges = new ArrayList<>();

    public SpanningWindow(int[][] matrix, int n1, int n2, int n3, int n4) {
        super("Побудова кістякового дерева");
        this.matrix = matrix;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.n4 = n4;
        this.setBounds(this.X, this.Y, this.WIDTH, this.HEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.init();
    }

    private void init() {
        if (this.initialization) {
            drawInitComponents();
            return;
        }
        if (this.finish) {
            drawFinishComponents();
            return;
        }
        this
            .drawGraph()
            .add(new Background(Color.white, 650, 650, 5, 5))
            .drawNextButton(700, 10)
            .drawMatrix("Матриця ваг", Matrix.generateWeightMatrix(n1, n2, n3, n4), 700, 75)
            .drawLabel("Ребра до обрацювання:", 1250, 75)
            .drawEdgesList(1250, 100)
            .drawMatrix("Матриця кістякового дерева", Matrix.edgesToMatrix(graph.getNodeCount(), spanEdges), 700, 600);
    }
    private SpanningWindow drawMatrix(String title, int[][] matrix, int x, int y) {
        if (matrix.length == 0) return this;
        Text text = new Text(title, x, y);
        text.setSize(x + 10 * matrix.length, 20);
        this.add(text);
        int n = matrix.length;
        int size = (Matrix.max(matrix) + "").length() * 13;
        if (size < 25) size = 25;

        //creating column and line headers
        for (int i = 0; i < n; i++) {
            Text textH = new Text(i + "", x + (i + 1) * size, y + size);
            Text textV = new Text(i + "", x, y + (i + 2) * size);
            textH.setForeground(Color.red);
            textV.setForeground(Color.red);
            this
                .add(textH)
                .add(textV);
        }
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            String str = matrix[i][j] + "";
            Text number = new Text(str, x + size * (i + 1), y + size * (j + 2));
            add(number);
        }
        return this;
    }

    private void drawInitComponents() {
        if (this.graph == null) {
            this.graph = Graph.fromMatrix(matrix, false, false, false)
                .circle(300, 300, 270);
            graph.setWeightMatrix(Matrix.generateWeightMatrix(n1, n2, n3, n4));
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
            .drawMatrix("Матриця ваг", Matrix.generateWeightMatrix(n1, n2, n3, n4), 700, 200)
            .add(new Background(Color.white, 650, 650, 5, 5));

    }
    private void drawFinishComponents() {
        Graph.fromWeightMatrix(Matrix.edgesToMatrixWeight(graph.getNodeCount(), spanEdges), EXPLORED_COLOR)
            .circle(1000, 400, 300)
            .draw(this);
        this
            .graph
            .circle(300, 400, 300)
            .draw(this);

        JButton button = new JButton("Заново");
        button.setLocation(10, 10);
        button.setSize(300, 70);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setActionCommand("NewSpan");
        button.addActionListener(new ButtonListener(this));
        this
            .add(new Background(Color.white, 1350, 1000, 5, 90))
            .drawMatrix("Матриця кістякового дерева", Matrix.edgesToMatrix(graph.getNodeCount(), spanEdges), 1375, 100)
            .drawMatrix("Матриця ваг кістяка", Matrix.edgesToMatrixWeight(graph.getNodeCount(), spanEdges), 1375, 450)
            .add(button);
    }
    public SpanningWindow restart() {
        this.finish = false;
        this.initialization = true;
        this.graph = null;
        this.edges = new ArrayList<>();
        this.spanEdges = new ArrayList<>();
        this.nodes = new HashSet<>();
        return this;
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
            if (dy >= edges.size() / 2) {
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
        if ( //terrible expression but who cares?
            Matrix.condensateMatrix(
                Matrix.edgesToMatrix(graph.getNodeCount(), spanEdges)
            ).length == 1 && nodes.size() == graph.getNodeCount()
        ) {
            finish = true;
            edges = new ArrayList<>();
            redraw();
            return this;
        }
        if (edges.size() == 0) {
            finish = true;
            redraw();
            return this;
        }
        if (algorithm) processNextKruskal();
        else processNextPrim();
        return this;
    }
    private void processNextKruskal() {
        Edge edge = edges.get(0);

        int counter = 0;
        for (Node node : edge.getNodes()) if (nodes.contains(node)) counter++;

        if (counter < 2) {
            edge.setColor(EXPLORED_COLOR);
            spanEdges.add(edge);
            edges.remove(0);
            for (Node node : edge.getNodes()) {
                if (!nodes.contains(node)) nodes.add(node);
                node
                    .setColor(EXPLORED_COLOR)
                    .setSpecialEdge(edge);
            }
            return;
        }

        Node node1 = edge.getNodes().get(0);
        Node node2 = edge.getNodes().get(1);
        if (hasWay(node1, node2)) {
            edge.setColor(CYCLE_COLOR);
            edge.getNodes().forEach(node -> node.setSpecialEdge(edge));
            edges.remove(0);
            return;
        }

        edge
            .setColor(EXPLORED_COLOR)
            .getNodes()
            .forEach(node -> node.setSpecialEdge(edge));
        edges.remove(0);
        spanEdges.add(edge);
    }
    private void processNextPrim() {
        if (nodes.size() == 0) {
            Edge edge = edges.get(0);
            edges.remove(0);
            nodes.addAll(edge.getNodes());
            spanEdges.add(edge);
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
                spanEdges.add(edge);
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
    private boolean hasWay(Node node1, Node node2) {
        int[][] matrix = Matrix.edgesToMatrix(graph.getNodeCount(), spanEdges);
        int[][] attainability = Matrix.getAttainabilityMatrix(matrix);
        if (attainability[node1.getId()][node2.getId()] == 0) return false;
        return true;
    }
}
