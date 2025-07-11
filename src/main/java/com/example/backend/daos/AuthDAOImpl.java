package com.example.backend.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.example.backend.connection.ConnectionManager;
import com.example.backend.exceptions.EmailAlreadyExistsException;
import com.example.backend.exceptions.UsernameAlreadyExistsException;
import com.example.backend.models.Role;
import com.example.backend.models.User;

public class AuthDAOImpl implements AuthDAO {

  public User register(String username, String email, String password)
      throws UsernameAlreadyExistsException, EmailAlreadyExistsException {

    String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, username);
      ps.setString(2, email);
      ps.setString(3, password);
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected > 0) {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            int id = generatedKeys.getInt(1);
            return new User(id, username, email, password);
          }
        }

      }
    } catch (SQLIntegrityConstraintViolationException e) {
      String errorMessage = e.getMessage().toLowerCase();
      if (errorMessage.contains("username")) {
        throw new UsernameAlreadyExistsException("User with this username already exists");
      } else if (errorMessage.contains("email")) {
        throw new EmailAlreadyExistsException("User with this email already exists");
      } else {
        throw new RuntimeException("Registration failed due to constraint violation", e);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Database error during registration", e);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public User loginWithEmailPassword(String email, String password) {
    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
    try (Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {
      ps.setString(1, email);
      ps.setString(2, password);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          String username = rs.getString("username");
          String userEmail = rs.getString("email");
          String userPassword = rs.getString("password");
          int id = rs.getInt("id");
          return new User(id, username, userEmail, userPassword);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public User loginWithUsernamePassword(String username, String password) {
    try (Connection conn = ConnectionManager.getConnection();) {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
      ps.setString(1, username);
      ps.setString(2, password);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          int id = rs.getInt(1);
          String usersname = rs.getString(2);
          String userEmail = rs.getString(3);
          String userPassword = rs.getString(4);
          Role role = Role.fromString(rs.getString(5));
          return new User(id, usersname, userEmail, userPassword, role);
        }
      }
      ;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
