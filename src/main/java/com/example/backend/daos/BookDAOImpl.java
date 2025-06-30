package com.example.backend.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.connection.ConnectionManager;
import com.example.backend.dtos.BookDetails;
import com.example.backend.models.Book;

public class BookDAOImpl implements BookDAO {

  @Override
  public List<BookDetails> findAll() {
    List<BookDetails> books = new ArrayList<>();
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement(
                "SELECT b.*, SUM(CASE WHEN ub.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as in_progress_count, SUM(CASE WHEN ub.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed_count FROM books b LEFT JOIN user_books ub ON b.id = ub.book_id GROUP BY b.id");) {

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          BookDetails book = new BookDetails();
          book.setId(rs.getInt("id"));
          book.setTitle(rs.getString("title"));
          book.setAuthor(rs.getString("author"));
          book.setYearPublished(rs.getInt("year_published"));
          book.setGenre(rs.getString("genre"));
          book.setRating(rs.getDouble("rating"));
          book.setPageCount(rs.getInt("page_count"));
          book.setCoverUrl(rs.getString("cover_url"));
          book.setInProgressCount(rs.getInt("in_progress_count"));
          book.setCompletedCount(rs.getInt("completed_count"));
          books.add(book);
        }
      }
      ;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return books;
  }

  @Override
  public Book findById(int id) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE id = ?");) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
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
          book.setCoverUrl(rs.getString("cover_url"));
          return book;
        }
      }
      ;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<Book> findByTitle(String title) {
    List<Book> books = new ArrayList<>();
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE title LIKE ?")) {
      ps.setString(1, "%" + title + "%");
      try (ResultSet rs = ps.executeQuery()) {
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
      }
      ;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return books;
  }

  @Override
  public List<Book> findByAuthor(String author) {
    List<Book> books = new ArrayList<>();
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE author LIKE ?");) {
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
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return books;
  }

  @Override
  public boolean save(String title, String author, int yearPublished, String genre, double rating, int pageCount,
      String coverUrl, String description) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO books (title, author, year_published, genre, rating, page_count, cover_url, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");) {

      ps.setString(1, title);
      ps.setString(2, author);
      ps.setInt(3, yearPublished);
      ps.setString(4, genre);
      ps.setDouble(5, rating);
      ps.setInt(6, pageCount);
      ps.setString(7, coverUrl);
      ps.setString(8, description);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean delete(int id) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id = ?");) {

      ps.setInt(1, id);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean update(Book book) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE books SET title = ?, author = ?, year_published = ?, genre = ?, rating = ?, page_count = ?, cover_url = ?, description = ? WHERE id = ?");) {
      ps.setString(1, book.getTitle());
      ps.setString(2, book.getAuthor());
      ps.setInt(3, book.getYearPublished());
      ps.setString(4, book.getGenre());
      ps.setDouble(5, book.getRating());
      ps.setInt(6, book.getPageCount());
      ps.setString(7, book.getCoverUrl());
      ps.setString(8, book.getDescription());
      ps.setInt(9, book.getId());
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

}
