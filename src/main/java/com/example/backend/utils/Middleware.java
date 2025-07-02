package com.example.backend.utils;

import java.util.Map;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class Middleware {
  public static void requireAuth(Context ctx) {
    try {
      System.out.println("Middleware: requireAuth called for path: " + ctx.path());
      String authHeader = ctx.header("Authorization");
      System.out.println("Authorization header: " + authHeader);
      if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.endsWith("null")
          || authHeader.endsWith("undefined")) {
        System.out.println("Authorization header missing or invalid");
        throw new UnauthorizedResponse("Authorization header missing or invalid");
      }

      String token = authHeader.substring(7);
      System.out.println("Token from Authorization header: " + token);

      var decodedJWT = JwtUtils.validateAccessToken(token);
      ctx.attribute("userId", decodedJWT.getSubject());
    } catch (JWTDecodeException e) {
      ctx.status(401).json(Map.of("error", "Invalid token", "code", "INVALID_TOKEN"));
      return;
    } catch (TokenExpiredException ex) {
      ctx.status(401).json(Map.of("error", "Access token expired", "code", "EXPIRED_TOKEN"));
      return;
    } catch (SignatureVerificationException ex) {
      ctx.status(401).json(Map.of("error", "Invalid token signature", "code", "INVALID_SIGNATURE"));
      return;
    } catch (JWTVerificationException ex) {
      ctx.status(401).json(Map.of("error", "Invalid token", "code", "INVALID_TOKEN"));
      return;
    }
  }
}
