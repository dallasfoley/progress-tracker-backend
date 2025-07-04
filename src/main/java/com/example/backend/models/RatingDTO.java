package com.example.backend.models;

public class RatingDTO {

  private Rating rating;

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  public void setRating(int rating) {
    this.rating = Rating.values()[rating];
  }

}
