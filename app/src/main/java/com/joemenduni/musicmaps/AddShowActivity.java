package com.joemenduni.musicmaps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddShowActivity extends AppCompatActivity {

    EditText showName;
    Spinner venue;
    EditText website;
    EditText pictureURL;
    EditText artistsPerforming;
    EditText startDateTime;
    EditText endDateTime;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show);
        setViewPointers();
        database = new DBHelper(this);
        setVenues();
    }

    public void setViewPointers() {
        showName = (EditText) findViewById(R.id.showName);
        venue = (Spinner) findViewById(R.id.showVenue);
        website = (EditText) findViewById(R.id.showWebsite);
        pictureURL = (EditText) findViewById(R.id.showPictureURL);
        artistsPerforming = (EditText) findViewById(R.id.showArtists);
        startDateTime = (EditText) findViewById(R.id.startDateTime);
        endDateTime = (EditText) findViewById(R.id.endDateTime);
    }

    public void setVenues() {
        List<String> list = database.getAllGenres();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venue.setAdapter(dataAdapter);
    }

    public void clearFields(View view) {
        showName.setText("");
        setVenues();
        website.setText("");
        pictureURL.setText("");
        artistsPerforming.setText("");
        startDateTime.setText("");
        endDateTime.setText("");
    }

    public void addShow(View view) {
        String theName = showName.getText().toString();
        String theVenue = venue.getSelectedItem().toString();
        String theWebsite = website.getText().toString();
        String thePictureURL = pictureURL.getText().toString();
        String artistsString = artistsPerforming.getText().toString();
        String[] theArtists = artistsString.split(",");
        String theStartDateTime = startDateTime.toString();
        String theEndDateTime = endDateTime.toString();
        database.addShow(theName, theVenue, theWebsite, thePictureURL, theArtists, theStartDateTime, theEndDateTime);
        finish();
    }

    public void goBack(View view) {
        finish();
    }


}
