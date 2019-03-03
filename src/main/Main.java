package main;

import graphs.Graph;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[][] matrix = Main.generateMatrix(8, 4, 1, 0, false);
        int[][] matrixS = Main.generateMatrix(8, 4, 1, 0, true);
        Window window = new Window("Graphs");
        Graph
            .fromMatrix(matrix, true)
            .circle(425, 205, 200)
            .draw(window);
        window.setVisible(true);
    }
    static int[][] generateMatrix(int n1, int n2, int n3, int n4, boolean symetric) {
        int n = 10 + n3;
        int[][] matrix = new int[n][n];
        Random random = new Random(n1  * 1000 + n2 * 100 + n3 * 10 + n4);

        if (symetric)
            for (int i = n - 1; i >= 0; i--) {
                for (int j = i; j >= 0; j--) {
                    double T = random.nextDouble() + random.nextDouble();
                    double element = T * (1 - n3 * 0.005 - n4 * 0.005 - 0.25);
                    matrix[i][j] = (int) Math.floor(element);
                    matrix[j][i] = matrix[i][j];
                }
            }
        else for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double T = random.nextDouble() + random.nextDouble();
                double element = T * (1 - n3 * 0.005 - n4 * 0.005 - 0.25);
                matrix[i][j] = (int) Math.floor(element);
            }
        }
        return matrix;
    }
}
