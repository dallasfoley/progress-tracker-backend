# Progress Tracker Backend

## The backend for a Reading Progress Tracker application

For our RESTful API, we have a Java/Javalin app running on an EC2 instance through a docker container connected to an AWD RDS MySQL instance.

## Technologies Used

### Java 17

Java is the primary programming language used for our RESTful API because for one, we were tasked with using Java and two, Java is a great middle ground between high level languages like JavaScript and low level languages like Rust where the language is simple to use (simple syntax, has a garbage collector) but also offers great performance through lower-level features such as memory management, multithreading and being a compiled language and maintainability through its object-oriented structure. For example, in Node.js or any of the many Python frameworks for server-side development, the code is interpreted and then compiled and then executed line by line where as in Java, the code is compiled at build time and then interpreted and executed at run time - much faster. Java also offers true multithreading, which neither JS or Python support, as well as the benefit of having a large community around it, creating a wide range of tools, libraries, frameworks, discussion and solutions around it.

### Javalin

We were tasked with creating a Java application that communicated with a MySQL database to store, retrieve and update data about users and a topic of our choice with the constraint that we needed to use raw SQL queries to do so with no ORM or JPA to manage our SQL for us. Given we weren't allowed to use Spring Hibernate or Spring Data JPA and how small of a project this was, I figured that Javalin could help us expose API endpoints to our frontend without the bulk of Spring. This allows enhanced performance and lower costs on AWS at the cost of having to manage the instantiation and configuration of our classes ourselves (which is quite managable for this small of an API).

We use Javalin to instantiate our application and configure our CORS settings:

```java

 public static void main(String[] args) {
        var app = Javalin.create(config -> {
            CorsConfig.configure(config);
        }).get("/", ctx -> ctx.result("Hello World"))
                .start(EnvironmentConfig.PORT);
...
```

We use Javalin to create our API endpoints and run our Middleware:

```java

public class AuthRoutes {

  public static void register(Javalin app) {
    AuthDAOImpl authDAO = new AuthDAOImpl();
    AuthController authController = new AuthController(authDAO);
    app.post("/api/auth/register", authController::register);
    app.post("/api/auth/login/username", authController::loginWithUsernamePassword);
    app.post("/api/auth/login/email", authController::loginWithEmailPassword);
    app.post("/api/auth/refresh", authController::refreshAccessToken);
  }
}

public class BookRoutes {
  public static void register(Javalin app) {
    BookDAOImpl bookDAO = new BookDAOImpl();
    BookController bookController = new BookController(bookDAO);
    app.before("/api/books", Middleware::requireAuth);
    app.before("/api/books/*", Middleware::requireAuth);
    app.before("/api/books/<id>", Middleware::requireAuth);
    app.get("/api/books", bookController::findAll);
    app.get("/api/books/<id>", bookController::findBookById);
    app.post("/api/books", bookController::save);
    app.delete("/api/books/<id>", bookController::delete);
    app.put("/api/books", bookController::update);
  }

}
```

Notice for routes that require authentication, we use a custom Middleware class which Javalin runs before the request to check if user provided a valid access token in the Authorization header and returns a 401 if not.
We also utilize Javalin to configure global headers, create cookies and manage sessions. We also use
a separate package for creating and managing the JSON our Javalin endpoints respond with.

### MySQL and AWS

We use MySQL as our relational database for storing, reading and updating data in development and production. In development we use a locally installed MySQL Server and MySQL Workbench to manage our database. In production, we use a locally-installed MariaDB instance (offical MySQL package isn't supported on AWS Linux) connected to an AWS RDS MySQL instance to persist and secure our data. To create
our database, we first needed to copy the .sql file containing our schema to the EC2 instance with

'''
bash
scp -i ~/<path-to-private-key>.pem schema.sql ec2-user@<ec2-instance-ip-address>:/home/ec2-user
'''
We then need to login to our AWS MySQL instance through the EC2 instance

### Docker

We use Docker to create a containerized environment for our application which allows us to easily deploy it to our AWS EC2 instance, scale, secure and manage the app through Docker commands that allow for reading logs, stopping and running the app. We use Docker Secrets to add an extra layer of security to our environment variables which are then passed to the app at build time. I even have a separate Java backend (with Spring Boot for a Todo App) running on a separate Docker container on the same AWS EC2 instance.

### HikariCP

HikariCP gives us a connection pool for MySQL which allows us to manage multiple connections to the database at once and reuse them if possible, helping to optimize performance and resource usage.

### SLF4J

SLF4J is a logging framework that allows us to connect our System.out.println statements to a Java framework, in our case, Javalin.

### FasterXML/Jackson

FasterXML/Jackson is a Java library that allows us to convert Java objects to JSON and vice versa. This
is necessary for us to send and receive JSON data from our endpoints.

### Java JWT

Java JWT is a Java library that allows us to create and validate JWT tokens for user authentication and authorization.

## Overall Structure of the Entire Application

```mermaid
graph TD
    subgraph Client/Browser
        A[User's Browser] --> B(Next.js Frontend - Client Components)
    end

    subgraph Next.js 15 App (EC2 Instance or Vercel/Similar)
        B --> C{Next.js Backend - Server Components/Route Handlers}
        C -- HTTP/S Requests --> D[Javalin Server]
    end

    subgraph Javalin Server (Separate EC2 Instance or Container)
        D -- JDBC Connection --> E[MySQL Database]
    end

    subgraph MySQL Database (AWS RDS)
        E
    end

    style A fill:#DCE5EE,stroke:#333,stroke-width:2px,color:#333
    style B fill:#E6F3F7,stroke:#333,stroke-width:2px,color:#333
    style C fill:#E6F3F7,stroke:#333,stroke-width:2px,color:#333
    style D fill:#FFF3E0,stroke:#333,stroke-width:2px,color:#333
    style E fill:#E0F7FA,stroke:#333,stroke-width:2px,color:#333

    linkStyle 0 stroke:#333,stroke-width:1.5px,fill:none,stroke-dasharray: 5 5;
    linkStyle 1 stroke:#333,stroke-width:1.5px,fill:none,stroke-dasharray: 5 5;
    linkStyle 2 stroke:#333,stroke-width:1.5px,fill:none;
    linkStyle 3 stroke:#333,stroke-width:1.5px,fill:none;

    click B "This represents the interactive parts of your Next.js app in the user's browser."
    click C "This represents the Server Components and Route Handlers that run on the server side in your Next.js app. They handle data fetching and API routes."
    click D "This is your independent Javalin application, serving as a dedicated backend API."
    click E "This is your MySQL database, likely managed by AWS RDS."

    %% Optional: Add notes for clarity
    classDef clientNode fill:#DCE5EE,stroke:#333,stroke-width:2px,color:#333
    classDef nextjsNode fill:#E6F3F7,stroke:#333,stroke-width:2px,color:#333
    classDef javalinNode fill:#FFF3E0,stroke:#333,stroke-width:2px,color:#333
    classDef mysqlNode fill:#E0F7FA,stroke:#333,stroke-width:2px,color:#333

    class A clientNode
    class B,C nextjsNode
    class D javalinNode
    class E mysqlNode
```

We utilize Next.js as a proxy layer between the client and the Javalin server, which allows us to keep all of calls to the Javalin server on the server side which enhances security by hiding sensitive data from the client, validates and sanitizes all user inputs. It also greatly enhances performance by allowing us to caching our statically rendered routes (technically caching their RSC Payload, we also cache the static components of our dynamically rendered routes through Next.js's experimental Partial Prerendering), caching our requests to the Javalin server with its Data Cache along with a few other caching layers detailed in the frontend README.md.

## Authentication and Authorization

### Authentication

We utilize JWTs stored in cookies passed back and forth from client to server. When the user logs in or signs up through a form, the request is sent through a Next.js server action to the Javalin server, which makes a call to the database through with HikariCP (to pool connections) to verify the given credentials. If successful, the Java backend will send back a successful response to the browser containing the user info, an httpOnly, secure cookie containing the encrypted access token and an httpOnly, secure cookie containing the encrypted refresh token. This is stored in the browser and attached to API calls to the backend in the Next.js proxy layer.

### Authorization

When a user makes a request to an API route that requires authentication, the Middleware class is run before the request and checks if the user has a valid access token in the Authorization header. If not, we return a 401 status code. When the Next.js proxy layer receives a 401 status code, it will attempt to call the /api/auth/refresh endpoint to get a new access token. If successful, the Next.js proxy layer will retry the request with the new access token, otherwise it will display an error message to the user and redirect them to the home page.
