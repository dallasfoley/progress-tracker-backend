package com.example.backend.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.backend.connection.ConnectionManager;
import com.example.backend.exceptions.UserNotCreatedException;
import com.example.backend.models.User;

public class UserDAOImpl implements UserDAO {

  @Override
  public boolean save(String username, String email, String password) throws UserNotCreatedException {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {
      ps.setString(1, username);
      ps.setString(2, email);
      ps.setString(3, password);
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected > 0) {
        return true;
      } else {
        throw new UserNotCreatedException(new User(0, username, email, password));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public User findUserById(int id) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new User(
              rs.getInt(1),
              rs.getString(2),
              rs.getString(3),
              rs.getString(4));
        } else {
          return null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public User findUserByUsername(String username) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new User(
              rs.getInt(1),
              rs.getString(2),
              rs.getString(3),
              rs.getString(4));
        } else {
          return null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public User updateUser(User user) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?")) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPassword());
      ps.setInt(4, user.getId());
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected > 0) {
        return user;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean deleteUser(int id) {
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
      ps.setInt(1, id);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

}
