package com.joemenduni.musicmaps;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddArtistActivity extends AppCompatActivity {

    EditText artistName;
    Spinner genre;
    EditText numberMembers;
    EditText website;
    EditText pictureURL;
    EditText town;
    EditText state;
    EditText zipCode;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);
        setViewPointers();
        database = new DBHelper(this);
        setGenres();
    }

    public void setViewPointers() {
        artistName = (EditText) findViewById(R.id.artistName);
        genre = (Spinner) findViewById(R.id.artistGenre);
        numberMembers = (EditText) findViewById(R.id.artistMemberCount);
        website = (EditText) findViewById(R.id.artistWebsite);
        pictureURL = (EditText) findViewById(R.id.artistPictureURL);
        town = (EditText) findViewById(R.id.artistTown);
        state = (EditText) findViewById(R.id.artistState);
        zipCode = (EditText) findViewById(R.id.artistZipCode);
    }

    public void setGenres() {
        List<String> list = database.getAllGenres();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(dataAdapter);
    }

    public void clearFields(View view) {
        artistName.setText("");
        List<String> list = database.getAllGenres();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(dataAdapter);
        numberMembers.setText("");
        website.setText("");
        pictureURL.setText("");
        town.setText("");
        state.setText("");
        zipCode.setText("");
    }

    public void addArtist(View view) {
        String theName = artistName.getText().toString();
        String theGenre = genre.getSelectedItem().toString();
        int theMembers = Integer.valueOf(numberMembers.getText().toString());
        String theWebsite = website.getText().toString();
        String thePictureURL = pictureURL.getText().toString();
        String theTown = town.getText().toString();
        String theState = state.getText().toString();
        String theZipCode = zipCode.getText().toString();
        database.addArtist(theName, theGenre, theMembers, theWebsite, thePictureURL, theTown, theState, theZipCode);
    }


}
