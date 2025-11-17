package com.example.sudoku2.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sudoku2.R;

import java.util.List;

public class SettingsAdapter extends ArrayAdapter<SettingItem> {
    private final LayoutInflater inflater;

    public SettingsAdapter(@NonNull Context context, @NonNull List<SettingItem> settings) {
        super(context, 0, settings);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_setting, parent, false);

        SettingItem item = getItem(position);

        if (item != null) {
            TextView tvName = convertView.findViewById(R.id.tvSettingName);
            Switch switchSetting = convertView.findViewById(R.id.switchSetting);

            tvName.setText(item.getName());
            switchSetting.setChecked(item.isEnabled());

            switchSetting.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setEnabled(isChecked);
            });
        }

        return convertView;
    }
}
