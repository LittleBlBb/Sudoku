package com.example.sudoku2;

public class GameStat {
    private final String date;
    private final String time;
    private final int result;

    public GameStat(String date, String time, int result) {
        this.date = date;
        this.time = time;
        this.result = result;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getResultId() { return result; }
}
