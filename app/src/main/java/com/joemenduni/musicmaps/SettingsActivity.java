package com.joemenduni.musicmaps;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    private EditText userName;
    private RadioButton musicianYes;
    private RadioButton musicianNo;
    private RadioButton locationYes;
    private RadioButton locationNo;
    private SeekBar range;

    /** user preferences **/
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // gets user preferences from file
        settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        setViewPointers();
        loadUserPreferences();

        // sets listener for name field
        userName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                handleNameChange();
            }
        });

        musicianYes.setOnClickListener(radio_listener_musician);
        musicianNo.setOnClickListener(radio_listener_musician);
        locationYes.setOnClickListener(radio_listener_location);
        locationNo.setOnClickListener(radio_listener_location);

        range.setOnSeekBarChangeListener(seekbar_listener);
    }

    public void setViewPointers() {
        userName = (EditText) findViewById(R.id.userName);
        musicianYes = (RadioButton) findViewById(R.id.settings_musician_yes);
        musicianNo = (RadioButton) findViewById(R.id.settings_musician_no);
        locationYes = (RadioButton) findViewById(R.id.settings_location_yes);
        locationNo = (RadioButton) findViewById(R.id.settings_location_no);
        range = (SeekBar) findViewById(R.id.seekBar);
    }

    public void goBack(View view) {
        finish();
    }

    public void loadUserPreferences() {
        if (settings.contains("name")) {
            String name = settings.getString("name", null);
            userName.setText(name);
        }
        if (settings.contains("musician")) {
            Boolean musician = settings.getBoolean("musician", true);
            if (musician) {
                if (!musicianYes.isChecked()) {
                    musicianYes.toggle();
                    if (musicianNo.isChecked()) {
                        musicianNo.toggle();
                    }
                }
            } else {
                if (!musicianNo.isChecked()) {
                    musicianNo.toggle();
                    if (musicianYes.isChecked()) {
                        musicianYes.toggle();
                    }
                }
            }
        }
        if (settings.contains("location")) {
            Boolean location = settings.getBoolean("location", true);
            if (location) {
                if (!locationYes.isChecked()) {
                    locationYes.toggle();
                    if (locationNo.isChecked()) {
                        locationNo.toggle();
                    }
                }
            } else {
                if (!locationNo.isChecked()) {
                    locationNo.toggle();
                    if (locationYes.isChecked()) {
                        locationYes.toggle();
                    }
                }
            }
        }
        if (settings.contains("range")) {
            int rangeDisplay = settings.getInt("range", 0);
            range.setProgress(rangeDisplay);
        }
    }

    private void handleNameChange() {
        settings.edit().putString("name", userName.getText().toString()).apply();
    }

    View.OnClickListener radio_listener_musician = new View.OnClickListener(){
        public void onClick(View v) {
            boolean isMusican = false;
            if (v == musicianYes) {
                isMusican = true;
            }
            else if (v == musicianNo) {
                isMusican = false;
            }
            settings.edit().putBoolean("musician", isMusican).apply();
        }
    };

    View.OnClickListener radio_listener_location = new View.OnClickListener(){
        public void onClick(View v) {
            boolean useLocation = false;
            if (v == locationYes) {
                useLocation = true;
            }
            else if (v == locationNo) {
                useLocation = false;
            }
            settings.edit().putBoolean("location", useLocation).apply();
        }
    };

    SeekBar.OnSeekBarChangeListener seekbar_listener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar seekBar) {};

        public void onStopTrackingTouch (SeekBar seekBar) {};

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            settings.edit().putInt("range", progress).apply();
        }
    };



}
