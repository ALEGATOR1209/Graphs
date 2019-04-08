package graphics;

import javax.swing.*;
import java.awt.*;

public class Background extends  JComponent {
    private int x = 0, y  = 0, width = 0, height = 0;
    private Color color = Color.black;
    public Background (int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.setSize(1000, 1000);
    }
    public Background (Color color, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        this.setSize(1000, 1000);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.color.getRGB() == Color.black.getRGB()) g.drawRect(this.x, this.y, this.width, this.height);
        else {
            g.setColor(this.color);
            g.fillRect(this.x, this.y, this.width, this.height);
        }
    }
}