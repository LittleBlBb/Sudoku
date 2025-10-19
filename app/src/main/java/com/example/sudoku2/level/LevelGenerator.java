package com.example.sudoku2.level;

import java.util.Random;

public class LevelGenerator {
    private int[][] grid;
    private final Random random;

    private static LevelGenerator instance;

    public static LevelGenerator getInstance(){
        if (instance == null){
            instance = new LevelGenerator();
        }
        return instance;
    }
    public void generateLevel() {
        grid = new int[][]{
                {1,2,3,4,5,6,7,8,9},
                {4,5,6,7,8,9,1,2,3},
                {7,8,9,1,2,3,4,5,6},
                {2,3,4,5,6,7,8,9,1},
                {5,6,7,8,9,1,2,3,4},
                {8,9,1,2,3,4,5,6,7},
                {3,4,5,6,7,8,9,1,2},
                {6,7,8,9,1,2,3,4,5},
                {9,1,2,3,4,5,6,7,8}
        };
        mix(random.nextInt(25));
        clearElements();
    }

    public void setLevel(int[][] grid){
        setGrid(grid);
    }

    public void setLevel(Level level){
        grid = level.getGrid();
    }

    public int[][] getGrid() {
        return grid;
    }

    private LevelGenerator(){
        grid = new int[9][9];
        random = new Random();
    }

    private void swapRowsSmall(){
        int area = random.nextInt(3);
        int row1 = random.nextInt(3);
        int row2 = random.nextInt(3);

        while (row1 == row2){
            row2 = random.nextInt(3);
        }

        int baseRow = area * 3;
        int idx1 = baseRow + row1;
        int idx2 = baseRow + row2;

        int[] temp = grid[idx1];
        grid[idx1] = grid[idx2];
        grid[idx2] = temp;
    }

    private void swapColumnsSmall(){
        int area = random.nextInt(3);
        int column1 = random.nextInt(3);
        int column2 = random.nextInt(3);

        while (column1 == column2){
            column2 = random.nextInt(3);
        }

        int baseColumn = area * 3;
        int idy1 = baseColumn + column1;
        int idy2 = baseColumn + column2;

        for (int i = 0; i < 9; i++) {
            int temp = grid[i][idy1];
            grid[i][idy1] = grid[i][idy2];
            grid[i][idy2] = temp;
        }
    }

    private void swapRowsArea(){
        int band1 = random.nextInt(3);
        int band2 = random.nextInt(3);

        while (band1 == band2) {
            band2 = random.nextInt(3);
        }

        int base1 = band1 * 3;
        int base2 = band2 * 3;

        for (int offset = 0; offset < 3; offset++) {
            int row1 = base1 + offset;
            int row2 = base2 + offset;
            int[] temp =  grid[row1];
            grid[row1] = grid[row2];
            grid[row2] = temp;
        }
    }

    private void swapColumnsArea(){
        int band1 = random.nextInt(3);
        int band2 = random.nextInt(3);

        while (band1 == band2) {
            band2 = random.nextInt(3);
        }

        int base1 = band1 * 3;
        int base2 = band2 * 3;

        for (int offset = 0; offset < 3; offset++) {
            int col1 = base1 + offset;
            int col2 = base2 + offset;
            for (int i = 0; i < 9; i++) {
                int temp = grid[i][col1];
                grid[i][col1] = grid[i][col2];
                grid[i][col2] = temp;
            }
        }
    }

    private void mix(int amt){
        Runnable[] functions = {
                this::transposing,
                this::swapColumnsArea,
                this::swapRowsArea,
                this::swapRowsSmall,
                this::swapColumnsSmall
        };

        for (int i = 0; i < amt; i++) {
            int id = random.nextInt(functions.length);
            functions[id].run();
        }
    }

    private void transposing(){
        int[][] transposingGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                transposingGrid[i][j] = grid[j][i];
            }
        }
        grid = transposingGrid;
    }

    private void clearElements(){
        int iterator = 0;

        while (iterator < 81){
            int i = random.nextInt(9);
            int j = random.nextInt(9);

            grid[i][j] = 0;

            iterator++;
        }
    }

    private void setGrid(int[][] grid){
        if (grid.length > 9){
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (grid[i].length > 9){
                return;
            }
        }
        this.grid = grid;
    }
}
