# Users Backend
This project implements the Users backend according to the provided requirement:
- Load users from https://dummyjson.com/users into an H2 DB
- Provide REST APIs: load, search (free-text), get by id/email, add user
- JWT-based authentication
- Resilience (Retry + CircuitBreaker)
- OpenAPI/Swagger docs
- Profiles: local, dev, test, prod
- Logging, exception handling, environment layering, README, and architecture diagram

## Requirements
- Java 17
- Maven 3.8+

## Run (local)
1. Set VM options:
   -Dspring-boot.run.profiles=local -DJWT_SECRET=this-is-a-secure-32-byte-secret-key -Dfile.encoding=UTF-8 -Dspring.output.ansi.enabled=ALWAYS
   
2. Env Vars:(When you don't want to use VM options)
   -Dspring-boot.run.profiles=local
   -DJWT_SECRET=this-is-a-secure-32-byte-secret-key
   -Dfile.encoding=UTF-8
   -Dspring.output.ansi.enabled=ALWAYS


3. Swagger UI: http://localhost:8080/swagger-ui/index.html
4. H2 Console: http://localhost:8080/h2-console (JDBC URL shown in application.yml)

## Profiles
Activate profile via VM option or env variable:
- `-Dspring.profiles.active=local` or set `SPRING_PROFILES_ACTIVE`

## Notes
- JWT secret must be provided as environment variable `JWT_SECRET` or JVM property `-DJWT_SECRET=...`
- The project uses Spring Boot 3.4.3 for compatibility with Springdoc 2.6.0
- If you are getting 500 error while opening Swagger UI, run 1. in terminal run rm -rf ~/.m2/repository/org/springdoc then run `mvn -U clean package`.
- "{{baseUrl}}/api/auth/login - {"username": "admin",  "password": "password123"}"
