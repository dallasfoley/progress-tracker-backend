package com.example.backend.daos;

import com.example.backend.exceptions.UserNotCreatedException;
import com.example.backend.models.User;

public interface UserDAO {

  boolean save(String username, String email, String password) throws UserNotCreatedException;

  User findUserById(int id);

  User findUserByUsername(String username);

  User updateUser(User user);

  boolean deleteUser(int id);

}
