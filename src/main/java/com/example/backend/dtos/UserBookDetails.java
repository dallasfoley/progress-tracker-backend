package com.example.backend.dtos;

import com.example.backend.models.ReadingStatus;

public class UserBookDetails {
  private int userId;
  private int bookId;
  private ReadingStatus status;
  private int userRating;
  private int currentPage;
  private String title;
  private String author;
  private int yearPublished;
  private String genre;
  private double rating;
  private int pageCount;
  private String description;
  private String coverUrl;

  public UserBookDetails() {

  }

  public UserBookDetails(int userId, int bookId, ReadingStatus status, int userRating, int currentPage, String title,
      String author, int yearPublished, String genre, double rating, int pageCount, String description,
      String coverUrl) {
    this.userId = userId;
    this.bookId = bookId;
    this.status = status;
    this.userRating = userRating;
    this.currentPage = currentPage;
    this.title = title;
    this.author = author;
    this.yearPublished = yearPublished;
    this.genre = genre;
    this.rating = rating;
    this.pageCount = pageCount;
    this.description = description;
    this.coverUrl = coverUrl;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getYearPublished() {
    return yearPublished;
  }

  public void setYearPublished(int yearPublished) {
    this.yearPublished = yearPublished;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

}