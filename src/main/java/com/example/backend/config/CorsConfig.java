package com.example.backend.config;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

public class CorsConfig {

  public static void configure(JavalinConfig config, EnvironmentConfig envConfig) {
    config.bundledPlugins.enableCors(cors -> {
      cors.addRule(it -> {
        it.allowHost(envConfig.getFrontendUrl());
        it.allowCredentials = true;
        it.exposeHeader("Content-Type");
        it.exposeHeader("Set-Cookie");
        it.exposeHeader("Authorization");
        it.exposeHeader("X-Requested-With");
        it.exposeHeader("Accept");
        it.exposeHeader("Access-Control-Allow-Origin");
        it.exposeHeader("Access-Control-Allow-Credentials");
      });
    });
  }

  public static void configureGlobalHeaders(Javalin app, EnvironmentConfig envConfig) {
    app.before("/api/*", ctx -> {
      ctx.contentType("application/json");
      ctx.header("Access-Control-Allow-Origin", envConfig.getFrontendUrl());
      ctx.header("Access-Control-Allow-Credentials", "true");
      ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
      ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Cookie");
    });
  }
}