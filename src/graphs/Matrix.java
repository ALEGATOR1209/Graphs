package graphs;

import java.util.HashMap;
import java.util.Random;

public class Matrix {
    public static int[][] multiplyMatrixes(int[][] matrix1, int[][] matrix2) {
        int m = matrix1.length;
        int n = matrix2[0].length;
        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                int[] a = matrix1[i];
                int[] b = Matrix.getColumnVector(matrix2, j);
                result[i][j] = Matrix.scalarMultiplication(a, b);
            }

        return result;
    }

    private static int scalarMultiplication(int[] a, int[] b) {
        int res = 0;
        for (int i = 0; i < a.length; i++) {
            res += a[i] * b[i];
        }
        return res;
    }

    private static int[] getColumnVector(int[][] matrix, int n) {
        int[] column = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            column[i] = matrix[i][n];
        return column;
    }

    public static int[][] generateMatrix(int n1, int n2, int n3, int n4, boolean symetric) {
        int n = 10 + n3;
        int[][] matrix = new int[n][n];
        Random random = new Random(n1 * 1000 + n2 * 100 + n3 * 10 + n4);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double element = Matrix.getRandomElement(random, n3, n4);
                matrix[i][j] = (int) Math.floor(element);
            }
        }

        if (symetric) {
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (matrix[i][j] == 1) matrix[j][i] = 1;
                    else if (matrix[j][i] == 1) matrix[i][j] = 1;
        }
        return matrix;
    }

    private static double getRandomElement(Random random, int n3, int n4) {
        return (random.nextDouble() + random.nextDouble()) * (1 - n3 * 0.01 - n4 * 0.01 - 0.3);
    }

    public static HashMap<Number, int[]> findWays(int[][] matrix, int vortex, int length) {
        HashMap<Number, int[]> ways = new HashMap<>();
        int[] line = matrix[vortex];
        if (length == 1) {
            for (int i = 0; i < line.length; i++) {
                if (line[i] == 1) {
                    int[] way = {vortex, i};
                    ways.put(ways.size(), way);
                }
            }
            return ways;
        }

        for (int i = 0; i < line.length; i++) {
            if (line[i] == 1) {
                HashMap<Number, int[]> anotherWays = Matrix.findWays(matrix, i, length - 1);
                for (Number key : anotherWays.keySet()) {
                    int[] array = anotherWays.get(key);
                    int[] patched = new int[array.length + 1];
                    patched[0] = vortex;
                    System.arraycopy(array, 0, patched, 1, array.length);
                    ways.put(ways.size(), patched);
                }
            }
        }

        return ways;
    }
    public static String countConnections(int[][] matrix, int node, boolean oriented) {
        String connections = "";

        if (!oriented) {
            int counter = 0;
            int[] line = matrix[node];
            for (int i = 0; i < line.length; i++)
                if (line[i] == 1) {
                    counter++;
                    if (i == node) counter++;
                }
            connections += counter + "";
        } else {
            int plus = 0, minus = 0;
            int[] line = matrix[node];
            for (int i : line) if (i == 1) minus++;
            for (int[] i : matrix) if (i[node] == 1) plus++;
            connections += "+" + plus + " -" + minus;
        }
        return connections;
    }
}