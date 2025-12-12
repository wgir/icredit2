## Architecture
- **Layering**: Controller -> Service -> Repository. Never bypass the Service layer.
- **DTOs**: Controllers must never return Entities. Map Entities to ResponseDTOs.
- **Dependency Injection**: Always use Constructor Injection (no @Autowired on fields).

## Coding Standards
- **Build Tool**: Maven. (Do not use Gradle).
- **Spring Boot Version**: 3.4.1
- **Java Version**: 21
- **Database Engine**: Mysql 5.5
- **Lombok**: Use @Data, @RequiredArgsConstructor, and @Builder.
- **Error Handling**: Use a GlobalExceptionHandler with ProblemDetails.
- **Testing**:
  - Unit tests for Services (Mockito).
  - Integration tests for Controllers (@WebMvcTest or @SpringBootTest).
  - Use AssertJ for assertions.
## Naming Conventions
- Interfaces: Do not use `I` prefix (e.g., `UserService`, not `IUserService`).
- Implementations: Append `Impl` only if necessary, otherwise rely on the interface name if strictly needed (Spring generally encourages class-based injection if no multiple impls exist).

## Code Organization
- Use the following package structure:
  - `com.icredit.api`: REST controllers.
  - `com.icredit.service`: Business logic.
  - `com.icredit.repository`: Data access.
  - `com.icredit.model`: Domain models.
  - `com.icredit.config`: Configuration classes.
  - `com.icredit.security`: Security configuration.
  - `com.icredit.util`: Utility classes.

## Key Principles:
- Convention over Configuration
- Standalone, production-grade applications
- Opinionated 'starter' dependencies
- Dependency Injection (IoC)
- Aspect-Oriented Programming (AOP)
- Show SQL queries in console

## Core Annotations:
- @SpringBootApplication: Main entry point
- @RestController / @Controller: Web layer
- @Service: Business logic layer
- @Repository: Data access layer
- @Component: Generic bean

## Data Access:
- Spring Data JPA for relational DBs
- Hibernate as JPA implementation
- Repository interfaces (JpaRepository)
- Transaction management (@Transactional)
- Flyway/Liquibase for migrations

## Configuration:
- application.properties / application.yml
- Profiles (dev, test, prod)
- @ConfigurationProperties for type-safe config
- @Value for simple injection
- Externalized configuration

## Security (Spring Security):
- Authentication and Authorization
- JWT or Session-based auth
- Method-level security (@PreAuthorize)
- CORS and CSRF configuration
- OAuth2 / OIDC integration

## Observability:
- Spring Boot Actuator for metrics/health
- Micrometer for metrics export
- Distributed tracing (Zipkin/Otel)
- Structured logging

## Best Practices:
- Use constructor injection (avoid @Autowired on fields)
- Handle exceptions globally (@ControllerAdvice)
- Validate inputs (@Valid, @NotNull)
- Write integration tests (@SpringBootTest)
- Use Lombok to reduce boilerplate
