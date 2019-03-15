package graphics;

import javax.swing.*;
import java.awt.*;

public class Vortex extends JComponent {
    private int x, y, size;
    private char[] value;
    private boolean isolated, leave;

    public Vortex(int x, int y, int size, int value, boolean isolated, boolean leave) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.value = String.valueOf(value).toCharArray();
        this.setSize(1000, 1000);
        this.isolated = isolated;
        this.leave = leave;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillOval(this.x - 1, this.y - 1, this.size + 2, this.size + 2);
        g.setColor(Color.white);
        if (this.leave) g.setColor(Color.green);
        if (this.isolated) g.setColor(Color.red);
        g.fillOval(this.x, this.y, this.size, this.size);
        char[] value = this.value;
        int x = -5 + this.x + this.size / 2;
        int y = 5 + this.y + this.size / 2;
        g.setColor(Color.black);
        g.drawChars(value, 0, value.length, x, y);
    }
}
