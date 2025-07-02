package com.example.backend;

import com.example.backend.config.CorsConfig;
import com.example.backend.config.EnvironmentConfig;
import com.example.backend.routes.AuthRoutes;
import com.example.backend.routes.BookRoutes;
import com.example.backend.routes.UserBookRoutes;
import com.example.backend.routes.UserRoutes;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        EnvironmentConfig envConfig = new EnvironmentConfig();

        var app = Javalin.create(config -> {
            CorsConfig.configure(config, envConfig);
        }).get("/", ctx -> ctx.result("Hello World"))
                .start(envConfig.getPort());

        CorsConfig.configureGlobalHeaders(app, envConfig);

        AuthRoutes.register(app);
        UserRoutes.register(app);
        BookRoutes.register(app);
        UserBookRoutes.register(app);

        app.options("/*", ctx -> {
            ctx.status(200);
        });

        app.error(400, ctx -> {
            System.out.println("400 Error triggered at path: " + ctx.path());
        });

        app.error(401, ctx -> {
            ctx.json(java.util.Map.of("error", "Unauthorized"));
        });

        app.error(500, ctx -> {
            System.out.println("500 Error triggered at path: " + ctx.path());
            ctx.json(java.util.Map.of("error", "Internal server error"));
        });
    }
}