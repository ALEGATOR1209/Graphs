package graphics;

import javax.swing.*;
import java.awt.*;

public class Background extends  JComponent {
    private int x, y, width, height;
    private Color color = Color.black;
    public Background (int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.setSize(width, height);
    }
    public Background (Color color, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        this.setSize(width, height);
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