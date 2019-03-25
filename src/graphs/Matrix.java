package graphs;

public class Matrix {
    public static int[][] multiplyMatrixes(int[][] matrix1, int[][] matrix2) {
        int m = matrix1.length;
        int n = matrix2[0].length;
        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) {
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
}
