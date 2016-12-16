package com.joemenduni.musicmaps;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {

    EditText searchTitle;
    EditText searchZipCode;
    SeekBar searchRange;
    TextView currentRange;

    Double latitude;
    Double longitude;

    GoogleApiClient mGoogleApiClient;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setViewPointers();

        searchRange.setOnSeekBarChangeListener(seekbar_listener);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }

        database = new DBHelper(this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void goBack(View view) {
        finish();
    }

    public void setViewPointers() {
        searchTitle = (EditText) findViewById(R.id.searchTitle);
        searchZipCode = (EditText) findViewById(R.id.searchZip);
        searchRange = (SeekBar) findViewById(R.id.searchSeek);
        currentRange = (TextView) findViewById(R.id.currentRange);
    }

    public void clearFields(View view) {
        searchTitle.setText("");
        searchZipCode.setText("");
        searchRange.setProgress(50);
        currentRange.setText("50 miles");
    }


    SeekBar.OnSeekBarChangeListener seekbar_listener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar seekBar) {};

        public void onStopTrackingTouch (SeekBar seekBar) {};

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentRange.setText(progress + " miles");
        }
    };

    public class Event implements Serializable {

        String name;
        String startDateTime;
        String venueName;
        double latitude;
        double longitude;

        public Event(String name, String startDateTime, String venueName, double latitude, double longitude) {
            this.name = name;
            this.startDateTime = startDateTime;
            this.venueName = venueName;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }


    public void displySearchResults(View view) {
        List<Event> eventList = getSearchResults();
        List<String> eventStringList = new ArrayList<String>();
        for (Event event: eventList) {
            eventStringList.add(event.name + "\n" + event.startDateTime + " at " + event.venueName);
        }
        if (eventStringList.size() > 0) {
            final ArrayAdapter<String> mShadeAdapter =
                    new ArrayAdapter<String>(this, // The current context (this activity)
                            R.layout.item_event, // The name of the layout ID.
                            R.id.event_name, // The ID of the textview to populate.
                            eventStringList);
        }
    }

    public void viewMap(View view) {
        List<Event> eventList = getSearchResults();
        Intent mapIntent = new Intent(this, SearchMapsActivity.class);
        Bundle theBundle = new Bundle();
        theBundle.putSerializable("eventList", (Serializable) eventList);
        mapIntent.putExtras(theBundle);
        startActivity(mapIntent);
    }


    public List<Event> getSearchResults() {
        String title = searchTitle.getText().toString();
        int mileRange = searchRange.getProgress();
        Double[] location = getLocationToUse();
        double minLatitude = location[0] - convertMileageToDegrees(mileRange);
        double maxLatitude = location[0] + convertMileageToDegrees(mileRange);
        double minLongitude = location[1] - convertMileageToDegrees(mileRange);
        double maxLongitude = location[1] + convertMileageToDegrees(mileRange);

        List<Event> eventList = new ArrayList<Event>();

        Cursor cursor = database.getAllShowsCursors();
        if ((cursor.moveToFirst())) {
            do {
                String name = cursor.getString(3);
                double[] latlng = database.findVenueLatLngByID(cursor.getInt(0));
                String venueName = database.findVenueNameByID(cursor.getInt(0));
                Double latitude = latlng[0];
                Double longitude = latlng[1];
                if (stringComparison(title, name)) {
                    if (latitude >= minLatitude && latitude <= maxLatitude && longitude >= minLongitude && longitude <= maxLongitude) {
                        String startDateTime = cursor.getString(7);
                        Event newEvent = new Event(name, startDateTime, venueName, latitude, longitude);
                        eventList.add(newEvent);

                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventList;
    }


    public boolean stringComparison(String userSearch, String showName) {
        String[] userSearchSplit = userSearch.split(" ");
        String[] showNameSplit = showName.split(" ");
        for (String word: userSearchSplit) {
            if (showName.contains(word)) {
                return true;
            }
        }
        for (String word: showNameSplit) {
            if (userSearch.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public double convertMileageToDegrees(double amountMiles) {
        Double amountKM = amountMiles * 0.621371;
        Double amountDeg = ( 1 / 110.54 ) * amountKM;
        return amountDeg;
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

    public Double[] getLocationToUse() {
        Double[] returnDouble = new Double[2];
        String zipCodeString = searchZipCode.getText().toString();
        String zipRegex = getString(R.string.zipCodeRegex);
        Pattern pattern = Pattern.compile(zipRegex);
        Matcher matcher = pattern.matcher(zipCodeString);
        int zipCode = -1;
        if (matcher.matches()) {
            zipCode = Integer.valueOf(zipCodeString);
        }

        if (zipCode != -1) {
            GeocodingLocation locationAddress = new GeocodingLocation();
            locationAddress.getAddressFromLocation(String.valueOf(zipCode),
                    getApplicationContext(), new GeocoderHandler());
            returnDouble = new Double[]{latitude, longitude};
        }
        else {
            try {
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    returnDouble[0] = mLastLocation.getLatitude();
                    returnDouble[1] = mLastLocation.getLongitude();
                }
            }
            catch (SecurityException se) {

            }
        }
        return returnDouble;
    }



}
