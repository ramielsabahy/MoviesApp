package com.example.rami.moviesapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rami.moviesapp.Favorites.MoviesFavoriteAdapter;
import com.example.rami.moviesapp.MyMovieData.SettersAndGetters;
import com.example.rami.moviesapp.Trailers.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieMainGrid extends AppCompatActivity {

    GridView gridView;

    public SettersAndGetters[] movieData = null;
    boolean tabletMode = false;
    Context context;
    private ArrayList<String> movieArrayList = null;
    TrailerAdapter trailerAdapter;
    GridView gridTrailer;
    MovieAdapter movieAdapter;
    static TextView Datetxt;
    static TextView RatingTxt;
    static TextView movieOV;

    static ImageView poster;
    static TextView review;

    MoviesFavoriteAdapter FavAdapter;
    static TextView Title;
    String id;
    String title;
    String Review;
    static View fragDetail;
    static String SortingState = "popular?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
        toolbar.setLogo(R.mipmap.logo);

        fragDetail = findViewById(R.id.fragment2);
        poster = (ImageView) findViewById(R.id.movieImage);
        Title = ((TextView) findViewById(R.id.movieName));
        Datetxt = (TextView) findViewById(R.id.movieDate);
        RatingTxt = ((TextView) findViewById(R.id.movieAvrg));
        movieOV = (TextView) findViewById(R.id.movieOver);
        review = ((TextView) findViewById(R.id.movieReview));
        gridView = (GridView) findViewById(R.id.grid_view);


        context = getBaseContext();


        if (isNetworkAvailable()) {
            executeTask();
        } else {
            Toast.makeText(getBaseContext(), "Please make sure that you have Internet connection", Toast.LENGTH_LONG).show();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (size.x > size.y || screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            View viewGroup = (View) findViewById(R.id.fragment2);
            viewGroup.setVisibility(View.INVISIBLE);
            tabletMode = true;
            MoviesDetailsFragment.LandScape = true;
        } else {
            tabletMode = false;
            MoviesDetailsFragment.LandScape = false;
        }
        fragmentTransaction.commit();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (tabletMode == false) {
                    Intent Deatail = new Intent(context, MoviesDetailsClass.class);
                    Deatail.putExtra("poster_url", movieData[index].getPoster_url());
                    Deatail.putExtra("title", movieData[index].getTitle());
                    Deatail.putExtra("date", movieData[index].getDate());
                    Deatail.putExtra("vote", movieData[index].getVote());
                    Deatail.putExtra("review", movieData[index].getOverview());
                    Deatail.putExtra("id", movieData[index].getId());
                    Deatail.putExtra("theoverview", movieData[index].getMovieOverView());
                    startActivity(Deatail);
                } else {
                    title = movieData[index].getTitle();
                    String date = movieData[index].getDate();
                    String vote = movieData[index].getVote();
                    Review = movieData[index].getOverview();
                    String Movieoverview = movieData[index].getMovieOverView();
                    id = movieData[index].getId();
                    fragDetail.setVisibility(View.VISIBLE);
                    String baseUrl = "http://image.tmdb.org/t/p/w185";
                    String poster_url = baseUrl + movieData[index].getPoster_url();
                    MoviesDetailsFragment.poster_url = poster_url;
                    MoviesDetailsFragment.MovieTitle = title;
                    MoviesDetailsFragment.Date = date;
                    MoviesDetailsFragment.ReView = Review;
                    MoviesDetailsFragment.id = id;
                    Picasso.with(context).load(poster_url).into(poster);
                    Title.setText(title);
                    Datetxt.setText("Date :" + date);
                    RatingTxt.setText("Rate : " + vote );
                    movieOV.setText("Over View : " + Movieoverview);

                    review.setText(Review);

                    final Button favorite = (Button) findViewById(R.id.favorite);

                    favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MoviesDetailsFragment.favBtn();
                        }
                    });

                    gridTrailer = (GridView) findViewById(R.id.trailer_view);
                    MovieTask2 task = new MovieTask2();
                    task.execute(id);
                    MovieTask3 task2 = new MovieTask3();
                    task2.execute(id);
                    gridTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieArrayList.get(i))));
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("state", SortingState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SortingState = savedInstanceState.getString("state");
        if (SortingState == null) {
            SortingState = "popular?";
        }
        executeTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.favorite_menu) {
            FavAdapter = new MoviesFavoriteAdapter(context);
            movieData = FavAdapter.getMovieData();
            //System.out.println(movieData[0].getPoster_url());
            movieAdapter = new MovieAdapter(context, movieData);
            gridView.setAdapter(movieAdapter);
            return true;


        }


        if (id == R.id.top_rated_menu) {
            SortingState = "top_rated?";
            executeTask();
            return true;
        }
        if (id == R.id.popular_menu) {
            SortingState = "popular?";
            executeTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void executeTask() {
        MovieTask task = new MovieTask();
        task.execute(SortingState);
    }


    public class MovieTask extends AsyncTask<String, Void, SettersAndGetters[]> {


        @Override
        protected SettersAndGetters[] doInBackground(String... params) {

            Log.d("message", "message");
            String FORECAST_BASE_URL =
                    "https://api.themoviedb.org/3/movie/" + params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            try {
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.d("message", "InputStream");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    Log.d("message", "buffer");
                    return null;
                }
                JsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("error", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("error", "On Close Error", e);
                    }
                }
            }
            try {
                Log.d("message", "data");

                return getDataFromJson(JsonStr);
            } catch (JSONException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            Log.d("message", "null");
            return null;
        }

        @Override
        protected void onPostExecute(SettersAndGetters[] movieData) {
            fetchData(movieData);
        }

        private SettersAndGetters[] getDataFromJson(String jsonStr) throws JSONException {
            final String jsonResults = "results";
            final String poster_path = "poster_path";
            final String release_date = "release_date";
            final String vote_average = "vote_average";
            final String overview = "overview";
            final String original_title = "original_title";
            final String id = "id";
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(jsonResults);
            movieData = new SettersAndGetters[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {
                movieData[i] = new SettersAndGetters();
                JSONObject movie = movieArray.getJSONObject(i);
                String poster = movie.getString(poster_path);
                String release = movie.getString(release_date);
                String vote = movie.getString(vote_average);
                String overview1 = movie.getString(overview);
                String title = movie.getString(original_title);
                String _id = movie.getString(id);
                movieData[i].setPoster_url(poster);
                movieData[i].setDate(release);
                movieData[i].setVote(vote);
                movieData[i].setTitle(title);
                movieData[i].setOverview(overview1);
                movieData[i].setMovieOverView(overview1);
                Log.d("overView", overview1);

                movieData[i].setId(_id);
            }
            return movieData;
        }

        public void fetchData(SettersAndGetters moviesData[]) {
            if (moviesData != null) {
                movieData = moviesData;
                movieAdapter = new MovieAdapter(getBaseContext(), moviesData);
                gridView.setAdapter(movieAdapter);

            } else
                Log.d("message", "No Data");
        }
    }

    public class MovieTask2 extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            Log.d("message", "message");
            String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;


            try {

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.d("message", "InputStream");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.d("message", "buffer");

                    return null;
                }
                JsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("error", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("error", "Error closing stream", e);
                    }
                }
            }

            try {
                Log.d("message", "data");

                return getDataFromJson(JsonStr);
            } catch (JSONException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            Log.d("message", "null");
            return null;
        }

        @Override
        protected void onPostExecute(String[] movie) {
            if (movie != null) {
                movieArrayList = new ArrayList<>();
                for (int i = 0; i < movie.length; i++) {
                    movieArrayList.add("https://www.youtube.com/watch?v=" + movie[i]);
                }
                gridTrailer = (GridView) findViewById(R.id.trailer_view);
                trailerAdapter = new TrailerAdapter(context, movieArrayList);
                gridTrailer.setAdapter(trailerAdapter);
            }
        }

        private String[] getDataFromJson(String jsonStr) throws JSONException {
            final String M_LIST = "results";
            final String key = "key";
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(M_LIST);

            String[] results = new String[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                String poster_key = movie.getString(key);
                results[i] = poster_key;
            }
            return results;
        }

    }

    public class MovieTask3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d("message", "message");
            String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;


            try {

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.d("message", "InputStream");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.d("message", "buffer");

                    return null;
                }
                JsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("error", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("error", "Error closing stream", e);
                    }
                }
            }

            try {
                Log.d("message", "data");

                return getDataFromJson(JsonStr);
            } catch (JSONException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }

            Log.d("message", "null");
            return null;
        }

        @Override
        protected void onPostExecute(String movie) {
            if (movie != null) {
                review.setText(movie);
            }
        }

        private String getDataFromJson(String jsonStr) throws JSONException {
            final String M_LIST = "results";
            final String author = "author";
            final String content = "content";
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(M_LIST);

            String review = null;

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                String Author = movie.getString(author);
                String Content = movie.getString(content);
                review = Author + "\n" + Content;
            }
            return review;
        }

    }
}
