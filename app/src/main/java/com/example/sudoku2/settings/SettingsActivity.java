package com.example.sudoku2.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudoku2.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements SettingFragment.OnSettingChangedListener {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("Prefs", MODE_PRIVATE);

        // Восстановим тему
        int nightMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(nightMode);

        // Восстановим язык
        String language = prefs.getString("language", "russian");
        setAppLocale(language.equals("russian") ? "ru" : "en");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Добавляем фрагменты настроек
        addFragment(R.id.fragmentContainer1, R.string.dark_theme, "dark_theme",
                prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_NO) == AppCompatDelegate.MODE_NIGHT_YES);
        addFragment(R.id.fragmentContainer2, R.string.sounds, "sounds",
                prefs.getBoolean("sounds", true));
        addFragment(R.id.fragmentContainer3, R.string.language_russian, "language",
                prefs.getString("language", "russian").equals("russian"));
    }

    private void addFragment(int containerId, int titleResId, String key, boolean isChecked) {
        SettingFragment fragment = SettingFragment.newInstance(getString(titleResId), key, isChecked);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    @Override
    public void onSettingChanged(String key, boolean isChecked) {
        switch (key) {
            case "dark_theme":
                prefs.edit().putInt("night_mode",
                        isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO).apply();
                AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                Toast.makeText(this, isChecked ? getString(R.string.dark_theme_enabled) : getString(R.string.light_theme_enabled), Toast.LENGTH_SHORT).show();
                break;

            case "sounds":
                prefs.edit().putBoolean("sounds", isChecked).apply();
                Toast.makeText(this, isChecked ? "Звук включен" : "Звук выключен", Toast.LENGTH_SHORT).show();
                break;

            case "language":
                String newLang = isChecked ? "ru" : "en";
                prefs.edit().putString("language", newLang.equals("ru") ? "russian" : "english").apply();
                setAppLocale(newLang);
                recreate();
                break;
        }
    }

    private void setAppLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
