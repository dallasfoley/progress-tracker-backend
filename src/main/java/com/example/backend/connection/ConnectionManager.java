package com.example.backend.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

  private static Connection connection;

  private ConnectionManager() {

  }

  private static void createConnection()
      throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {

    Properties properties = new Properties();

    properties.load(ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties"));

    String url = properties.getProperty("url");
    String username = properties.getProperty("username");
    String password = properties.getProperty("password");
    Class.forName("com.mysql.cj.jdbc.Driver");
    connection = DriverManager.getConnection(url, username, password);
  }

  public static Connection getConnection()
      throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {

    if (connection == null || connection.isClosed()) {
      createConnection();
    }

    return connection;
  }

}
