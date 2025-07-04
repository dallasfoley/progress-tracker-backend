package com.example.backend.controllers;

import java.util.Map;

import com.example.backend.config.EnvironmentConfig;
import com.example.backend.daos.AuthDAOImpl;
import com.example.backend.exceptions.EmailAlreadyExistsException;
import com.example.backend.exceptions.UsernameAlreadyExistsException;
import com.example.backend.models.User;
import com.example.backend.utils.JwtUtils;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.SameSite;

public class AuthController {

  private final AuthDAOImpl authDAO;

  public AuthController(AuthDAOImpl authDAO) {
    this.authDAO = authDAO;
  }

  private Cookie[] setHeadersAndAuthCookies(Context ctx, String username) {
    String frontendUrl = EnvironmentConfig.FRONTEND_URL;
    ctx.header("Access-Control-Allow-Origin", frontendUrl);
    ctx.header("Access-Control-Allow-Credentials", "true");
    ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
    ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Cookie");
    String accessToken = JwtUtils.generateAccessToken(username);
    String refreshToken = JwtUtils.generateRefreshToken(username);
    Cookie accessCookie = new Cookie("accessToken", accessToken);
    accessCookie.setHttpOnly(true);
    accessCookie.setMaxAge(60 * 60 * 24); // 1 day
    ctx.cookie(accessCookie);
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
    if (EnvironmentConfig.IS_DEV) {
      accessCookie.setSecure(false);
      accessCookie.setSameSite(SameSite.LAX);
      refreshCookie.setSecure(false);
      refreshCookie.setSameSite(SameSite.LAX);
    } else {
      accessCookie.setSecure(true);
      accessCookie.setSameSite(SameSite.NONE);
      refreshCookie.setSameSite(SameSite.NONE);
      refreshCookie.setSecure(true);
    }
    ctx.cookie(refreshCookie);
    return new Cookie[] { accessCookie, refreshCookie };
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
        Cookie[] cookies = setHeadersAndAuthCookies(ctx, username);

        ctx.status(201).json(Map.of(
            "success", true,
            "message", "User registered successfully",
            "data", user,
            "accessToken", cookies[0].getValue(),
            "refreshToken", cookies[1].getValue()));
      }
    } catch (UsernameAlreadyExistsException e) {
      ctx.status(409).json(Map.of("error", e.getMessage())); // Use the message from the exception
    } catch (EmailAlreadyExistsException e) {
      ctx.status(409).json(Map.of("error", e.getMessage()));
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
        ctx.status(400).json(Map.of("error", "Missing required fields"));
        return;
      }

      User user = authDAO.loginWithEmailPassword(email, password);
      if (user != null) {
        Cookie[] cookies = setHeadersAndAuthCookies(ctx, email);

        ctx.status(200).json(Map.of(
            "success", true,
            "message", "User logged in successfully",
            "data", user,
            "accessToken", cookies[0].getValue(),
            "refreshToken", cookies[1].getValue()));
      } else {
        ctx.status(401).json(Map.of("error", "Invalid email or password"));
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
    System.out.println(ctx.path());
    try {
      String username = ctx.formParam("username");
      String password = ctx.formParam("password");

      if (username == null || password == null) {
        ctx.status(400).json(Map.of("error", "Missing required fields"));
        return;
      }

      User user = authDAO.loginWithUsernamePassword(username, password);
      if (user != null) {
        System.out.println("generating tokens");
        Cookie[] cookies = setHeadersAndAuthCookies(ctx, username);

        ctx.status(200).json(Map.of(
            "success", true,
            "message", "User logged in successfully",
            "data", user,
            "accessToken", cookies[0].getValue(),
            "refreshToken", cookies[1].getValue()));
      } else {
        ctx.status(401).json(Map.of("error", "Invalid username or password"));
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
    String frontendUrl = EnvironmentConfig.FRONTEND_URL;
    ctx.header("Access-Control-Allow-Origin", frontendUrl);
    ctx.header("Access-Control-Allow-Credentials", "true");
    ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
    ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Cookie");
    @SuppressWarnings("unchecked")
    String refreshToken = ((Map<String, String>) ctx.bodyAsClass(Map.class)).get("refreshToken");

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

    String accessToken = JwtUtils.generateAccessToken(username);
    Cookie accessCookie = new Cookie("accessToken", accessToken);
    accessCookie.setHttpOnly(true);
    accessCookie.setSecure(true); // Enable in production
    accessCookie.setSameSite(SameSite.LAX);
    accessCookie.setMaxAge(60 * 60);
    ctx.cookie(accessCookie);

    ctx.status(200).json(Map.of(
        "accessToken", accessCookie.getValue()));
  }
}