package com.example.sudoku2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.core.splashscreen.SplashScreen;

public class SplashScreenActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ Устанавливаем SplashScreen API (импорт из androidx)
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        // Проверяем, вошёл ли пользователь
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("loggedIn", false);

        // Переход к нужному экрану
        if (loggedIn) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
