package com.joemenduni.musicmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by joemenduni on 12/13/16.
 */

public class DBHelper extends SQLiteOpenHelper {


    //TASK 1: DEFINE THE DATABASE AND TABLE
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MusicMapsDB";

    private static final String GENRE_TABLE_NAME = "genre";
    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String VENUE_TABLE_NAME = "venue";
    private static final String SHOW_TABLE_NAME = "show";
    private static final String ARTIST_SHOW_TABLE_NAME = "artistToshow";

    private static final String GENRE_ID = "_id";
    private static final String GENRE_NAME = "name";

    private static final String ARTIST_id = "_id";
    private static final String ARTIST_name = "name";
    private static final String ARTIST_genre_id = "genre_id";
    private static final String ARTIST_members = "members";
    private static final String ARTIST_website = "website";
    private static final String ARTIST_picture_url = "picture_url";
    private static final String ARTIST_town = "town";
    private static final String ARTIST_state = "state";
    private static final String ARTIST_zip_code = "zip_code";

    private static final String VENUE_id = "_id";
    private static final String VENUE_name = "name";
    private static final String VENUE_website = "website";
    private static final String VENUE_picture_url = "picture_url";
    private static final String VENUE_address = "address";
    private static final String VENUE_town = "town";
    private static final String VENUE_state = "state";
    private static final String VENUE_zip_code = "zip_code";
    private static final String VENUE_latitude = "latitude";
    private static final String VENUE_longitude = "longitude";

    private static final String SHOW_id = "_id";
    private static final String SHOW_name = "name";
    private static final String SHOW_website = "website";
    private static final String SHOW_picture_url = "picture_url";
    private static final String SHOW_venue_id = "venue_id";
    private static final String SHOW_start_datetime = "start_datetime";
    private static final String SHOW_end_datetime = "end_datetime";
    private static final String SHOW_attendance = "attendance";

    private static final String ARTISTSHOW_id = "_id";
    private static final String ARTISTSHOW_artist_id = "artist_id";
    private static final String ARTISTSHOW_show_id = "show_id";

    private static int genreCount = 0;
    private static int artistCount = 0;
    private static int showCount = 0;
    private static int venueCount = 0;
    private static int artistShowCount = 0;

    private static boolean created = false;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("START DB HELPER");
        currentDB = this.getWritableDatabase();
        String[] tables = {GENRE_TABLE_NAME, ARTIST_TABLE_NAME, VENUE_TABLE_NAME, SHOW_TABLE_NAME, ARTIST_SHOW_TABLE_NAME};
        for (String table: tables) {
            currentDB.execSQL("DROP TABLE IF EXISTS " + table);
        }
        createTables();
    }

    boolean isCreating = false;
    SQLiteDatabase currentDB = null;

    @Override
    public void onCreate(SQLiteDatabase database) {
        //if (created == false) {
            String genreTable = "CREATE TABLE " + GENRE_TABLE_NAME + "(" + makeGenreTableSQL() + ")";
            String artistTable = "CREATE TABLE " + ARTIST_TABLE_NAME + "(" + makeArtistTableSQL() + ")";
            String venueTable = "CREATE TABLE " + VENUE_TABLE_NAME + "(" + makeVenueTableSQL() + ")";
            String showTable = "CREATE TABLE " + SHOW_TABLE_NAME + "(" + makeShowTableSQL() + ")";
            String artistToshowTable = "CREATE TABLE " + ARTIST_SHOW_TABLE_NAME + "(" + makeArtistShowTableSQL() + ")";
            database.execSQL(genreTable);
            database.execSQL(artistTable);
            database.execSQL(venueTable);
            //database.execSQL(showTable);
            //database.execSQL(artistToshowTable);
            //database.execSQL("CREATE INDEX artist_index ON " + ARTIST_SHOW_TABLE_NAME + " (artist_id);");
            //database.execSQL("CREATE INDEX show_index ON " + ARTIST_SHOW_TABLE_NAME + " (show_id);");
        isCreating = true;
        currentDB = database;
        makeInitialGenres();
        isCreating = false;
        currentDB = null;
    }

   /* @Override
    public SQLiteDatabase getWritableDatabase() {
        // TODO Auto-generated method stub
        if(isCreating && currentDB != null){
            return currentDB;
        }
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        // TODO Auto-generated method stub
        if(isCreating && currentDB != null){
            return currentDB;
        }
        return super.getReadableDatabase();
    }*/

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

    private void createTables() {
        currentDB = this.getWritableDatabase();
        String genreTable = "CREATE TABLE " + GENRE_TABLE_NAME + "(" + makeGenreTableSQL() + ")";
        String artistTable = "CREATE TABLE " + ARTIST_TABLE_NAME + "(" + makeArtistTableSQL() + ")";
        String venueTable = "CREATE TABLE " + VENUE_TABLE_NAME + "(" + makeVenueTableSQL() + ")";
        String showTable = "CREATE TABLE " + SHOW_TABLE_NAME + "(" + makeShowTableSQL() + ")";
        String artistToshowTable = "CREATE TABLE " + ARTIST_SHOW_TABLE_NAME + "(" + makeArtistShowTableSQL() + ")";
        currentDB.execSQL(genreTable);
        currentDB.execSQL(artistTable);
        currentDB.execSQL(venueTable);
        //database.execSQL(showTable);
        //database.execSQL(artistToshowTable);
        //database.execSQL("CREATE INDEX artist_index ON " + ARTIST_SHOW_TABLE_NAME + " (artist_id);");
        //database.execSQL("CREATE INDEX show_index ON " + ARTIST_SHOW_TABLE_NAME + " (show_id);");
        makeInitialGenres();
    }

    private String makeGenreTableSQL() {
        Map<String, String> genreDBMap = new HashMap<String, String>();
        genreDBMap.put(GENRE_ID, "INTEGER PRIMARY KEY");
        genreDBMap.put(GENRE_NAME, "TEXT");
        return makeDBStringFromMap(genreDBMap);
    }

    private String makeArtistTableSQL() {
        Map<String, String> artistDBMap = new HashMap<String, String>();
        artistDBMap.put(ARTIST_id, "INTEGER PRIMARY KEY");
        artistDBMap.put(ARTIST_name, "TEXT");
        artistDBMap.put(ARTIST_genre_id, "INTEGER");
        artistDBMap.put(ARTIST_members, "INTEGER");
        artistDBMap.put(ARTIST_website, "TEXT");
        artistDBMap.put(ARTIST_picture_url, "TEXT");
        artistDBMap.put(ARTIST_town, "TEXT");
        artistDBMap.put(ARTIST_state, "TEXT");
        artistDBMap.put(ARTIST_zip_code, "INTEGER");
        artistDBMap.put("FOREIGN KEY (" + ARTIST_genre_id + ")", "REFERENCES " + GENRE_TABLE_NAME + "(" + GENRE_ID + ")");
        return makeDBStringFromMap(artistDBMap);
    }

    private String makeVenueTableSQL() {
        Map<String, String> venueDBMap = new HashMap<String, String>();
        venueDBMap.put(VENUE_id, "INTEGER PRIMARY KEY");
        venueDBMap.put(VENUE_name, "TEXT");
        venueDBMap.put(VENUE_website, "TEXT");
        venueDBMap.put(VENUE_picture_url, "TEXT");
        venueDBMap.put(VENUE_address, "TEXT");
        venueDBMap.put(VENUE_town, "TEXT");
        venueDBMap.put(VENUE_state, "TEXT");
        venueDBMap.put(VENUE_zip_code, "INTEGER");
        venueDBMap.put(VENUE_latitude, "INTEGER");
        venueDBMap.put(VENUE_longitude, "INTEGER");
        return makeDBStringFromMap(venueDBMap);
    }

    private String makeShowTableSQL() {
        Map<String, String> showDBMap = new HashMap<String, String>();
        showDBMap.put(SHOW_id, "INTEGER PRIMARY KEY");
        showDBMap.put(SHOW_name, "TEXT");
        showDBMap.put(SHOW_website, "TEXT");
        showDBMap.put(SHOW_picture_url, "TEXT");
        showDBMap.put(SHOW_venue_id, "INTEGER");
        showDBMap.put(SHOW_start_datetime, "INTEGER");
        showDBMap.put(SHOW_end_datetime, "INTEGER");
        showDBMap.put(SHOW_attendance, "INTEGER");
        showDBMap.put("FOREIGN KEY (" + SHOW_venue_id + ")", "REFERENCES " + VENUE_TABLE_NAME + "(" + VENUE_id + ")");
        return makeDBStringFromMap(showDBMap);
    }

    private String makeArtistShowTableSQL() {
        Map<String, String> artistshowDBMap = new HashMap<String, String>();
        artistshowDBMap.put(ARTISTSHOW_id, "INTEGER PRIMARY KEY");
        artistshowDBMap.put(ARTISTSHOW_artist_id, "INTEGER");
        artistshowDBMap.put(ARTISTSHOW_show_id, "INTEGER");
        artistshowDBMap.put("FOREIGN KEY (" + ARTISTSHOW_artist_id + ")", "REFERENCES " + ARTIST_TABLE_NAME + "(" + ARTIST_id + ") ON DELETE CASCADE");
        artistshowDBMap.put("FOREIGN KEY (" + ARTISTSHOW_show_id + ")", "REFERENCES " + SHOW_TABLE_NAME + "(" + SHOW_id + ") ON DELETE CASCADE");
        return makeDBStringFromMap(artistshowDBMap);
    }

    private String makeDBStringFromMap(Map<String, String> dbMap) {
        String returnString = "";
        Iterator iterator = dbMap.entrySet().iterator();
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

    private void makeInitialGenres() {
        System.out.println("MAKE INITIAL");
        String[] genres = {"Rock 'n Roll", "Pop", "Heavy Metal", "Rap", "Country", "Punk", "R & B", "Jazz", "Classical", "Alternative", "Hip Hop", "Soul", "Reggae", "Techno", "Grunge", "EDM", "Hard Rock", "Blues"};
        for (String genre: genres) {
            genreCount ++;
            ContentValues values = new ContentValues();
            values.put(GENRE_ID, genreCount);
            values.put(GENRE_NAME, genre);
            this.getWritableDatabase().insert(GENRE_TABLE_NAME, null, values);
        }
    }

    public List<String> getAllGenres() {
        List<String> genreList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GENRE_TABLE_NAME + ";";
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        if ((cursor.moveToFirst())) {
            do {
                genreList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return genreList;
    }

    public Map<Integer, String> getAllArtists() {
        Map<Integer, String> artistMap = new HashMap<Integer, String>();
        String selectQuery = "SELECT * FROM " + ARTIST_TABLE_NAME + ";";
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        if ((cursor.moveToFirst())) {
            do {
                artistMap.put(cursor.getInt(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return artistMap;
    }


    public void addArtist(String theName, String theGenre, int theMembers, String theWebsite, String thePictureURL, String theTown, String theState, String theZipCode) {
        artistCount ++;
        ContentValues values = new ContentValues();
        values.put(ARTIST_id, artistCount);
        values.put(ARTIST_name, theName);
        values.put(ARTIST_genre_id, theGenre);
        values.put(ARTIST_members, theMembers);
        values.put(ARTIST_website, theWebsite);
        values.put(ARTIST_picture_url, thePictureURL);
        values.put(ARTIST_town, theTown);
        values.put(ARTIST_state, theState);
        values.put(ARTIST_zip_code, theZipCode);
        currentDB.insert(ARTIST_TABLE_NAME, null, values);
    }

    public void addShow(String theName, String theVenue, String theWebsite, String thePictureURL, String[] theArtists, String theStartDateTime, String theEndDateTime) {
        showCount ++;
        ContentValues values = new ContentValues();
        values.put(SHOW_id, showCount);
        values.put(SHOW_name, theName);
        values.put(SHOW_venue_id, theVenue);
        values.put(SHOW_website, theWebsite);
        values.put(SHOW_picture_url, thePictureURL);
        values.put(SHOW_start_datetime, theStartDateTime);
        values.put(SHOW_end_datetime, theEndDateTime);
        currentDB.insert(SHOW_TABLE_NAME, null, values);
    }

    public int findArtistByName(String artistName) {
        Map<Integer, String> artistMap = getAllArtists();
        Iterator it = artistMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (artistName.equals(pair.getValue())) {
                return (int) pair.getKey();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return -1;
    }

    private void makeArtistsToShows(int show_id, String[] artists) {
       for (String artistName: artists) {
           artistShowCount ++;
           ContentValues values = new ContentValues();
           values.put(ARTISTSHOW_id, artistShowCount);
           values.put(ARTISTSHOW_show_id, show_id);
           int artistID = findArtistByName(artistName);
           if (findArtistByName(artistName) == -1) {
               artistCount ++;
               addArtist(artistName, null, -1, null, null, null, null, null);
           }
           values.put(ARTISTSHOW_artist_id, artistCount);
           currentDB.insert(ARTIST_SHOW_TABLE_NAME, null, values);
        }
    }
}
