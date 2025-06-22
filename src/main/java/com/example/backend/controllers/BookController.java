package com.example.backend.controllers;

import com.example.backend.daos.BookDAOImpl;

public class BookController {

  private final BookDAOImpl bookDAO;

  public BookController(BookDAOImpl bookDAO) {
    this.bookDAO = bookDAO;
  }
}
