package com.hcmiu.tsweeper;

public class Minefield {

    // Minefield dimensions
    int gridNormalHeight = 10;
    int gridNormalWidth = 10;

    //Define board length
    int minefieldWidth  = 10;                               // Control Number of Cell X
    int minefieldHeight = 10;                               // Control Number of Cell Y

    // grid amount of mines
    int gridNumMines = 50;                                  // Number Mines    old:gridNormalNumMines

    int numMinesatStart;                                    // Number mines at start
    int numMinesLeft   ;                                    // number mines left
    int cellsUncovered = 0;                                 // Number of cell clicked, = 0 at start
    boolean exploded;                                       // Keep track of losing the game
    Cell[][] minefield;                                     // DSA Array 2d
    int totalCells=0;                                       // Keep track of number of cell created
    int exposedCells=0;                                     // Keep track of number of cell clicked/exposed

    //constructor minefield
    public Minefield(){
        numMinesLeft = 0;
        numMinesatStart = 0;
        cellsUncovered =0;
        Cell[][] minefield = new Cell[10][10];                                     // DSA Array 2d
        exploded = false;
    }

    // Generate MineField Cell[][]
    public void makeMinefield()
    {
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

    // Exposed/ Left click Cell
    public int expose(int x, int y)
    {
        Cell Cell = minefield[x][y];                        // Get Cell variable in index x,y
        if (minefield[x][y].mined)                          // Case 1: Click in mine cell
        {
            exploded = true;                                //
            Cell.button.setText("!");
            return -1;                                      // will return -1 if mine exploded
        }
        if (minefield[x][y].exposed == false)               // Case 2: Click in non-mine, non-exposed cell
        {
            Cell.button.setText("");
            exposedCells++;
            return neighborsMined(x,y);                     // will return 0-8
        }
        return 0;
    }

    // Return # of a neighbor mined around the clicked Cell
    public int neighborsMined(int x, int y){
        int countToShow =0;
        int newXL;                                                      // looking at +/- 1 row
        int newYL;                                                      // looking +/- 1 column
        int newXH;
        int newYH;
                                                                        // EDGE case compares
        if ( x-1 <minefieldWidth)                                       // wall to left
        {
            newXL = x;
        }
        else
        {
            newXL = x-1;                                                // XL = left cell
        }
        if (x+1 >minefieldWidth)                                        // wall to right
        {
            newXH = x;
        }
        else
        {
            newXH = x+1;                                                // XH  = right cell
        }
        if (y-1 <minefieldHeight)                                       // wall below
        {
            newYL = y;
        }
        else
        {
            newYL = y-1;                                                // YL = below cell
        }
        if (y+1 >minefieldHeight)                                       // wall above
        {
            newYH = y;
        }
        else
        {
            newYH = y+1;                                                // YH = Above cell
        }
        // Sumup, we have x,y, left x, right x, down y, up y to get the index of all 8 cells surrounding the chosen cell
        for (int i=newXL; i <= newXH ; i++   )                          //transverse from x left -> x -> x right
        {
            for ( int j=newYL ; j<= newYH ; j++)                        //transverse from y low -> y -> y up
            {
                if ((i == x) && (j == y)){ continue; }                  // skip self
                if (minefield[i][j].mined == true){ countToShow++; }    // add to # mines nearby
                if (minefield[i][j].mined == false){ expose(i,j);  }    // expose Cell if .mined = false
            }
        }
        cellsUncovered++;
        return countToShow;
    }

    // set flag or unflag or mark for right-click action cell to note
    boolean mark(Cell cell) {
        if (cell.flagged != true) {
            cell.flagged = true;
            numMinesLeft--;}
        else { cell.flagged = false; }
        return cell.flagged;
    }

    // random() method returns a random number between 0.0 and 0.999. then multiply it by 10,
    // so upper limit becomes 0.0 to 9.95, when you add 1, it becomes 1.0 to 10.95, Truncates to 10
    void addMines(){
        for (int i = 0; i < numMinesLeft; ++i)
        {
            int randomX = (int )(Math.random() * minefieldWidth );
            int randomY = (int )(Math.random() * minefieldHeight );
            if (minefield[randomX][randomY].mined == true ){ i--; }  // try again
            if (minefield[randomX][randomY].mined == false) {        // set mine
                minefield[randomX][randomY].mined = true;   }
        }
    }

    public int getNumMinesLeft()
    { return numMinesLeft; }                                        // total cells at start = difficulty level

}







