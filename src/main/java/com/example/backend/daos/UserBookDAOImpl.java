package com.example.backend.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.example.backend.connection.ConnectionManager;
import com.example.backend.models.ReadingStatus;
import com.example.backend.models.UserBook;

public class UserBookDAOImpl implements UserBookDAO {

  private Connection conn;

  @Override
  public UserBook findById(int userId, int bookId) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_books WHERE user_id = ? AND book_id = ?");
      ps.setInt(1, userId);
      ps.setInt(2, bookId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        UserBook userBook = new UserBook();
        userBook.setId(rs.getInt("id"));
        userBook.setUserId(rs.getInt("user_id"));
        userBook.setBookId(rs.getInt("book_id"));
        userBook.setStatus(ReadingStatus.valueOf(rs.getString("status")));
        userBook.setUserRating(rs.getInt("user_rating"));
        userBook.setCurrentPage(rs.getInt("current_page"));
        return userBook;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<UserBook> findByUserId(int userId) {
    List<UserBook> userBooks = new ArrayList<>();
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_books WHERE user_id = ?");
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        UserBook userBook = new UserBook();
        userBook.setId(rs.getInt("id"));
        userBook.setUserId(rs.getInt("user_id"));
        userBook.setBookId(rs.getInt("book_id"));
        userBook.setStatus(ReadingStatus.valueOf(rs.getString("status")));
        userBook.setUserRating(rs.getInt("user_rating"));
        userBook.setCurrentPage(rs.getInt("current_page"));
        userBooks.add(userBook);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userBooks;
  }

  @Override
  public List<UserBook> findByBookId(int userId, int bookId) {
    List<UserBook> userBooks = new ArrayList<>();
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_books WHERE book_id = ?");
      ps.setInt(1, bookId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        UserBook userBook = new UserBook();
        userBook.setId(rs.getInt("id"));
        userBook.setUserId(rs.getInt("user_id"));
        userBook.setBookId(rs.getInt("book_id"));
        userBook.setStatus(ReadingStatus.valueOf(rs.getString("status")));
        userBook.setUserRating(rs.getInt("user_rating"));
        userBook.setCurrentPage(rs.getInt("current_page"));
        userBooks.add(userBook);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userBooks;
  }

  @Override
  public boolean save(UserBook userBook) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO user_books (user_id, book_id, status, user_rating, current_page) VALUES (?, ?, ?, ?, ?)");
      ps.setInt(1, userBook.getUserId());
      ps.setInt(2, userBook.getBookId());
      ps.setString(3, userBook.getStatus().name());
      ps.setInt(4, userBook.getUserRating());
      ps.setInt(5, userBook.getCurrentPage());
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean delete(int userId, int bookId) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn.prepareStatement("DELETE FROM user_books WHERE user_id = ? AND book_id = ?");
      ps.setInt(1, userId);
      ps.setInt(2, bookId);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateStatus(int userId, int bookId, ReadingStatus status) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn
          .prepareStatement("UPDATE user_books SET status = ? WHERE user_id = ? AND book_id = ?");
      ps.setString(1, status.name());
      ps.setInt(2, userId);
      ps.setInt(3, bookId);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateRating(int userId, int bookId, int rating) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn
          .prepareStatement("UPDATE user_books SET user_rating = ? WHERE user_id = ? AND book_id = ?");
      ps.setInt(1, rating);
      ps.setInt(2, userId);
      ps.setInt(3, bookId);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateCurrentPage(int user_id, int book_id, int currentPage) {
    try {
      conn = ConnectionManager.getConnection();
      PreparedStatement ps = conn
          .prepareStatement("UPDATE user_books SET current_page = ? WHERE user_id = ? AND book_id = ?");
      ps.setInt(1, currentPage);
      ps.setInt(2, user_id);
      ps.setInt(3, book_id);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
