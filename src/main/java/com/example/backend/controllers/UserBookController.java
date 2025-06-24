package com.example.backend.controllers;

import java.util.List;

import com.example.backend.daos.UserBookDAOImpl;
import com.example.backend.dtos.ApiResponseDTO;
import com.example.backend.dtos.UserBookDetails;
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
        ctx.json(ApiResponseDTO.success("UserBook found", book)).status(200);
      } else {
        ctx.json(ApiResponseDTO.error("UserBook not found")).status(404);
      }
    } catch (NumberFormatException e) {
      ctx.json(ApiResponseDTO.error("Invalid user ID or book ID format")).status(400);
    } catch (RuntimeException e) {
      String errorMessage = e.getMessage();
      if (errorMessage != null && !errorMessage.isEmpty()) {
        ctx.json(ApiResponseDTO.error(errorMessage)).status(500);
      } else {
        ctx.json(ApiResponseDTO.error("Database error occurred")).status(500);
      }
    } catch (Exception e) {
      e.printStackTrace();
      ctx.json(ApiResponseDTO.error("Internal server error")).status(500);
    }
  }

  public void findByUserId(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      List<UserBookDetails> books = userBookDAO.findByUserId(userId);

      if (books.isEmpty()) {
        ctx.json(ApiResponseDTO.success("No books found for this user", books)).status(200);
      } else {
        ctx.json(ApiResponseDTO.success("Books retrieved successfully", books)).status(200);
      }
    } catch (NumberFormatException e) {
      ctx.json(ApiResponseDTO.error("Invalid user ID format")).status(400);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.json(ApiResponseDTO.error("Internal server error")).status(500);
    }
  }

  public void save(Context ctx) {
    try {
      UserBook userBook = ctx.bodyAsClass(UserBook.class);

      // Validate required fields
      if (userBook.getUserId() <= 0 || userBook.getBookId() <= 0) {
        ctx.json(ApiResponseDTO.error("Invalid user ID or book ID")).status(400);
        return;
      }

      boolean success = userBookDAO.save(userBook);

      if (success) {
        ctx.json(ApiResponseDTO.success("Book successfully added to reading list", userBook)).status(201);
      } else {
        ctx.json(ApiResponseDTO.error("Failed to add book to reading list")).status(400);
      }
    } catch (IllegalArgumentException e) {
      ctx.json(ApiResponseDTO.error("Invalid request data: " + e.getMessage())).status(400);
    } catch (RuntimeException e) {
      String errorMessage = e.getMessage();

      // Handle specific database errors
      if (errorMessage != null && errorMessage.contains("duplicate")) {
        ctx.json(ApiResponseDTO.error("This book is already in your reading list")).status(409);
      } else if (errorMessage != null && errorMessage.contains("constraint")) {
        ctx.json(ApiResponseDTO.error("Invalid user or book reference")).status(400);
      } else {
        ctx.json(ApiResponseDTO.error("Database error: " + (errorMessage != null ? errorMessage : "Unknown error")))
            .status(500);
      }
    } catch (Exception e) {
      e.printStackTrace();
      ctx.json(ApiResponseDTO.error("Internal server error")).status(500);
    }
  }

  public void delete(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));

      boolean success = userBookDAO.delete(userId, bookId);

      if (success) {
        ctx.json(ApiResponseDTO.success("Book removed from reading list successfully")).status(200);
      } else {
        ctx.json(ApiResponseDTO.error("Book not found in reading list or already removed")).status(404);
      }
    } catch (NumberFormatException e) {
      ctx.json(ApiResponseDTO.error("Invalid user ID or book ID format")).status(400);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.json(ApiResponseDTO.error("Internal server error")).status(500);
    }
  }

  public void updateStatus(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      String statusField = ctx.formParam("status");

      if (statusField == null || statusField.trim().isEmpty()) {
        ctx.json(ApiResponseDTO.error("Status is required")).status(400);
        return;
      }

      ReadingStatus status = ReadingStatus.fromString(statusField);
      boolean success = userBookDAO.updateStatus(userId, bookId, status);

      if (success) {
        ctx.json(ApiResponseDTO.success("Reading status updated successfully")).status(200);
      } else {
        ctx.json(ApiResponseDTO.error("Failed to update reading status")).status(400);
      }
    } catch (NumberFormatException e) {
      ctx.json(ApiResponseDTO.error("Invalid user ID or book ID format")).status(400);
    } catch (IllegalArgumentException e) {
      ctx.json(ApiResponseDTO.error("Invalid status value")).status(400);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.json(ApiResponseDTO.error("Internal server error")).status(500);
    }
  }

  public void updateCurrentPage(Context ctx) {
    try {
      int userId = Integer.parseInt(ctx.pathParam("userId"));
      int bookId = Integer.parseInt(ctx.pathParam("bookId"));
      String pageParam = ctx.formParam("page");

      if (pageParam == null || pageParam.trim().isEmpty()) {
        ctx.json(ApiResponseDTO.error("Page number is required")).status(400);
        return;
      }

      int currentPage = Integer.parseInt(pageParam);

      if (currentPage < 0) {
        ctx.json(ApiResponseDTO.error("Page number must be non-negative")).status(400);
        return;
      }

      boolean success = userBookDAO.updateCurrentPage(userId, bookId, currentPage);

      if (success) {
        ctx.json(ApiResponseDTO.success("Current page updated successfully")).status(200);
      } else {
        ctx.json(ApiResponseDTO.error("Failed to update current page")).status(400);
      }
    } catch (NumberFormatException e) {
      ctx.json(ApiResponseDTO.error("Invalid user ID, book ID, or page number format")).status(400);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.json(ApiResponseDTO.error("Internal server error")).status(500);
    }
  }
}