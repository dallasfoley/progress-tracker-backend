package com.example.backend.daos;

import java.util.List;

import com.example.backend.dtos.UserBookDetails;
import com.example.backend.models.ReadingStatus;
import com.example.backend.models.UserBook;

public interface UserBookDAO {

  UserBook findById(int userId, int bookId);

  List<UserBookDetails> findByUserId(int userId);

  boolean save(UserBook userBook);

  boolean delete(int userId, int bookId);

  boolean updateStatus(int userId, int bookId, ReadingStatus status);

  boolean updateRating(int userId, int bookId, int rating);

  boolean updateCurrentPage(int userId, int bookId, int currentPage);
}
