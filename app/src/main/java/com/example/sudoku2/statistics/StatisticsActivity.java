package com.example.sudoku2.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku2.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsActivity extends AppCompatActivity {

    private List<GameStat> stats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_statistics);

        MaterialToolbar toolbar = findViewById(R.id.toolbarStats);
        toolbar.setNavigationOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences("game_stats", MODE_PRIVATE);
        Set<String> statsSet = prefs.getStringSet("stats", new HashSet<>());

        for (String record : statsSet) {
            String[] parts = record.split("\\|");
            if (parts.length == 3) {
                stats.add(new GameStat(parts[0], parts[1], Integer.parseInt(parts[2])));
            }
        }

        ListView lvStats = findViewById(R.id.lvStats);
        StatsAdapter adapter = new StatsAdapter(this, stats);
        lvStats.setAdapter(adapter);

        lvStats.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            GameStat stat = stats.get(position);
            Toast.makeText(this, getString(R.string.game_from_statistics) + stat.getDate() + ": " + getString(stat.getResultId()), Toast.LENGTH_SHORT).show();
        });
    }
}

