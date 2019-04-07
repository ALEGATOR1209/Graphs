package graphs;

import java.util.HashMap;
import java.util.Random;

public class Matrix {
    private static int[][] multiplyMatrixes(int[][] matrix1, int[][] matrix2) {
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
        if (length == 0) return ways;
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
    public static int[][] power(int[][] matrix, int power) {
        if (matrix.length != matrix[0].length) throw new Error("Matrix is not square");
        if (power == 0) {
            return Matrix.getAttainabilityMatrix(matrix);
        }
        int[][] resMatrix = matrix.clone();
        for (int i = 1; i < power; i++) resMatrix = Matrix.multiplyMatrixes(resMatrix, matrix);
        return resMatrix;
    }
    private static int[][] getAttainabilityMatrix(int[][] matrix) {
        int[][] reachability = matrix.clone();
        int n = matrix.length;
        for (int i = 1; i < n; i++) {
            int[][] powered = Matrix.power(matrix.clone(), i);
            reachability = Matrix.unite(reachability, powered);
        }
        return reachability;
    }
    private static int[][] and(int[][] matrix1, int[][] matrix2) {
        int n = matrix1.length;
        int m = matrix1[0].length;
        int[][] united = new int[matrix1.length][matrix2.length];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) {
            if (matrix1[i][j] > 0 && matrix2[i][j] > 0) {
                united[i][j] = 1;
            } else united[i][j] = 0;
        }
        return united;
    }
    private static int[][] unite(int[][] matrix1, int[][] matrix2) {
        int n = matrix1.length;
        int m = matrix1[0].length;
        int[][] united = new int[matrix1.length][matrix2.length];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) {
            if (matrix1[i][j] > 0 || matrix2[i][j] > 0) {
                united[i][j] = 1;
            } else united[i][j] = 0;
        }
        return united;
    }
    public static int[][] getStrongMatrix(int[][] matrix) {
        int[][] attainability = Matrix.getAttainabilityMatrix(matrix);
        attainability = Matrix.and(attainability, Matrix.transposition(attainability));
        return Matrix.unite(Matrix.E(matrix.length), attainability);
    }
    private static int[][] transposition(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] transponed = new int[n][m];
        for (int i = 0; i < n; i++) for (int j = 0; j < m; j++) {
            transponed[j][i] = matrix[i][j];
        }
        return transponed;
    }
    private static int[][] E(int n) {
        int[][] E = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            if (i == j) E[i][j] = 1;
            else E[i][j] = 0;
        }
        return E;
    }

    private static int[][] O(int n) {
        int[][] O = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            if (i == j) O[i][j] = 0;
            else O[i][j] = 1;
        }
        return O;
    }
    private static int[][] delete(int[][] matrix, int num) {
        int n = matrix.length;
        int[][] newMatrix = new int[n - 1][n - 1];
        int lastI = 0;
        for (int i = 0; i < n; i++) {
            if (i != num) {
                int lastJ = 0;
                for (int j = 0; j < n; j++) {
                    if (j != num) {
                        newMatrix[lastI][lastJ] = matrix[i][j];
                        lastJ++;
                    }
                }
                lastI++;
            }
        }
        return newMatrix;
    }
    private static int count(int[] arr, int n) {
        int count = 0;
        for (int value : arr) if (value == n) count++;
        return count;
    }
    private static int[] getNums(int[] arr, int n) {
        int[] res = new int[Matrix.count(arr, n)];
        int last = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == n) {
                res[last] = i;
                last++;
            }
        }
        return res;
    }
    private static int[] getKeys(int[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = i;
        }
        return res;
    }
    private static int[] pop(int[] arr, int n) {
        int[] res = new int[arr.length - 1];
        for (int i = 0; i < arr.length; i++) {
            if (i < n) res[i] = arr[i];
            if (i > n) res[i - 1] = arr[i];
        }
        return res;
    }
    static HashMap<Number, int[]> getStrongComponents(int[][] matrix) {
        HashMap<Number, int[]> strongComponents = new HashMap<>();
        int[][] strongMatrix = Matrix.getStrongMatrix(matrix);
        int[] live = Matrix.getKeys(matrix[0]);
        while(strongMatrix.length > 0) {
            int[] currentStrong = Matrix.getNums(strongMatrix[0], 1);
            int[] names = new int[currentStrong.length];
            int removed = 0;
            for (int i = 0; i < names.length; i++) {
                names[i] = live[currentStrong[i] - removed];
                live = Matrix.pop(live, currentStrong[i] - removed);
                strongMatrix = Matrix.delete(strongMatrix, currentStrong[i] - removed);
                removed++;
            }
            strongComponents.put(strongComponents.size(), names);
        }
        return strongComponents;
    }
    private static boolean checkConnection(int[][] matrix, int[] component1, int[] component2) {
        for (int a : component1) for (int b : component2)
            if (matrix[a][b] == 1) return true;
        return false;
    }
    public static int[][] condensateMatrix(int[][] matrix) {
        HashMap<Number, int[]> strong = Matrix.getStrongComponents(matrix);
        int[][] condensated = new int[strong.size()][strong.size()];
        for (Number key : strong.keySet()) {
            int[] component = strong.get(key);
            for (Number otherKey : strong.keySet()) {
                int[] otherComponent = strong.get(otherKey);
                if (Matrix.checkConnection(matrix, component, otherComponent))
                    condensated[key.intValue()][otherKey.intValue()] = 1;
                else condensated[key.intValue()][otherKey.intValue()] = 0;
            }
        }
        return Matrix.and(condensated, Matrix.O(condensated.length));
    }
}