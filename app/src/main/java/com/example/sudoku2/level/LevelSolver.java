package com.example.sudoku2.level;

public class LevelSolver {
    int[][] grid;
    private static LevelSolver instance;

    public static LevelSolver getInstance() {
        if (instance == null) {
            instance = new LevelSolver();
        }
        return instance;
    }

    public boolean solveSudoku() {
        int[] empty = findEmpty();
        if (empty == null) {
            return true;
        }

        int row = empty[0];
        int col = empty[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(num, empty)) {
                grid[row][col] = num;

                if (solveSudoku()) {
                    return true;
                }

                grid[row][col] = 0;
            }
        }
        return false;
    }

    private int[] findEmpty() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean isValid(int num, int[] pos) {
        int row = pos[0];
        int col = pos[1];

        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num && col != i) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (grid[i][col] == num && row != i) {
                return false;
            }
        }

        int areaX = col / 3;
        int areaY = row / 3;

        for (int i = areaY * 3; i < areaY * 3 + 3; i++) {
            for (int j = areaX * 3; j < areaX * 3 + 3; j++) {
                if (grid[i][j] == num && !(i == row && j == col)) {
                    return false;
                }
            }
        }
        return true;
    }

    private LevelSolver() {
        grid = new int[9][9];
    }

    public void setGrid(int[][] inputGrid) {
        grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(inputGrid[i], 0, grid[i], 0, 9);
        }
    }

    public int[][] getGrid() {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid,0, copy, 0, 9);
        }
        return copy;
    }
}