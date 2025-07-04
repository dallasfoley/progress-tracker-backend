package com.example.backend.models;

public class AuthResponse {
  private User user;
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";

  public AuthResponse() {
  }

  public AuthResponse(User user, String accessToken, String refreshToken) {
    this.user = user;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public User getUser() {
    return user;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

}