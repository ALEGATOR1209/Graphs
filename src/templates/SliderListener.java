package templates;

import main.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderListener implements ChangeListener {
    private WaysWindow waysWindow;
    private Window window;
    private String type;
    public SliderListener (Window window, String type) {
        this.window = window;
        this.type = type;
    }
    public SliderListener (WaysWindow window, String type) {
        this.waysWindow = window;
        this.type = type;
    }
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        int value = source.getValue();
        if (this.type.equals("vortex")) {
            this.vortexHandler(value);
            return;
        }
        if (this.type.equals("length")) {
            this.lengthHandler(value);
        }
        if (this.type.equals("Matrix")) {
            this.matrixHandler(value);
        }
    }
    private void vortexHandler(int value) {
        int oldValue = this.waysWindow.getVortex();
        if (oldValue == value) return;
        this.waysWindow
            .setVortex(value)
            .redraw();
    }
    private void lengthHandler(int value) {
        int oldValue = this.waysWindow.getLength();
        if (oldValue == value) return;
        this.waysWindow
            .setLength(value)
            .redraw();
    }
    private void matrixHandler(int value) {
        this.window
            .setMatrix(value)
            .redraw();
    }
}
