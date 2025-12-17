package com.example.sudoku2.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameResultDao {
    @Insert
    void insert(GameResult result);

    @Query("SELECT * FROM game_results WHERE user_id = :userId")
    List<GameResult> getResultsForUser(int userId);

    @Query("DELETE FROM game_results WHERE id = :id")
    void deleteById(int id);
}