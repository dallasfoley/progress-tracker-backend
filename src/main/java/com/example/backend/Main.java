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

// public class Main {
// public static void main(String[] args) {

// final boolean isDev = System.getenv("READING_PROGRESS_TRACKER_DB_URL") ==
// null;
// final Dotenv dotenv;
// if (isDev) {
// dotenv = Dotenv.configure().filename(".env.local").ignoreIfMissing().load();
// dotenv.get("READING_PROGRESS_TRACKER_DB_URL");
// } else {
// dotenv = null;
// }

// int PORT_NUMBER = !isDev
// ? Integer.parseInt(System.getenv("READING_PROGRESS_TRACKER_PORT"))
// : Integer.parseInt(dotenv.get("READING_PROGRESS_TRACKER_PORT"));

// System.out.println("PORT_NUMBER: " + PORT_NUMBER);

// UserDAOImpl userDAO = new UserDAOImpl();
// BookDAOImpl bookDAO = new BookDAOImpl();
// UserBookDAOImpl userBookDAO = new UserBookDAOImpl();
// AuthDAOImpl authDAO = new AuthDAOImpl();

// UserController userController = new UserController(userDAO);
// BookController bookController = new BookController(bookDAO);
// UserBookController userBookController = new UserBookController(userBookDAO);
// AuthController authController = new AuthController(authDAO);

// var app = Javalin.create(config -> {
// config.bundledPlugins.enableCors(cors -> {
// cors.addRule(it -> {
// if (isDev == true) {
// it.allowHost(dotenv.get("READING_PROGRESS_TRACKER_FRONTEND_URL"));
// } else {
// it.allowHost(System.getenv("READING_PROGRESS_TRACKER_FRONTEND_URL"));
// }
// it.allowCredentials = true;
// it.exposeHeader("Content-Type");
// it.exposeHeader("Set-Cookie");
// it.exposeHeader("Authorization");
// it.exposeHeader("X-Requested-With");
// it.exposeHeader("Accept");
// it.exposeHeader("Access-Control-Allow-Origin");
// it.exposeHeader("Access-Control-Allow-Credentials");
// });
// });
// }).get("/", ctx -> ctx.result("Hello World")).start(PORT_NUMBER);

// app.before("/api/*", ctx -> {
// ctx.contentType("application/json");
// if (isDev == true) {
// ctx.header("Access-Control-Allow-Origin",
// dotenv.get("READING_PROGRESS_TRACKER_FRONTEND_URL"));
// } else {
// ctx.header("Access-Control-Allow-Origin",
// System.getenv("READING_PROGRESS_TRACKER_FRONTEND_URL"));
// }
// ctx.header("Access-Control-Allow-Credentials", "true");
// ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
// });

// app.get("/api/users/<id>", userController::findUserById);
// app.get("/api/users/username/<username>",
// userController::findUserByUsername);
// app.post("/api/users", userController::createUser);
// app.put("/api/users", userController::updateUser);
// app.delete("/api/users/<id>", userController::deleteUser);

// app.get("/api/books", bookController::findAll);
// app.get("/api/books/<id>", bookController::findBookById);
// app.get("/api/books/title", bookController::findBooksByTitle);
// app.get("/api/books/author", bookController::findBooksByAuthor);
// app.post("/api/books", bookController::save);
// app.delete("/api/books/<id>", bookController::delete);
// app.put("/api/books", bookController::update);

// app.get("/api/user_books/<userId>/<bookId>", userBookController::findById);
// app.get("/api/user_books/<userId>", userBookController::findByUserId);
// app.post("/api/user_books/<userId>/<bookId>", userBookController::save);
// app.delete("/api/user_books/<userId>/<bookId>", userBookController::delete);
// app.patch("/api/user_books/status/<userId>/<bookId>",
// userBookController::updateStatus);
// app.patch("/api/user_books/page/<userId>/<bookId>",
// userBookController::updateCurrentPage);
// app.patch("/api/user_books/rating/<userId>/<bookId>",
// userBookController::updateRating);

// app.post("/api/auth/register", authController::register);
// app.post("/api/auth/login/username",
// authController::loginWithUsernamePassword);
// app.post("/api/auth/login/email", authController::loginWithEmailPassword);
// app.post("/api/auth/refresh", authController::refreshAccessToken);
// app.get("/api/auth/logout", authController::logout);

// app.error(400, ctx -> {
// System.out.println("400 Error triggered at path: " + ctx.path());
// });

// }
// }