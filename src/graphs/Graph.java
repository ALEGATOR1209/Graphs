package graphs;

import javax.swing.*;
import java.util.HashMap;

public class Graph {
    boolean directed = false;
    HashMap<Number, Node> nodes;
    HashMap<Number, String> edges;

    public Graph(boolean directed) {
        this.directed = directed;
        this.init();
    }
    public Graph() {
        this.init();
    }
    private void init() {
        nodes = new HashMap();
        edges = new HashMap();
    }
    public Graph add(Node node) {
        Number num = node.getId();
        this.nodes.put(num, node);
        return this;
    }
    public Graph draw(JFrame window) {
        this.nodes.values().forEach(
                node -> node.draw(window)
        );
        return this;
    }
    public static Graph fromMatrix(int[][] matrix, boolean directed) {
        int n = matrix.length;
        Node[] nodes = new Node[n];
        Graph graph = new Graph(directed);

        for (int i = 0; i < n; i++) {
            if (matrix[i].length != n)
                throw new Error(i + " line of matrix has wrong length.");
            nodes[i] = new Node(i * 10, i * 10, i, i);
            graph.add(nodes[i]);
        }
        if (nodes.length <= 0) throw new Error("Matrix is empty.");

        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++)
            if (matrix[i][j] == 1) nodes[i].connect(nodes[j]);

        return graph;
    }
    public Graph circle(int x, int y, int R) {
        int n = this.nodes.size();
        int angleStep = (int) Math.floor(360.0 / n);
        int angle = 0;
        for (int i = 0; i < n; i++) {
            int dX = (int) Math.floor(x - R + R * Math.sin(angle));
            int dY = (int) Math.floor(y - R * Math.cos(angle));
            this.nodes
                .get(i)
                .setCoordinates(dX, dY);
            angle += angleStep;
        }

        return this;
    }
}
