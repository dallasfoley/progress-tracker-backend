package com.example.backend.exceptions;

import com.example.backend.models.User;

public class UserNotCreatedException extends Exception {

  public UserNotCreatedException(User user) {
    super("User with the following values could not be created: " + user);
  }

}
