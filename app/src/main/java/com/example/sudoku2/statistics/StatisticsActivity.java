package com.example.sudoku2.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudoku2.R;
import com.example.sudoku2.database.AppDatabase;
import com.example.sudoku2.database.GameResult;
import com.example.sudoku2.database.GameResultDao;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements StatItemFragment.OnDeleteClickListener {

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

        loadStatistics();
    }

    private void loadStatistics() {
        statsContainer.removeAllViews();

        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = userPrefs.getInt("userId", -1);
        if (userId == -1) {
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        GameResultDao dao = db.gameResultDao();
        List<GameResult> results = dao.getResultsForUser(userId);

        for (GameResult stat : results) {
            StatItemFragment fragment = StatItemFragment.newInstance(stat.date,
                    stat.result == R.string.result_win_statistics ? getString(R.string.result_win_statistics) : getString(R.string.result_abandoned_statistics),
                    stat.id);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(statsContainer.getId(), fragment);
            transaction.commit();
        }
    }

    @Override
    public void onDeleteClicked(int id) {
        AppDatabase db = AppDatabase.getInstance(this);
        GameResultDao dao = db.gameResultDao();

        dao.deleteById(id);
        loadStatistics();
        Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show();
    }
}