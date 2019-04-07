package templates;

import main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private int[][] matrix;
    private boolean oriented;
    private Window window;
    public ButtonListener(int[][] matrix, boolean oriented) {
        this.oriented = oriented;
        this.matrix = matrix;
    }
    public ButtonListener(Window window) {
        this.window = window;
    }
    public void actionPerformed(ActionEvent e) {
        if ("Show Ways Window".equals(e.getActionCommand())) {
            WaysWindow window = new WaysWindow(this.matrix, this.oriented);
            window.setVisible(true);
        }
        if ("Condensation".equals(e.getActionCommand())) {
            this.window.changeCondensation();
            this.window.redraw();
        }
    }
}
