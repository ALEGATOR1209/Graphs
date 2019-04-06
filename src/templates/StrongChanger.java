package templates;

import main.Window;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class StrongChanger implements ItemListener {
    private Window window;
    public StrongChanger(Window window) { this.window = window; }

    public void itemStateChanged(ItemEvent e) {
        this.window.changeStrong();
        this.window.redraw();
    }
}
