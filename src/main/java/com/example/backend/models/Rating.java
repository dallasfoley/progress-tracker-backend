package com.example.backend.models;

public enum Rating {
  ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

  private int rating;

  private Rating(int rating) {
    this.rating = rating;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public static Rating fromInt(int rating) {
    switch (rating) {
      case 0:
        return ZERO;
      case 1:
        return ONE;
      case 2:
        return TWO;
      case 3:
        return THREE;
      case 4:
        return FOUR;
      case 5:
        return FIVE;
      default:
        return ZERO;
    }

  }

}
