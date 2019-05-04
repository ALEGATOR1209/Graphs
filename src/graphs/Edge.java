package graphs;

import java.awt.*;
import java.util.ArrayList;

public class Edge {
    private ArrayList<Node> nodes = new ArrayList<>();
    private Node through;
    private int weight = 0;
    private Color color;

    public Edge() {}
    public Edge(int weight) {
        this.weight = weight;
    }
    public Edge connect(Node node) {
        nodes.add(node);
        return this;
    }
    public int getWeight() {
        return weight;
    }
    public Edge setWeight(int weight) {
        this.weight = weight;
        return this;
    }
    public String toString() {
        String result = "";
        for (Node node : nodes) {
            if (result.length() == 0) {
                result = node.getLetter();
                continue;
            }
            result = result.concat("-" + (through == null ? "-" : through.getId()) + "->" + node.getLetter());
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
    public String toWay() {
        return this.toString() + " L = " + (weight > 1000000 ? "+∞" : weight < -1000000 ? "-∞" : weight);
    }
    public Color getColor() { return color; }
    public boolean contains(Node node) {
        return nodes.contains(node);
    }
    private Edge setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
        return this;
    }
    public Edge clone() {
        Edge newEdge = new Edge(this.weight);
        newEdge
            .setColor(this.color)
            .setNodes(this.nodes);
        return newEdge;
    }
    public void setThrought(Node through) {
        this.through = through;
    }
    public Node getThrough() {
        return this.through;
    }
}
