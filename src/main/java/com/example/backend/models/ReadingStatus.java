package com.example.backend.models;

public enum ReadingStatus {
  NOT_STARTED("NOT_STARTED"),
  IN_PROGRESS("IN_PROGRESS"),
  COMPLETED("COMPLETED");

  private final String status;

  ReadingStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public static ReadingStatus fromString(String status) {
    for (ReadingStatus readingStatus : ReadingStatus.values()) {
      if (readingStatus.status.equalsIgnoreCase(status)) {
        return readingStatus;
      }
    }
    throw new IllegalArgumentException("No constant with text " + status + " found");
  }
}
