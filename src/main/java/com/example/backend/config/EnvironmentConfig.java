package com.example.backend.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentConfig {
  private final boolean isDev;
  private final Dotenv dotenv;
  private final int port;
  private final String frontendUrl;

  public EnvironmentConfig() {
    this.isDev = System.getenv("READING_PROGRESS_TRACKER_DB_URL") == null;

    if (isDev) {
      this.dotenv = Dotenv.configure().filename(".env.local").ignoreIfMissing().load();
    } else {
      this.dotenv = null;
    }

    this.port = isDev
        ? Integer.parseInt(dotenv.get("READING_PROGRESS_TRACKER_PORT"))
        : Integer.parseInt(System.getenv("READING_PROGRESS_TRACKER_PORT"));

    this.frontendUrl = isDev
        ? dotenv.get("READING_PROGRESS_TRACKER_FRONTEND_URL")
        : System.getenv("READING_PROGRESS_TRACKER_FRONTEND_URL");

    System.out.println("PORT_NUMBER: " + port);
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