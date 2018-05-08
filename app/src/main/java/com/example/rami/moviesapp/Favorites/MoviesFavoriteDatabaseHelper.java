package com.example.rami.moviesapp.Favorites;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public  class MoviesFavoriteDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "Favorites";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "Movies";
    public final static String UID = "_id";
    public final static String  POSTER = "poster";
    public final static String TITEL = "titel";
    public final static String DATE = "date";
    public final static String VOTE = "vote";
    public final static String OVERVIEW = "overview";
    public static final String REVIEW = "review";
    public final static String POSTER_ID = "POSTER_ID";
    public final static String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+POSTER+" VARCHAR(255) ,"+TITEL+" VARCHAR(255) ,"+DATE+" VARCHAR(255) ,"+VOTE+" VARCHAR(255) ,"+OVERVIEW+" VARCHAR(255) ,"+REVIEW+" VARCHAR(255) ,"+POSTER_ID+" INTEGER);";
    public final static String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    public Context context;
    public MoviesFavoriteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }catch (SQLException e){
            Toast.makeText(context, "Sorry there is Error in create your database \n"+e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }catch (SQLException e)
        {
            Toast.makeText(context, "Sorry there is Error in upgrade your database \n"+e, Toast.LENGTH_SHORT).show();
        }
    }
}