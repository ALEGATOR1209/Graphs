package graphs;

import graphics.*;
import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

class Node {
    private int x, y, value, id;
    private Graph graph;
    private int size = 50;
    private int valency = 0;
    private String letter = "";
    private Color color = Color.black;
    private HashMap<Integer, Node> connections = new HashMap<>();
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
    void setLetter(String letter) { this.letter = letter; }
    int getId() { return this.id; }
    Node setColor(Color color) {
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
        this.drawConnections(window);
        boolean isolated = this.valency == 0;
        boolean leave = this.valency == 1;
        char[] value = this.letter.length() > 0 ?
            this.letter.toCharArray() :
            String.valueOf(this.value).toCharArray();
        Vortex vortex = new Vortex(this.x, this.y, this.size, value, isolated, leave, this.color);
        container.add(vortex);
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
            }
        );
    }
    private void drawSelfArrow(JFrame window) {
        Arc arc = new Arc(this.x, this.y);
        window.getContentPane().add(arc);
    }
}
