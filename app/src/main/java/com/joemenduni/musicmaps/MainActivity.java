package com.joemenduni.musicmaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DBHelper database;

    /** name of the user preferences files **/
    public static final String PREFS_NAME = "AppPrefsFile";

    /** user preferences **/
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DBHelper(this);

        // gets user preferences from file
        settings = getSharedPreferences(PREFS_NAME, 0);
    }

    public void gotoAddArtist(View view) {
        Intent addArtistIntent = new Intent(getApplicationContext(), AddArtistActivity.class);
        startActivity(addArtistIntent);
    }

    public void gotoAddShow(View view) {
        Intent addShowIntent = new Intent(getApplicationContext(), AddShowActivity.class);
        startActivity(addShowIntent);
    }
    public void gotoAddVenue(View view) {
        Intent addVenueIntent = new Intent(getApplicationContext(), AddVenueActivity.class);
        startActivity(addVenueIntent);
    }

    public void gotoAbout(View view) {
        Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(aboutIntent);
    }

    public void gotoSettings(View view) {
        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingsIntent);
    }



}
