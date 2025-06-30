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

  public void updateUser(Context ctx) {
    try {
      User user = ctx.bodyAsClass(User.class);

      if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
        ctx.status(400).json(Map.of("error", "Missing required fields"));
        return;
      }

      User updatedUser = userDAO.updateUser(user);
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
      if (isDeleted == false) {
        ctx.status(400).json(Map.of("error", "User not found"));
        System.out.println("User not found");
      } else {
        System.out.println("User deleted successfully");
        ctx.status(200).json(Map.of("message", "User deleted successfully"));
      }
    } catch (Exception e) {
      ctx.status(500).json(Map.of("error", e.getMessage()));
      e.printStackTrace();
    }
  }
}
