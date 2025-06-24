package com.example.backend.controllers;

import java.sql.SQLException;
import java.util.Map;

import com.example.backend.daos.AuthDAO;
import com.example.backend.models.User;

import io.javalin.http.Context;

public class AuthController {

  private final AuthDAO authDAO;

  public AuthController(AuthDAO authDAO) {
    this.authDAO = authDAO;
  }

  public void register(Context ctx) {
    try {
      String username = ctx.formParam("username");
      String email = ctx.formParam("email");
      String password = ctx.formParam("password");

      User user = authDAO.register(username, email, password);
      if (user != null) {
        ctx.json(user).status(201);
      }
    } catch (RuntimeException e) {
      String errorMessage = e.getMessage();

      if (errorMessage.contains("Username already exists")) {
        ctx.status(409).json(Map.of("error", "Username is already registered"));
      } else if (errorMessage.contains("Email already exists")) {
        ctx.status(409).json(Map.of("error", "Email is already registered"));
      } else if (errorMessage.contains("constraint violation")) {
        ctx.status(409).json(Map.of("error", "Username is already registered"));
      } else {
        ctx.status(500).json(Map.of("error", "Registration failed"));
      }
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(Map.of("error", "Registration failed"));
    }

  }

  public void loginWithEmailPassword(Context ctx) {
    try {
      String email = ctx.formParam("email");
      String password = ctx.formParam("password");

      if (email == null || password == null) {
        ctx.status(400).result("Missing required fields");
        return;
      }

      User user = authDAO.loginWithEmailPassword(email, password);
      if (user != null) {
        ctx.json(user).status(200);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      ctx.status(500).json(Map.of("error", "Login failed"));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(Map.of("error", "Login failed"));
    }
  }

  public void loginWithUsernamePassword(Context ctx) {
    try {
      String username = ctx.formParam("username");
      String password = ctx.formParam("password");

      if (username == null || password == null) {
        ctx.status(400).result("Missing required fields");
        return;
      }

      User user = authDAO.loginWithUsernamePassword(username, password);
      if (user != null) {
        ctx.status(200);
        ctx.json(user);
      } else {
        ctx.status(401).result("Invalid email or password");
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      ctx.status(500).json(Map.of("error", "Login failed"));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(Map.of("error", "Login failed"));
    }

  }
}
