package com.example.sudoku2.statistics;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import com.example.sudoku2.R;

public class StatItemFragment extends Fragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_RESULT = "result";
    private static final String ARG_ID = "id";

    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClicked(int id);
    }

    public static StatItemFragment newInstance(String date, String result, int id) {
        StatItemFragment fragment = new StatItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_RESULT, result);
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDeleteClickListener) {
            deleteListener = (OnDeleteClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDeleteClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat_item, container, false);
        TextView tv = view.findViewById(R.id.statText);
        Button btnDelete = view.findViewById(R.id.btnDeleteStat);

        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                if (getArguments() != null) {
                    int id = getArguments().getInt(ARG_ID, -1);
                    if (id != -1 && deleteListener != null) {
                        deleteListener.onDeleteClicked(id);
                    }
                }
            });
        }

        if (getArguments() != null) {
            String date = getArguments().getString(ARG_DATE);
            String result = getArguments().getString(ARG_RESULT);
            tv.setText(date + ": " + result);
        }

        return view;
    }
}