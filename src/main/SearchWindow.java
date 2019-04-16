package main;

import graphics.Background;
import graphs.*;
import templates.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class SearchWindow extends JFrame {
    private final Color
        ACTIVE_COLOR = Color.red,
        EXPLORED_COLOR = new Color(53, 145, 10),
        UNEXPLORED_COLOR = Color.gray;
    private int[][] matrix;
    private final int
        X = 0,
        Y = 0,
        WIDTH = 1500,
        HEIGHT = 1000;
    private boolean
        initialization = true,
        searchType = false,
        finish = false,
        oriented;
    private int startVortex = 0;
    private ArrayDeque<Node> activeList;
    private ArrayList<Node> exploredList;
    private Graph graph, tree;

    public SearchWindow(int[][] matrix, boolean oriented) {
        super("Обхід графу");
        this.matrix = matrix;
        this.setBounds(this.X, this.Y, this.WIDTH, this.HEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.oriented = oriented;
        this.init();
    }
    private void init() {
        if (this.initialization) {
            drawInitComponents();
            return;
        }
        if (this.finish) {
            drawFinishScreen();
            return;
        }
        this.drawGraph()
            .add(new Background(Color.white, 650, 650, 5, 5))
            .drawNextButton()
            .drawList()
            .drawTree(700, 400);
        if (tree.getNodeCount() > 0) this
            .drawLabel("Матриця дерева обходу", 1000, 150)
            .drawMatrix(this.tree.toMatrix(), 1000, 200);
    }
    private SearchWindow add(JComponent comp) {
        this.getContentPane().add(comp);
        return this;
    }
    private void drawInitComponents() {
        this.tree = new Graph(oriented, false);
        this.graph = Graph.fromMatrix(matrix, oriented, false, false)
            .circle(300, 300, 250);
        activeList = new ArrayDeque<>();
        exploredList = new ArrayList<>();

        Text text = new Text(18, "Оберіть параметри обходу графа:", 700, 5);
        text.setSize(750, 30);
        Text startPoint = new Text("Стартова вершина: ", 700, 50);
        startPoint.setSize(200, 30);
        Slider sliderPoint = new Slider(875, 50, this.startVortex, this.matrix.length - 1);
        sliderPoint.setSize(300, 50);
        sliderPoint.addChangeListener(new SliderListener(this, "Search"));
        Text type = new Text("Тип обходу: ", 700, 125);
        type.setSize(150, 30);
        JRadioButton width = new JRadioButton("У ширину");
        width.setLocation(850, 120);
        width.setSize(100, 40);
        width.setActionCommand("Width");
        width.addActionListener(new ButtonListener(this));
        JRadioButton height = new JRadioButton("У висоту");
        height.setLocation(950, 120);
        height.setSize(100, 40);
        height.setActionCommand("Height");
        height.addActionListener(new ButtonListener(this));
        ButtonGroup group = new ButtonGroup();
        group.add(width); group.add(height);
        group.setSelected(this.searchType ? height.getModel() : width.getModel(), true);
        JButton start = new JButton("Почати обхід");
        start.setLocation(700, 175);
        start.setSize(400, 30);
        start.setFont(new Font("Arial", Font.BOLD, 16));
        start.setActionCommand("Start Search");
        start.addActionListener(new ButtonListener(this));
        this
            .add(text)
            .add(startPoint)
            .add(sliderPoint)
            .add(type)
            .add(width)
            .add(height)
            .add(start)
            .drawGraph()
            .add(new Background(Color.white, 650, 650, 5, 5));
    }
    private void drawFinishScreen() {
        this
            .drawTree(50, 600)
            .drawLabel("Матриця дерева обходу", 800, 75)
            .drawMatrix(tree.toMatrix(), 800, 125)
            .drawNewSearchButton()
            .drawLabel("Матриця відповідності вершин", 800, 450)
            .drawMatrix(graph.toIdLabelMatrix(), 800, 470, "labels");
        this.graph.draw(this, false);
        this.add(new Background(Color.white,700, 1000, 5, 5));
    }
    public SearchWindow setSearchType(boolean searchType) {
        this.searchType = searchType;
        return this;
    }
    public void redraw() {
        Container content = this.getContentPane();
        Component[] components = content.getComponents();
        for (Component component : components) component.setVisible(false);
        content.removeAll();
        this.init();
    }
    public SearchWindow setStart(int value) {
        this.startVortex = value;
        return this;
    }
    public SearchWindow startSearch() {
        this.initialization = false;
        this.graph.setColorAll(UNEXPLORED_COLOR);
        this.redraw();
        return this;
    }
    private SearchWindow drawGraph() {
        this.graph.draw(this);
        return this;
    }
    private SearchWindow drawNextButton() {
        JButton button = new JButton("Далі");
        button.setLocation(700, 10);
        button.setSize(500, 100);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setActionCommand("Next Vortex");
        button.addActionListener(new ButtonListener(this));
        this.add(button);
        return this;
    }
    private SearchWindow drawList() {
        Text title = new Text(this.searchType ? "Стек" : "Черга", 700, 150);
        title.setSize(200, 20);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        String list = "";

        if (activeList.size() == 0) list = "∅";
        else {
            ArrayDeque<Node> cloneList = activeList.clone();
            for (int i = 0; i < activeList.size(); i++) {
                list = list.concat(" " + cloneList.pop().getLetter());
            }
        }

        Text listText = new Text(list, 700, 200);
        listText.setSize(500, 20);

        this
            .add(title)
            .add(listText);
        return this;
    }
    private SearchWindow drawTree(int x, int y) {
        Text tree = new Text("Дерево обходу", x, y);
        tree.setFont(new Font("Arial", Font.BOLD, 16));
        tree.setSize(600, 20);
        this.tree
            .tree(searchType ? x : x + 300, y + 50, searchType)
            .setColorAll(EXPLORED_COLOR)
            .draw(this);
        this.add(tree);
        return this;
    }
    private void setNodeColor(Node node, Color color) {
        node.setColor(color);
        node.setConnectionColor(color);
    }
    public SearchWindow exploreNext() {
        if (activeList.size() == 0) {
            if (tree.getNodeCount() == 0) {
                Node firstVortex = graph.get(startVortex);
                activeList.add(firstVortex);
                exploredList.add(firstVortex);
                tree.add(new Node(0, 0, exploredList.size() - 1, exploredList.size() - 1));
                firstVortex.setLetter(exploredList.size() - 1 + "");
                this.setNodeColor(firstVortex, ACTIVE_COLOR);
                firstVortex.setRepetitiveWaysPolicy(Node.WaysPolicies.FROM_VORTEX_ONLY);
            } else {
                if (tree.getNodeCount() == graph.getNodeCount()) {
                    graph
                        .setColorAll(EXPLORED_COLOR)
                        .setPoliciesAll(Node.WaysPolicies.DEFAULT)
                        .setConnectionsColorAll(Color.BLACK);
                    this.finish = true;
                    redraw();
                    return this;
                }
                for (int i = 0; i < graph.getNodeCount(); i++) {
                    Node currentNode = graph.get(i);
                    if (exploredList.contains(currentNode)) continue;
                    activeList.add(currentNode);
                    exploredList.add(currentNode);
                    currentNode.setLetter(exploredList.size() - 1 + "");
                    this.setNodeColor(currentNode, ACTIVE_COLOR);
                    currentNode.setRepetitiveWaysPolicy(Node.WaysPolicies.FROM_VORTEX_ONLY);
                    break;
                }
            }
        } else {
            //searchType: true = height search; true = width search;
            Node activeVortex = searchType ? activeList.getLast() : activeList.getFirst();
            Node newVortex = null;
            for (Integer key : activeVortex.getConnections().keySet()) {
                Node currentVortex = activeVortex.getConnections().get(key);
                if (exploredList.contains(currentVortex)) continue;
                if (currentVortex == activeVortex) continue;
                newVortex = currentVortex;
                break;
            }
            if (newVortex == null) {
                Node deleted = searchType ? activeList.removeLast() : activeList.removeFirst();
                if (deleted == null || activeList.size() == 0) {
                    redraw();
                    return this;
                }
                newVortex = searchType ? activeList.getLast() : activeList.getFirst();
                this.setNodeColor(newVortex, ACTIVE_COLOR);
                newVortex.setRepetitiveWaysPolicy(Node.WaysPolicies.FROM_VORTEX_ONLY);
                this.setNodeColor(activeVortex, EXPLORED_COLOR);
                activeVortex.setRepetitiveWaysPolicy(Node.WaysPolicies.DEFAULT);
                redraw();
                return this;
            }
            if (searchType) {
                this.setNodeColor(newVortex, ACTIVE_COLOR);
                newVortex.setRepetitiveWaysPolicy(Node.WaysPolicies.FROM_VORTEX_ONLY);
                this.setNodeColor(activeVortex, EXPLORED_COLOR);
                activeVortex.setRepetitiveWaysPolicy(Node.WaysPolicies.DEFAULT);
            }
            else {
                this.setNodeColor(newVortex, EXPLORED_COLOR);
            }
            exploredList.add(newVortex);
            newVortex.setLetter(exploredList.size() - 1 + "");
            Node node = new Node(0, 0, exploredList.size() - 1, exploredList.size() - 1);
            tree
                .get(Integer.parseInt(activeVortex.getLetter()))
                .connect(node);
            tree.add(node);
            activeList.add(newVortex);
        }

        redraw();
        return this;
    }
    private SearchWindow drawLabel(String text, int x, int y) {
        Text label = new Text(text, x, y);
        label.setSize(text.length() * 10, 20);
        this.add(label);
        return this;
    }
    private SearchWindow drawMatrix(int[][] matrix, int x, int y, String options) {
        if (matrix.length == 0) return this;
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            if (!options.equals("labels")) {
                Text horizontalNumber = new Text(i + "", 5 + x + 20 * (i + 1), y, 20);
                horizontalNumber.setForeground(Color.red);
                this.add(horizontalNumber);
                Text verticalNumber = new Text(i + "", x, y + 20 * (i + 1));
                verticalNumber.setForeground(Color.red);
                this.add(verticalNumber);
            }

            String connections = "";
            for (int j = 0; j < matrix[i].length; j++) {
                if (options.equals("labels"))
                    if (j > 0) connections = connections.concat(" -> " + matrix[i][j]);
                    else connections = connections.concat("" + matrix[i][j]);
                else connections = connections.concat("  " + matrix[i][j]);
            }
            connections = connections.trim();
            JLabel string = new JLabel(connections);
            string.setSize(220, 20);
            string.setLocation(x + 25, y + 20 * (i + 1));
            string.setFont(new Font("Arial", Font.PLAIN, 16));
            this.add(string);
        }
        return this;
    }
    private SearchWindow drawMatrix(int[][] matrix, int x, int y) {
        if (matrix.length == 0) return this;
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            Text horizontalNumber = new Text(i + "", 5 + x + 20 * (i + 1), y, 20);
            horizontalNumber.setForeground(Color.red);
            this.add(horizontalNumber);
            Text verticalNumber = new Text(i + "", x, y + 20 * (i + 1));
            verticalNumber.setForeground(Color.red);
            this.add(verticalNumber);

            String connections = "";
            for (int j = 0; j < matrix[i].length; j++)
                connections = connections.concat("  " + matrix[i][j]);
            connections = connections.trim();
            JLabel string = new JLabel(connections);
            string.setSize(220, 20);
            string.setLocation(x + 25, y + 20 * (i + 1));
            string.setFont(new Font("Arial", Font.PLAIN, 16));
            this.add(string);
        }
        return this;
    }
    private SearchWindow drawNewSearchButton() {
        JButton button = new JButton("Обійти ще раз");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setSize(400, 50);
        button.setLocation(800, 5);
        button.setActionCommand("New Search");
        button.addActionListener(new ButtonListener(this));
        this.add(button);
        return this;
    }
    public SearchWindow newSearch() {
        this.initialization = true;
        this.finish = false;
        return this;
    }
}
