package com.example.backend.controllers;

import java.util.Map;

import com.example.backend.daos.UserDAOImpl;
import com.example.backend.models.User;

import io.javalin.http.Context;

public class UserController {

  private final UserDAOImpl userDAO;

  public UserController(UserDAOImpl userDAO) {
    this.userDAO = userDAO;
  }

  public void findUserById(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      var user = userDAO.findUserById(id);
      if (user == null) {
        ctx.status(404).result("User not found");
      } else {
        ctx.json(user).status(200);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void findUserByUsername(Context ctx) {
    try {
      String username = ctx.pathParam("username");
      var user = userDAO.findUserByUsername(username);
      if (user == null) {
        ctx.status(404).result("User not found");
      } else {
        ctx.json(user).status(200);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void createUser(Context ctx) {
    try {
      String username = ctx.formParam("username");
      String email = ctx.formParam("email");
      String password = ctx.formParam("password");

      if (username == null || email == null || password == null) {
        ctx.status(400).result("Missing required fields");
        return;
      }

      boolean isCreated = userDAO.save(username, email, password);
      if (isCreated) {
        ctx.status(201).result("User created successfully");
      } else {
        ctx.status(500).result("Failed to create user");
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void updateUser(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      String username = ctx.formParam("username");
      String email = ctx.formParam("email");
      String password = ctx.formParam("password");

      if (username == null || email == null || password == null) {
        ctx.status(400).result("Missing required fields");
        return;
      }

      User updatedUser = userDAO.updateUser(new User(id, username, email, password));
      if (updatedUser != null) {
        ctx.json(updatedUser).status(200);
      } else {
        ctx.json(Map.of("error", "Bad user info")).status(404);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void deleteUser(Context ctx) {
    try {
      System.out.println(ctx.path());
      int id = Integer.parseInt(ctx.pathParam("id"));
      boolean isDeleted = userDAO.deleteUser(id);
      if (isDeleted != false) {
        ctx.status(400).json(Map.of("error", "User not found"));
      } else {
        System.out.println("User deleted successfully");
        ctx.status(200).json(Map.of("message", "User deleted successfully"));
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }
}
