package main;

import graphs.Graph;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class GraphChanger implements ItemListener {
    private Graph orientedGraph, unorientedGraph;
    private Window window;
    public GraphChanger(Window window) { this.window = window; }

    public void itemStateChanged(ItemEvent e) {
        this.window.changeOrientation();
        this.window.redraw();
    }
}
