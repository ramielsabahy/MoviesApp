package com.example.rami.moviesapp.MyMovieData;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by RAMI on 27/10/2016.
 */
public class MyContract {

    public static final String AUTHURITY = "com.example.rami.moviesapp.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHURITY);
    public static final String PATH = "Movies";

    public static final class movieEntry {

        public final static String DATABASE_NAME = "Favorites";
        public final static int DATABASE_VERSION = 1;
        public final static String TABLE_NAME = "Movies";
        public final static String UID = "_id";
        public final static String POSTER = "poster";
        public final static String TITEL = "titel";
        public final static String DATE = "date";
        public final static String VOTE = "vote";
        public final static String OVERVIEW = "overview";
        public final static String REVIEW = "review";
        public final static String POSTER_ID = "POSTER_ID";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHURITY + "/" + PATH;
        public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHURITY + "/" + PATH;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
