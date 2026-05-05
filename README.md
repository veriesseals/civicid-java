# CivicID

A secure, role-based government identity management system built with Spring Boot. CivicID provides a centralized backend for managing citizen records, birth records, and law enforcement identity verification — with a full audit trail on every action.

---

## What It Does

CivicID is a multi-module API that different government agencies connect to based on their role. A registrar can file birth records. A law enforcement officer can verify a citizen's identity and log the reason. An auditor can see every action ever taken in the system. Each role sees exactly what they need — nothing more.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (HS384) |
| Database | H2 (in-memory, dev) |
| ORM | Hibernate / Spring Data JPA |
| Build | Maven (via `mvnw` wrapper) |
| Validation | Jakarta Bean Validation |

---

## Roles

| Role | What They Can Do |
|---|---|
| `SUPER_ADMIN` | Full access to everything |
| `REGISTRAR` | Create and view persons and birth records |
| `LAW_ENFORCEMENT` | Verify citizen identity (reason required), view own lookup history |
| `AUDITOR` | Read-only access to all records and the full audit trail |
| `DMV` | Search and view persons |
| `SSA` | Search and view persons |
| `ELECTIONS` | View persons |
| `STATE_DEPT` | View persons |
| `IMMIGRATION` | View persons |

---

## Modules Built So Far

### Accounts (`/api/auth`)
Handles authentication. Login returns a JWT used on every subsequent request.

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/login` | Public |

### Persons (`/api/persons`)
The core citizen record. Every other module references a person.

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/persons` | SUPER_ADMIN, REGISTRAR |
| GET | `/api/persons` | SUPER_ADMIN, REGISTRAR, AUDITOR |
| GET | `/api/persons/{id}` | Most roles |
| GET | `/api/persons/search?firstName=&lastName=` | SUPER_ADMIN, REGISTRAR, AUDITOR, DMV, SSA |
| PUT | `/api/persons/{id}` | SUPER_ADMIN, REGISTRAR |
| PUT | `/api/persons/{id}/deceased` | SUPER_ADMIN, REGISTRAR |

> SSNs are masked in all responses (`***-**-6789`).

### Birth Records (`/api/birth_records`)
Official birth certificates filed by registrars. One record per person.

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/birth_records/person/{personId}` | SUPER_ADMIN, REGISTRAR |
| GET | `/api/birth_records` | SUPER_ADMIN, REGISTRAR, AUDITOR |
| GET | `/api/birth_records/{id}` | SUPER_ADMIN, REGISTRAR, AUDITOR |
| GET | `/api/birth_records/person/{personId}` | SUPER_ADMIN, REGISTRAR, AUDITOR |

### Law Enforcement (`/api/law_enforcement`)
The most restricted module. Officers must provide a reason for every lookup. All lookups are permanently logged.

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/law_enforcement/verify/{personId}` | SUPER_ADMIN, LAW_ENFORCEMENT |
| GET | `/api/law_enforcement/history/me` | SUPER_ADMIN, LAW_ENFORCEMENT |
| GET | `/api/law_enforcement/history/person/{personId}` | SUPER_ADMIN, AUDITOR |
| GET | `/api/law_enforcement/history` | SUPER_ADMIN, AUDITOR |

> Verify returns only: name, date of birth, and deceased status. No SSN, no address.

### Audit (`/api/audit`)
Read-only. Every action in the system creates an audit log entry automatically. No one can write to or delete from the audit trail through the API.

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/audit/logs` | SUPER_ADMIN, AUDITOR |
| GET | `/api/audit/user/{username}` | SUPER_ADMIN, AUDITOR |
| GET | `/api/audit/person/{personId}` | SUPER_ADMIN, AUDITOR |
| GET | `/api/audit/action/{action}` | SUPER_ADMIN, AUDITOR |
| GET | `/api/audit/role/{role}` | SUPER_ADMIN, AUDITOR |

---

## Running Locally

**Prerequisites:** Java 17+, Maven (or use the included wrapper)

```bash
# Clone the repo
git clone <repo-url>
cd civicid-java

# Run
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

A default `SUPER_ADMIN` account is created automatically on first run:
- Username: `admin`
- Password: `Tyaanah201!`

The H2 console is available at `http://localhost:8080/h2-console` for inspecting the in-memory database during development.

### Getting a Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Tyaanah201!"}'
```

Use the returned token in subsequent requests:

```bash
curl http://localhost:8080/api/persons \
  -H "Authorization: Bearer <token>"
```

---

## Project Structure

```
src/main/java/com/civicid/
├── apps/
│   ├── accounts/          # Auth, users, roles, JWT login
│   ├── audit/             # System-wide audit log
│   ├── birth_records/     # Birth certificate management
│   ├── law_enforcement/   # Identity verification + lookup history
│   └── persons/           # Core citizen records
├── config/                # Password encoder config
└── shared/
    ├── config/            # Spring Security configuration
    ├── health/            # Health check endpoint
    └── security/          # JWT filter and utilities
```

---

## Still To Come

### Backend
- [ ] Death records module
- [ ] DMV module (driver licenses, vehicle registration)
- [ ] Elections module (voter registration)
- [ ] SSA module (Social Security records)
- [ ] State Department module (passports)
- [ ] Immigration module
- [ ] User management endpoints (create/deactivate accounts, assign roles)
- [ ] MFA support (field exists on User, logic not yet implemented)
- [ ] Migrate from H2 to PostgreSQL for production
- [ ] Spring profiles (dev / staging / prod)
- [ ] Standardized API error responses
- [ ] Input validation hardening
- [ ] Rate limiting on sensitive endpoints
- [ ] Unit and integration tests

### Frontend
- [ ] React frontend (not started)
- [ ] Role-based dashboard — each role sees only their relevant modules
- [ ] Citizen record search UI
- [ ] Birth record filing UI
- [ ] Law enforcement verification UI
- [ ] Audit log viewer
- [ ] Admin panel (user management, role assignment)
- [ ] Login / JWT session management

---

## Security Design

- All endpoints require a valid JWT except `/api/auth/login` and `/api/health`
- Roles are enforced at the method level with `@PreAuthorize`
- Sessions are stateless — no cookies, no server-side session storage
- Law enforcement lookups are logged before data is returned, not after
- SSNs are never returned in plaintext
- Audit logs cannot be created, modified, or deleted through the API
