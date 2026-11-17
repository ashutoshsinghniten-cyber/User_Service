# user-service  (port 8080)

Spring Boot REST project in **MVC structure**. It calls both other services using `RestTemplate`.

| Service         | Port |
|-----------------|------|
| user-service    | 8080 |
| order-service   | 8081 |
| product-service | 8082 |

## MVC structure
```
src/main/java/com/demo/user/
├── UserServiceApplication.java
├── controller/   UserController (own API), RemoteCallController (/call/...)
├── service/      UserService (business logic), RemoteCallService (RestTemplate calls)
├── repository/   UserRepository (in-memory data)
├── model/        UserDto, OrderDto, ProductDto  (own = provider contract, others = consumer copies)
├── config/       RestConfig (strict RestTemplate)
└── exception/    DownstreamExceptionHandler (returns 502 if a downstream call fails)
```

## Run (Java 17+, Maven)
    mvn spring-boot:run
Start all three projects to test the cross-service calls.

## Own endpoints
    GET http://localhost:8080/api/users
    GET http://localhost:8080/api/users/{id}

## Calls to the other services (through this project)
    GET http://localhost:8080/call/orders         -> order-service /api/orders
    GET http://localhost:8080/call/orders/{id}    -> order-service /api/orders/{id}
    GET http://localhost:8080/call/products         -> product-service /api/products
    GET http://localhost:8080/call/products/{id}    -> product-service /api/products/{id}
    GET http://localhost:8080/call/all          -> all of the above

## Break test
Change a field name, field type, URL or return type in this project's own model/controller,
restart it, then call /call/all on the other two services: they return **502** with the reason.

## Docker
    docker build -t user-service .
Use env vars to point to other containers, e.g. `ORDER_SERVICE_URL=http://order-service:8081`.
