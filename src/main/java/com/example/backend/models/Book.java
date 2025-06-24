package com.example.backend.models;

public class Book {

  private int id;
  private String title;
  private String author;
  private int yearPublished;
  private String genre;
  private double rating;
  private int pageCount;
  private String coverUrl;
  private String description;

  public Book() {

  }

  public Book(int id, String title, String author, int yearPublished, String genre) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.yearPublished = yearPublished;
    this.genre = genre;
    this.rating = 0.0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
