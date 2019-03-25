package templates;

import javax.swing.*;

public class Slider extends JSlider {
    public Slider(int x, int y, int val) {
        super(0, 9, val);
        this.init(x, y);
    }
    public Slider(int x, int y, int val, int max) {
        super(0, max, val);
        this.init(x, y);
    }
    public Slider(int x, int y, int val, int min, int max) {
        super(min, max, val);
        this.init(x, y);
    }
    private void init(int x, int y) {
        this.setLocation(x, y);
        this.setSize(200, 50);
        this.setPaintTicks(true);
        this.setPaintLabels(true);
        this.setMajorTickSpacing(1);
    }
}
