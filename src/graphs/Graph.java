package graphs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    boolean directed;
    private HashMap<Number, Node> nodes = new HashMap<>();
    private boolean strong;
    int[][] weightMatrix;

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
            node -> {
                if (this.weightMatrix != null && this.weightMatrix.length > 0) {
                    node.setWeight(true);
                }
                node.draw(window, false)
                    .drawConnections(window);
            }
        );
    }
    public void draw(JFrame window, boolean labels) {
        this.nodes.values().forEach(
            node -> {
                if (this.weightMatrix.length > 0) {
                    node.setWeight(true);
                }
                node.draw(window, false)
                    .drawConnections(window);
            }
        );
    }
    public Graph showStrong() {
        this.strong = !this.strong;
        return this;
    }
    public static Graph fromWeightMatrix(int[][] matrix, Color color) {
        int n = matrix.length;
        Graph graph = new Graph(false, false);
        graph.setWeightMatrix(matrix);
        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            if (matrix[i].length != n) throw new Error(i + " line of matrix has wrong length.");
            nodes[i] = new Node(i + 10, i * 10, i, i);
            graph.add(nodes[i]);
            nodes[i].setGraph(graph);
        }

        if (nodes.length <= 0) throw new Error("Matrix is empty.");

        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            if (matrix[i][j] != 0) {
                nodes[i].connect(nodes[j]);
                nodes[i].setColor(color);
                nodes[i].setWeight(true);
                nodes[i].setSpecialEdge(new Edge(matrix[i][j])
                    .setColor(color)
                    .connect(nodes[i])
                    .connect(nodes[j])
                );
            }
        }
        return graph;
    }
    public static Graph fromWeightMatrix(int[][] matrix, Color color, boolean isolated) {
        Graph graph = Graph.fromWeightMatrix(matrix, color);
        if (!isolated) {
            int count = graph.getNodeCount();
            for (int i = 0; i < count; i++) {
                Node node = graph.get(i);
                if (node.getConnections().size() == 0) {
                    graph.remove(i);
                }
            }
        }
        return graph;
    }
    private Graph remove(int id) {
        this.nodes.get(id).setGraph(null);
        this.nodes.remove(id);
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
        for (Number i : nodes.keySet()) {
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
    public void setWeightMatrix(int[][] matrix) {
        weightMatrix = matrix;
    }
    public ArrayList<Edge> toEdgeList() {
        if (this.weightMatrix == null) throw new Error("WeightMatrix needed");

        ArrayList<Edge> edges = new ArrayList<>();
        for (Number id1 : nodes.keySet()) {
            Node node = nodes.get(id1);
            for (Number id2 : node.getConnections().keySet()) {
                if (id1.intValue() > id2.intValue()) continue;
                if (id1.intValue() == id2.intValue()) continue;
                Edge edge = new Edge(weightMatrix[id1.intValue()][id2.intValue()]);
                edge.connect(node)
                    .connect(nodes.get(id2));
                edges.add(edge);
            }
        }
        return edges;
    }
    public static ArrayList<Edge> sortEdges(ArrayList<Edge> unsorted) { //smth like bubble sort
        ArrayList<Edge> sorted = new ArrayList<>();                     //WORST SPEED   - O(nn)
        int size = unsorted.size();                                     //AVERAGE SPEED - O(nn)
        while (sorted.size() < size) {                                  //MINIMAL SPEED - O(n)
            Edge minimum = new Edge(1000000000); //one milliard - it's almost infinity, isn't it?
            for (Edge edge : unsorted) {
                if (edge.getWeight() < minimum.getWeight()) {
                    minimum = edge;
                }
            }
            unsorted.remove(minimum);
            sorted.add(minimum);
        }
        return sorted;
    }
    public HashMap<Number, Node> getNodes() {
        return this.nodes;
    }
    public Graph removeAllSpecialEdges() {
        this.nodes
            .forEach((key, node) -> node.removeSpecialEdges());
        return this;
    }
}
