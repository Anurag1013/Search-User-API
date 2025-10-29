# Low Level Design (LLD) - Users Backend

## Packages
- com.example.usersbackend.model - JPA entities (User)
- com.example.usersbackend.dto - DTOs for external API parsing
- com.example.usersbackend.repository - Spring Data JPA repositories
- com.example.usersbackend.service - Service interfaces and implementations
- com.example.usersbackend.controller - REST controllers
- com.example.usersbackend.security - JWT utilities, filter, userdetails
- com.example.usersbackend.config - Spring configuration (RestTemplate, OpenAPI)
- com.example.usersbackend.exception - Global exception handling

## SOLID & Design choices
- Single Responsibility (SRP): Each service class has one responsibility (fetching, mapping, persistence)
- Open/Closed: Services are coded to interfaces so behavior can be extended without modification
- Liskov Substitution: Repositories and services use interfaces enabling substitution in tests
- Interface Segregation: Clients depend only on the service interfaces they need
- Dependency Inversion: High-level modules depend on abstractions (service interfaces)

## Error handling & resilience
- Resilience4j annotations provide retry and circuit-breaker around external calls.
- GlobalExceptionHandler centralizes HTTP error mapping.
- Validation annotations on input DTOs and controller params guard inputs.

## Logging
- Logback JSON encoder configuration with structured logs.
- Service and controller level logs use slf4j.
