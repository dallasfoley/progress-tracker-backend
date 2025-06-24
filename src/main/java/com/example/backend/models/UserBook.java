package com.example.backend.models;

public class UserBook {

  private int userId;
  private int bookId;
  private ReadingStatus status;
  private int userRating;
  private int currentPage;

  public UserBook() {

  }

  public UserBook(int userId, int bookId, ReadingStatus status, int userRating, int currentPage) {
    this.userId = userId;
    this.bookId = bookId;
    this.status = status;
    this.userRating = userRating;
    this.currentPage = currentPage;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getBookId() {
    return bookId;
  }

  public void setBookId(int bookId) {
    this.bookId = bookId;
  }

  public ReadingStatus getStatus() {
    return status;
  }

  public void setStatus(ReadingStatus status) {
    this.status = status;
  }

  public int getUserRating() {
    return userRating;
  }

  public void setUserRating(int userRating) {
    this.userRating = userRating;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

}
