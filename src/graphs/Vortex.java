package graphs;

import javax.swing.*;
import java.awt.*;

public class Vortex extends JComponent {
    private int x, y, size;
    private char[] value;

    Vortex(int x, int y, int size, int value) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.value = String.valueOf(value).toCharArray();
        this.setSize(1000, 1000);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(this.x, this.y, this.size, this.size);
        char[] value = this.value;
        int x = -5 + this.x + this.size / 2;
        int y = 5 + this.y + this.size / 2;
        g.drawChars(value, 0, value.length, x, y);
    }
}
