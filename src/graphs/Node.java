package graphs;

import graphics.*;
import main.WaysWindow;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public class Node {
    public enum WaysPolicies {
        DRAW_ALL,
        FROM_VORTEX_ONLY,
        TO_VORTEX_ONLY,
        DEFAULT;
    }
    private int x, y, value, id;
    private Graph graph;
    private int size = 50;
    private int valency = 0;
    private String letter = "";
    private Color color = Color.black;
    private HashMap<Integer, Node> connections = new HashMap<>();
    private Color connectionColor = Color.black;
    WaysPolicies drawRepetitiveWays = WaysPolicies.DEFAULT;
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
    public void setLetter(String letter) { this.letter = letter; }
    public int getId() { return this.id; }
    public Node setColor(Color color) {
        this.color = color;
        return this;
    }
    void connect(Node node) {
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

                Line line = new Line(startX, startY, endX, endY, this.graph.directed, connectionColor);
                container.add(line);
            }
        );
    }
    private void drawSelfArrow(JFrame window) {
        Arc arc = new Arc(this.x, this.y);
        window.getContentPane().add(arc);
    }
    public HashMap<Integer, Node> getConnections() { return this.connections; }
    public void setConnectionColor(Color color) { this.connectionColor = color; }
    public String getLetter() { return letter; }
    void locateChilds(int width, int height) {
        int k = this.x - connections.size() * width / 2;
        for (Integer key : connections.keySet()) {
            Node node = connections.get(key);
            node.setCoordinates(k, y + height);
            k += width;
            node.locateChilds(width, height);
        }
    }
}
