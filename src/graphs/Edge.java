package graphs;

import java.awt.*;
import java.util.ArrayList;

public class Edge {
    private ArrayList<Node> nodes = new ArrayList<>();
    private int weight;
    private Color color;

    Edge(int weight) {
        this.weight = weight;
    }
    Edge connect(Node node) {
        nodes.add(node);
        return this;
    }
    int getWeight() {
        return weight;
    }
    public String toString() {
        String result = "";
        for (Node node : nodes) {
            if (result.length() == 0) {
                result = node.getLetter();
                continue;
            }
            result = result.concat("-" + weight + "->" + node.getLetter());
        }
        return result;
    }
    public ArrayList<Node> getNodes() {
        return nodes;
    }
    public Edge setColor(Color color) {
        this.color = color;
        return this;
    }
    public Color getColor() { return color; }
}
