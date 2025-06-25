package com.example.backend.dtos;

import com.example.backend.models.ReadingStatus;

public class UpdateStatusDTO {
  private ReadingStatus status;

  public ReadingStatus getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = ReadingStatus.fromString(status);
  }

  public void setStatus(ReadingStatus status) {
    this.status = status;
  }

}
