package com.example.backend.controllers;

import java.util.List;

import com.example.backend.daos.UserBookDAOImpl;
import com.example.backend.models.ReadingStatus;
import com.example.backend.models.UserBook;

import io.javalin.http.Context;

public class UserBookController {

  private final UserBookDAOImpl userBookDAO;

  public UserBookController(UserBookDAOImpl userBookDAO) {
    this.userBookDAO = userBookDAO;
  }

  public void findById(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      UserBook book = userBookDAO.findById(userId, bookId);
      if (book != null) {
        ctx.json(book).status(200).result("UserBook found");
      } else {
        ctx.status(400).result("Userbook not found");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void findByUserId(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      List<UserBook> books = userBookDAO.findByUserId(userId);
      if (books.isEmpty()) {
        ctx.status(400).result("Userbooks not found");
      } else {
        ctx.json(books).status(200).result("UserBooks found");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void save(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      boolean success = userBookDAO.save(userId, bookId);
      if (success) {
        ctx.status(200).result("UserBook created successfully");
      } else {
        ctx.status(400).result("Error creating UserBook");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void delete(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      boolean success = userBookDAO.delete(userId, bookId);
      if (success) {
        ctx.status(200).result("UserBook deleted successfully");
      } else {
        ctx.status(400).result("Error deleting UserBook");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void updateStatus(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      String statusField = ctx.formParam("status");
      ReadingStatus status = ReadingStatus.fromString(statusField);
      boolean success = userBookDAO.updateStatus(userId, bookId, status);
      if (success) {
        ctx.status(200).result("UserBook updated successfully");
      } else {
        ctx.status(400).result("Error updating UserBook");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void updateCurrentPage(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      int currentPage = Integer.parseInt(ctx.formParam("page"));
      boolean success = userBookDAO.updateCurrentPage(userId, bookId, currentPage);
      if (success) {
        ctx.status(200).result("UserBook updated successfully");
      } else {
        ctx.status(400).result("Error updating UserBook");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

}
