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
    double probability;

    public Cell()
    {
        x =0;
        y =0;
        button = null;
        flagged = false;
        mined = false;
        exposed = false;
        nearMines = 0;
        probability = 0.0;
    }


    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getNearMines() {
        return nearMines;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
