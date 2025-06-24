package com.example.backend.daos;

import com.example.backend.models.User;

public interface AuthDAO {

  public User register(String username, String email, String password);

  public User loginWithEmailPassword(String email, String password);

  public User loginWithUsernamePassword(String username, String password);

}
