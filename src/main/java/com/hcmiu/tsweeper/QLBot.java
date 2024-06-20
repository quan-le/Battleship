package com.hcmiu.tsweeper;
import java.util.*;

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
        expose(9,5);
        expose(0,4);
        for(int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                if(minefield[x][y].exposed == false && mf.neighborsMined(x,y) > 0)
                {
                    assignProbabilityToCell(x,y);
                    System.out.println( x + "," + y + "," + mf.minefield[x][y].probability);
                }
            }
        }
    }
    public void assignProbabilityToCell(int x,int y)                                           //the smaller the probability, the higher the chance that's a bomb
    {
        double surround = mf.getSurroundedUnexposedCell(x ,y);
        //System.out.println(String.valueOf(surround));
        mf.minefield[x][y].probability = 1 / surround;
        //System.out.println(mf.minefield[0][5].probability);
    }
    public void updateProbability()
    {
        for(int x = 0; x < 10; x++)
        {
            for(int y = 0; y < 10; y++)
            {
                if(!mf.minefield[x][y].exposed && mf.minefield[x][y].button.getText() != "\uD83D\uDC80")
                {
                    //if(!mf.minefield[x][y].exposed && !mf.minefield[x][y].mined)
                    if(mf.neighborsMined(x,y) > 0 || mf.minefield[x][y].mined)
                    {
                        mf.minefield[x][y].button.textProperty().set(String.valueOf(mf.minefield[x][y].probability) +"%");
                        mf.minefield[x][y].button.setStyle("-fx-border-color: #FF0000");
                    }
                }
            }
        }
    }
    public String smallestProbability()
    {
        double smallestProbability = mf.getSmallestProbability();
        String lowProbCell = "";
        for(int x = 0; x < 10; x++)
        {
            for (int y = 0; y < 10; y++)
            {
                if (mf.minefield[x][y].probability == smallestProbability)
                {
                    lowProbCell += x + "," + y + "; ";
                }
            }
        }
        return lowProbCell;
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
            for (int j = i + 1; j < n; j++)
            {
                int factor = matrix[j][i];
                for (int k = i; k < n + 1; k++)
                {
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
