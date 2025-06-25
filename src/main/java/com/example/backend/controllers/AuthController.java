package com.example.backend.controllers;

import java.util.Map;

import com.example.backend.daos.AuthDAOImpl;
import com.example.backend.models.User;
import com.example.backend.utils.JwtUtils;

import io.javalin.http.Context;

public class AuthController {

  private final AuthDAOImpl authDAO;

  public AuthController(AuthDAOImpl authDAO) {
    this.authDAO = authDAO;
  }

  public void register(Context ctx) {
    System.out.println(ctx.path());
    try {
      String username = ctx.formParam("username");
      String email = ctx.formParam("email");
      String password = ctx.formParam("password");

      User user = authDAO.register(username, email, password);
      if (user != null) {
        System.out.println("generating tokens");
        String accessToken = JwtUtils.generateAccessToken(username);
        ctx.cookie("accessToken", accessToken, 60 * 60); // 1 hour

        // Set CORS headers to allow credentials
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.header("Access-Control-Allow-Origin", "http://localhost:3000");
        ctx.json(Map.of(
            "success", true,
            "message", "User registered successfully",
            "data", user,
            "accessToken", accessToken)).status(201);
      }
    } catch (RuntimeException e) {
      String errorMessage = e.getMessage();
      e.printStackTrace();

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
        String accessToken = JwtUtils.generateAccessToken(email);
        ctx.header("Authorization", "Bearer " + accessToken);
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.header("Access-Control-Allow-Origin", "http://localhost:3000");
        ctx.json(Map.of(
            "success", true,
            "message", "User logged in successfully",
            "data", user,
            "accessToken", accessToken)).status(201);
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
        String accessToken = JwtUtils.generateAccessToken(username);
        ctx.header("Authorization", "Bearer " + accessToken);
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.header("Access-Control-Allow-Origin", "http://localhost:3000");
        ctx.json(Map.of(
            "success", true,
            "message", "User registered successfully",
            "data", user,
            "accessToken", accessToken)).status(201);
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

  public void refreshAccessToken(Context ctx) {
    String refreshToken = ctx.cookie("refreshToken");

    if (refreshToken == null) {
      ctx.status(401).json(Map.of("error", "Missing refresh token"));
      return;
    }

    var decodedJWT = JwtUtils.validateRefreshToken(refreshToken);
    if (decodedJWT == null) {
      ctx.status(403).json(Map.of("error", "Invalid refresh token"));
      return;
    }

    String username = decodedJWT.getSubject();
    String newAccessToken = JwtUtils.generateAccessToken(username);

    ctx.header("Authorization", "Bearer " + newAccessToken);
    ctx.json(Map.of("accessToken", newAccessToken, "tokenType", "Bearer")).status(200);
  }

  public void logout(Context ctx) {
    ctx.cookie("accessToken", "", -1);
    ctx.cookie("refreshToken", "", -1);
    ctx.status(200).json(Map.of("message", "Logout successful")).status(200);
  }

}
