package com.sample.honeybuser.Models;

/**
 * Created by Im033 on 10/14/2016.
 */

public class Ratings {
    private String username;
    private String star_rating;
    private String review;
    private String date_time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(String star_rating) {
        this.star_rating = star_rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }


}
