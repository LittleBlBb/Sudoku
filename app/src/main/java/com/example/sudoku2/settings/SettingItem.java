package com.example.sudoku2.settings;

public class SettingItem {
    private final String name;
    private boolean enabled;

    public SettingItem(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
