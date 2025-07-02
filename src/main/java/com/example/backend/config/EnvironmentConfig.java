package com.example.backend.config;

public class EnvironmentConfig {
  private final boolean isDev;
  private final int port;
  private final String frontendUrl;

  public EnvironmentConfig() {
    this.isDev = System.getenv("READING_PROGRESS_TRACKER_DB_URL") == null;

    this.port = isDev
        ? 8081
        : Integer.parseInt(System.getenv("READING_PROGRESS_TRACKER_PORT"));

    this.frontendUrl = isDev
        ? "http://localhost:8081"
        : System.getenv("READING_PROGRESS_TRACKER_FRONTEND_URL");

    if (frontendUrl == null || frontendUrl.isEmpty()) {
      System.out.println("FRONTEND_URL: " + frontendUrl);
    }
  }

  public boolean isDev() {
    return isDev;
  }

  public int getPort() {
    return port;
  }

  public String getFrontendUrl() {
    return frontendUrl;
  }
}