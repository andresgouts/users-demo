# users-demo
Spring application that store and login users. additionaly it makes use of spring security for authorization with JWT token 

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle](https://maven.apache.org](https://gradle.org/)https://gradle.org/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.golballogic.usersdemo.UsersDemoApplication` class from your IDE.

Alternatively you can use gradle Sprinboot Pluging like so:

```shell
.\gradlew bootRun
```

## Calling endpoints

This service expose two endpoints, Post `/api/v1/users` and Get `/api/v1/users/login`

### /api/v1/users
This POST endpoint manage the user creation and the request body contract looks like

```json
{
    "name": "name",
    "email": "name@gmail.com",
    "password": "Password23",
    "phones": [
        {
            "number": "2222222",
            "cityCode": 4,
            "countryCode": "+57"
        },
        {
            "number": "333333",
            "cityCode": 4,
            "countryCode": "+57"
        }
    ]
}
```

### /api/v1/users/login
This GET endpoint reads the jwt token from Authorization header and returns the authenticated user info

