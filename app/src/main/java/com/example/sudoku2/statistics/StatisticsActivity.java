package com.example.sudoku2.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudoku2.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsActivity extends AppCompatActivity {

    private List<GameStat> stats = new ArrayList<>();
    private LinearLayout statsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistics);

        MaterialToolbar toolbar = findViewById(R.id.toolbarStats);
        toolbar.setNavigationOnClickListener(v -> finish());

        statsContainer = findViewById(R.id.statsContainer);

        SharedPreferences prefs = getSharedPreferences("game_stats", MODE_PRIVATE);
        Set<String> statsSet = prefs.getStringSet("stats", new HashSet<>());

        for (String record : statsSet) {
            String[] parts = record.split("\\|");
            if (parts.length == 3) {
                stats.add(new GameStat(parts[0], parts[1], Integer.parseInt(parts[2])));
            }
        }

        for (GameStat stat : stats) {
            StatItemFragment fragment = StatItemFragment.newInstance(stat.getDate(),
                    stat.getResultId() == R.string.result_win_statistics ? getString(R.string.result_win_statistics) : getString(R.string.result_abandoned_statistics));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(statsContainer.getId(), fragment);
            transaction.commit();
        }
    }
}
