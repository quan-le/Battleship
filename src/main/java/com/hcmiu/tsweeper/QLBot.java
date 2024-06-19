package com.hcmiu.tsweeper;

    public class QLBot {
    Minefield mf;
    Cell[][] minefield;
    int[][] matrix;
    //Constructor
    public QLBot(Cell[][] minefield)
    {
        this.minefield = minefield;
    }
    public void mark(int x, int y)
    {
        minefield[x][y].flagged = true;
        //minefield.expose(x,y)
    }
    public void QLAlgo()
    {
        //some algo
    }

    public static double[][] gaussElimination(double[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            // Find the pivot row and swap
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[max][i])) {
                    max = j;
                }
            }
            double[] temp = matrix[i];
            matrix[i] = matrix[max];
            matrix[max] = temp;

            // Make the diagonal element 1
            for (int j = i + 1; j < n; j++) {
                matrix[i][j] /= matrix[i][i];
            }
            matrix[i][i] = 1.0;

            // Eliminate column entries below the pivot
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i];
                for (int k = i; k < n + 1; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
                matrix[j][i] = 0;
            }
        }

        return matrix;
    }

    public void main(String[] args)
    {
        mark(0,0);
    }
}
