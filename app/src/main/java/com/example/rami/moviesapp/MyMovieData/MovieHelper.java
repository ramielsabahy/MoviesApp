package com.example.rami.moviesapp.MyMovieData;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by RAMI on 27/10/2016.
 */
public class MovieHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = MyContract.movieEntry.DATABASE_NAME;

    public static final int DATABASE_VERSION = MyContract.movieEntry.DATABASE_VERSION;
    public Context context;

    public MovieHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE "+MyContract.movieEntry.TABLE_NAME
                +"("+MyContract.movieEntry.UID+" INTEGER AUTO_INCREMENT PRIMARY KEY ,"
                +MyContract.movieEntry.POSTER+" VARCHAR(255) ,"
                +MyContract.movieEntry.TITEL+" VARCHAR(255) ,"
                +MyContract.movieEntry.DATE+" VARCHAR(255) ,"
                +MyContract.movieEntry.VOTE+" VARCHAR(255) ,"
                +MyContract.movieEntry.OVERVIEW+" VARCHAR(255) ,"
                +MyContract.movieEntry.POSTER_ID+" INTEGER )";
        try {
            db.execSQL(SQL);
        }catch (SQLException e)
        {
            Toast.makeText(context, "Sorry there is Error in upgrade your database \n" + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String SQL = MyContract.movieEntry.DROP_TABLE;
            db.execSQL(SQL);
            onCreate(db);
        }catch (SQLException e)
        {
            Toast.makeText(context, "Sorry there is Error in upgrade your database \n" + e, Toast.LENGTH_SHORT).show();
        }

    }
}
