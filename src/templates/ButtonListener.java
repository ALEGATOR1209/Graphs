package templates;

import main.WaysWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private int[][] matrix;
    private boolean oriented;
    public ButtonListener(int[][] matrix, boolean oriented) {
        this.oriented = oriented;
        this.matrix = matrix;
    }
    public void actionPerformed(ActionEvent e) {
        if ("Show Ways Window".equals(e.getActionCommand())) {
            WaysWindow window = new WaysWindow(this.matrix, this.oriented);
            window.setVisible(true);
        }
    }
}
