package com.joemenduni.musicmaps;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
    }

    public void gotoAddArtist(View view) {
        Intent addArtistIntent = new Intent(getApplicationContext(), AddArtistActivity.class);
        startActivity(addArtistIntent);
    }



}
