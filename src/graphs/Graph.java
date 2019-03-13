package graphs;

import javax.swing.*;
import java.util.HashMap;

public class Graph {
    public boolean directed = false;
    private HashMap<Number, Node> nodes = new HashMap<>();
    private HashMap<Number, String> edges = new HashMap<>();

    private Graph(boolean directed) {
        this.directed = directed;
    }
    private void add(Node node) {
        Number num = node.getId();
        this.nodes.put(num, node);
        node.setGraph(this);
    }
    public Graph draw(JFrame window) {
        this.nodes.values().forEach(
                node -> node.draw(window)
        );
        return this;
    }
    public Graph hide() {
        this.nodes.values().forEach(Node::hide);
        return this;
    }
    public void show() {
        this.nodes.values().forEach(Node::show);
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
            nodes[i].setGraph(graph);
        }
        if (nodes.length <= 0) throw new Error("Matrix is empty.");

        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++)
            if (matrix[i][j] == 1) {
                if (!nodes[j].isConnected(nodes[i]))
                    nodes[i].connect(nodes[j]);
            }

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
    HashMap getCrossed(int startNode, int endNode, int startX, int startY, int endX, int endY) {
        HashMap<Integer, Node> nodes = new HashMap<>();
        for (Node node : this.nodes.values()) {
            if (startNode == node.id) continue;
            if (endNode == node.id) continue;

            int counter = 0;
            for (int x = node.x - node.size; x < node.x + node.size; x++) {
                int y = startY + (x - startX) * (int) Math.floor((endY - startY) * 1.0 / (endX - startX));
                if (Math.pow(x - node.x, 2) + Math.pow(y - node.y, 2) == Math.pow(node.size, 2))
                    counter++;
            }
            if (counter == 2) nodes.put(node.id, node);
        }
        return nodes;
    }
    public String getConnectionsString (int id) {
        String res = "";
        Node node = this.nodes.get(id);
        HashMap connections = node.getConnections();
        for (int i = 0; i < this.nodes.size(); i++) {
            if (connections.containsKey(i))
                res = res.concat("  1");
            else res = res.concat("  0");
        }
        res = res.trim();
        return res;
    }
}
