package com.example.backend.dtos;

public class CurrentPageDTO {
  private int currentPage;

  public CurrentPageDTO() {}

  public CurrentPageDTO(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

}