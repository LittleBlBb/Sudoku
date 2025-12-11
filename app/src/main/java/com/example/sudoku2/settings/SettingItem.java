package com.example.sudoku2.settings;

public class SettingItem {
    private String name;
    private boolean enabled;
    private String key;

    public SettingItem(String name, boolean enabled, String key) {
        this.name = name;
        this.enabled = enabled;
        this.key = key;
    }

    public String getName() { return name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getKey() { return key; }
}
