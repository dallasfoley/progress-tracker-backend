package com.example.backend.dtos;

/**
 * Generic API response wrapper
 */
public class ApiResponseDTO<T> {
  private boolean success;
  private String message;
  private T data;

  public ApiResponseDTO() {
  }

  public ApiResponseDTO(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public ApiResponseDTO(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  // Static factory methods for convenience
  public static <T> ApiResponseDTO<T> success(String message, T data) {
    return new ApiResponseDTO<>(true, message, data);
  }

  public static <T> ApiResponseDTO<T> success(String message) {
    return new ApiResponseDTO<>(true, message, null);
  }

  public static <T> ApiResponseDTO<T> error(String message) {
    return new ApiResponseDTO<>(false, message, null);
  }

  // Getters and setters
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
