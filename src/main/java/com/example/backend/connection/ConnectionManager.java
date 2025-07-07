package com.example.backend.connection;

import com.example.backend.config.EnvironmentConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
  private static HikariDataSource dataSource;

  static {
    try {
      initializeDataSource();
      System.out.println("Database connection pool initialized");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Failed to initialize database connection pool");
      throw new RuntimeException("Failed to initialize database connection pool", e);
    }
  }

  private static void initializeDataSource() throws IOException {

    String host;
    String port;
    String db;
    String username;
    String password;

    if (!EnvironmentConfig.IS_DEV) {
      host = System.getenv("MYSQL_URL");
      port = System.getenv("MYSQL_PORT");
      db = System.getenv("MYSQL_DB");
      username = System.getenv("MYSQL_USERNAME");
      password = System.getenv("MYSQL_PASSWORD");
    } else {
      host = "localhost";
      port = "3306";
      db = "books";
      username = "root";
      password = "";
    }

    String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true";
    System.out.println("jdbcUrl: " + jdbcUrl);
    System.out.println("Connecting to DB at: " + jdbcUrl);

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");

    config.setMaximumPoolSize(20);
    config.setMinimumIdle(5);
    config.setConnectionTimeout(30000);
    config.setIdleTimeout(600000);
    config.setMaxLifetime(1800000);

    dataSource = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public static void closeDataSource() {
    if (dataSource != null && !dataSource.isClosed()) {
      dataSource.close();
    }
  }
}