package graphs;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class Node {
    private int x, y, value, id;
    private Graph graph;
    private int size = 50;
    private HashMap<Integer, Node> connections = new HashMap<>();
    public Node(int x, int y, int value, int id) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.id = id;
    }
    public Node(int x, int y, int value, int id, int size) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.id = id;
        this.size = size;
    }
    public HashMap getCoordinates() {
        HashMap<String, Number> coordinates = new HashMap<>();
        coordinates.put("x", this.x); coordinates.put("y", this.y);
        return coordinates;
    }
    public Node setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public Node setValue(int value) {
        this.value = value;
        return this;
    }
    public int getValue() { return this.value; }
    public int getId() { return this.id; }
    public Node connect(Node node) {
        this.connections.put(node.getId(), node);
        return this;
    }
    public Node disconnect(Node node) {
        this.connections.remove(node.getId());
        return this;
    }
    public Node getConnection(Integer number) {
        return this.connections.get(number);
    }
    public Node setGraph (Graph graph) { this.graph = graph; return this; }

    public String print() {
        int x = this.x;
        int y = this.y;
        int value = this.value;
        return String.format("{Vortex: x: %d, y: %d, value: %d }", x, y, value);
    }
    public Node draw(JFrame window) {
        Container container = window.getContentPane();
        this.drawArrows(window);
        container.add(new Vortex(this.x, this.y, this.size, this.value));
        return this;
    }

    public boolean isConnected(Node node) {
        if (this.connections.containsKey(node.value)) return true;
        return false;
    }

    public Node drawArrows(JFrame window) {
        Container container = window.getContentPane();
        this.connections.forEach(
            (key, graph) -> {
                if (this.id == graph.getId()) { this.drawSelfArrow(window); }
                if (graph.isConnected(this)) return;
                int dx = graph.x - this.x;
                int dy = graph.y - this.y;
                double tang = 1.0 * dy / dx;
                double angle = Math.atan(tang);
                if (angle != angle) angle = 90;
                int kx = 1, ky = 1;
                if (dy > 0 && dx < 0) { ky = -1; kx = 1; }
                if (dy > 0 && dx > 0) { ky = -1; kx = -1; }
                if (dy < 0 && dx > 0) { kx = -1; }

                int startX = this.x - kx * (int) Math.floor(this.size / 2.0 * Math.abs(Math.cos(angle)));
                int startY = this.y - ky * (int) Math.floor(this.size / 2.0 * Math.abs(Math.sin(angle)));
                int endX = graph.x + kx * (int) Math.floor(graph.size * Math.abs(Math.sin(-180-angle)) / 2);
                int endY = graph.y + ky * (int) Math.floor(graph.size * Math.abs(Math.cos(-180-angle)) / 2);

                ArrayList<Node> onTheRoad = new ArrayList();
                for (int i = 0; i < this.graph.nodes.size(); i++) {
                    Node node = this.graph.nodes.get(i);
                    if (node.value == graph.value || node.value == this.value) continue;
                    int leftX = node.x - node.size; int rightX = node.x - node.size;
                    if (graph.x > this.x) {
                        if (leftX <= this.x || rightX >= graph.x) continue;
                    } else if (leftX <= graph.x || rightX >= this.x) continue;

                    int top = node.y - node.size;
                    int bottom = node.y + node.size;
                    int topBorder = startY > endY ? startY : endY;
                    int bottomBorder = startY > endY ? endY : startY;
                    if (bottomBorder < top || topBorder > bottom) continue;

                    onTheRoad.add(node);
                }

                if (onTheRoad.size() > 0) {
                    for (int i = 0; i < onTheRoad.size(); i++) {
                        Node node = onTheRoad.get(i);
                        int R = node.y + (node.size / 2);
                        int leftX = node.x - node.size;
                        int leftY = R + (int) Math.floor(Math.sqrt(Math.pow(node.size, 2) - Math.pow(leftX, 2)));
                        int rightX = node.x  + node.size;
                        int k = 15; if (node.y > this.y) k = -15;
                        int rightY = R + k + (int) Math.floor(Math.sqrt(Math.pow(node.size, 2) - Math.pow(rightX, 2)));
                        container.add(new Line(startX, startY, leftX, leftY));
                        container.add(new Line(rightX, rightY, endX, endY));
                        container.add(new Line(leftX, leftY, rightX, rightY));
                    }
                } else container.add(new Line(startX, startY, endX, endY));
            }
        );
        return this;
    }
    public Node drawSelfArrow(JFrame window) {
        window.getContentPane().add(new Arc(this.x, this.y));
        return this;
    }
}

class Vortex extends JComponent {
    private int x, y, size;
    private char[] value;

    Vortex(int x, int y, int size, int value) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.value = String.valueOf(value).toCharArray();
        this.setSize(500, 500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(this.x, this.y, this.size, this.size);
        char[] value = this.value;
        int x = -5 + this.x + this.size / 2;
        int y = 5 + this.y + this.size / 2;
        g.drawChars(value, 0, value.length, x, y);
    }
}

class Line extends JComponent {
    private int startX, startY, endX, endY;

    Line(int startX, int startY, int endX, int endY) {
        this.startX = startX; this.startY = startY;
        this.endX = endX; this.endY = endY;
        this.setSize(500, 500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int startX = this.startX + 25;
        int startY = this.startY + 25;
        int endX = this.endX + 25;
        int endY = this.endY + 25;
        g.drawLine(startX, startY, endX, endY);
    }
}
class Arc extends JComponent {
    private int startX, startY;
    private int width = 50, height = 50;
    private int startAngle = -180, arcAngle = -270;

    Arc(int startX, int startY) {
        this.startX = startX + 25; this.startY = startY - 25;
        this.setSize(500, 500);
    }
    Arc(int startX, int startY, int width, int height, int startAngle, int arcAngle) {
        this.startX = startX; this.startY = startY;
        this.width = width; this.height = height;
        this.startAngle = startAngle; this.arcAngle = arcAngle;
        this.setSize(500, 500);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawArc(this.startX, this.startY, this.width, this.height, this.startAngle, this.arcAngle);
    }
}