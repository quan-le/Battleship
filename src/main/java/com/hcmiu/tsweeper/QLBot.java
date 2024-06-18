package com.hcmiu.tsweeper;

    public class QLBot {
    Minefield mf;
    Cell[][] minefield;
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
    public void main(String[] args)
    {
        mark(0,0);
    }
}
