package com.example.rami.moviesapp.MyMovieData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by RAMI on 27/10/2016.
 */
public class MovieProvider extends ContentProvider {

    static final UriMatcher uriMatcher = buildUriMatcher();

    public MovieHelper movieHelper;



    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;

    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authurity = MyContract.AUTHURITY;
        matcher.addURI(authurity,MyContract.PATH,MOVIE);
        matcher.addURI(authurity,MyContract.PATH+"/*",MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor ;

        final SQLiteDatabase db = movieHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch(match){
            case MOVIE :
                cursor = db.query(MyContract.movieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MOVIE_WITH_ID :
                long _id = ContentUris.parseId(uri);
                cursor = db.query(MyContract.movieEntry.TABLE_NAME,projection,MyContract.movieEntry.UID+"=?",new String[] {String.valueOf(_id)},null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);
        switch(match){
            case MOVIE :
                return MyContract.movieEntry.CONTENT_TYPE_DIR;
            case MOVIE_WITH_ID :
                return MyContract.movieEntry.CONTENT_TYPE_ITEM;
            default :
                throw new UnsupportedOperationException("Unknown URI : "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = movieHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri ;

        switch (match){
            case MOVIE : {
                long _id = db.insert(MyContract.movieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MyContract.movieEntry.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
