package com.example.backend.config;

public class EnvironmentConfig {
  public static final boolean IS_DEV;
  public static final int PORT;
  public static final String FRONTEND_URL;

  private EnvironmentConfig() {

  }

  static {
    IS_DEV = System.getenv("READING_PROGRESS_TRACKER_DB_URL") == null;

    PORT = IS_DEV
        ? 8081
        : Integer.parseInt(System.getenv("READING_PROGRESS_TRACKER_PORT"));

    String url = IS_DEV
        ? "http://localhost:8081"
        : System.getenv("READING_PROGRESS_TRACKER_FRONTEND_URL");

    if (url == null || url.isEmpty()) {
      System.out.println("FRONTEND_URL: " + url);
    }

    FRONTEND_URL = url;
  }

}
