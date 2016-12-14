package com.joemenduni.musicmaps;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DBHelper(this);
        SQLiteDatabase db = database.getDB();
        List<String> genreList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + "genre" + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if ((cursor.moveToFirst())) {
            do {
                genreList.add(cursor.getString(1));
                System.out.println(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void gotoAddArtist(View view) {
        Intent addArtistIntent = new Intent(getApplicationContext(), AddArtistActivity.class);
        Bundle theBundle = new Bundle();
        theBundle.putSerializable("dbHelper", database);
        addArtistIntent.putExtras(theBundle);
        startActivity(addArtistIntent);
    }



}
