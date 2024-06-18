package com.hcmiu.tsweeper;
import javafx.scene.control.*;
import javafx.application.Platform;

public class Minefield {

    // Minefield dimensions
    final int gridHeight = 10;                              // Control Number of Cell X
    final int gridWidth = 10;                               // Control Number of Cell Y

    // grid amount of mines
    int gridNumMines = 10;                                  // Number Mines

    int minefieldWidth  = gridHeight;
    int minefieldHeight = gridWidth;
    int numMinesatStart;                                    // Number mines at start
    int numMinesLeft   ;                                    // number mines left
    int cellsUncovered = 0;                                 // Number of cell clicked, = 0 at start
    boolean exploded;                                       // Keep track of losing the game
    Cell[][] minefield;                                     // DSA Array 2d
    int totalCells=0;                                       // Keep track of number of cell created
    int exposedCells=0;                                     // Keep track of number of cell clicked/exposed

    //constructor minefield
    public Minefield(){
        numMinesLeft = gridNumMines;
        numMinesatStart = gridNumMines;
        cellsUncovered =0;
        Cell[][] minefield = new Cell[numMinesLeft][numMinesLeft];                      // DSA Array 2d
        exploded = false;
    }

    // Generate MineField Cell[][]
    public void makeMinefield()
    {
        //Define board length

        minefield = new Cell[minefieldWidth][minefieldHeight];      //2D Array Cell[][]
        System.out.println("generating 'MineField' ");
        //Use for loop to create cell in minefield
        for (int i = 0; i < minefieldWidth; i++) {
            for (int j = 0; j < minefieldHeight ; j++ ){
                Cell Cell = new Cell();                             // make Cell
                minefield[i][j] = Cell;                             // newCellArray[x][y] is one cell
                minefield[i][j].mined = false;                      // default no mine
                minefield[i][j].flagged = false;                    // default isn't clicked
                minefield[i][j].button =null;                       //
                minefield[i][j].x = i;                              // x index in Cell[][] minefield
                minefield[i][j].y = j;                              // y index in Cell[][] minefield
                totalCells++;                                       // number of cell++
                System.out.println("Cell X = "+ minefield[i][j].x +": Y = "+ minefield[i][j].y
                        +": Mined = "+ minefield[i][j].mined +": flagged : "+ minefield[i][j].flagged);
            }
        }
    }

    public void printMinefield()
    {
        for (int i = 0; i < minefieldWidth; i++) {
            for (int j = 0; j < minefieldHeight; j++) {
                System.out.println("Cell X = " + minefield[i][j].x + ": Y = " + minefield[i][j].y
                        + ": Mined = " + minefield[i][j].mined + ": flagged : " + minefield[i][j].flagged);
            }
        }
    }


    //Check if cell is exposed/clicked or not
    public int isExposed(int column, int row)
    {
        if (minefield[column][row].exposed){
            return 1;
        }
        return 0;
    }

    //return MineField Cell[][]
    public Cell[][] getMinefield()
    { return minefield; }

    // Return # of mine left
    public int unexposedMines()
    { return numMinesatStart - numMinesLeft; }

    // Return # of mine unclicked
    public int unexposedCount()
    { return totalCells - exposedCells -numMinesLeft; }

    public int expose(int x, int y) {
        Cell cell = minefield[x][y];
        if (minefield[x][y].mined) {
            exploded = true;
            cell.button.setText("!");
            return -1;
        }
        if (!minefield[x][y].exposed) {
            cell.exposed = true;
            exposedCells++;
            int minesAround = neighborsMined(x, y);
            if (minesAround == 0) {
                cell.button.setText("#");  // Leave # if no adjacent mines
                checkSurroundingMines(x, y);
            } else {
                cell.button.setText(String.valueOf(minesAround));
            }
            return minesAround;
        }
        return 0;
    }

    public void checkSurroundingMines(int x, int y) {
        int[] dx = {-1,  0, 0, 1};                                  //XDirection
        int[] dy = { 0, -1, 1, 0};                                  //YDirection, they go pair together (up, down, left, right)
        for (int i = 0; i < 4; i++) {                               //iterate thru each neighbors cells
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && ny >= 0 && nx < minefieldWidth && ny < minefieldHeight && !minefield[nx][ny].exposed) {
                expose(nx, ny);
            }
        }
    }

    public int neighborsMined(int x, int y) {
        int countToShow = 0;
        int newXL = Math.max(0, x - 1);
        int newXH = Math.min(minefieldWidth - 1, x + 1);
        int newYL = Math.max(0, y - 1);
        int newYH = Math.min(minefieldHeight - 1, y + 1);

        for (int i = newXL; i <= newXH; i++) {
            for (int j = newYL; j <= newYH; j++) {
                if (i == x && j == y) continue;
                if (minefield[i][j].mined) countToShow++;
            }
        }
        return countToShow;
    }

    // set flag or unflag or mark for right-click action cell to note, only when the flag are put correctly, numMineleft--
    boolean mark(Cell cell) {
        if (!cell.flagged && numMinesLeft > 0) {
            cell.flagged = true;
            cell.button.setText("\uD83D\uDC80");

            if (cell.mined) {
                numMinesLeft--;
                updateMineLeftDisplay();
            }
        } else {
            cell.flagged = false;
            cell.button.setText("");

            if (cell.mined) {
                numMinesLeft++;
                updateMineLeftDisplay();
            }
        }
        return cell.flagged;
    }

    // Method to update the mine left display
    void updateMineLeftDisplay() {
        Platform.runLater(() -> {
            MainController.updateMineLeftDisplay(numMinesLeft);
        });
    }

    // random() method returns a random number between 0.0 and 0.999. then multiply it by 10,
    // so upper limit becomes 0.0 to 9.95, when you add 1, it becomes 1.0 to 10.95, Truncates to 10
    void addMines() {
        int minesAdded = 0;
        while (minesAdded < numMinesatStart) {
            int randomX = (int) (Math.random() * minefieldWidth);
            int randomY = (int) (Math.random() * minefieldHeight);
            if (!minefield[randomX][randomY].mined) { //Passion
                minefield[randomX][randomY].mined = true;
                minesAdded++;
            }
        }
    }

    public int getNumMinesLeft()
    { return numMinesLeft; }                                        // total cells at start = difficulty level
    //add button into minefield
    public void addButtonToCell(int x, int y, Button bt)
    {
        minefield[x][y].button = bt;
    }
}







