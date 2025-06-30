package com.example.backend.routes;

import com.example.backend.controllers.BookController;
import com.example.backend.daos.BookDAOImpl;
import com.example.backend.utils.Middleware;

import io.javalin.Javalin;

public class BookRoutes {
  public static void register(Javalin app) {
    BookDAOImpl bookDAO = new BookDAOImpl();
    BookController bookController = new BookController(bookDAO);
    app.before("/api/books", Middleware::requireAuth);
    app.before("/api/books/*", Middleware::requireAuth);
    app.before("/api/books/<id>", Middleware::requireAuth);
    app.before("/api/books/title", Middleware::requireAuth);
    app.before("/api/books/author", Middleware::requireAuth);
    app.get("/api/books", bookController::findAll);
    app.get("/api/books/<id>", bookController::findBookById);
    app.get("/api/books/title", bookController::findBooksByTitle);
    app.get("/api/books/author", bookController::findBooksByAuthor);
    app.post("/api/books", bookController::save);
    app.delete("/api/books/<id>", bookController::delete);
    app.put("/api/books", bookController::update);
  }

}