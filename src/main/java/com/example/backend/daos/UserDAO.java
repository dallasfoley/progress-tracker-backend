package com.example.backend.daos;

import com.example.backend.exceptions.UserNotCreatedException;
import com.example.backend.models.User;

public interface UserDAO {

  boolean save(String username, String email, String password) throws UserNotCreatedException;

  User getUserById(int id);

  User getUserByUsername(String username);

  boolean updateUser(User user);

  boolean deleteUser(int id);

}
