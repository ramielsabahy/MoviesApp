package com.example.rami.moviesapp.MyMovieData;

/**
 * Created by RAMI on 25/10/2016.
 */
public class SettersAndGetters {
    private String poster_url;
    private String id;
    private String Title;
    private String date;
    private String vote;
    private String overview;
    private String review;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getMovieOverView() {
        return MovieOverView;
    }

    public void setMovieOverView(String movieOverView) {
        MovieOverView = movieOverView;
    }

    private String MovieOverView;

    public String getPoster_url() {
        return poster_url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return date;
    }

    public String getVote() {
        return vote;
    }

    public String getOverview() {
        return overview;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}
