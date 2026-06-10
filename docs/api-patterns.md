# API Patterns — ZY-Commerce Android

## Base URL
- **Debug (emulator):** `http://10.0.2.2:5015/` (configured via `BuildConfig.BASE_URL`)
- **Release:** `https://api.zy-commerce.com/` (replace when backend is deployed)

## Endpoints Consumed

| Method | Path | Use Case |
|--------|------|----------|
| `GET` | `/api/catalog/products` | Search / list products |
| `GET` | `/api/catalog/products/{id}` | Get product detail |
| `POST` | `/api/catalog/products` | Create product |
| `DELETE` | `/api/catalog/products/{id}` | Deactivate product |

## Response Envelope (Search)
```json
{
  "items": [...],
  "pageNumber": 1,
  "pageSize": 20,
  "totalCount": 42,
  "totalPages": 3,
  "hasPreviousPage": false,
  "hasNextPage": true
}
```

## Field Constraints (from backend validators)
- **SKU**: required, uppercase letters/numbers/hyphen/underscore only, max 64 chars
- **Name**: required, max 200 chars  
- **Description**: optional, max 2000 chars
- **pageNumber**: ≥ 1
- **pageSize**: 1–100

## Auth
Currently **no auth** — backend Auth module is skeleton only. No tokens required.
