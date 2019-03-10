package main;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int n1 = 8,
            n2 = 4,
            n3 = 1,
            n4 = 0;

        int[][] matrixO = Main.generateMatrix(n1, n2, n3, n4, false);
        int[][] matrixS = Main.generateMatrix(n1, n2, n3, n4, true);
        new Window("Graphs")
            .drawGraph(matrixS, false)
            .drawGraph(matrixO, true)
            .setVisible(true);
    }
    private static int[][] generateMatrix(int n1, int n2, int n3, int n4, boolean symetric) {
        int n = 10 + n3;
        int[][] matrix = new int[n][n];
        Random random = new Random(n1  * 1000 + n2 * 100 + n3 * 10 + n4);

        if (symetric)
            for (int i = n - 1; i >= 0; i--) {
                for (int j = i; j >= 0; j--) {
                    double element = Main.getRandomElement(random, n3, n4);
                    matrix[i][j] = (int) Math.floor(element);
                    matrix[j][i] = matrix[i][j];
                }
            }
        else for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double element = Main.getRandomElement(random, n3, n4);
                matrix[i][j] = (int) Math.floor(element);
            }
        }
        return matrix;
    }
    private static double getRandomElement(Random random, int n3, int n4) {
        return (random.nextDouble() + random.nextDouble()) * (1 - n3 * 0.005 - n4 * 0.005 - 0.25);
    }
}
