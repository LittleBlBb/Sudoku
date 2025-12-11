package com.example.sudoku2.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sudoku2.R;

public class SettingFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_KEY = "key";

    private String key;
    private String title;
    private Switch switchView;
    private TextView titleView;

    private OnSettingChangedListener listener;

    public interface OnSettingChangedListener {
        void onSettingChanged(String key, boolean isChecked);
    }

    public static SettingFragment newInstance(String title, String key, boolean isChecked) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_KEY, key);
        args.putBoolean("isChecked", isChecked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingChangedListener) {
            listener = (OnSettingChangedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSettingChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        switchView = view.findViewById(R.id.settingSwitch);
        titleView = view.findViewById(R.id.settingTitle);

        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            key = getArguments().getString(ARG_KEY);
            boolean isChecked = getArguments().getBoolean("isChecked", false);
            titleView.setText(title);
            switchView.setChecked(isChecked);
        }

        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onSettingChanged(key, isChecked);
            }
        });

        return view;
    }
}
