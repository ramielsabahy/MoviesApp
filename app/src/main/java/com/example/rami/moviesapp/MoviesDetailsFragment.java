package com.example.rami.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rami.moviesapp.Favorites.MoviesFavoriteAdapter;
import com.example.rami.moviesapp.MyMovieData.MyContract;
//import com.example.rami.moviesapp.Trailers.TrailerAdapter;
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


public class MoviesDetailsFragment extends Fragment {
    static String ReView;
    static String shortOV;
    static String id;
    static View view;
    static Context context;
    TextView review;
    static String poster_url;
    static String MovieTitle;
    static String Date;
    static String Rating;

    String myOV;
    String myRev;

    Intent Intent;
    static boolean LandScape = true ;
    private ArrayList<String> movieArrayList = null;
    static MoviesFavoriteAdapter FavAdapter;
    TrailerAdapter trailerAdapter;
    GridView trailergGridView;

    public MoviesDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        context = getActivity();

        if(LandScape ==true){
            Toast.makeText(getActivity(), "LandScape Mode", Toast.LENGTH_SHORT).show();

        }else {
            Intent = getActivity().getIntent();
            poster_url = Intent.getStringExtra("poster_url");
            MovieTitle = Intent.getStringExtra("title");
            Date = Intent.getStringExtra("date");
            Rating = Intent.getStringExtra("vote");
            ReView = Intent.getStringExtra("review");
            id = Intent.getStringExtra("id");
            shortOV=Intent.getStringExtra("theoverview");
            MovieTask task = new MovieTask();
            task.execute(id);
            MovieTask3 secondTask = new MovieTask3();
            secondTask.execute(id);
            ImageView poster = (ImageView) view.findViewById(R.id.movieImage);

            final TextView Title = ((TextView) view.findViewById(R.id.movieName));
            final TextView date = ((TextView) view.findViewById(R.id.movieDate));
            final TextView vote = ((TextView) view.findViewById(R.id.movieAvrg));
            final TextView movieOV = (TextView)view.findViewById(R.id.movieOver);


            String baseUrl;
            // Setting Pic
            if(poster_url.contains("http://image.tmdb.org/t/p/w185")){
                baseUrl = "";
            }else {
                baseUrl = "http://image.tmdb.org/t/p/w185";
            }
            poster_url = baseUrl+poster_url;
            //myOV = shortOV;
            //myRev = ReView;
            Picasso.with(getActivity()).load(poster_url).into(poster);
            System.out.println(poster_url);
            //Setting Data
            if(ReView.contains("Overview : Overview"))
                ReView="";

            movieOV.setText("Overview : "+ReView);
            Title.setText(MovieTitle);
            date.setText("Date : " + Date);
            vote.setText("Rate : "+Rating);
            review = ((TextView) view.findViewById(R.id.movieReview));
            review.setText(ReView);


            final Button favorite = (Button) view.findViewById(R.id.favorite);
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues values = new ContentValues();
                    //System.out.println(movieOV.getText().toString());
                    FavAdapter = new MoviesFavoriteAdapter(context);
                    if (FavAdapter.getData(id) == null) {
                        long FAVinsert = FavAdapter.insertData(id, poster_url, MovieTitle, Date, Rating, movieOV.getText().toString(),review.getText().toString());
                        if (FAVinsert != -1) {
                            values.put(MyContract.movieEntry.TITEL,Title.getText().toString());
                            values.put(MyContract.movieEntry.POSTER_ID,id);
                            values.put(MyContract.movieEntry.POSTER,poster_url);
                            values.put(MyContract.movieEntry.OVERVIEW,movieOV.getText().toString());
                            values.put(MyContract.movieEntry.REVIEW,review.getText().toString());
                            values.put(MyContract.movieEntry.DATE, date.getText().toString());
                            values.put(MyContract.movieEntry.VOTE, vote.getText().toString());
                            Toast.makeText(context, "Movie Added To your Favorite List", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Error adding Movie", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "It is already in your List", Toast.LENGTH_SHORT).show();
                    }


                    //Uri uri = getActivity().getContentResolver().insert(MyContract.movieEntry.CONTENT_URI, values);
                    //Log.e("Output Uri: ", uri.toString());
                    //favBtn();
                }
            });
            trailergGridView = (GridView) view.findViewById(R.id.trailer_view);
            trailergGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieArrayList.get(i))));
                }
            });

        }
        this.setHasOptionsMenu(true);
        return view;
    }



    public static void favBtn(){
        FavAdapter = new MoviesFavoriteAdapter(context);
        if (FavAdapter.getData(id) == null) {
            long FAVinsert = FavAdapter.insertData(id, poster_url, MovieTitle, Date, Rating, shortOV,ReView);
            if (FAVinsert != -1) {
                Toast.makeText(context, "Movie Added To Favorite List", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "Movie Can't be added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "It is in your List", Toast.LENGTH_SHORT).show();
        }
    }

        public class MovieTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            Log.d("message", "message");
            String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/movie/"+ params[0]+"/videos?";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;


            try {

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM,BuildConfig.MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
try {


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

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

                return getDataFromJson(JsonStr);
            } catch (JSONException e) {
                Log.e("error", e.getMessage(), e);
                e.printStackTrace();
            }
            }catch (Exception e){

            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] movie) {
            if (movie != null) {
                movieArrayList = new ArrayList<>();
                for (int i = 0; i < movie.length; i++) {
                    movieArrayList.add("https://www.youtube.com/watch?v=" + movie[i]);
                }
                trailergGridView = (GridView) view.findViewById(R.id.trailer_view);
                trailerAdapter = new TrailerAdapter(getActivity(), movieArrayList);
                trailergGridView.setAdapter(trailerAdapter);
            }
        }

        private String[] getDataFromJson(String jsonStr) throws JSONException {
            final String jsonResults = "results";
            final String key = "key";
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(jsonResults);

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
                    "http://api.themoviedb.org/3/movie/"+ params[0]+"/reviews?";

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

            // This will only happen if there was an error getting or parsing the forecast.
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
            final String jsonResults = "results";
            final String author = "author";
            final String content = "content";
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray(jsonResults);

            String review = null;

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                String auther = movie.getString(author);
                String contnt = movie.getString(content);
                review = auther+"\n"+contnt;
            }
            return review;
        }

    }

}
