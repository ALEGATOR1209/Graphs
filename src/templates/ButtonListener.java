package templates;

import main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private int[][] matrix;
    private boolean oriented = true;
    private Window window;
    private SearchWindow searchWindow;
    private SpanningWindow spanningWindow;
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
    public ButtonListener(SpanningWindow window) {
        this.spanningWindow = window;
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
        if ("Span".equals(e.getActionCommand())) {
            SpanningWindow window = new SpanningWindow(this.matrix);
            window.setVisible(true);
        }
        if ("Kruskal".equals(e.getActionCommand())) {
            this.spanningWindow
                .setAlgorithm(true)
                .redraw();
        }
        if ("Prim".equals(e.getActionCommand())) {
            this.spanningWindow
                .setAlgorithm(false)
                .redraw();
        }
        if ("StartSpan".equals(e.getActionCommand())) {
            this.spanningWindow
                .start()
                .redraw();
        }
        if ("NextEdge".equals(e.getActionCommand())) {
            this.spanningWindow
                .processNext()
                .redraw();
        }
    }
}
