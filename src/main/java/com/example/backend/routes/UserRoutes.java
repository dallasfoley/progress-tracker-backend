package com.example.backend.routes;

import com.example.backend.controllers.UserController;
import com.example.backend.daos.UserDAOImpl;
import com.example.backend.utils.Middleware;

import io.javalin.Javalin;

public class UserRoutes {

  public static void register(Javalin app) {
    UserDAOImpl userDAO = new UserDAOImpl();
    UserController userController = new UserController(userDAO);
    app.before("/api/user", Middleware::requireAuth);
    app.before("/api/user/<id>", Middleware::requireAuth);
    app.put("/api/user/<id>", userController::updateUser);
    app.delete("/api/user/<id>", userController::deleteUser);
  }
}
