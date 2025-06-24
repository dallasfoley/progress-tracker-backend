package com.example.backend.controllers;

import java.util.List;

import com.example.backend.daos.BookDAOImpl;
import com.example.backend.models.Book;

import io.javalin.http.Context;

public class BookController {

  private final BookDAOImpl bookDAO;

  public BookController(BookDAOImpl bookDAO) {
    this.bookDAO = bookDAO;
  }

  public void findAll(Context ctx) {
    try {
      List<Book> books = bookDAO.findAll();
      if (books.isEmpty()) {
        ctx.status(500).result("Internal Server Error");
      } else {
        ctx.json(books).status(200);
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
        ctx.json(found).status(200).result("Book found successfully");
      } else {
        ctx.status(400).result("User not found");
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
        ctx.status(400).result("No books found");
      } else {
        ctx.json(books).status(200).result("Book found successfully");
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
        ctx.status(400).result("No books found");
      } else {
        ctx.json(books).status(200).result("Book found successfully");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void save(Context ctx) {
    try {
      String title = ctx.formParam("title");
      String author = ctx.formParam("author");
      Integer yearPublished = Integer.parseInt(ctx.formParam("yearPublished"));
      String genre = ctx.formParam("genre");
      boolean success = bookDAO.save(title, author, yearPublished, genre);
      if (success) {
        ctx.status(200).result("Book created successfully");
      } else {
        ctx.status(500).result("Internal Server Error");
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

  }

}
