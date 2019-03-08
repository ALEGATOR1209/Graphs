package graphs;
import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public class Node {
    private int x, y, value, id;
    private Graph graph;
    private int size = 50;
    private HashMap<Integer, Node> connections = new HashMap<>();
    private HashMap<Integer, JComponent> graphicalObject = new HashMap<>();
    Node(int x, int y, int value, int id) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.id = id;
    }
    void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    int getId() { return this.id; }
    void connect(Node node) {
        this.connections.put(node.getId(), node);
    }
    void setGraph(Graph graph) { this.graph = graph; }

    public String print() {
        int x = this.x;
        int y = this.y;
        int value = this.value;
        return String.format("{Vortex: x: %d, y: %d, value: %d }", x, y, value);
    }
    void draw(JFrame window) {
        Container container = window.getContentPane();
        this.drawConnections(window);
        Vortex vortex = new Vortex(this.x, this.y, this.size, this.value);
        container.add(vortex);
        this.graphicalObject.put(this.graphicalObject.size(), vortex);
    }

    boolean isConnected(Node node) {
        return this.connections.containsKey(node.value);
    }

    private void drawConnections(JFrame window) {
        Container container = window.getContentPane();
        this.connections.forEach(
            (key, graph) -> {
                if (this.id == graph.getId()) {
                    this.drawSelfArrow(window);
                    return;
                }
                if (graph.isConnected(this)) return;
                int dx = graph.x - this.x;
                int dy = graph.y - this.y;
                double tang = 1.0 * dy / dx;
                double angle = Math.atan(tang);
                if (angle != angle) angle = 90;
                int kx = 1, ky = 1;
                if (dy > 0 && dx < 0) {
                    ky = -1;
                    kx = 1;
                }
                if (dy > 0 && dx > 0) {
                    ky = -1;
                    kx = -1;
                }
                if (dy < 0 && dx > 0) {
                    kx = -1;
                }

                int startX = this.x - kx * (int) Math.floor(this.size / 2.0 * Math.abs(Math.cos(angle)));
                int startY = this.y - ky * (int) Math.floor(this.size / 2.0 * Math.abs(Math.sin(angle)));
                int endX = graph.x + kx * (int) Math.floor(graph.size * Math.abs(Math.sin(-180 - angle)) / 2);
                int endY = graph.y + ky * (int) Math.floor(graph.size * Math.abs(Math.cos(-180 - angle)) / 2);

                Line line = new Line(startX, startY, endX, endY, this.graph.directed);
                container.add(line);
                this.graphicalObject.put(this.graphicalObject.size(), line);
            }
        );
    }
    private void drawSelfArrow(JFrame window) {
        Arc arc = new Arc(this.x, this.y);
        window.getContentPane().add(arc);
        this.graphicalObject.put(this.graphicalObject.size(), arc);
    }

    void hide() {
        this.graphicalObject.values().forEach(
            obj -> obj.setVisible(false)
        );
    }
    void show() {
        this.graphicalObject.values().forEach(
            obj -> obj.setVisible(true)
        );
    }
}
