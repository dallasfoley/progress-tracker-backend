package com.example.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtils {

  private static final String SECRET_KEY = System.getenv("SECRET_KEY") != null ? System.getenv("SECRET_KEY")
      : "secret_key";
  private static final String REFRESH_SECRET_KEY = System.getenv("REFRESH_SECRET_KEY") != null
      ? System.getenv("REFRESH_SECRET_KEY")
      : "refresh_secret";
  private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 minutes
  private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

  public static String generateAccessToken(String subject) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
      return JWT.create()
          .withSubject(subject)
          .withIssuer("auth0")
          .withExpiresAt(new java.util.Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      exception.printStackTrace();
      return null;
    }
  }

  public static String generateRefreshToken(String subject) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(REFRESH_SECRET_KEY);
      return JWT.create()
          .withSubject(subject)
          .withIssuer("auth0")
          .withExpiresAt(new java.util.Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      exception.printStackTrace();
      return null;
    }
  }

  public static DecodedJWT validateAccessToken(String token) throws JWTVerificationException {
    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
    return verifier.verify(token);
  }

  public static DecodedJWT validateRefreshToken(String token) throws JWTVerificationException {
    try {
      Algorithm algorithm = Algorithm.HMAC256(REFRESH_SECRET_KEY);
      JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
      return verifier.verify(token);
    } catch (JWTVerificationException exception) {
      exception.printStackTrace();
      return null;
    }
  }
}
