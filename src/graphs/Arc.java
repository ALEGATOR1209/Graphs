package graphs;

import javax.swing.*;
import java.awt.*;

class Arc extends JComponent {
    private int startX, startY;
    private int startAngle, arcAngle;

    Arc(int startX, int startY) {
        if (startX <= 250) {
            this.startX = startX - 25;
            if (startY <= 250) {
                this.startAngle = -90;
                this.arcAngle = -270;
                this.startY = startY - 25;
            } else {
                this.startAngle = 90;
                this.arcAngle = 270;
                this.startY = startY + 25;
            }
        } else {
            this.startX = startX + 25;
            if (startY <= 250) {
                this.startY = startY - 25;
                this.startAngle = -90;
                this.arcAngle = 270;
            } else {
                this.startY = startY + 25;
                this.startAngle = 90;
                this.arcAngle = -270;
            }
        }
        this.setSize(500, 500);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = 50;
        int height = 50;
        g.drawArc(this.startX, this.startY, width, height, this.startAngle, this.arcAngle);
    }
}
