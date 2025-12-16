
Help me to define a prompt to develop a new feature for existent company application for implement a CRUD of new entity city, each city belong to company table.

# City CRUD Feature – Spring Boot + JPA (SDD, Company from JWT)

## 1️⃣ AI Prompt (Spring Boot + JPA + MySQL)

**Role:** You are a senior Java backend engineer.

**Context:**
An existing Spring Boot application manages `Company` entities and uses:
- Java 21
- Spring Boot
- Spring Security with JWT
- Spring Data JPA (Hibernate)
- MySQL 5.5

The authenticated user belongs to **exactly one Company**.
The `companyId` is included as a **claim inside the JWT token**.

**Objective:**
Implement a City CRUD feature where:
- Each City belongs to one Company
- The Company is resolved **from JWT**, not from URL parameters

---

## 2️⃣ Specification-Driven Development (SDD)

### 📌 Feature: Manage Cities (JWT-scoped)

### Business Rules
- Users can only manage Cities from their own Company
- Company context is extracted from JWT
- No endpoint accepts `companyId` as input
- City name is required

### Scenarios

#### Scenario 1: Create City
**Given** a valid JWT with `35c59b16-d19d-4352-b38f-63e15044b46c`  
**When** the user creates a city  
**Then** the city is stored with `35c59b16-d19d-4352-b38f-63e15044b46c`

#### Scenario 2: Cross-Company Access
**Given** a JWT with `35c59b16-d19d-4352-b38f-63e15044b46c`  
**When** the user tries to access a city belonging to company `35c59b16-25452-4352-b38f-1111`   
**Then** the city is not found

---

## 3️⃣ API Design (No companyId in URL)

```http
POST   /api/cities
GET    /api/cities
GET    /api/cities/{id}
PUT    /api/cities/{id}
DELETE /api/cities/{id}
```

---

## 4️⃣ Database Schema (MySQL)

```sql
CREATE TABLE cities (
  id varchar(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  company_id  varchar(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_city_company FOREIGN KEY (company_id) REFERENCES companies(id),
  CONSTRAINT uq_city_code_company UNIQUE (company_id, code)
) ENGINE=InnoDB;
```

---

## 5️⃣ Security: Company Resolution from JWT

### JWT Claim Example
```json
{
  "sub": "user@email.com",
  "company_id": 10,
  "roles": ["ADMIN"]
}
```

### Security Utility
```java
@Component
public class CompanyContext {

    public Long getCompanyId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getClaim("company_id");
    }
}
```

---

## 6️⃣ Repository

```java
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByCompanyId(Long companyId);

    Optional<City> findByIdAndCompanyId(Long id, Long companyId);

    boolean existsByCompanyIdAndCode(Long companyId, String code);
}
```

---

## 7️⃣ Service Layer

```java
@Service
@RequiredArgsConstructor
@Transactional
public class CityService {

    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final CompanyContext companyContext;

    public CityResponse create(CityRequest request) {
        Long companyId = companyContext.getCompanyId();

        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new NotFoundException("Company not found"));

        if (request.code() != null &&
            cityRepository.existsByCompanyIdAndCode(companyId, request.code())) {
            throw new BusinessException("City code already exists");
        }

        City city = new City();
        city.setName(request.name());
        city.setCode(request.code());
        city.setCompany(company);

        return map(cityRepository.save(city));
    }

    @Transactional(readOnly = true)
    public List<CityResponse> findAll() {
        return cityRepository.findByCompanyId(companyContext.getCompanyId())
            .stream()
            .map(this::map)
            .toList();
    }

    @Transactional(readOnly = true)
    public CityResponse findById(Long cityId) {
        return cityRepository.findByIdAndCompanyId(
                cityId,
                companyContext.getCompanyId())
            .map(this::map)
            .orElseThrow(() -> new NotFoundException("City not found"));
    }

    public CityResponse update(Long cityId, CityRequest request) {
        City city = cityRepository.findByIdAndCompanyId(
                cityId,
                companyContext.getCompanyId())
            .orElseThrow(() -> new NotFoundException("City not found"));

        city.setName(request.name());
        city.setCode(request.code());
        return map(city);
    }

    public void delete(Long cityId) {
        City city = cityRepository.findByIdAndCompanyId(
                cityId,
                companyContext.getCompanyId())
            .orElseThrow(() -> new NotFoundException("City not found"));
        cityRepository.delete(city);
    }

    private CityResponse map(City city) {
        return new CityResponse(city.getId(), city.getName(), city.getCode());
    }
}
```

---

## 8️⃣ REST Controller

```java
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityResponse> create(@Valid @RequestBody CityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(cityService.create(request));
    }

    @GetMapping
    public List<CityResponse> list() {
        return cityService.findAll();
    }

    @GetMapping("/{id}")
    public CityResponse get(@PathVariable Long id) {
        return cityService.findById(id);
    }

    @PutMapping("/{id}")
    public CityResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest request) {
        return cityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cityService.delete(id);
    }
}
```

---

## 9️⃣ Testing Strategy

- Mock JWT with `company_id` claim
- Repository tests ensure company isolation
- Controller tests validate no `companyId` leakage

---

## ✅ Result

- No companyId exposed in API
- Strong multi-tenant isolation
- JWT-driven security
- SDD-aligned and production-ready



implement the City CRUD Feature related in ./Prompts/city-crud-spring-boot-sdd-jwt.md
