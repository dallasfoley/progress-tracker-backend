package com.example.backend.controllers;

import java.util.List;
import java.util.Map;

import com.example.backend.daos.BookDAOImpl;
import com.example.backend.dtos.BookDetails;
import com.example.backend.models.Book;

import io.javalin.http.Context;

public class BookController {

  private final BookDAOImpl bookDAO;

  public BookController(BookDAOImpl bookDAO) {
    this.bookDAO = bookDAO;
  }

  public void findAll(Context ctx) {
    try {
      List<BookDetails> books = bookDAO.findAll();
      if (books.isEmpty()) {
        ctx.status(500).json(Map.of("error", "No books found"));
      } else {
        ctx.status(200).json(books);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void findBookById(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      Book found = bookDAO.findById(id);
      if (found != null) {
        ctx.json(found).status(200);
      } else {
        ctx.status(400).json(Map.of("error", "No books found"));
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void findBooksByTitle(Context ctx) {
    try {
      String title = ctx.formParam("title");
      List<Book> books = bookDAO.findByTitle(title);
      if (books.isEmpty()) {
        ctx.status(400).json(Map.of("error", "No books found"));
      } else {
        ctx.json(books).status(200);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }

  }

  public void findBooksByAuthor(Context ctx) {
    try {
      String author = ctx.formParam("author");
      List<Book> books = bookDAO.findByAuthor(author);
      if (books.isEmpty()) {
        ctx.status(400).json(Map.of("error", "No books found"));
      } else {
        ctx.json(books).status(200);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void save(Context ctx) {
    try {
      Book book = ctx.bodyAsClass(Book.class);
      boolean success = bookDAO.save(book.getTitle(), book.getAuthor(), book.getYearPublished(), book.getGenre(),
          book.getRating(), book.getPageCount(), book.getCoverUrl(), book.getDescription());
      if (success) {
        ctx.status(200).json(Map.of("message", "Book saved successfully"));
      } else {
        ctx.status(500).json(Map.of("error", "No books found"));
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void delete(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      boolean success = bookDAO.delete(id);
      if (success) {
        ctx.status(200).result("Book deleted successfully");
      } else {
        ctx.status(500).result("Internal Server Error");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void update(Context ctx) {
    System.out.println(ctx.path());
    try {
      Book book = ctx.bodyAsClass(Book.class);
      boolean success = bookDAO.update(book);
      if (success) {
        System.out.println("Book updated successfully");
        ctx.status(200).json(Map.of("message", "Book updated successfully"));
      } else {
        System.out.println("No books found");
        ctx.status(500).json(Map.of("error", "No books found"));
      }
    } catch (Exception e) {
      System.out.println("Internal Server Error");
      ctx.status(500).json(Map.of("error", "Internal Server Error"));
      e.printStackTrace();
    }
  }

}
