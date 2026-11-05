package com.darkmodmenu.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private final int BG = Color.rgb(13, 13, 17);
    private final int PANEL = Color.rgb(29, 29, 37);
    private final int RED = Color.rgb(255, 55, 70);
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("menu_settings", MODE_PRIVATE);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(20), dp(28), dp(20), dp(24));
        scroll.addView(page);

        TextView title = new TextView(this);
        title.setText("DARK MOD MENU");
        title.setTextColor(Color.WHITE);
        title.setTextSize(25);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        page.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("ANDROID CONTROL PANEL  •  v1.0");
        subtitle.setTextColor(0xFFAAAAAF);
        subtitle.setTextSize(11);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(-1, -2);
        subLp.bottomMargin = dp(22);
        page.addView(subtitle, subLp);

        addSwitch(page, "Crosshair preview", "crosshair", true);
        addSwitch(page, "Performance mode", "performance", false);
        addSwitch(page, "Low animation mode", "low_animation", false);
        addSwitch(page, "Sound feedback", "sound", true);

        TextView fovTitle = sectionTitle("VISUAL FOV");
        page.addView(fovTitle);
        TextView fovValue = new TextView(this);
        fovValue.setTextColor(Color.WHITE);
        fovValue.setTextSize(14);
        page.addView(fovValue);
        SeekBar fov = new SeekBar(this);
        fov.setMax(100);
        fov.setProgress(prefs.getInt("fov", 50));
        fov.setProgressTintList(android.content.res.ColorStateList.valueOf(RED));
        fov.setThumbTintList(android.content.res.ColorStateList.valueOf(RED));
        page.addView(fov);
        fovValue.setText("Visual FOV: " + fov.getProgress());
        fov.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int value, boolean user) {
                fovValue.setText("Visual FOV: " + value);
                if (user) prefs.edit().putInt("fov", value).apply();
            }
            public void onStartTrackingTouch(SeekBar b) {}
            public void onStopTrackingTouch(SeekBar b) {}
        });

        page.addView(sectionTitle("CROSSHAIR STYLE"));
        Spinner spinner = new Spinner(this);
        String[] styles = {"Dot", "Classic", "Plus", "Circle"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_dropdown_item, styles);
        spinner.setAdapter(adapter);
        spinner.setSelection(prefs.getInt("crosshair_style", 0));
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(android.widget.AdapterView<?> p) {}
            public void onItemSelected(android.widget.AdapterView<?> p, View v, int pos, long id) {
                prefs.edit().putInt("crosshair_style", pos).apply();
            }
        });
        page.addView(spinner);

        TextView note = new TextView(this);
        note.setText("Standalone settings demo. These controls change this app only; they do not modify Free Fire or provide in-game cheats.");
        note.setTextColor(0xFF9999A3);
        note.setTextSize(12);
        note.setPadding(0, dp(24), 0, 0);
        page.addView(note);
        setContentView(scroll);
    }

    private void addSwitch(LinearLayout page, String label, String key, boolean def) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), dp(8), dp(10), dp(8));
        row.setBackground(round(PANEL, 16));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(58));
        lp.bottomMargin = dp(10);
        page.addView(row, lp);

        TextView text = new TextView(this);
        text.setText(label);
        text.setTextColor(Color.WHITE);
        text.setTextSize(15);
        row.addView(text, new LinearLayout.LayoutParams(0, -2, 1));

        Switch toggle = new Switch(this);
        toggle.setButtonTintList(null);
        toggle.setChecked(prefs.getBoolean(key, def));
        toggle.setOnCheckedChangeListener((button, checked) ->
            prefs.edit().putBoolean(key, checked).apply());
        row.addView(toggle);
    }

    private TextView sectionTitle(String value) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextColor(RED);
        t.setTextSize(12);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(0, dp(22), 0, dp(8));
        return t;
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }
    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
