package com.example.backend.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.backend.connection.ConnectionManager;
import com.example.backend.dtos.UserBookDetails;
import com.example.backend.models.ReadingStatus;
import com.example.backend.models.UserBook;

public class UserBookDAOImpl implements UserBookDAO {

  @Override
  public UserBook findById(int userId, int bookId) {
    try (
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_books WHERE user_id = ? AND book_id = ?");) {
      ps.setInt(1, userId);
      ps.setInt(2, bookId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UserBook userBook = new UserBook();
          userBook.setUserId(rs.getInt("user_id"));
          userBook.setBookId(rs.getInt("book_id"));
          userBook.setStatus(ReadingStatus.valueOf(rs.getString("status")));
          userBook.setUserRating(rs.getInt("user_rating"));
          userBook.setCurrentPage(rs.getInt("current_page"));
          return userBook;
        }
      }
      ;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<UserBookDetails> findByUserId(int userId) {
    List<UserBookDetails> userBooks = new ArrayList<>();
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT ub.user_id, ub.book_id, ub.status, ub.user_rating, ub.current_page, b.title, b.author, b.year_published, b.genre, b.rating, b.page_count, b.description, b.cover_url FROM user_books ub JOIN books b ON ub.book_id = b.id WHERE ub.user_id = ?");) {
      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          UserBookDetails userBookDetails = new UserBookDetails();
          userBookDetails.setUserId(rs.getInt("user_id"));
          userBookDetails.setBookId(rs.getInt("book_id"));
          userBookDetails.setStatus(ReadingStatus.valueOf(rs.getString("status")));
          userBookDetails.setUserRating(rs.getInt("user_rating"));
          userBookDetails.setCurrentPage(rs.getInt("current_page"));
          userBookDetails.setTitle(rs.getString("title"));
          userBookDetails.setAuthor(rs.getString("author"));
          userBookDetails.setYearPublished(rs.getInt("year_published"));
          userBookDetails.setGenre(rs.getString("genre"));
          userBookDetails.setRating(rs.getDouble("rating"));
          userBookDetails.setPageCount(rs.getInt("page_count"));
          userBookDetails.setDescription(rs.getString("description"));
          userBookDetails.setCoverUrl(rs.getString("cover_url"));
          userBooks.add(userBookDetails);
        }
      }
      ;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userBooks;
  }

  @Override
  public boolean save(UserBook userBook) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO user_books (user_id, book_id, status, user_rating, current_page) VALUES (?, ?,  ?, ?, ?)");) {
      ps.setInt(1, userBook.getUserId());
      ps.setInt(2, userBook.getBookId());
      ps.setString(3, userBook.getStatus().name());
      ps.setInt(4, userBook.getUserRating());
      ps.setInt(5, userBook.getCurrentPage());
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected > 0 && userBook.getStatus() == ReadingStatus.COMPLETED && userBook.getUserRating() > 0) {
        try (PreparedStatement ps2 = conn.prepareStatement(
            "UPDATE books SET rating = (SELECT AVG(user_rating) FROM user_books WHERE book_id = ? AND user_rating > 0) WHERE id = ?")) {
          ps2.setInt(1, userBook.getBookId());
          ps2.setInt(2, userBook.getBookId());
          int rowsAffected2 = ps2.executeUpdate();
          return rowsAffected2 > 0;
        }
      }
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean delete(int userId, int bookId) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM user_books WHERE user_id = ? AND book_id = ?")) {
      ps.setInt(1, userId);
      ps.setInt(2, bookId);
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected > 0) {
        try (PreparedStatement ps1 = conn.prepareStatement(
            "SELECT COUNT(*) FROM user_books WHERE book_id = ? AND user_rating > 0")) {
          ps1.setInt(1, bookId);
          try (ResultSet rs = ps1.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
              PreparedStatement ps3 = conn.prepareStatement(
                  "UPDATE books SET rating = 0 WHERE id = ?");
              ps3.setInt(1, bookId);
              int rowsAffected3 = ps3.executeUpdate();
              return rowsAffected3 > 0;
            } else {
              PreparedStatement ps2 = conn.prepareStatement(
                  "UPDATE books SET rating = (SELECT AVG(user_rating) FROM user_books WHERE book_id = ? AND user_rating > 0) WHERE id = ?");
              ps2.setInt(1, bookId);
              ps2.setInt(2, bookId);
              int rowsAffected2 = ps2.executeUpdate();
              return rowsAffected2 > 0;
            }
          }
        }
      }
      return rowsAffected > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateStatus(int userId, int bookId, ReadingStatus status) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement("UPDATE user_books SET status = ? WHERE user_id = ? AND book_id = ?")) {
      ps.setString(1, status.getStatus());
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
    try (Connection conn = ConnectionManager.getConnection()) {
      String sql = "UPDATE user_books SET user_rating = ? WHERE user_id = ? AND book_id = ?";
      try (PreparedStatement ps1 = conn.prepareStatement(sql)) {
        ps1.setInt(1, rating);
        ps1.setInt(2, userId);
        ps1.setInt(3, bookId);
        int rowsAffected = ps1.executeUpdate();
        if (rowsAffected == 0) {
          return false;
        }
      }

      try (PreparedStatement ps2 = conn.prepareStatement(
          "UPDATE books SET rating = (SELECT AVG(user_rating) FROM user_books WHERE book_id = ? AND user_rating > 0) WHERE id = ?")) {
        ps2.setInt(1, bookId);
        ps2.setInt(2, bookId);
        ps2.executeUpdate();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean updateCurrentPage(int user_id, int book_id, int currentPage) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement("UPDATE user_books SET current_page = ? WHERE user_id = ? AND book_id = ?")) {
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
