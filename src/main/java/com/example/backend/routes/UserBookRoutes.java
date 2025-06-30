package com.example.backend.routes;

import com.example.backend.controllers.UserBookController;
import com.example.backend.daos.UserBookDAOImpl;
import com.example.backend.utils.Middleware;

import io.javalin.Javalin;

public class UserBookRoutes {

  public static void register(Javalin app) {
    UserBookDAOImpl userBookDAO = new UserBookDAOImpl();
    UserBookController userBookController = new UserBookController(userBookDAO);
    // app.before("/api/user_books/*",
    // Middleware::requireAuth);
    app.before("/api/user_books/<userId>", Middleware::requireAuth);
    app.before("/api/user_books/<userId>/<bookId>", Middleware::requireAuth);
    app.before("/api/user_books/status/<userId>/<bookId>", Middleware::requireAuth);
    app.before("/api/user_books/page/<userId>/<bookId>", Middleware::requireAuth);
    app.before("/api/user_books/rating/<userId>/<bookId>", Middleware::requireAuth);

    app.get("/api/user_books/<userId>/<bookId>", userBookController::findById);
    app.get("/api/user_books/<userId>", userBookController::findByUserId);
    app.post("/api/user_books/<userId>/<bookId>", userBookController::save);
    app.delete("/api/user_books/<userId>/<bookId>", userBookController::delete);
    app.patch("/api/user_books/status/<userId>/<bookId>",
        userBookController::updateStatus);
    app.patch("/api/user_books/page/<userId>/<bookId>",
        userBookController::updateCurrentPage);
    app.patch("/api/user_books/rating/<userId>/<bookId>",
        userBookController::updateRating);
  }
}
