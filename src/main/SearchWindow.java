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
        HEIGHT = 750;
    private boolean initialization = true;
    private boolean searchType = true;
    private int startVortex = 0;
    private ArrayDeque<Node> activeList = new ArrayDeque<>();
    private ArrayList<Node> exploredList = new ArrayList<>();
    private Graph graph, tree;
    private boolean finish = false;

    public SearchWindow(int[][] matrix, boolean oriented) {
        super("Обхід графу");
        this.matrix = matrix;
        this.setBounds(this.X, this.Y, this.WIDTH, this.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.tree = new Graph(oriented, false);
        this.graph = Graph.fromMatrix(matrix, oriented, false, false)
            .circle(300, 300, 250);
        this.init();
    }
    private void init() {
        if (this.initialization) {
            drawInitComponents();
            return;
        }
        this.drawGraph()
            .drawNextButton()
            .drawList()
            .drawTree();
    }
    private SearchWindow add(JComponent comp) {
        this.getContentPane().add(comp);
        return this;
    }
    private void drawInitComponents() {
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
        width.setLocation(800, 120);
        width.setSize(100, 40);
        width.setActionCommand("Width");
        width.addActionListener(new ButtonListener(this));
        JRadioButton height = new JRadioButton("У висоту");
        height.setLocation(950, 120);
        height.setSize(100, 40);
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
            .drawGraph();
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
        this.add(new Background(Color.white, 650, 650, 5, 5));
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
                list = list + " " + cloneList.pop().getLetter();
            }
        }

        Text listText = new Text(list, 700, 200);
        listText.setSize(500, 20);

        this
            .add(title)
            .add(listText);
        return this;
    }
    private void drawTree() {
        Text tree = new Text("Дерево обходу", 700, 250);
        tree.setFont(new Font("Arial", Font.BOLD, 16));
        tree.setSize(600, 20);
        this.tree
            .tree(800, 300)
            .setColorAll(UNEXPLORED_COLOR)
            .draw(this);
        this.add(tree);
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
                tree.add(new Node(0, 0, 0, exploredList.size() - 1));
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
            tree.add(new Node(0, 0, 0, exploredList.size() - 1));
            activeList.add(newVortex);
        }

        redraw();
        return this;
    }
}
