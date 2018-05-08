package com.example.rami.moviesapp.Favorites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rami.moviesapp.MyMovieData.SettersAndGetters;

/**
 * Created by RAMI on 25/10/2016.
 */
public class MoviesFavoriteAdapter {
    MoviesFavoriteDatabaseHelper moviesFavoriteDatabaseHelper;
    Context context;

    public MoviesFavoriteAdapter(Context context) {
        moviesFavoriteDatabaseHelper = new MoviesFavoriteDatabaseHelper(context);
        this.context = context;
    }


    public long insertData(String poster_id, String POSTER, String TITLE, String DATE, String VOTE, String OVERVIEW,String REVIEW) {
        SQLiteDatabase sqLiteDatabase = moviesFavoriteDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesFavoriteDatabaseHelper.POSTER_ID, poster_id);
        contentValues.put(MoviesFavoriteDatabaseHelper.POSTER, POSTER);
        contentValues.put(MoviesFavoriteDatabaseHelper.TITEL, TITLE);
        contentValues.put(MoviesFavoriteDatabaseHelper.DATE, DATE);
        contentValues.put(MoviesFavoriteDatabaseHelper.VOTE, VOTE);
        contentValues.put(MoviesFavoriteDatabaseHelper.OVERVIEW, OVERVIEW);
        contentValues.put(MoviesFavoriteDatabaseHelper.REVIEW , REVIEW);



        long id = sqLiteDatabase.insert(MoviesFavoriteDatabaseHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public SettersAndGetters[] getMovieData() {


        int i = 0;
        SQLiteDatabase db = moviesFavoriteDatabaseHelper.getWritableDatabase();
        String[] columns = {MoviesFavoriteDatabaseHelper.UID, MoviesFavoriteDatabaseHelper.POSTER, MoviesFavoriteDatabaseHelper.TITEL, MoviesFavoriteDatabaseHelper.DATE, MoviesFavoriteDatabaseHelper.VOTE, MoviesFavoriteDatabaseHelper.OVERVIEW};
        Cursor cursor = db.query(MoviesFavoriteDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        int counter = 0;
        while (cursor.moveToNext()) {
            counter = cursor.getInt(0);
        }
        SQLiteDatabase db2 = moviesFavoriteDatabaseHelper.getWritableDatabase();
        String[] secondColumns = {MoviesFavoriteDatabaseHelper.UID, MoviesFavoriteDatabaseHelper.POSTER, MoviesFavoriteDatabaseHelper.TITEL, MoviesFavoriteDatabaseHelper.DATE, MoviesFavoriteDatabaseHelper.VOTE, MoviesFavoriteDatabaseHelper.OVERVIEW};
        Cursor cursor2 = db2.query(MoviesFavoriteDatabaseHelper.TABLE_NAME, secondColumns, null, null, null, null, null);
        SettersAndGetters[] movieData = new SettersAndGetters[counter];
        while (cursor2.moveToNext()) {

            String POSTER = cursor2.getString(1);
            String TITEL = cursor2.getString(2);
            String DATE = cursor2.getString(3);
            String VOTE = cursor2.getString(4);
            String OVERVIE = cursor2.getString(5);
            String REVIEW = "any";
            String poster_id = "dd";
            movieData[i] = new SettersAndGetters();
            movieData[i].setPoster_url(POSTER);
            movieData[i].setDate(DATE);
            movieData[i].setVote(VOTE);
            movieData[i].setTitle(TITEL);
            movieData[i].setOverview(OVERVIE);
            movieData[i].setId(poster_id);
            movieData[i].setReview(REVIEW);
            i++;
            System.out.println(REVIEW);
        }

        return movieData;
    }

    public String getData(String Poster_Id) {
        SQLiteDatabase db = moviesFavoriteDatabaseHelper.getWritableDatabase();
        String[] columns = {MoviesFavoriteDatabaseHelper.UID, MoviesFavoriteDatabaseHelper.POSTER, MoviesFavoriteDatabaseHelper.TITEL, MoviesFavoriteDatabaseHelper.DATE, MoviesFavoriteDatabaseHelper.VOTE, MoviesFavoriteDatabaseHelper.OVERVIEW};
        Cursor cursor = db.query(MoviesFavoriteDatabaseHelper.TABLE_NAME, columns, MoviesFavoriteDatabaseHelper.POSTER_ID + "='" + Poster_Id + "'", null, null, null, null, null);
        String data = null;
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(MoviesFavoriteDatabaseHelper.POSTER);
            data = cursor.getString(index1);
        }

        return data;
    }


}
