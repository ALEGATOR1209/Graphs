package graphs;
import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public class Node {
    private int x, y, value, id;
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

    public String print() {
        int x = this.x;
        int y = this.y;
        int value = this.value;
        return String.format("{ x: %d, y: %d, value: %d }", x, y, value);
    }
    public Node draw(JFrame window) {
        Container container = window.getContentPane();
        container.add(new Vortex(this.x, this.y, this.size, this.value));
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
