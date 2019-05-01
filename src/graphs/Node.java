package graphs;

import graphics.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class Node {
    public enum WaysPolicies {
        FROM_VORTEX_ONLY,
        DEFAULT
    }
    private WaysPolicies drawRepetitiveWays = WaysPolicies.DEFAULT;
    private Graph graph;
    private int
        x, y, value, id,
        size = 50,
        valency = 0;
    private HashMap<Integer, Node> connections = new HashMap<>();
    private String letter = "";
    private Color
        color = Color.black,
        connectionColor = Color.black;
    private boolean drawWeight = false;
    private ArrayList<Edge> specialEdges = new ArrayList<>();

    public Node(int x, int y, int value, int id) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.id = id;
    }
    Node setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public void setRepetitiveWaysPolicy(WaysPolicies policy) { this.drawRepetitiveWays = policy; }
    public void setLetter(String letter) {
        this.letter = letter;
    }
    int getId() { return this.id; }
    public Node setColor(Color color) {
        this.color = color;
        return this;
    }
    public void connect(Node node) {
        this.connections.put(node.getId(), node);
        this.addValency();
        node.addValency();
    }
    private void addValency() { this.valency++; }
    void setGraph(Graph graph) { this.graph = graph; }

    void draw(JFrame window) {
        Container container = window.getContentPane();
        boolean isolated = this.valency == 0;
        boolean leave = this.valency == 1;
        char[] value = this.letter.length() > 0 ?
            this.letter.toCharArray() :
            String.valueOf(this.value).toCharArray();
        Vortex vortex = new Vortex(this.x, this.y, this.size, value, isolated, leave, this.color);
        container.add(vortex);
    }
    Node draw(JFrame window, boolean labels) {
        Container container = window.getContentPane();
        boolean isolated = this.valency == 0;
        boolean leave = this.valency == 1;
        char[] value = this.letter.length() > 0 ?
            this.letter.toCharArray() :
            String.valueOf(this.value).toCharArray();
        if (!labels) value = String.valueOf(this.value).toCharArray();
        Vortex vortex = new Vortex(this.x, this.y, this.size, value, isolated, leave, this.color);
        container.add(vortex);
        return this;
    }

    boolean isConnected(Node node) {
        return this.connections.containsKey(node.getId());
    }

    void drawConnections(JFrame window) {
        Container container = window.getContentPane();
        this.connections.forEach(
            (key, graph) -> {
                if (this.id == graph.getId()) {
                    this.drawSelfArrow(window);
                    return;
                }
                if (!this.graph.directed) {
                    if (drawRepetitiveWays == graph.drawRepetitiveWays && drawRepetitiveWays == WaysPolicies.DEFAULT) {
                        if (this.id > graph.id) return;
                    } else if (graph.drawRepetitiveWays == WaysPolicies.FROM_VORTEX_ONLY) return;
                }
                int dx = graph.x - this.x;
                int dy = graph.y - this.y;
                double tang = 1.0 * dy / dx;
                double angle = Math.atan(tang);
                if (angle != angle) angle = 90;
                int kx = 1, ky = 1;
                if (dy > 0 && dx < 0) {
                    ky = 1;
                    kx = 1;
                }
                if (dy > 0 && dx > 0) {
                    ky = -1;
                    kx = -1;
                }
                if (dx > 0 && dy == 0) {
                    kx = -1;
                    ky = -1;
                }
                if (dx == 0 && dy > 0) {
                    kx = -1;
                    ky = -1;
                }
                if (dx == 0 && dy < 0) {
                    kx = 1;
                    ky = -1;
                }
                if (dy < 0 && dx > 0) {
                    kx = -1;
                    ky = -1;
                }

                int startX = this.x - kx * (int) Math.floor(this.size / 2.0 * (Math.cos(angle)));
                int startY = this.y - ky * (int) Math.floor(this.size / 2.0 * (Math.sin(angle)));
                int endX = graph.x + kx * (int) Math.floor(graph.size * (Math.sin(-180 - angle)) / 2);
                int endY = graph.y + ky * (int) Math.floor(graph.size * (Math.cos(-180 - angle)) / 2);

                int weight = this.graph.weightMatrix == null ? 0 : this.graph.weightMatrix[this.id][graph.id];
                JLabel text = new JLabel(weight + "");
                if (drawWeight && this.id != graph.id) {
                    int labelX = (int) Math.floor((startX + endX) / 2) + 15;
                    int labelY = (int) Math.floor((startY + endY) / 2) + 15;
                    text.setForeground(this.connectionColor);
                    text.setLocation(labelX, labelY);
                    text.setSize(20, 20);
                }
                for (Edge edge : specialEdges) {
                    if (edge.getNodes().contains(this) && edge.getNodes().contains(graph)) {
                        Line line = new Line(startX, startY, endX, endY, this.graph.directed, edge.getColor());
                        text.setForeground(edge.getColor());
                        container.add(text);
                        container.add(line);
                        return;
                    }
                }

                Line line = new Line(startX, startY, endX, endY, this.graph.directed, connectionColor);
                container.add(line);
                container.add(text);
            }
        );
    }
    private void drawSelfArrow(JFrame window) {
        Arc arc = new Arc(this.x, this.y);
        window.getContentPane().add(arc);
    }
    public HashMap<Integer, Node> getConnections() { return this.connections; }
    public void setConnectionColor(Color color) { this.connectionColor = color; }
    public String getLetter() { return letter == null ? letter : "" + value; }
    void locateChilds(int width, int height, boolean type) {
        int k = this.x - connections.size() * width / 2;
        if (connections.size() == 1) {
            if (type) connections.forEach((n, node) -> node
                .setCoordinates(x + width, y)
                .locateChilds(width, height, true)
            );
            else connections.forEach((n, node) -> node
                .setCoordinates(x, y + height)
                .locateChilds(width, height, false)
            );
            return;
        }
        for (Integer key : connections.keySet()) {
            Node node = connections.get(key);
            node.setCoordinates(k, y + height);
            k += width;
            node.locateChilds(width, height, type);
        }
    }
    int getChildrenCount() {
        if (connections.size() == 0) return 0;
        return connections.size();
    }
    void setWeight(boolean weight) {
        drawWeight = weight;
    }
    public void setSpecialEdge(Edge edge) {
        specialEdges.add(edge);
    }
}
