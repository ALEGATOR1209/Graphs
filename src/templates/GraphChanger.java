package templates;

import main.Window;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GraphChanger implements ItemListener {
    private Window window;
    public GraphChanger(Window window) { this.window = window; }

    public void itemStateChanged(ItemEvent e) {
        this.window.changeOrientation();
        this.window.redraw();
    }
}
