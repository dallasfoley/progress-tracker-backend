package com.example.backend.models;

public enum Role {
  USER, ADMIN;

  public String getAuthority() {
    return "ROLE_" + name();
  }

  public static Role fromString(String role) {
    if (role == null) {
      return null;
    }
    try {
      return Role.valueOf(role.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
