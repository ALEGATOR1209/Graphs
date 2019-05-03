package main;

import javax.swing.*;

import graphics.Background;
import graphs.*;
import templates.*;

import java.awt.*;
import java.util.ArrayList;

public class DijkstraWindow extends JFrame {
    private final Color
        ACTIVE_COLOR = Color.red,
        TEMPORARY_COLOR = Color.blue,
        PERMANENT_COLOR = new Color(48, 121, 0),
        UNEXPLORED_COLOR = Color.gray;
    private int[][] matrix;
    private int[][] weightMatrix;
    private String status = "";
    private final int
        X = 0,
        Y = 0,
        WIDTH = 1000,
        HEIGHT = 800;
    private int n1, n2, n3, n4;
    private boolean
        initialization = true,
        finish = false,
        algorithm = true; //true = minimum, false = maximum
    private int
        startVortex = 0,
        wortexToShow = 0,
        active;
    private Graph graph;
    private ArrayList<Edge> ways, edges;
    private ArrayList<Node> temporary;

    public DijkstraWindow(int n1, int n2, int n3, int n4) {
        super("Пошук найкоротшого шляху");
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.n4 = n4;
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
        if (this.finish) {
            drawFinalComponents();
            return;
        }
        this.graph.get(active)
            .setColor(ACTIVE_COLOR)
            .setConnectionColor(ACTIVE_COLOR)
            .setRepetitiveWaysPolicy(Node.WaysPolicies.FROM_VORTEX_ONLY);
        this.graph
            .draw(this);
        this
            .add(new Background(Color.white, 650, 650, 10, 10))
            .drawNextButton(700, 10)
            .drawLabel("Шляхи:", 700, 100)
            .drawLabel(status, 10, 650)
            .drawWays(700, 100);
    }
    public void redraw() {
        Container content = this.getContentPane();
        Component[] components = content.getComponents();
        for (Component component : components) component.setVisible(false);
        content.removeAll();
        this.init();
    }

    private DijkstraWindow add(JComponent comp) {
        this.getContentPane().add(comp);
        return this;
    }
    private DijkstraWindow drawLabel(String text, int x, int y) {
        Text label = new Text(text, x, y, text.length() * 20);
        add(label);
        return this;
    }
    public DijkstraWindow setAlgorithm(boolean algo) {
        this.algorithm = algo;
        return this;
    }
    public DijkstraWindow setShowVortex(int vortex) {
        this.wortexToShow = vortex;
        return this;
    }
    public DijkstraWindow start() {
        this.initialization = false;
        this.graph
            .setColorAll(UNEXPLORED_COLOR)
            .setConnectionsColorAll(UNEXPLORED_COLOR);
        return this;
    }
    public DijkstraWindow setStartVortex(int n) {
        this.startVortex = n;
        return this;
    }


    private void drawInitComponents() {
        if (this.matrix == null) matrix = Matrix.generateMatrix(n1, n2, n3, n4, true);
        if (this.weightMatrix == null) weightMatrix = Matrix.generateWeightMatrix(n1, n2, n3, n4);

        if (this.graph == null) {
            this.graph = Graph.fromMatrix(matrix, false, false, false);
            graph.setWeightMatrix(Matrix.generateWeightMatrix(n1, n2, n3, n4));
            edges = Graph.sortEdges(graph.toEdgeList());
        }

        graph
            .circle(300, 300, 270)
            .draw(this);
        add(new Background(Color.white, 650, 650, 10, 10));
        drawLabel("Оберіть алгоритм:", 700, 10);
        ways = new ArrayList<>();
        temporary = new ArrayList<>();
        temporary.add(graph.get(active));
        graph.getNodes().forEach(
            (key, node) -> {
                if (key.intValue() == startVortex) {
                    ways.add(new Edge(0).connect(node).connect(node));
                    return;
                }
                ways.add(new Edge(algorithm ? 10000000 : -10000000).connect(graph.get(startVortex)).connect(node));
            }
        );
        active = startVortex;

        JRadioButton min = new JRadioButton("Мінімальний шлях");
        min.setLocation(700, 50);
        min.setFont(new Font("Arial", Font.PLAIN, 16));
        min.setSize(200, 30);
        min.setActionCommand("Min");
        min.addActionListener(new ButtonListener(this));
        JRadioButton max = new JRadioButton("Максимальний шлях");
        max.setLocation(700, 75);
        max.setSize(200, 30);
        max.setFont(new Font("Arial", Font.PLAIN, 16));
        max.setActionCommand("Max");
        max.addActionListener(new ButtonListener(this));
        ButtonGroup group = new ButtonGroup();
        group.add(min);
        group.add(max);
        group.setSelected(this.algorithm ? min.getModel() : max.getModel(), true);
        drawLabel("Оберіть початкову вершину:", 700, 120);
        Slider slider = new Slider(700, 150, startVortex, graph.getNodeCount() - 1);
        slider.addChangeListener(new SliderListener(this, "startVortex"));
        JButton start = new JButton("Знайти шляхи");
        start.setLocation(700, 200);
        start.setSize(250, 30);
        start.setFont(new Font("Arial", Font.PLAIN, 16));
        start.setActionCommand("StartDijkstra");
        start.addActionListener(new ButtonListener(this));
        this
            .add(slider)
            .add(min)
            .add(max)
            .add(start);
    }

    private void drawFinalComponents() {
        drawLabel("Оберіть шлях, для перегляду:", 700, 10);
        Slider slider = new Slider(700, 50, wortexToShow, graph.getNodeCount() - 1);
        slider.addChangeListener(new SliderListener(this, "showWay"));
        this.add(slider)
            .add(new Background(Color.white, 650, 650, 10, 10))
            .drawWays(700, 70)
            .drawWay(10, 10, wortexToShow);
    }

    private DijkstraWindow drawWays(int x, int y) {
        ways = Graph.sortEdges(ways);
        if (this.algorithm) for (int i = 0; i < ways.size(); i++) drawLabel(ways.get(i).toWay(), x, y + 50 * (i + 1));
        else for (int i = ways.size() - 1; i >= 0; i--) drawLabel(ways.get(i).toWay(), x, y + 50 * (i + 1));
        return this;
    }
    private DijkstraWindow drawWay(int x, int y, int destination) {
        Node through = null;
        Node startVortex = graph.get(this.startVortex);
        Node end = graph.get(destination);
        ArrayList<Edge> way = new ArrayList<>();
        for (Edge edge : ways) {
            ArrayList<Node> nodes = edge.getNodes();
            Node node1 = nodes.get(0);
            Node node2 = nodes.get(1);
            if ((node1 == startVortex && node2 == end) || (node1 == end && node2 == startVortex)) {
                way.add(edge);
                through = edge.getThrough();
            }
        }

        while (through != null) {
            for (Edge edge : ways) {
                ArrayList<Node> nodes = edge.getNodes();
                Node node1 = nodes.get(0);
                Node node2 = nodes.get(1);
                if ((node1 == startVortex && node2 == through) || (node1 == through && node2 == startVortex)) {
                    way.add(edge);
                    through = edge.getThrough();
                }
            }
        }

        System.out.println(Matrix.toString(Matrix.edgesToMatrix(graph.getNodeCount(), way)));

        return this;
    }
    private DijkstraWindow drawNextButton(int x, int y) {
        JButton button = new JButton("Далі");
        button.setLocation(x, y);
        button.setSize(200, 50);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setActionCommand("DijkstraNext");
        button.addActionListener(new ButtonListener(this));
        add(button);
        return this;
    }
    public DijkstraWindow processNext() {
        boolean foundNew = false;
        if (algorithm) {
            for (Edge edge : edges) {
                ArrayList<Node> nodes = edge.getNodes();
                if (nodes.contains(graph.get(active))) {
                    edges.remove(edge);
                    processWay(edge);
                    foundNew = true;
                    break;
                }
            }
        } else {
            for (int i = edges.size() - 1; i >= 0; i--) {
                Edge edge = edges.get(i);
                ArrayList<Node> nodes = edge.getNodes();
                if (nodes.contains(graph.get(active))) {
                    edges.remove(edge);
                    processWay(edge);
                    foundNew = true;
                    break;
                }
            }
        }
        if (!foundNew) {
            temporary.remove(graph.get(active));
            findNewActive();
        }
        return this;
    }
    private void processWay(Edge way) {
        Node startNode = graph.get(startVortex);
        Node activeNode = graph.get(active);
        Node destination = null;
        Edge wayToDestination = null;
        Edge wayToActive = null;
        for (Node node : way.getNodes()) if (node != activeNode) destination = node;

        for (Edge road : ways) {
            Node node1 = road.getNodes().get(0);
            Node node2 = road.getNodes().get(1);
            if ((node1 == startNode && node2 == activeNode) || (node2 == startNode && node1 == activeNode)) {
                wayToActive = road;
            }
            if ((node1 == startNode && node2 == destination) || (node1 == destination && node2 == startNode)) {
                wayToDestination = road;
            }
        }
        if (algorithm) {
            if (way.getWeight() + wayToActive.getWeight() < wayToDestination.getWeight()) {
                status = way.getWeight() +
                    wayToActive.getWeight() +
                    " < " +
                    (wayToDestination.getWeight() > 100000 ? "+∞" : wayToDestination.getWeight()) +
                    ". Шлях у " + destination.getId() +
                    " тепер " + (way.getWeight() + wayToActive.getWeight());
                if (activeNode != startNode) {
                    wayToDestination.setThrought(activeNode);
                }
                wayToDestination.setWeight(way.getWeight() + wayToActive.getWeight());
                way.setColor(TEMPORARY_COLOR);
                activeNode.setSpecialEdge(way);
                destination.setSpecialEdge(way.clone().setColor(PERMANENT_COLOR));
                destination.setColor(TEMPORARY_COLOR);

                if (!temporary.contains(destination)) temporary.add(destination);
            } else {
                status =
                    way.getWeight() + wayToActive.getWeight() + " > " +
                    wayToDestination.getWeight() + ". Шлях у " + destination.getId() + " залишається " +
                    wayToDestination.getWeight();
                way.setColor(TEMPORARY_COLOR);
                activeNode.setSpecialEdge(way);
                destination.setSpecialEdge(way.clone().setColor(PERMANENT_COLOR));
                destination.setColor(TEMPORARY_COLOR);
            }
        } else {
            if (way.getWeight() + wayToActive.getWeight() > wayToDestination.getWeight()) {
                status = way.getWeight() +
                    wayToActive.getWeight() +
                    " > " +
                    (wayToDestination.getWeight() < -100000 ? "-∞" : wayToDestination.getWeight()) +
                    ". Шлях у " + destination.getId() +
                    " тепер " + (way.getWeight() + wayToActive.getWeight());
                if (activeNode != startNode) {
                    wayToDestination.setThrought(activeNode);
                }
                wayToDestination.setWeight(way.getWeight() + wayToActive.getWeight());
                way.setColor(TEMPORARY_COLOR);
                activeNode.setSpecialEdge(way);
                destination.setSpecialEdge(way.clone().setColor(PERMANENT_COLOR));
                destination.setColor(TEMPORARY_COLOR);

                if (!temporary.contains(destination)) temporary.add(destination);
            } else {
                status =
                    way.getWeight() + wayToActive.getWeight() + " < " +
                        wayToDestination.getWeight() + ". Шлях у " + destination.getId() + " залишається " +
                        wayToDestination.getWeight();
                way.setColor(TEMPORARY_COLOR);
                activeNode.setSpecialEdge(way);
                destination.setSpecialEdge(way.clone().setColor(PERMANENT_COLOR));
                destination.setColor(TEMPORARY_COLOR);
            }
        }
    }
    private void findNewActive() {
        Node currentActive = graph.get(active);
        Node newActive = null;
        temporary.remove(currentActive);
        if (temporary.size() == 0) {
            finish = true;
            status = "Пошук завершено.";
            return;
        }
        ways = Graph.sortEdges(ways);
        if (!algorithm) {
            for (Edge way : ways) {
                for (Node node : way.getNodes()) {
                    if (node != graph.get(startVortex) && temporary.contains(node)) newActive = node;
                }
            }
        } else {
            for (int i = ways.size() - 1; i >= 0; i--) {
                for (Node node : ways.get(i).getNodes()) {
                    if (node != graph.get(startVortex) && temporary.contains(node)) newActive = node;
                }
            }
        }
        status = "Активною вершиною встановлено " + newActive.getId();
        newActive
            .setColor(ACTIVE_COLOR)
            .setConnectionColor(ACTIVE_COLOR)
            .setRepetitiveWaysPolicy(Node.WaysPolicies.FROM_VORTEX_ONLY);
        active = newActive.getId();
        currentActive
            .setColor(PERMANENT_COLOR)
            .setConnectionColor(PERMANENT_COLOR)
            .removeSpecialEdges()
            .setRepetitiveWaysPolicy(Node.WaysPolicies.DEFAULT);
    }
}
