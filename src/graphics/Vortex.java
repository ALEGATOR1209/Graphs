package graphics;

import javax.swing.*;
import java.awt.*;

public class Vortex extends JComponent {
    private int x, y, size;
    private char[] value;
    private boolean isolated, leave;
    private Color color;

    public Vortex(int x, int y, int size, char[] value, boolean isolated, boolean leave, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = size;
        this.value = value;
        this.setSize(x * size, y * size);
        this.isolated = isolated;
        this.leave = leave;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);
        int k1 = 5;
        int k2 = 2;
        if (this.color.getRGB() != Color.black.getRGB()) {
            k1 = 10;
            k2 = 5;
            g.setFont(new Font("Arial", Font.BOLD, 16));
        }
        g.fillOval(this.x - k2, this.y - k2, this.size + k1, this.size + k1);
        g.setColor(Color.white);
        if (this.color.getRGB() == Color.black.getRGB()){
            if (this.leave) g.setColor(Color.green);
            if (this.isolated) g.setColor(Color.red);
        }
        g.fillOval(this.x, this.y, this.size, this.size);
        char[] value = this.value;
        int x = -5 + this.x + this.size / 2;
        int y = 5 + this.y + this.size / 2;
        g.setColor(Color.black);
        g.drawChars(value, 0, value.length, x, y);
    }
}
