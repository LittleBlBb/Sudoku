package com.example.sudoku2.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sudoku2.R;

public class StatItemFragment extends Fragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_RESULT = "result";

    public static StatItemFragment newInstance(String date, String result) {
        StatItemFragment fragment = new StatItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat_item, container, false);
        TextView tv = view.findViewById(R.id.statText);

        if (getArguments() != null) {
            String date = getArguments().getString(ARG_DATE);
            String result = getArguments().getString(ARG_RESULT);
            tv.setText(date + ": " + result);
        }

        return view;
    }
}
