package com.example.sudoku2.settings;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku2.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private List<SettingItem> settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);


        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });


        ListView listView = findViewById(R.id.settingsListView);

        settings = new ArrayList<>();
        settings.add(new SettingItem("Тёмная тема", false));
        settings.add(new SettingItem("Русский язык", true));
        settings.add(new SettingItem("Звуки", true));

        SettingsAdapter adapter = new SettingsAdapter(this, settings);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}