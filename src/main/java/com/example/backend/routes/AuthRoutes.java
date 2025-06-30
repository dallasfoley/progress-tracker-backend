package com.example.backend.routes;

import com.example.backend.controllers.AuthController;
import com.example.backend.daos.AuthDAOImpl;
import com.example.backend.utils.Middleware;

import io.javalin.Javalin;

public class AuthRoutes {

  public static void register(Javalin app) {
    AuthDAOImpl authDAO = new AuthDAOImpl();
    AuthController authController = new AuthController(authDAO);
    app.post("/api/auth/register", authController::register);
    app.post("/api/auth/login/username", authController::loginWithUsernamePassword);
    app.post("/api/auth/login/email", authController::loginWithEmailPassword);
    app.post("/api/auth/refresh", authController::refreshAccessToken);
    app.before("/api/auth/logout", Middleware::requireAuth);
    app.get("/api/auth/logout", authController::logout);
  }
}
