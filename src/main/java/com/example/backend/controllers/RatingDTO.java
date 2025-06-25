package com.example.backend.controllers;

import com.example.backend.models.Rating;

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
