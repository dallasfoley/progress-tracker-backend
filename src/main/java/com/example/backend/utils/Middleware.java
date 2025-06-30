package com.example.backend.utils;

import com.auth0.jwt.exceptions.TokenExpiredException;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

import java.util.Map;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;

public class Middleware {
  public static void requireAuth(Context ctx) {
    System.out.println("Middleware: requireAuth called for path: " + ctx.path());
    String authHeader = ctx.header("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ") | authHeader.endsWith("null")) {
      System.out.println("Authorization header missing or invalid");
      throw new UnauthorizedResponse("Authorization header missing or invalid");
    }

    String token = authHeader.substring(7);
    System.out.println("Token from Authorization header: " + token);

    try {
      var decodedJWT = JwtUtils.validateAccessToken(token);
      ctx.attribute("userId", decodedJWT.getSubject());
    } catch (TokenExpiredException ex) {
      ctx.status(401).json(Map.of("error", "Access token expired", "code", "EXPIRED_TOKEN"));
      throw new RuntimeException("Access token expired");
    } catch (SignatureVerificationException ex) {
      ctx.status(401).json(Map.of("error", "Invalid token signature", "code", "INVALID_SIGNATURE"));
      throw new RuntimeException("Invalid token signature");
    } catch (JWTVerificationException ex) {
      ctx.status(401).json(Map.of("error", "Invalid token", "code", "INVALID_TOKEN"));
      throw new RuntimeException("Invalid token");
    }
  }
}
