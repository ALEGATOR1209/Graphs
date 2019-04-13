package graphs;

import graphics.Background;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Graph {
    boolean directed;
    private HashMap<Number, Node> nodes = new HashMap<>();
    private boolean strong;

    private Graph(boolean directed, boolean strong) {
        this.strong = strong;
        this.directed = directed;
    }
    private void add(Node node) {
        Number num = node.getId();
        this.nodes.put(num, node);
        node.setGraph(this);
    }
    public void draw(JFrame window) {
        window
            .getContentPane()
            .add(new Background(650, 650, 5, 5));
        this.nodes.values().forEach(
            node -> node.drawConnections(window)
        );
        this.nodes.values().forEach(
                node -> node.draw(window)
        );
    }
    public Graph showStrong() {
        this.strong = !this.strong;
        return this;
    }
    public static Graph fromMatrix(int[][] matrix, boolean directed, boolean strong, boolean condensated) {
        Graph graph = new Graph(directed, strong);
        if (condensated) matrix = Matrix.condensateMatrix(matrix);
        int n = matrix.length;
        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            if (matrix[i].length != n)
                throw new Error(i + " line of matrix has wrong length.");
            nodes[i] = new Node(i * 10, i * 10, i, i);
            graph.add(nodes[i]);
            nodes[i].setGraph(graph);
        }
        if (nodes.length <= 0) throw new Error("Matrix is empty.");

        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            if (matrix[i][j] == 1) {
                if (!directed && nodes[j].isConnected(nodes[i])) continue;
                nodes[i].connect(nodes[j]);
            }
        }

        if (strong) {
            int k = 0x0;
            int delta = 0xFFAD5A - 0x85FF5A;
            String letter = "A";
            HashMap<Number, int[]> strongComponents = Matrix.getStrongComponents(matrix.clone());
            for (Number key : strongComponents.keySet()) {
                int[] component = strongComponents.get(key);
                for (int node : component) {
                    Color color = new Color(0xFF5A5A + k * delta);
                    graph
                        .nodes
                        .get(node)
                        .setColor(color)
                        .setLetter(letter);
                }
                k++;
                letter = letter.replace(letter.charAt(0), (char) (letter.codePointAt(0) + 1));
            }
        }

        if (condensated)  {
            int k = 0x0;
            int delta = 0xFFAD5A - 0x85FF5A;
            String letter = "A";
            for (Number num : graph.nodes.keySet()) {
                Color color = new Color(0xFF5A5A + k * delta);
                Node node = graph.nodes.get(num);
                node.setColor(color)
                    .setLetter(letter);
                k++;
                letter = letter.replace(letter.charAt(0), (char) (letter.codePointAt(0) + 1));
            }
        }

        return graph;
    }
    public Graph circle(int x, int y, int R) {
        int n = this.nodes.size();
        double angleStep = Math.PI * 2 / n;
        double angle = 0;
        for (int i = 0; i < n; i++) {
            int dX = (int) Math.floor(x + R * Math.sin(angle));
            int dY = (int) Math.floor(y - R * Math.cos(angle));
            this.nodes
                .get(i)
                .setCoordinates(dX, dY);
            angle += angleStep;
        }
        return this;
    }
    public Node get(int id) {
        return this.nodes.get(id);
    }
    public Graph setColorAll(Color color) {
        this.nodes.forEach((k, node) -> node.setColor(color));
        return this;
    }
    public Graph setConnectionsColorAll(Color color) {
        this.nodes.forEach((k, node) -> node.setConnectionColor(color));
        return this;
    }
    public int getNodeCount() { return this.nodes.size(); }
}
