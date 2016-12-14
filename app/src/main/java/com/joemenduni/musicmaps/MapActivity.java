package com.joemenduni.musicmaps;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = new DBHelper(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Cursor cursor = database.getAllVenuesCursors();
        LatLng latLng = null;
        if ((cursor.moveToFirst())) {
            do {
                String name = cursor.getString(6);
                Double latitude = cursor.getDouble(5);
                Double longitude = cursor.getDouble(2);
                latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        cursor = database.getAllShowsCursors();
        latLng = null;
        if ((cursor.moveToFirst())) {
            do {
                String name = cursor.getString(3);
                System.out.println(cursor.getInt(0));
                double[] latlng = database.findVenueLatLngByID(cursor.getInt(0));
                System.out.println(latlng[0]);
                Double latitude = latlng[0] + .0000000001;
                Double longitude = latlng[1] + .0000000001;
                latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


}
