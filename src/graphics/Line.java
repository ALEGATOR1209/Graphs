package graphics;

import javax.swing.*;
import java.awt.*;

public class Line extends JComponent {
    private int startX, startY, endX, endY;
    private boolean arrow;
    private Color color;

    public Line(int startX, int startY, int endX, int endY, boolean arrow, Color color) {
        this.startX = startX; this.startY = startY;
        this.endX = endX; this.endY = endY;
        this.arrow = arrow;
        this.setSize(1000, 1000);
        this.color = color;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int startX = this.startX + 25;
        int startY = this.startY + 25;
        int endX = this.endX + 25;
        int endY = this.endY + 25;
        g.setColor(color);
        g.drawLine(startX, startY, endX, endY);

        if (!this.arrow) return;
        int dx = endX - startX;
        int dy = endY - startY;
        double angle = Math.atan(1.0 * dx / dy);
        if (angle != angle) angle = Math.PI / 2;
        double P2 = Math.PI / 2;

        if (dy > 0)
            angle = P2 - angle;
        else if (dy < 0)
            angle = -P2 - angle;
        else if (dx > 0)
            angle = -P2 + angle;
        else
            angle = P2 + angle;

        int[] coordinates = Line.getCoordinates(angle, endX, endY);
        g.drawLine(coordinates[0], coordinates[1], endX, endY);
        g.drawLine(coordinates[2], coordinates[3], endX, endY);
    }

    private static double[] turnPoint(double angle, int pointX, int pointY, int zeroX, int zeroY) {
        double[] coordinates = new double[2];

        pointX -= zeroX; pointY -= zeroY;
        double x = pointX * Math.cos(angle) - pointY * Math.sin(angle);
        double y = pointX * Math.sin(angle) + pointY * Math.cos(angle);
        x += zeroX; y += zeroY;

        coordinates[0] = x; coordinates[1] = y;
        return coordinates;
    }

    private static int[] getCoordinates(double a, int endX, int endY) {
        int length = 10;
        double arrowAngle = Math.PI / 6;

        int leftWindX = endX - (int) Math.floor(length * Math.cos(arrowAngle));
        int rightWindX = leftWindX;
        int leftWindY = endY + (int) Math.floor(length * Math.cos(arrowAngle));
        int rightWindY = endY - (int) Math.floor(length * Math.cos(arrowAngle));

        double[] coordinates = Line.turnPoint(a, leftWindX, leftWindY, endX, endY);
        leftWindX = (int) Math.floor(coordinates[0]);
        leftWindY = (int) Math.floor(coordinates[1]);

        coordinates = Line.turnPoint(a, rightWindX, rightWindY, endX, endY);
        rightWindX = (int) Math.floor(coordinates[0]);
        rightWindY = (int) Math.floor(coordinates[1]);

        int[] res = {leftWindX, leftWindY, rightWindX, rightWindY};
        return res;
    }
}
