package templates;

import main.WaysWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderListener implements ChangeListener {
    private WaysWindow window;
    private String type;
    public SliderListener (WaysWindow window, String type) {
        this.window = window;
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
    }
    private void vortexHandler(int value) {
        int oldValue = this.window.getVortex();
        if (oldValue == value) return;
        this.window
            .setVortex(value)
            .redraw();
    }
    private void lengthHandler(int value) {
        int oldValue = this.window.getLength();
        if (oldValue == value) return;
        this.window
            .setLength(value)
            .redraw();
    }
}
