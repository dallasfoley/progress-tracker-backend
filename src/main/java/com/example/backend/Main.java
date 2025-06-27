package com.example.backend;

import com.example.backend.controllers.AuthController;
import com.example.backend.controllers.BookController;
import com.example.backend.controllers.UserBookController;
import com.example.backend.controllers.UserController;
import com.example.backend.daos.AuthDAOImpl;
import com.example.backend.daos.BookDAOImpl;
import com.example.backend.daos.UserBookDAOImpl;
import com.example.backend.daos.UserDAOImpl;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        int PORT_NUMBER = 7000;

        UserDAOImpl userDAO = new UserDAOImpl();
        BookDAOImpl bookDAO = new BookDAOImpl();
        UserBookDAOImpl userBookDAO = new UserBookDAOImpl();
        AuthDAOImpl authDAO = new AuthDAOImpl();

        UserController userController = new UserController(userDAO);
        BookController bookController = new BookController(bookDAO);
        UserBookController userBookController = new UserBookController(userBookDAO);
        AuthController authController = new AuthController(authDAO);

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.allowHost("localhost:3000");
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
        }).get("/", ctx -> ctx.result("Hello World")).start(PORT_NUMBER);

        app.before("/api/*", ctx -> {
            ctx.contentType("application/json");
            ctx.header("Access-Control-Allow-Origin", "http://localhost:3000");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });

        app.get("/api/users/<id>", userController::findUserById);
        app.get("/api/users/username/<username>", userController::findUserByUsername);
        app.post("/api/users", userController::createUser);
        app.put("/api/users", userController::updateUser);
        app.delete("/api/users/<id>", userController::deleteUser);

        app.get("/api/books", bookController::findAll);
        app.get("/api/books/<id>", bookController::findBookById);
        app.get("/api/books/title", bookController::findBooksByTitle);
        app.get("/api/books/author", bookController::findBooksByAuthor);
        app.post("/api/books", bookController::save);
        app.delete("/api/books/<id>", bookController::delete);
        app.put("/api/books", bookController::update);

        app.get("/api/user_books/<userId>/<bookId>", userBookController::findById);
        app.get("/api/user_books/<userId>", userBookController::findByUserId);
        app.post("/api/user_books/<userId>/<bookId>", userBookController::save);
        app.delete("/api/user_books/<userId>/<bookId>", userBookController::delete);
        app.patch("/api/user_books/status/<userId>/<bookId>", userBookController::updateStatus);
        app.patch("/api/user_books/page/<userId>/<bookId>", userBookController::updateCurrentPage);
        app.patch("/api/user_books/rating/<userId>/<bookId>", userBookController::updateRating);

        app.post("/api/auth/register", authController::register);
        app.post("api/auth/login/username", authController::loginWithUsernamePassword);
        app.post("api/auth/login/email", authController::loginWithEmailPassword);
        app.post("api/auth/refresh", authController::refreshAccessToken);
        app.get("api/auth/logout", authController::logout);

        app.error(400, ctx -> {
            System.out.println("400 Error triggered at path: " + ctx.path());
        });

    }
}