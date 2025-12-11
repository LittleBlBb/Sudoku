package com.example.sudoku2.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sudoku2.R;

import java.util.List;

public class SettingsAdapter extends BaseAdapter {

    public interface SwitchListener {
        void onSwitchChanged(SettingItem item, boolean isChecked);
    }

    private final Context context;
    private final List<SettingItem> items;
    private final SwitchListener listener;

    public SettingsAdapter(Context context, List<SettingItem> items, SwitchListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_setting, parent, false);
        }

        SettingItem item = items.get(position);
        TextView tvName = convertView.findViewById(R.id.tvSettingName);
        Switch switchSetting = convertView.findViewById(R.id.switchSetting);

        tvName.setText(item.getName());
        switchSetting.setOnCheckedChangeListener(null);
        switchSetting.setChecked(item.isEnabled());

        switchSetting.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setEnabled(isChecked);
            listener.onSwitchChanged(item, isChecked);
        });

        return convertView;
    }
}
