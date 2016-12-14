package com.joemenduni.musicmaps;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddVenueActivity extends AppCompatActivity {

    EditText venueName;
    EditText website;
    EditText pictureURL;
    EditText streetAddress;
    EditText town;
    EditText state;
    EditText zipCode;
    DBHelper database;

    double latitude = 0;
    double longitude = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);
        setViewPointers();
        database = new DBHelper(this);
    }

    public void setViewPointers() {
        venueName = (EditText) findViewById(R.id.venueName);
        website = (EditText) findViewById(R.id.venueWebsite);
        pictureURL = (EditText) findViewById(R.id.venuePictureURL);
        streetAddress = (EditText) findViewById(R.id.venueStreetAddress);
        town = (EditText) findViewById(R.id.venueTown);
        state = (EditText) findViewById(R.id.venueState);
        zipCode = (EditText) findViewById(R.id.venueZipCode);
    }

    public void clearFields(View view) {
        venueName.setText("");
        website.setText("");
        pictureURL.setText("");
        streetAddress.setText("");
        town.setText("");
        state.setText("");
        zipCode.setText("");
    }

    private class GeocoderHandler extends Handler {
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    latitude = bundle.getDouble("latitude");
                    longitude = bundle.getDouble("longitude");
                    break;
                default:
            }
        }
    }


    public void addVenue(View view) {
        String theName = venueName.getText().toString();
        String theWebsite = website.getText().toString();
        String thePictureURL = pictureURL.getText().toString();
        String theStreetAddress = streetAddress.getText().toString();
        String theTown = town.getText().toString();
        String theState = state.getText().toString();
        String theZipCode = zipCode.getText().toString();

        String fullAddress = theStreetAddress + ", " + theTown + ", " + theState + " " + theZipCode;
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(fullAddress,
                getApplicationContext(), new GeocoderHandler());

        database.addVenue(theName, theWebsite, thePictureURL, theStreetAddress, theTown, theState, theZipCode, latitude, longitude);
        finish();
    }

    public void goBack(View view) {
        finish();
    }


}
