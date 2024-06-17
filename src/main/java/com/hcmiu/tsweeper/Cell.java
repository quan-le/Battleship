package com.hcmiu.tsweeper;
import javafx.scene.control.Button;

public class Cell {
    int x =0;                                               // Keep track of X index
    int y =0;                                               // Keep track of Y index
    Button button = new Button();                           // Each Cell represented by a Button
    boolean flagged = false;                                // For checking if cell is clicked
    boolean mined = false;                                  // For checking if cell has mine
    boolean exposed = false;                                // For checking if cell is showed
    int nearMines = 0;                                      // int: check # surrounding mines

    public Cell()
    {
        x =0;
        y =0;
        button = null;
        flagged = false;
        mined = false;
        exposed = false;
        nearMines = 0;
    }

}
