package com.example.rami.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.rami.moviesapp.MyMovieData.SettersAndGetters;
import com.squareup.picasso.Picasso;

/**
 * Created by RAMI on 25/10/2016.
 */
public class MovieAdapter extends BaseAdapter {

    Context context;
    SettersAndGetters[] movieData;
    public MovieAdapter(Context context, SettersAndGetters[] movieData) {
        this.context = context;
        this.movieData = movieData;
    }

    @Override
    public int getCount() {
        return movieData.length;
    }

    @Override
    public Object getItem(int i) {
        return movieData[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.movie_list,viewGroup,false);
        }
        ImageView imgView = (ImageView) view.findViewById(R.id.list_item_movie_imageView);
        String baseUrl = "http://image.tmdb.org/t/p/w185";
        String poster_url = baseUrl+movieData[i].getPoster_url();
        System.out.println(poster_url);
        if(poster_url.contains("http://image.tmdb.org/t/p/w185http://image.tmdb.org/t/p/w185")){
            baseUrl="";
            poster_url = baseUrl+movieData[i].getPoster_url();
        }
        Picasso.with(context).load(poster_url).into(imgView);
        return view;
    }
}
