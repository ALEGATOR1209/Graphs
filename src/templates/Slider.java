package templates;

import javax.swing.*;

public class Slider extends JSlider {
    public Slider(int x, int y, int val) {
        super(0, 9, 1);
        this.setLocation(x, y);
        this.setSize(200, 50);
        this.setValue(val);
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.setMajorTickSpacing(1);
    }
    public Slider(int x, int y, int val, int max) {
        super(0, max, 1);
        this.setLocation(x, y);
        this.setSize(200, 50);
        this.setValue(val);
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.setMajorTickSpacing(1);
    }
}
