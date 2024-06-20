package com.hcmiu.tsweeper;

    public class QLBot {
    Minefield mf;
    Cell[][] minefield;
    int[][] matrix;
    //Constructor
    public QLBot(Minefield mf)
    {
        this.mf = mf;
        minefield = mf.getMinefield();
    }
    public void expose(int x, int y)
    {
        mf.expose(x,y);
    }
    public void mark(int x, int y)
    {
        mf.mark(mf.minefield[x][y]);
    }
    public void QLAlgo()
    {
        //some algo
        expose(9,4);
        mark(9,5);
    }
    public void assignProbabilityToCell()
    {
        for(int x  = 0; x < 10; x++)
        {
            for(int y = 0; y < 10; y++)
            {

            }
        }
    }
    public static int[][] gaussElimination(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            // Find the pivot row and swap
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[max][i])) {
                    max = j;
                }
            }
            int[] temp = matrix[i];
            matrix[i] = matrix[max];
            matrix[max] = temp;

            // Make the diagonal element 1
            for (int j = i + 1; j < n; j++) {
                matrix[i][j] /= matrix[i][i];
            }
            matrix[i][i] = 1;

            // Eliminate column entries below the pivot
            for (int j = i + 1; j < n; j++) {
                int factor = matrix[j][i];
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
        expose(0,0);
    }
}
