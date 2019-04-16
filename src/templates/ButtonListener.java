package templates;

import main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private int[][] matrix;
    private boolean oriented = true;
    private Window window;
    private SearchWindow searchWindow;
    public ButtonListener(int[][] matrix, boolean oriented) {
        this.oriented = oriented;
        this.matrix = matrix;
    }
    public ButtonListener(Window window) {
        this.window = window;
    }
    public ButtonListener(SearchWindow window) {
        this.searchWindow = window;
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
        if ("Search".equals(e.getActionCommand())) {
            SearchWindow window = new SearchWindow(this.matrix, oriented);
            window.setVisible(true);
        }
        if ("Width".equals(e.getActionCommand())) {
            this.searchWindow
                .setSearchType(false)
                .redraw();
        }
        if ("Height".equals(e.getActionCommand())) {
            this.searchWindow
                .setSearchType(true)
                .redraw();
        }
        if ("Start Search".equals(e.getActionCommand())) {
            this.searchWindow
                .startSearch()
                .redraw();
        }
        if ("Next Vortex".equals(e.getActionCommand())) {
            this.searchWindow
                .exploreNext()
                .redraw();
        }
        if ("New Search".equals(e.getActionCommand())) {
            this.searchWindow
                .newSearch()
                .redraw();
        }
    }
}
