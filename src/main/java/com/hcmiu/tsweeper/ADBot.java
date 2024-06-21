package com.hcmiu.tsweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ADBot {
    Minefield mf;
    Cell[][] minefield;
    int width;
    int height;

    // Constructor
    public ADBot(Minefield mf) {
        this.mf = mf;
        this.minefield = mf.getMinefield();
        this.width = minefield.length;
        this.height = minefield[0].length;
    }

    public void expose(int x, int y) {
        mf.expose(x, y);
    }

    public void mark(int x, int y) {
        mf.mark(minefield[x][y]);
    }

    public void ADAlgo() {
        boolean changeMade;
        do {
            changeMade = false;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell cell = minefield[x][y];
                    if (cell.isExposed()) {
                        int adjMines = cell.getNearMines();
                        List<Cell> adjCells = getAdjCells(x, y);
                        int markedMines = countMarked(adjCells);
                        int hiddenCells = countHidden(adjCells);

                        // If the number of marked cells equals the number on the cell, expose the rest
                        if (markedMines == adjMines) {
                            for (Cell adj : adjCells) {
                                if (!adj.isExposed() && !adj.isMined()) {
                                    expose(adj.getX(), adj.getY());
                                    changeMade = true;
                                }
                            }
                        }

                        // If the number of marked cells plus hidden cells equals the number, mark the hidden cells
                        if (markedMines + hiddenCells == adjMines) {
                            for (Cell adj : adjCells) {
                                if (!adj.isExposed() && !adj.isMined()) {
                                    mark(adj.getX(), adj.getY());
                                    changeMade = true;
                                }
                            }
                        }

                        // Recognize advanced patterns
                        if (!changeMade) {
                            changeMade = recognizePatterns(cell, adjCells, adjMines, markedMines, hiddenCells);
                        }
                    }
                }
            }

            // Apply probabilistic reasoning if no logical move is possible
            if (!changeMade) {
                Cell safestCell = getSafestCell();
                if (safestCell != null) {
                    expose(safestCell.getX(), safestCell.getY());
                    changeMade = true;
                }
            }
        } while (changeMade);
    }

    private List<Cell> getAdjCells(int x, int y) {
        List<Cell> adj = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    adj.add(minefield[nx][ny]);
                }
            }
        }
        return adj;
    }

    private int countMarked(List<Cell> cells) {
        int count = 0;
        for (Cell cell : cells) {
            if (cell.isMined()) {
                count++;
            }
        }
        return count;
    }

    private int countHidden(List<Cell> cells) {
        int count = 0;
        for (Cell cell : cells) {
            if (!cell.isExposed() && !cell.isMined()) {
                count++;
            }
        }
        return count;
    }

    private boolean recognizePatterns(Cell cell, List<Cell> adjCells, int adjMines, int markedMines, int hiddenCells) {
        boolean changeMade = false;

        // Recognize and handle 1-2 pattern
        for (Cell adj : adjCells) {
            if (adj.isExposed()) {
                int nearMines = adj.getNearMines();
                if (nearMines == 2) {
                    List<Cell> adjAdjCells = getAdjCells(adj.getX(), adj.getY());
                    for (Cell adjAdj : adjAdjCells) {
                        if (adjAdj.isExposed() && adjAdj.getNearMines() == 1) {
                            List<Cell> adjAdjAdjCells = getAdjCells(adjAdj.getX(), adjAdj.getY());
                            for (Cell potentialMine : adjAdjAdjCells) {
                                if (!potentialMine.isExposed() && !potentialMine.isMined()) {
                                    mark(potentialMine.getX(), potentialMine.getY());
                                    changeMade = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return changeMade;
    }

    private Cell getSafestCell() {
        Map<Cell, Double> cellProbabilities = calculateProbabilities();

        // Find the cell with the lowest probability of being a mine
        Cell safestCell = null;
        double minProbability = Double.MAX_VALUE;
        for (Map.Entry<Cell, Double> entry : cellProbabilities.entrySet()) {
            if (entry.getValue() < minProbability) {
                minProbability = entry.getValue();
                safestCell = entry.getKey();
            }
        }
        return safestCell;
    }

    private Map<Cell, Double> calculateProbabilities() {
        Map<Cell, Double> probabilities = new HashMap<>();

        // Iterate over all cells to calculate probabilities
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = minefield[x][y];
                if (!cell.isExposed() && !cell.isMined()) {
                    probabilities.put(cell, calculateCellProbability(cell));
                }
            }
        }
        return probabilities;
    }

    private double calculateCellProbability(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        List<Cell> adjCells = getAdjCells(x, y);

        int totalMines = 0;
        int totalHidden = 0;
        int totalMarked = 0;

        for (Cell adj : adjCells) {
            if (adj.isExposed()) {
                totalMines += adj.getNearMines();
                totalHidden += countHidden(getAdjCells(adj.getX(), adj.getY()));
                totalMarked += countMarked(getAdjCells(adj.getX(), adj.getY()));
            }
        }

        if (totalHidden == 0) return 1.0; // If no hidden cells, probability is 1 (i.e., a mine)

        // Calculate probability based on the ratio of mines to hidden cells
        return (double) (totalMines - totalMarked) / totalHidden;
    }
}
