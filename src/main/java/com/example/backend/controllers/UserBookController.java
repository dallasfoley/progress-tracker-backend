package com.example.backend.controllers;

import com.example.backend.daos.UserBookDAOImpl;

public class UserBookController {

  private final UserBookDAOImpl userBookDAO;

  public UserBookController(UserBookDAOImpl userBookDAO) {
    this.userBookDAO = userBookDAO;
  }
}
