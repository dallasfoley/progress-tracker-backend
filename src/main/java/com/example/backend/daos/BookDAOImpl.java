package com.example.backend.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.connection.ConnectionManager;
import com.example.backend.models.Book;

public class BookDAOImpl implements BookDAO {

  private Connection conn;

  @Override
  public List<Book> findAll() {
    List<Book> books = new ArrayList<>();
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM books");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setYearPublished(rs.getInt("year_published"));
        book.setGenre(rs.getString("genre"));
        book.setRating(rs.getDouble("rating"));
        book.setPageCount(rs.getInt("page_count"));
        book.setGenre(rs.getString("genre"));
        book.setCoverUrl(rs.getString("cover_url"));
        books.add(book);
      }
    } catch (IOException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  @Override
  public Book findById(int id) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE id = ?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setYearPublished(rs.getInt("year_published"));
        book.setGenre(rs.getString("genre"));
        book.setRating(rs.getDouble("rating"));
        book.setPageCount(rs.getInt("page_count"));
        book.setGenre(rs.getString("genre"));
        book.setCoverUrl(rs.getString("cover_image"));
        return book;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Book> findByTitle(String title) {
    List<Book> books = new ArrayList<>();
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE title LIKE ?");
      ps.setString(1, "%" + title + "%");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setYearPublished(rs.getInt("year_published"));
        book.setGenre(rs.getString("genre"));
        book.setRating(rs.getDouble("rating"));
        book.setPageCount(rs.getInt("page_count"));
        book.setGenre(rs.getString("genre"));
        book.setCoverUrl(rs.getString("cover_url"));
        books.add(book);
      }
    } catch (IOException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  @Override
  public List<Book> findByAuthor(String author) {
    List<Book> books = new ArrayList<>();
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE author LIKE ?");
      ps.setString(1, "%" + author + "%");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setYearPublished(rs.getInt("year_published"));
        book.setGenre(rs.getString("genre"));
        book.setRating(rs.getDouble("rating"));
        book.setPageCount(rs.getInt("page_count"));
        book.setGenre(rs.getString("genre"));
        book.setCoverUrl(rs.getString("cover_url"));
        books.add(book);
      }
    } catch (IOException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  @Override
  public boolean save(String title, String author, int yearPublished, String genre) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO books (title, author, year_published, genre) VALUES (?, ?, ?, ?)");
      ps.setString(1, title);
      ps.setString(2, author);
      ps.setInt(3, yearPublished);
      ps.setString(4, genre);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (IOException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean delete(int id) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id = ?");
      ps.setInt(1, id);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (IOException | ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}
