package templates;

import main.Window;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MatrixChanger implements ChangeListener {
    private Window window;
    public MatrixChanger (Window window) {
        this.window = window;
    }
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        this.window.setMatrix(source.getValue())
            .redraw();
    }
}

