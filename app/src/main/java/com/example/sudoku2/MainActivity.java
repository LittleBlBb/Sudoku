package com.example.sudoku2;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.sudoku2.level.level;
import com.google.android.material.appbar.MaterialToolbar;


public class MainActivity extends AppCompatActivity {

    private GridLayout topSquare;
    private LinearLayout controlPanel;
    private Button btnDelete, btnCheck;
    private FrameLayout pauseOverlay;
    private FrameLayout[][] cells = new FrameLayout[9][9];
    private TextView[][] cellTexts = new TextView[9][9];
    TextView pauseText;
    private boolean[][] isFixed = new boolean[9][9];

    private int selectedRow = -1;
    private int selectedCol = -1;

    private int gamesPlayed = 0;
    private int movesMade = 0;
    private long gameStartTimeMillis = 0L;

    private boolean isPaused = false;
    private long pauseStartMillis = 0L;
    private long accumulatedPauseMillis = 0L;

    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferences prefs = base.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String savedLocale = prefs.getString("Locale", "en");

        super.attachBaseContext(updateLocale(base, savedLocale));
    }

    private Context updateLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        updatePauseMenuIcon(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_pause) {
            if (isPaused) {
                resumeGame();
            } else {
                pauseGame();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        int savedMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
        invalidateOptionsMenu();

        pauseOverlay = findViewById(R.id.pauseOverlay);
        topSquare = findViewById(R.id.topSquare);
        controlPanel = findViewById(R.id.controlPanelInner);
        btnDelete = findViewById(R.id.btnDelete);
        btnCheck = findViewById(R.id.btnCheck);
        pauseText = findViewById(R.id.pauseText);

        if (pauseOverlay != null) {
            pauseOverlay.setOnClickListener(v -> resumeGame());
        }
        if (pauseText != null) {
            pauseText.setOnClickListener(v -> resumeGame());
        }

        pauseOverlay.setVisibility(View.GONE);

        topSquare.setBackgroundColor(0xFFFFFFFF);

        startNewGame();

        setupMenu();
    }

    private void setLocale(String lang) {
        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        prefs.edit().putString("Locale", lang).apply();

        recreate();
    }


    private void startNewGame() {
        gamesPlayed++;
        movesMade = 0;
        gameStartTimeMillis = System.currentTimeMillis();
        accumulatedPauseMillis = 0L;
        isPaused = false;
        pauseStartMillis = 0L;

        generateBoard();

        loadLevel(1);

        setupNumberButtons();
        setupDeleteButton();
        setupCheckButton();

        setControlsEnabled(true);
        topSquare.setVisibility(View.VISIBLE);
        if (pauseOverlay != null) pauseOverlay.setVisibility(View.GONE);

        selectedRow = -1;
        selectedCol = -1;
    }

    private void loadLevel(int levelNumber) {
        switch (levelNumber){
            case 1:
                applyPuzzle(level.LEVEL1.getGrid());
                break;
            case 2:
                break;
            default:
                clearPuzzle();
                break;
        }
    }

    private void level1() {
        int[][] puzzle = level.LEVEL1.getGrid();

        applyPuzzle(puzzle);
    }

    private void applyPuzzle(int[][] puzzle) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                isFixed[r][c] = false;
            }
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int v = (puzzle != null) ? puzzle[r][c] : 0;
                TextView tv = cellTexts[r][c];
                FrameLayout cell = cells[r][c];
                if (tv == null || cell == null) continue;

                if (v != 0) {
                    tv.setText(String.valueOf(v));
                    isFixed[r][c] = true;
                    cell.setClickable(false);
                    cell.setFocusable(false);
                    cell.setBackgroundColor(getResources().getColor(R.color.surface_variant, getTheme()));  // Светлее surface для fixed
                    tv.setTypeface(null, Typeface.BOLD);
                } else {
                    tv.setText("");
                    isFixed[r][c] = false;
                    cell.setClickable(true);
                    cell.setFocusable(true);
                    cell.setBackgroundResource(R.drawable.inner_button_selector);  // Уже theme-aware
                    tv.setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }

    private void clearPuzzle() {
        applyPuzzle(null);
    }

    private void setupMenu() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> {
                android.widget.PopupMenu popup = new android.widget.PopupMenu(MainActivity.this, v);
                popup.getMenu().add(getString(R.string.restart));
                popup.getMenu().add(getString(R.string.statistics));
                popup.getMenu().add(getString(R.string.logout));

                MenuItem darkTheme = popup.getMenu().add(getString(R.string.dark_theme));
                darkTheme.setCheckable(true);
                int currentMode = AppCompatDelegate.getDefaultNightMode();
                darkTheme.setChecked(currentMode == AppCompatDelegate.MODE_NIGHT_YES);

                MenuItem eng = popup.getMenu().add(getString(R.string.language_english));
                MenuItem rus = popup.getMenu().add(getString(R.string.language_russian));
                eng.setCheckable(true);
                rus.setCheckable(true);

                SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
                String currentLocale = prefs.getString("Locale", "en");
                eng.setChecked(currentLocale.equals("en"));
                rus.setChecked(currentLocale.equals("ru"));

                popup.setOnMenuItemClickListener(item -> {
                    String title = item.getTitle().toString();

                    if (title.equals(getString(R.string.restart))) {
                        startNewGame();
                        Toast.makeText(this, getString(R.string.game_restarted), Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (title.equals(getString(R.string.statistics))) {
                        boolean wasPausedBefore = isPaused;
                        pauseGame();
                        showStatisticsDialog(() -> {
                            if (!wasPausedBefore) resumeGame();
                        });
                        return true;
                    } else if (title.equals(getString(R.string.logout))) {
                        logOut();
                    } else if (title.equals(getString(R.string.dark_theme))) {
                        boolean isDarkNow = darkTheme.isChecked();
                        boolean newIsDark = !isDarkNow;

                        darkTheme.setChecked(newIsDark);
                        SharedPreferences prefsTheme = getSharedPreferences("Prefs", MODE_PRIVATE);
                        prefsTheme.edit().putInt("night_mode", newIsDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO).apply();
                        AppCompatDelegate.setDefaultNightMode(newIsDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                        recreate();

                        if (newIsDark) {
                            Toast.makeText(MainActivity.this, getString(R.string.dark_theme_enabled), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.light_theme_enabled), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    } else if (title.equals(getString(R.string.language_english))) {
                        setLocale("en");
                        Toast.makeText(this, getString(R.string.language_english), Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (title.equals(getString(R.string.language_russian))) {
                        setLocale("ru");
                        Toast.makeText(this, getString(R.string.language_russian), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }


    private void pauseGame() {
        if (isPaused) return;
        isPaused = true;
        pauseStartMillis = System.currentTimeMillis();

        topSquare.setVisibility(View.GONE);
        controlPanel.setVisibility(View.GONE);
        setControlsEnabled(false);

        if (pauseOverlay != null) {
            pauseOverlay.setAlpha(0f);
            pauseOverlay.setVisibility(View.VISIBLE);
            pauseOverlay.animate().alpha(1f).setDuration(160).start();
        }

        invalidateOptionsMenu();
    }

    private void resumeGame() {
        if (!isPaused) return;
        long now = System.currentTimeMillis();
        accumulatedPauseMillis += (now - pauseStartMillis);
        pauseStartMillis = 0L;
        isPaused = false;

        if (pauseOverlay != null) {
            pauseOverlay.animate().alpha(0f).setDuration(160).withEndAction(() -> {
                pauseOverlay.setVisibility(View.GONE);
            }).start();
        }

        topSquare.setVisibility(View.VISIBLE);
        controlPanel.setVisibility(View.VISIBLE);
        setControlsEnabled(true);

        invalidateOptionsMenu();
    }

    private void updatePauseMenuIcon(Menu menu) {
        MenuItem pauseItem = menu.findItem(R.id.action_pause);
        if (pauseItem != null) {
            pauseItem.setIcon(isPaused ? R.drawable.ic_play : R.drawable.ic_pause);
            pauseItem.setTitle(isPaused ? "Resume" : getString(R.string.btnPause));
        }
    }

    private void showStatisticsDialog(Runnable onClose) {
        long elapsed;
        if (gameStartTimeMillis == 0L) elapsed = 0L;
        else {
            if (isPaused) {
                elapsed = (pauseStartMillis - gameStartTimeMillis) - accumulatedPauseMillis;
            } else {
                elapsed = (System.currentTimeMillis() - gameStartTimeMillis) - accumulatedPauseMillis;
            }
            if (elapsed < 0) elapsed = 0;
        }

        long seconds = elapsed / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        String msg = getString(R.string.games_count_statistics) + ": " + gamesPlayed + "\n"
                + getString(R.string.moves_count_statistics) + ": " + movesMade + "\n"
                + getString(R.string.game_time_statistics) + ": " + minutes + " мин " + seconds + " сек";

        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.statistics))
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create();

        dlg.setOnDismissListener(dialog -> {
            if (onClose != null) onClose.run();
        });

        dlg.show();
    }

    private void generateBoard() {
        topSquare.removeAllViews();
        topSquare.setColumnCount(3);
        topSquare.setRowCount(3);

        final int blockMargin = dpToPx(0);
        final int blockInnerPadding = dpToPx(2);
        final int cellInnerMargin = dpToPx(1);

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                GridLayout block = new GridLayout(this);
                block.setColumnCount(3);
                block.setRowCount(3);

                GridLayout.LayoutParams blockParams = new GridLayout.LayoutParams(
                        GridLayout.spec(blockRow, 1f),
                        GridLayout.spec(blockCol, 1f)
                );
                blockParams.width = 0;
                blockParams.height = 0;
                blockParams.setMargins(blockMargin, blockMargin, blockMargin, blockMargin);
                block.setLayoutParams(blockParams);

                block.setBackgroundResource(R.drawable.block_border);
                block.setPadding(blockInnerPadding, blockInnerPadding, blockInnerPadding, blockInnerPadding);

                for (int innerRow = 0; innerRow < 3; innerRow++) {
                    for (int innerCol = 0; innerCol < 3; innerCol++) {
                        int globalRow = blockRow * 3 + innerRow;
                        int globalCol = blockCol * 3 + innerCol;

                        FrameLayout cell = new FrameLayout(this);
                        GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(
                                GridLayout.spec(innerRow, 1f),
                                GridLayout.spec(innerCol, 1f)
                        );
                        cellParams.width = 0;
                        cellParams.height = 0;
                        cellParams.setMargins(cellInnerMargin, cellInnerMargin, cellInnerMargin, cellInnerMargin);
                        cell.setLayoutParams(cellParams);

                        try {
                            cell.setBackgroundResource(R.drawable.inner_button_selector);
                        } catch (Exception ex) {
                            cell.setBackgroundColor(0xFFFFFFFF);
                        }

                        TextView tv = new TextView(this);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tv.setTextColor(getResources().getColor(R.color.onSurface, getTheme()));  // Theme-aware
                        cell.addView(tv);

                        final int r = globalRow;
                        final int c = globalCol;
                        cell.setClickable(true);
                        cell.setFocusable(true);
                        cell.setOnClickListener(v -> selectCell(r, c));

                        cells[globalRow][globalCol] = cell;
                        cellTexts[globalRow][globalCol] = tv;
                        block.addView(cell);
                    }
                }

                topSquare.addView(block);
            }
        }
    }

    private void setupNumberButtons() {
        for (int i = 1; i <= 9; i++) {
            int resId = getResources().getIdentifier("btnNum" + i, "id", getPackageName());
            Button btn = findViewById(resId);
            final int number = i;
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    if (isPaused) return;
                    if (selectedRow != -1 && selectedCol != -1) {
                        if (isFixed[selectedRow][selectedCol]) {
                            Toast.makeText(this, "Эта ячейка фиксирована", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cellTexts[selectedRow][selectedCol].setText(String.valueOf(number));
                        movesMade++;
                        moveToNextCell();
                    } else {
                        Toast.makeText(this, "Сначала выберите ячейку", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void setupDeleteButton() {
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                if (isPaused) return;
                if (selectedRow != -1 && selectedCol != -1) {
                    if (isFixed[selectedRow][selectedCol]) {
                        Toast.makeText(this, "Эта ячейка фиксирована", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cellTexts[selectedRow][selectedCol].setText("");
                } else {
                    Toast.makeText(this, getString(R.string.choose_sheet), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupCheckButton() {
        if (btnCheck != null) {
            btnCheck.setOnClickListener(v -> {
                if (isPaused) return;
                checkSolution();
            });
        }
    }

    private void selectCell(int row, int col) {
        if (isPaused) return;
        if (selectedRow != -1 && selectedCol != -1) {
            cells[selectedRow][selectedCol].setSelected(false);
        }
        selectedRow = row;
        selectedCol = col;
        cells[row][col].setSelected(true);
    }

    private int[] findNextEditableCell(int fromRow, int fromCol) {
        int r = fromRow;
        int c = fromCol;

        do {
            c++;
            if (c >= 9) {
                c = 0;
                r++;
                if (r >= 9) r = 0;
            }
            if (r == fromRow && c == fromCol) {
                if (!isFixed[r][c]) return new int[]{r, c};
                return null;
            }
        } while (isFixed[r][c]);

        return new int[]{r, c};
    }

    private void moveToNextCell() {
        if (isPaused) return;
        if (selectedRow == -1 || selectedCol == -1) return;

        int[] next = findNextEditableCell(selectedRow, selectedCol);
        if (next != null) {
            cells[selectedRow][selectedCol].setSelected(false);
            selectCell(next[0], next[1]);
        } else {
            selectCell(0,0);
        }
    }

    private void animateErrorCell(TextView cell) {
        int originalColor = getResources().getColor(R.color.surface, getTheme());

        cell.setBackgroundColor(Color.RED);

        cell.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(200)
                .withEndAction(() -> cell.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .withEndAction(() -> {
                            cell.animate()
                                    .scaleX(1.5f)
                                    .scaleY(1.5f)
                                    .setDuration(200)
                                    .withEndAction(() -> cell.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(200)
                                            .withEndAction(() -> {
                                                cell.setBackgroundColor(originalColor);
                                                cell.setBackgroundResource(R.drawable.btn_inner_default);
                                                cell.setTextColor(getResources().getColor(R.color.error, getTheme()));  // Красный текст для ошибки
                                            })
                                            .start())
                                    .start();
                        })
                        .start())
                .start();
    }

    private void checkSolution() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                TextView tv = cellTexts[r][c];
                if (tv != null) tv.setTextColor(0xFF000000);
            }
        }

        int[][] grid = new int[9][9];
        boolean hasEmpty = false;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String s = "";
                TextView tv = cellTexts[r][c];
                if (tv != null) s = tv.getText().toString().trim();
                if (s.isEmpty()) {
                    grid[r][c] = 0;
                    hasEmpty = true;
                } else {
                    try {
                        int v = Integer.parseInt(s);
                        if (v >= 1 && v <= 9) grid[r][c] = v;
                        else {
                            grid[r][c] = 0;
                            hasEmpty = true;
                        }
                    } catch (NumberFormatException e) {
                        grid[r][c] = 0;
                        hasEmpty = true;
                    }
                }
            }
        }

        java.util.Set<String> conflicts = new java.util.HashSet<>();

        for (int r = 0; r < 9; r++) {
            java.util.Map<Integer, Integer> seen = new java.util.HashMap<>();
            for (int c = 0; c < 9; c++) {
                int v = grid[r][c];
                if (v == 0) continue;
                if (seen.containsKey(v)) {
                    conflicts.add(r + "," + seen.get(v));
                    conflicts.add(r + "," + c);
                } else {
                    seen.put(v, c);
                }
            }
        }

        for (int c = 0; c < 9; c++) {
            java.util.Map<Integer, Integer> seen = new java.util.HashMap<>();
            for (int r = 0; r < 9; r++) {
                int v = grid[r][c];
                if (v == 0) continue;
                if (seen.containsKey(v)) {
                    conflicts.add(seen.get(v) + "," + c);
                    conflicts.add(r + "," + c);
                } else {
                    seen.put(v, r);
                }
            }
        }

        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                java.util.Map<Integer, int[]> seen = new java.util.HashMap<>();
                for (int dr = 0; dr < 3; dr++) {
                    for (int dc = 0; dc < 3; dc++) {
                        int r = br * 3 + dr;
                        int c = bc * 3 + dc;
                        int v = grid[r][c];
                        if (v == 0) continue;
                        if (seen.containsKey(v)) {
                            int[] first = seen.get(v);
                            conflicts.add(first[0] + "," + first[1]);
                            conflicts.add(r + "," + c);
                        } else {
                            seen.put(v, new int[]{r, c});
                        }
                    }
                }
            }
        }

        if (!conflicts.isEmpty()) {
            for (String pos : conflicts) {
                String[] p = pos.split(",");
                int r = Integer.parseInt(p[0]);
                int c = Integer.parseInt(p[1]);
                TextView tv = cellTexts[r][c];
                if (tv != null) {
                    animateErrorCell(tv);
                }
            }
            Toast.makeText(this, getString(R.string.errors_found), Toast.LENGTH_LONG).show();
            return;
        }

        if (hasEmpty) {
            Toast.makeText(this, getString(R.string.empty_cells_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Решение верно!", Toast.LENGTH_LONG).show();
        }
    }

    private void setControlsEnabled(boolean enabled) {
        for (int i = 1; i <= 9; i++) {
            int resId = getResources().getIdentifier("btnNum" + i, "id", getPackageName());
            View v = findViewById(resId);
            if (v != null) v.setEnabled(enabled);
        }
        if (btnDelete != null) btnDelete.setEnabled(enabled);
        if (btnCheck != null) btnCheck.setEnabled(enabled);
    }

    private int dpToPx(int dp) {
        float d = getResources().getDisplayMetrics().density;
        return Math.round(dp * d);
    }


    private void logOut(){
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("loggedIn", false)
                .apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
}