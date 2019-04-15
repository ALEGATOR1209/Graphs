package graphs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Graph {
    boolean directed;
    private HashMap<Number, Node> nodes = new HashMap<>();
    private boolean strong;

    public Graph(boolean directed, boolean strong) {
        this.strong = strong;
        this.directed = directed;
    }
    public void add(Node node) {
        Number num = node.getId();
        this.nodes.put(num, node);
        node.setGraph(this);
    }
    public void draw(JFrame window) {
        this.nodes.values().forEach(
            node -> node.drawConnections(window)
        );
        this.nodes.values().forEach(
                node -> node.draw(window)
        );
    }
    public void draw(JFrame window, boolean labels) {
        this.nodes.values().forEach(
            node -> node.drawConnections(window)
        );
        this.nodes.values().forEach(
            node -> node.draw(window, labels)
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
    public Graph tree(int x, int y, boolean type) {
        HashMap<Number, Node> roots = new HashMap<>();
        for (Number key : nodes.keySet()) {
            if (!this.hasInputWays(key.intValue())) roots.put(key, nodes.get(key));
        }
        int k = 1;
        for (Number root : roots.keySet()) {
            roots.get(root)
                .setCoordinates(x + 75 * k + roots.get(root).getChildrenCount() * (k + 2), y)
                .locateChilds(75, 75, type);
            k++;
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
    public void setConnectionsColorAll(Color color) {
        this.nodes.forEach((k, node) -> node.setConnectionColor(color));
    }
    public Graph setPoliciesAll(Node.WaysPolicies policy) {
        this.nodes.forEach((k, node) -> node.setRepetitiveWaysPolicy(policy));
        return this;
    }
    public int getNodeCount() { return this.nodes.size(); }
    private boolean hasInputWays(int id) {
        Node node = nodes.get(id);
        for (Number key : nodes.keySet()) {
            if (nodes.get(key).isConnected(node)) return true;
        }
        return false;
    }
    public int[][] toMatrix() {
        int n = nodes.size();
        if (n == 0) return new int[0][0];
        int[][] matrix = new int[n][n];

        for (Number key1 : nodes.keySet()) for (Number key2 : nodes.keySet()) {
            Node node1 = nodes.get(key1);
            Node node2 = nodes.get(key2);
            if (node1.isConnected(node2)) {
                matrix[node1.getId()][node2.getId()] = 1;
            } else matrix[node1.getId()][node2.getId()] = 0;
        }

        return matrix;
    }
    public int[][] toIdLabelMatrix() {
        int n = nodes.size();
        if (n == 0) return new int[0][0];
        int[][] matrix = new int[n][2];

        for (Number key : nodes.keySet()) {
            Node node = nodes.get(key);
            int id = node.getId();
            String letter = node.getLetter();
            matrix[id][0] = id;
            matrix[id][1] = (int) Double.parseDouble(letter);
        }

        return matrix;
    }
    public Graph removeLabelAll() {
        this.nodes.forEach((k, node) -> node.setLetter(node.getId() + ""));
        return this;
    }
}
