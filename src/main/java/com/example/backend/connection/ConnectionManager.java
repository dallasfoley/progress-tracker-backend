package com.example.backend.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;

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
      System.out.println("Failed to initialize database connection pool");
      throw new RuntimeException("Failed to initialize database connection pool", e);
    }
  }

  private static void initializeDataSource() throws IOException {
    Dotenv dotenv = Dotenv.configure().filename(".env.local").ignoreIfMissing().load();

    String host = System.getenv("MYSQL_URL") != null ? System.getenv("MYSQL_URL") : dotenv.get("MYSQL_URL");
    String port = System.getenv("MYSQL_PORT") != null ? System.getenv("MYSQL_PORT") : dotenv.get("MYSQL_PORT");
    String db = System.getenv("MYSQL_DB") != null ? System.getenv("MYSQL_DB") : dotenv.get("MYSQL_DB");
    String username = System.getenv("MYSQL_USERNAME") != null ? System.getenv("MYSQL_USERNAME")
        : dotenv.get("MYSQL_USERNAME");
    String password = System.getenv("MYSQL_PASSWORD") != null ? System.getenv("MYSQL_PASSWORD")
        : dotenv.get("MYSQL_PASSWORD");

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