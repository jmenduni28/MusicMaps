package com.joemenduni.musicmaps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by joemenduni on 10/11/16.
 */
public class SQLiteDBHelper {

    private static String dbName = "NYPDRecords";

    public void createDBandTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + "~/Documents/" + dbName + ".db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE records " +
                    "(objectID       INT     NOT NULL," +
                    " identifier    TEXT PRIMARY KEY    NOT NULL, " +
                    " occurDate     INT     NOT NULL," +
                    " dayOfWeek     TEXT    NOT NULL," +
                    " occurMonth    TEXT    NOT NULL," +
                    " occurDay      INT     NOT NULL," +
                    " occurYear     INT     NOT NULL," +
                    " occurHour     INT     NOT NULL," +
                    " compStatMonth INT     NOT NULL," +
                    " compStatDay   INT     NOT NULL," +
                    " compStatYear  INT     NOT NULL," +
                    " offense       TEXT    NOT NULL," +
                    " offenseClass  TEXT    NOT NULL," +
                    " sector        TEXT    NOT NULL," +
                    " precinct      INT     NOT NULL," +
                    " borough       TEXT    NOT NULL," +
                    " jurisdiction  TEXT    NOT NULL," +
                    " xCoo          REAL    NOT NULL," +
                    " yCoo          REAL    NOT NULL," +
                    " xCoo2         REAL    NOT NULL," +
                    " yCoo2         REAL    NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

}
