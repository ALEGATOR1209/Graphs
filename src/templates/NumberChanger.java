package templates;

import main.Window;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberChanger implements ChangeListener {
    private Window window;
    private int number;
    public NumberChanger (Window window, int number) {
        this.window = window;
        this.number = number;
    }
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (this.window.getNumber(this.number) == source.getValue()) return;
        this.window.setNumber(this.number, source.getValue())
            .redraw();
    }
}
