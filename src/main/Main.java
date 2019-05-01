package main;

import graphs.Matrix;

public class Main {
    public static void main(String[] args) {
        int[][] matrix = Matrix.generateMatrix(8, 4, 1, 0, true);
        new SpanningWindow(matrix)
            .setVisible(true);
//        new Window("Graphs")
//            .setVisible(true);
    }
}
