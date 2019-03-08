package main;

import graphs.Graph;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class GraphChanger implements ItemListener {
    private Graph orientedGraph, unorientedGraph;
    void addGraph(Graph graph) {
        if (graph.directed) this.orientedGraph = graph;
        else {
            this.unorientedGraph = graph;
            this.unorientedGraph.show();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            this.orientedGraph.hide();
            this.unorientedGraph.show();
        } else {
            this.unorientedGraph.hide();
            this.orientedGraph.show();
        }
    }
}
