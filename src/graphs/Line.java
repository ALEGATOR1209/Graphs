package graphs;

import javax.swing.*;
import java.awt.*;

class Line extends JComponent {
    private int startX, startY, endX, endY;
    private boolean arrow;

    Line(int startX, int startY, int endX, int endY, boolean arrow) {
        this.startX = startX; this.startY = startY;
        this.endX = endX; this.endY = endY;
        this.arrow = arrow;
        this.setSize(500, 500);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int startX = this.startX + 25;
        int startY = this.startY + 25;
        int endX = this.endX + 25;
        int endY = this.endY + 25;
        g.drawLine(startX, startY, endX, endY);
        if (!this.arrow) return;
        g.drawOval(endX - 5, endY - 5, 10, 10);
    }
}