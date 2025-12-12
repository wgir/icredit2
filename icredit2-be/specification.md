# Specification: Companies API (Users, Roles, JWT Auth)

**Author:** William Giraldo

**Date:** 2025-12-05

**Version:** 0.1

**Status:** Draft

---

## 1. Purpose & Scope

This document specifies an HTTP JSON API to manage **Companies**, **Roles**, and **Users**. It includes registration and lifecycle operations for companies, roles and users, plus an authentication/authorization system using JWT (JSON Web Tokens).

**In scope:**
- Company CRUD and registration
- Role CRUD (scoped to company)
- User registration, update, assignment to roles
- Authentication using JWT access tokens and refresh tokens
- RBAC authorization checks

**Out of scope:**
- UI implementation
- Billing, payments, or multi-tenant DB isolation beyond a company_id field (clarify if required)
- Audit log persistence for every field change (optional extension)

---

## 2. Concepts & Definitions

- **Company**: Tenant organization. Has metadata and admin users.
- **Role**: Named permission set within a company (e.g., `admin`, `manager`, `viewer`). Roles belong to a company.
- **User**: Person with credentials and membership in exactly one role within a company.
- **JWT (access_token)**: Short-lived token 24h, used to authorize API calls.
- **Refresh Token**: Long-lived token 30d used to obtain new access tokens.
- **RBAC**: Role-Based Access Control, enforced per-company.

---

## 3. High-level Requirements

1. Register a company and create an initial company owner (user) in one flow.
2. Create roles for the company; roles have a name and a set of permissions (string identifiers).
3. Register users and assign them to one or more roles in the same company.
4. Login / Issue JWT access and refresh tokens.
5. Token revocation (logout) and refresh flows.
6. Authorization checks based on role permissions for protected endpoints.

---

## 4. Data Models (JSON + Example DB Schema)

### Company
```json
{
  "id": "uuid",
  "name": "string",
  "domain": "string (optional)",
  "metadata": {"...": "..."},
  "created_at": "timestamp",
  "updated_at": "timestamp"
}
```

DB (Mysql example): `companies(id uuid pk, name text, domain text, metadata jsonb, created_at timestamptz, updated_at timestamptz)`

### Role
```json
{
  "id": "uuid",
  "company_id": "uuid",
  "name": "string",
  "permissions": ["string"],
  "created_at": "timestamp"
}
```

DB (Mysql example): `roles(id uuid pk, company_id uuid fk, name text, permissions text[] or jsonb, created_at timestamptz)`

### User
```json
{
  "id": "uuid",
  "company_id": "uuid",
  "email": "string",
  "display_name": "string",
  "password_hash": "string",
  "is_verified": "bool",
  "roles": ["role_id"],
  "created_at": "timestamp"
}
```

DB (Mysql example): `users(id uuid pk, company_id uuid fk, email text unique per company?, display_name text, password_hash text, is_verified boolean, created_at timestamptz)`

### Auth Tokens
- `access_tokens` are JWTs, not stored server-side (stateless) except for a revocation list or token versioning stored per-user.
- `refresh_tokens` should be stored hashed in DB: `refresh_tokens(id uuid pk, user_id uuid fk, token_hash text, expires_at timestamptz, revoked boolean)`

---

## 5. API Endpoints (suggested)

> All endpoints accept/return JSON. Use HTTP status codes: 200/201/204/400/401/403/404/409/500.

### Public / Auth

- `POST /v1/companies/register` — Create company + initial owner
  - Body: `{ name, domain?, owner: { email, password, display_name } }`
  - Returns: `201` with company and owner user id + tokens

- `POST /v1/auth/login` — Login (email + password + company_id or company_domain)
  - Body: `{ email, password, company_id | domain }`
  - Returns: `{ access_token, expires_in, refresh_token }`

- `POST /v1/auth/refresh` — Exchange refresh token for new access token
  - Body: `{ refresh_token }`
  - Returns: new `access_token` (and optionally new `refresh_token`)

- `POST /v1/auth/logout` — Revoke refresh token(s)
  - Body: `{ refresh_token }` or use Auth header to revoke all for user

### Companies
- `GET /v1/companies/{company_id}` — Get company (auth: company admin or service)
- `PUT /v1/companies/{company_id}` — Update company (auth: admin)
- `DELETE /v1/companies/{company_id}` — Delete company (auth: admin)

### Roles (company-scoped)
- `POST /v1/companies/{company_id}/roles` — Create role (auth: admin)
- `GET /v1/companies/{company_id}/roles` — List roles
- `GET /v1/companies/{company_id}/roles/{role_id}` — Get role
- `PUT /v1/companies/{company_id}/roles/{role_id}` — Update role
- `DELETE /v1/companies/{company_id}/roles/{role_id}` — Delete role (disallow delete if assigned to users unless `force=true`)

Role payload: `{ name: string, permissions: ["perm:create_user", "perm:delete_user"] }

### Users
- `POST /v1/companies/{company_id}/users` — Create user (auth: admin or invite flow)
  - Body: `{ email, display_name, password?, role_ids: [] }`
- `GET /v1/companies/{company_id}/users` — List users (paginated)
- `GET /v1/companies/{company_id}/users/{user_id}` — Get user
- `PUT /v1/companies/{company_id}/users/{user_id}` — Update user (auth: admin or user themself)
- `DELETE /v1/companies/{company_id}/users/{user_id}` — Delete user (auth: admin)
- `POST /v1/companies/{company_id}/users/{user_id}/roles` — Assign roles
- `DELETE /v1/companies/{company_id}/users/{user_id}/roles/{role_id}` — Remove role

---

## 6. Authentication & Authorization

### JWT Access Token (example claims)
```
{
  "iss": "https://api.example.com",
  "sub": "user:uuid",
  "aud": "example-api",
  "exp": 1699999999,
  "iat": 1699990000,
  "cid": "company_uuid",
  "roles": ["role_id1", "role_id2"],
  "perm": ["perm:create_user","perm:view_company"]
}
```

- `cid` = company id, used for quick tenant checks.
- `perm` array can be embedded or derived from roles server-side.

**Signing & Validation**
- Use RS256 (asymmetric) or HS256 depending on needs. Prefer RS256 for rotation and introspection.
- Validate `iss`, `aud`, `exp`, `nbf` (if used), signature.
- Reject tokens older than a short TTL or with mismatched `token_version` if you use rotating keys or in-token counters.

**Refresh tokens**
- Issue opaque long-lived refresh tokens (store hashed server-side).
- On refresh: validate DB record, rotate refresh token, revoke previous refresh token (optional sliding sessions).
- Support revocation and logout (set `revoked=true`).

**Authorization checks**
- Prefer server-side permission resolution: from token claims + DB lookup of roles/permissions for critical checks.
- For performance, include derived permissions in token; but always re-check for sensitive ops (or support token versioning to force immediate revocation).

---

## 7. Validation & Error Handling

- Use structured error responses: `{ code: "ERR_INVALID_INPUT", message: "...", details: {...} }`
- Validate email format, password strength (min length, complexity rules), company name uniqueness.
- Return `409 Conflict` when trying to create duplicate company domain or duplicate user email within company.

---

## 8. Invariants (MUST always be true)

1. User.company_id must match the company where roles are assigned.
2. Role.company_id must equal the company for any assignment.
3. Email uniqueness: either unique globally or unique per company—decide and document (RECOMMEND: unique per company with optional global uniqueness flag).
4. Password hashes must be stored using a secure algorithm (bcrypt/argon2) and never returned by API.
5. Refresh tokens stored hashed in DB and revocable.

---

## 9. Acceptance Criteria / Tests

### Basic flows
- Company registration returns `201` with `company_id` and creates initial owner and tokens.
- Login with correct credentials returns `200` with `access_token` and `refresh_token`.
- Accessing protected endpoint with valid access_token returns `200`.
- Accessing protected endpoint with expired/malformed token returns `401`.

### Role & Authorization
- Create role and assign permission; user with role can access permitted endpoint; user without role receives `403`.
- Deleting role in use returns `409` unless `force=true`.

### Token lifecycle
- Refresh endpoint rotates refresh token and issues new access token.
- Logout revokes refresh token and subsequent refresh attempts fail with `401`.

---

## 10. Security Considerations

- Use TLS for all endpoints.
- Rate limit auth endpoints to prevent brute force.
- Protect against account enumeration (careful error messages on login/register).
- Use secure cookie or Authorization header for tokens; prefer Authorization: `Bearer <token>`.
- Implement CSRF protections if tokens are stored in cookies.
- Log auth events (login, refresh, logout, failed login) to help detect attacks.

---

## 11. Observability & Metrics

- Request counts, latencies, errors per endpoint
- Auth metrics: login_success, login_failure, refresh_success, refresh_failure, revoked_tokens_count
- User metrics: users_created_per_company
- Security: failed_login_rate, suspicious_ip_rate

---

## 12. API Example (short OpenAPI-like snippets)

**POST /v1/auth/login**
```json
Request:
{ "email": "ana@example.com", "password": "P@ssw0rd", "company_domain": "acme" }

Response 200:
{ "access_token": "ey...", "expires_in": 900, "refresh_token": "rt_..." }
```

**POST /v1/companies/register**
```json
Request:
{
  "name": "Acme Inc",
  "domain": "acme",
  "owner": { "email": "ana@acme.com", "password": "P@ssw0rd", "display_name": "Ana" }
}

Response 201:
{ "company": { "id": "...", "name": "Acme Inc" }, "owner": { "id": "..." }, "tokens": { "access_token": "..", "refresh_token": ".." } }
```

---

## 13. Migration & Backwards Compatibility

- Add new permissions as additive; old tokens may not include new perms until rotation.
- Provide a `token_version` per user to force invalidation of previously issued tokens if required.

---

## 14. Open Questions

- Should email be globally unique or unique per company?
- Will companies have subdomains and automatic onboarding via SSO (SAML / OIDC)?
- Do roles include hierarchical inheritance?

---

## 15. Next Steps (Implementation)

1. Approve spec
2. Generate OpenAPI definition
3. Implement database schema and migrations
4. Implement auth service (login, refresh, revoke)
5. Implement roles and RBAC enforcement middleware
6. Add tests: unit, integration, and security tests

---


*End of specification template.*

