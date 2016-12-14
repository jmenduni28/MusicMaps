package com.joemenduni.musicmaps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by joemenduni on 12/13/16.
 */

public class DBHelper extends SQLiteOpenHelper {
    //TASK 1: DEFINE THE DATABASE AND TABLE
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MusicMapsDB";

    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String GENRE_TABLE_NAME = "genre";
    private static final String SHOW_TABLE_NAME = "show";
    private static final String VENUE_TABLE_NAME = "venue";
    private static final String ARTIST_SHOW_TABLE_NAME = "artistToshow";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String artistTable = "CREATE TABLE " + ARTIST_TABLE_NAME + "(" + makeArtistTableSQL() + ")";
        String genreTable = "CREATE TABLE " + GENRE_TABLE_NAME + "(" + makeGenreTableSQL() + ")";
        String showTable = "CREATE TABLE " + SHOW_TABLE_NAME + "(" + makeShowTableSQL() + ")";
        String venueTable = "CREATE TABLE " + VENUE_TABLE_NAME + "(" + makeVenueTableSQL() + ")";
        String artistToshowTable = "CREATE TABLE " + ARTIST_SHOW_TABLE_NAME + "(" + makeArtistShowTableSQL() + ")";
        database.execSQL(genreTable);
        database.execSQL(artistTable);
        database.execSQL(venueTable);
        database.execSQL(showTable);
        database.execSQL(artistToshowTable);
        database.execSQL("CREATE INDEX artist_index ON " + ARTIST_SHOW_TABLE_NAME + " (artist_id);");
        database.execSQL("CREATE INDEX show_index ON " + ARTIST_SHOW_TABLE_NAME + " (show_id);");
    }

    private String makeArtistTableSQL() {
        Map<String, String> artistDBMap = new HashMap<String, String>();
        artistDBMap.put("_id", "INTEGER PRIMARY KEY");
        artistDBMap.put("name", "TEXT");
        artistDBMap.put("genre_id", "INTEGER");
        artistDBMap.put("member_count", "INTEGER");
        artistDBMap.put("website", "TEXT");
        artistDBMap.put("picture_url", "TEXT");
        artistDBMap.put("zip_code", "INTEGER");
        artistDBMap.put("FOREIGN KEY (genre_id)", "REFERENCES genre(id)");
        return makeDBStringFromMap(artistDBMap);
    }

    private String makeGenreTableSQL() {
        Map<String, String> genreDBMap = new HashMap<String, String>();
        genreDBMap.put("_id", "INTEGER PRIMARY KEY");
        genreDBMap.put("name", "TEXT");
        return makeDBStringFromMap(genreDBMap);
    }

    private String makeShowTableSQL() {
        Map<String, String> showDBMap = new HashMap<String, String>();
        showDBMap.put("_id", "INTEGER PRIMARY KEY");
        showDBMap.put("name", "TEXT");
        showDBMap.put("website", "TEXT");
        showDBMap.put("picture_url", "TEXT");
        showDBMap.put("venue_id", "INTEGER");
        showDBMap.put("start_datetime", "INTEGER");
        showDBMap.put("end_datetime", "INTEGER");
        showDBMap.put("FOREIGN KEY (venue_id)", "REFERENCES venue(id)");
        return makeDBStringFromMap(showDBMap);
    }

    private String makeVenueTableSQL() {
        Map<String, String> venueDBMap = new HashMap<String, String>();
        venueDBMap.put("_id", "INTEGER PRIMARY KEY");
        venueDBMap.put("name", "TEXT");
        venueDBMap.put("website", "TEXT");
        venueDBMap.put("picture_url", "TEXT");
        venueDBMap.put("address", "TEXT");
        venueDBMap.put("town", "TEXT");
        venueDBMap.put("state", "TEXT");
        venueDBMap.put("zip", "INTEGER");
        venueDBMap.put("latitude", "INTEGER");
        venueDBMap.put("longitude", "INTEGER");
        return makeDBStringFromMap(venueDBMap);
    }

    private String makeArtistShowTableSQL() {
        Map<String, String> artistshowDBMap = new HashMap<String, String>();
        artistshowDBMap.put("_id", "INTEGER PRIMARY KEY");
        artistshowDBMap.put("artist_id", "INTEGER");
        artistshowDBMap.put("show_id", "INTEGER");
        artistshowDBMap.put("FOREIGN KEY(artist_id)", "REFEREES artist(id) ON DELETE CASCADE");
        artistshowDBMap.put("FOREIGN KEY(show_id)", "REFEREES show(id) ON DELETE CASCADE");
        return makeDBStringFromMap(artistshowDBMap);
    }

    private String makeDBStringFromMap(Map<String, String> dbMap) {
        String returnString = "";
        Iterator iterator = dbMap.entrySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            if (iterator.hasNext()) {
                returnString += pair.getKey() + " " + pair.getValue() + ", ";
            }
            else {
                returnString += pair.getKey() + " " + pair.getValue();
            }
            iterator.remove(); // avoids a ConcurrentModificationException
        }
        return returnString;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        String[] tables = {GENRE_TABLE_NAME, ARTIST_TABLE_NAME, VENUE_TABLE_NAME, SHOW_TABLE_NAME, ARTIST_SHOW_TABLE_NAME};
        for (String table: tables) {
            database.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(database);
    }

}
