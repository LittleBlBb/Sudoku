package com.example.sudoku2.statistics;

import android.annotation.SuppressLint;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sudoku2.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatsAdapter extends ArrayAdapter<GameStat> {
    private final LayoutInflater inflater;

    public StatsAdapter(@NonNull Context context, @NonNull List<GameStat> stats) {
        super(context, 0, stats);
        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_stat, parent, false);

        GameStat stat = getItem(position);
        if (stat != null) {
            ((TextView) convertView.findViewById(R.id.tvGameDate))
                    .setText(getContext().getString(R.string.date_statistics) + ": " + stat.getDate());
            ((TextView) convertView.findViewById(R.id.tvGameTime))
                    .setText(getContext().getString(R.string.time_statistics) + ": " + millisecondsToDefaultTime(stat.getTime()));
            ((TextView) convertView.findViewById(R.id.tvGameResult))
                    .setText(getContext().getString(R.string.result_statistics) + ": " + getContext().getString(stat.getResultId()));
        }

        return convertView;
    }

    @SuppressLint("DefaultLocale")
    private static String millisecondsToDefaultTime(String ms){
        long longMs = Long.parseLong(ms);
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(longMs),
                TimeUnit.MILLISECONDS.toMinutes(longMs) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(longMs)),
                TimeUnit.MILLISECONDS.toSeconds(longMs) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(longMs)));
    }

}


