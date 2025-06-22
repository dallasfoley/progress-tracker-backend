package com.example.backend;

import com.example.backend.controllers.BookController;
import com.example.backend.controllers.UserBookController;
import com.example.backend.controllers.UserController;
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

        UserController userController = new UserController(userDAO);
        BookController bookController = new BookController(bookDAO);
        UserBookController userBookController = new UserBookController(userBookDAO);

        var app = Javalin.create().get("/", ctx -> ctx.result("Hello World")).start(PORT_NUMBER);

        app.get("/users/<id>", userController::getUserById);
        app.get("/users/<username>", userController::getUserByUsername);
        app.post("/users", userController::createUser);
        app.put("/users/<id>", userController::updateUser);
        app.delete("/users/<id>", userController::deleteUser);

    }
}