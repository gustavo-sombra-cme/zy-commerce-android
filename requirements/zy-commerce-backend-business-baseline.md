# ZY-Commerce Backend Baseline

Date: 2026-06-16

Purpose: summarize the implemented backend business logic, request/response flows, runtime behavior, and existing harness so Android work can start from a stable source of truth.

## Evidence Sources

- Repository code under `src/`, `tests/`, `docs/project/`, `docs/decisions/`
- Live OpenAPI document at `http://localhost:5015/swagger/v1/swagger.json`
- Live read-only probes against `http://localhost:5015`

## Verification Status

Verified live:

- Swagger/OpenAPI is reachable.
- Catalog list endpoint responds.
- Protected Auth current-user endpoint returns `401` without a token.
- Search validation returns `400` with `ValidationProblemDetails`.
- `GET /api/catalog/products/{productId}` returns `400` for `Guid.Empty`.

Verified from repository code and project memory:

- Clean Architecture modular monolith structure
- Auth and Catalog module behavior
- JWT bearer authentication setup
- Error-handling middleware
- Architecture and unit test coverage

Not exercised live to avoid mutating local training data:

- Register user
- Login user
- Create product
- Update product
- Deactivate product

## Existing Harness Already In Repo

The backend already implements harness-engineering practices:

- `AGENT.md` is a short router.
- `instructions/00-05-*.md` hold execution, architecture, CQRS, testing, security, memory, and completion rules.
- `docs/project/PROJECT_STATUS.md` stores implementation snapshot.
- `docs/project/AI_HANDOFF.md` stores session constraints and architecture context.
- `docs/project/NEXT_SESSION.md` is a fast resume file.
- `docs/project/ROADMAP.md` tracks completed and candidate work.
- `docs/project/PROMPT_TEMPLATE.md` standardizes future plan/execute prompts.
- `docs/prompts/` stores execution history.
- `docs/decisions/ADR-001-product-search-read-model.md` records an accepted architecture decision.

This is already a strong base for multi-session AI-assisted Android/backend parallel work.

## System Shape

Architecture style:

- .NET 9 ASP.NET Core backend
- Clean Architecture
- Modular monolith
- CQRS with MediatR
- DDD-style aggregates and value objects
- Thin controllers
- EF Core persistence
- SQL Server LocalDB in development

Modules:

- Catalog
- Auth

Shared building blocks:

- Application
- Domain
- Infrastructure

Dependency rules enforced by tests:

- Domain does not depend on Application, Infrastructure, Contracts, or API.
- Application does not depend on Infrastructure or API.
- Infrastructure does not depend on API.
- BuildingBlocks do not depend on modules.
- Catalog and Auth do not reference each other.

## Business Modules

### Catalog

Business responsibility:

- manage products
- expose public read operations
- require authentication for write operations

Domain model:

- Aggregate: `Product`
- Value objects: `ProductId`, `Sku`, `ProductName`

Persisted product fields:

- `Id`
- `Sku`
- `Name`
- `Description`
- `IsActive`
- `CreatedAt`
- `UpdatedAt`

Core product rules:

- SKU is required.
- SKU is normalized to uppercase and trimmed.
- SKU max length is 64.
- SKU allows only `A-Z`, `0-9`, `-`, `_`.
- Product name is required.
- Product name is trimmed.
- Product name max length is 200.
- Description is optional.
- Blank description becomes `null`.
- Description max length is 2000.
- New products start as `IsActive = true`.
- Deactivation is idempotent.
- Updating details changes only `Name`, `Description`, and `UpdatedAt`.
- Updating details does not change SKU.
- Duplicate SKU is blocked.

### Auth

Business responsibility:

- register users
- validate credentials
- issue short-lived JWT bearer access tokens
- return current authenticated user from token claims

Domain model:

- Aggregate: `User`
- Value objects: `UserId`, `Email`, `PasswordHash`

Persisted user fields:

- `Id`
- `Email`
- `PasswordHash`
- `IsActive`
- `IsEmailVerified`
- `CreatedAt`
- `UpdatedAt`

Core user rules:

- Email is required.
- Email is trimmed and normalized to lowercase.
- Email max length is 320.
- Email shape must contain one `@`, a domain, and a dot in the domain.
- Password is required for registration and login.
- Registration password length must be 8-128.
- Duplicate email is blocked.
- New users start as `IsActive = true`.
- New users start as `IsEmailVerified = false`.
- Inactive users cannot log in.

Auth intentionally does not implement:

- refresh tokens
- roles
- permissions
- token persistence

## API Surface

Base URL:

- `http://localhost:5015`

Swagger:

- `http://localhost:5015/swagger/index.html`

### Auth Endpoints

#### `POST /api/auth/users/register`

Purpose:

- create a new user account

Request body:

```json
{
  "email": "user@example.com",
  "password": "string"
}
```

Behavior:

- normalizes email
- validates email format
- validates password length
- checks uniqueness by email
- hashes password with ASP.NET Core Identity password hasher
- persists user

Expected outcomes from code:

- `201 Created` with `userId`, `email`
- `400 Bad Request` for validation failures
- `409 Conflict` for duplicate email

#### `POST /api/auth/users/login`

Purpose:

- authenticate a user and issue an access token

Request body:

```json
{
  "email": "user@example.com",
  "password": "string"
}
```

Behavior:

- normalizes email
- loads user by email
- rejects unknown email
- rejects inactive user
- verifies password against stored hash
- generates JWT access token

Response shape:

```json
{
  "userId": "uuid",
  "email": "user@example.com",
  "accessToken": "jwt",
  "tokenType": "Bearer",
  "expiresAt": "date-time"
}
```

Expected outcomes from code:

- `200 OK`
- `400 Bad Request` for validation failures
- `401 Unauthorized` for invalid credentials
- `403 Forbidden` for inactive user

#### `GET /api/auth/users/me`

Purpose:

- return current authenticated user from JWT claims

Auth:

- bearer token required

Behavior:

- reads `sub` and `email` from token claims
- returns only identity projection, not full user profile

Response shape:

```json
{
  "userId": "uuid",
  "email": "user@example.com"
}
```

Live verification:

- returns `401 Unauthorized` with no token

### Catalog Endpoints

#### `GET /api/catalog/products`

Purpose:

- list/search products

Auth:

- public

Query params:

- `searchTerm`
- `isActive`
- `pageNumber`
- `pageSize`

Behavior:

- defaults `pageNumber` to `1`
- defaults `pageSize` to `20`
- validates `pageNumber >= 1`
- validates `1 <= pageSize <= 100`
- trims `searchTerm`
- filters by SKU or name using SQL `LIKE`
- optional filter by `isActive`
- sorts by `CreatedAt DESC`, then `Name ASC`
- returns paged result

Response shape:

```json
{
  "items": [
    {
      "productId": "uuid",
      "sku": "string",
      "name": "string",
      "description": "string|null",
      "isActive": true,
      "createdAt": "date-time"
    }
  ],
  "pageNumber": 1,
  "pageSize": 20,
  "totalCount": 0,
  "totalPages": 0,
  "hasPreviousPage": false,
  "hasNextPage": false
}
```

Live verification:

- current local database returns empty list
- invalid `pageNumber=0&pageSize=101` returns `400` with validation errors

#### `GET /api/catalog/products/{productId}`

Purpose:

- fetch one product by id

Auth:

- public

Behavior:

- explicit transport-level rejection of `Guid.Empty`
- returns read projection
- returns `404` when not found

Response shape:

```json
{
  "productId": "uuid",
  "sku": "string",
  "name": "string",
  "description": "string|null",
  "isActive": true,
  "createdAt": "date-time",
  "updatedAt": "date-time|null"
}
```

Live verification:

- `Guid.Empty` returns `400` with `{ "message": "Product id cannot be empty." }`

#### `POST /api/catalog/products`

Purpose:

- create a product

Auth:

- bearer token required

Request body:

```json
{
  "sku": "SKU-001",
  "name": "Product name",
  "description": "Optional description"
}
```

Behavior:

- validates SKU, name, description
- normalizes SKU
- checks uniqueness by SKU
- creates active product with generated id and `CreatedAt`
- persists product

Expected outcomes from code:

- `201 Created`
- `400 Bad Request` for validation failures
- `401 Unauthorized` without valid token
- `409 Conflict` for duplicate SKU

#### `PUT /api/catalog/products/{productId}`

Purpose:

- update mutable product details

Auth:

- bearer token required

Request body:

```json
{
  "name": "Updated name",
  "description": "Updated description"
}
```

Behavior:

- validates `productId`
- validates name and description
- loads aggregate by id
- updates only name and description
- sets `UpdatedAt`
- returns no content

Expected outcomes from code:

- `204 No Content`
- `400 Bad Request` for validation failures
- `401 Unauthorized` without valid token
- `404 Not Found` if product does not exist

#### `DELETE /api/catalog/products/{productId}`

Purpose:

- deactivate a product

Auth:

- bearer token required

Behavior:

- validates `productId`
- loads aggregate by id
- sets `IsActive = false`
- sets `UpdatedAt`
- repeated deactivation is harmless

Expected outcomes from code:

- `204 No Content`
- `400 Bad Request` for validation failures
- `401 Unauthorized` without valid token
- `404 Not Found` if product does not exist

## Data Flow By Use Case

### Register User

1. API receives `RegisterUserRequest`.
2. Controller sends `RegisterUserCommand` through MediatR.
3. FluentValidation validates email and password.
4. Application checks email uniqueness through `IUserRepository`.
5. Password is hashed.
6. Domain `User.Register(...)` creates aggregate.
7. Repository saves user.
8. Unit of work commits.
9. API returns `201 Created`.

### Login User

1. API receives `LoginUserRequest`.
2. Controller sends `LoginUserCommand`.
3. FluentValidation validates request.
4. Repository loads user by normalized email.
5. Application rejects missing user or inactive user.
6. Password hash is verified.
7. JWT generator creates access token with `sub`, `email`, `jti`, `iat`.
8. API returns token payload.

### Get Current User

1. JWT bearer middleware authenticates token.
2. Controller reads `sub` and `email` claims.
3. Controller returns `userId` and `email`.
4. Missing/invalid token returns `401`.

### Create Product

1. Authenticated request hits controller.
2. Controller sends `CreateProductCommand`.
3. FluentValidation validates SKU/name/description.
4. Application normalizes SKU and checks uniqueness.
5. Domain `Product.Create(...)` creates aggregate.
6. Repository saves product.
7. Unit of work commits.
8. API returns `201 Created`.

### Search Products

1. Public request hits controller.
2. Controller sends `SearchProductsQuery`.
3. Validator checks pagination bounds.
4. Query handler normalizes search term and fills defaults.
5. Read repository queries infrastructure read model.
6. API returns paged projection.

### Update Product Details

1. Authenticated request hits controller.
2. Controller sends `UpdateProductDetailsCommand`.
3. Validator checks `productId`, name, description.
4. Repository loads aggregate.
5. Domain updates name/description and `UpdatedAt`.
6. Unit of work commits.
7. API returns `204 No Content`.

### Deactivate Product

1. Authenticated request hits controller.
2. Controller sends `DeactivateProductCommand`.
3. Validator checks `productId`.
4. Repository loads aggregate.
5. Domain deactivates product and sets `UpdatedAt`.
6. Unit of work commits.
7. API returns `204 No Content`.

## Search Read Model Decision

Catalog search does not query `Product.Sku.Value` or `Product.Name.Value` directly in EF filters.

Accepted solution:

- keep DDD value objects in the write model
- introduce infrastructure-only `ProductSearchReadModel`
- query through `CatalogReadDbContext`

Reason:

- EF Core translation over value-object members was unreliable for server-side search

Impact on Android:

- no contract impact
- search remains server-side
- list responses are read projections, not full aggregates

## Authentication Details Relevant To Android

- Token type returned by login is `Bearer`.
- Swagger and API expect `Authorization: Bearer <token>`.
- JWT lifetime is configured to 15 minutes in development settings.
- Current token claims used by `/me` are `sub` and `email`.
- There is no refresh flow yet, so Android should assume re-login is required after expiration until backend evolves.

## Error Model

Global exception middleware translates common failures into HTTP responses:

- validation errors -> `400` with `ValidationProblemDetails`
- duplicate SKU -> `409 Conflict`
- duplicate email -> `409 Conflict`
- invalid credentials -> `401 Unauthorized`
- inactive user -> `403 Forbidden`
- missing resource -> `404 Not Found`
- unknown errors -> `500 Internal Server Error`

Observed response shapes:

- validation failures use `application/problem+json`
- `GET /api/catalog/products/{Guid.Empty}` currently returns a simple JSON message from controller transport checks, not `ProblemDetails`
- unauthenticated `/api/auth/users/me` returned bare `401` with `WWW-Authenticate: Bearer`

Implication:

- Android client should not assume one single error JSON shape for every failure case.

## OpenAPI Contract Caveats

The live Swagger document is useful, but it is not a perfect expression of runtime behavior.

Observed mismatch:

- `POST /api/auth/users/register` is implemented with `Created(...)` in the controller, but the live OpenAPI document currently advertises only `200 OK`.

Likely additional documentation gaps:

- some exception-driven `400`, `409`, `403`, and `404` outcomes are enforced by middleware/code but are not exhaustively declared in Swagger for every endpoint.

Implication:

- use repository code plus this baseline as the stronger source of truth for Android client handling, not generated OpenAPI alone.

## Database State And Local Environment

Development persistence:

- SQL Server LocalDB
- Catalog DB: `EcommerceCatalog`
- Auth DB: `EcommerceAuth`

Known migrations:

- Catalog: `20260608111338_InitialCatalogSchema`
- Auth: `20260609092505_InitialAuthSchema`

Live current state observed:

- product catalog is empty

## What Is Ready For Android Right Now

Ready now:

- user registration
- user login
- access-token based authenticated session
- current-user fetch
- public catalog listing with pagination
- public product detail fetch
- protected product create/update/deactivate flows for internal/admin use

Not ready or intentionally absent:

- customer profile module
- cart
- checkout
- orders
- payments
- refresh tokens
- roles/permissions
- protected customer-specific catalog behavior

## Android Integration Notes

Recommended client sequence for current backend:

1. Implement Auth service first: register, login, store bearer token in secure storage, fetch `/me`.
2. Implement public Catalog browse flows second: list and detail.
3. Keep write-product flows behind a dev/admin-only flag if the Android app will expose them at all.
4. Model error handling with both `ProblemDetails` and plain JSON/message or empty-body auth failures.
5. Treat access tokens as short-lived and re-authenticate when expired.

## Bottom Line

The backend currently provides a clean initial vertical slice for Android:

- account creation
- login with JWT
- current authenticated user
- public catalog browse/list/detail
- protected catalog management operations

It also already contains a solid harness layer for multi-session AI work, so future Android and backend sessions can continue with explicit memory, constraints, and documented execution history instead of relying on chat context.
