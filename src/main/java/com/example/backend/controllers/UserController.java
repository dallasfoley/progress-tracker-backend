package com.example.backend.controllers;

import com.example.backend.daos.UserDAOImpl;
import com.example.backend.models.User;

import io.javalin.http.Context;

public class UserController {

  private final UserDAOImpl userDAO;

  public UserController(UserDAOImpl userDAO) {
    this.userDAO = userDAO;
  }

  public void getUserById(Context ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      var user = userDAO.getUserById(id);
      if (user == null) {
        ctx.status(404).result("User not found");
      } else {
        ctx.json(user);
      }
    } catch (Exception e) {
      ctx.status(500).result("Internal Server Error");
      e.printStackTrace();
    }
  }

  public void getUserByUsername(Context ctx) {
    try {
      String username = ctx.pathParam("username");
      var user = userDAO.getUserByUsername(username);
      if (user == null) {
        ctx.status(404).result("User not found");
      } else {
        ctx.json(user);
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

  // public void updateUser(Context ctx, int id) {
  // try {
  // String username = ctx.formParam("username");
  // String email = ctx.formParam("email");
  // String password = ctx.formParam("password");

  // if (username == null || email == null || password == null) {
  // ctx.status(400).result("Missing required fields");
  // return;
  // }

  // boolean isUpdated = userDAO.updateUser(new User(id, username, email,
  // password));
  // if (isUpdated) {
  // ctx.status(200).result("User updated successfully");
  // } else {
  // ctx.status(500).result("Failed to update user");
  // }
  // } catch (Exception e) {
  // ctx.status(500).result("Internal Server Error");
  // e.printStackTrace();
  // }
  // }
}
