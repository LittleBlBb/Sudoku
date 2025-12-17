package com.example.sudoku2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, GameResult.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract GameResultDao gameResultDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "sudoku_db")
                    .allowMainThreadQueries()  // For simplicity; in production, use async
                    .fallbackToDestructiveMigration()  // Add this line
                    .build();
        }
        return instance;
    }
}