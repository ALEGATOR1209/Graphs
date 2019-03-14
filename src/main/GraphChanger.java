package main;

import graphs.Graph;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class GraphChanger implements ItemListener {
    private Window window;
    GraphChanger(Window window) { this.window = window; }

    public void itemStateChanged(ItemEvent e) {
        this.window.changeOrientation();
        this.window.redraw();
    }
}
