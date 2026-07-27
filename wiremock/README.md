# WireMock – Mock Banking API

Local HTTP mock server that implements the same endpoints used by Postman, Playwright and Rest Assured.

## Endpoints mocked

| Method | Path | Behaviour |
|--------|------|-----------|
| POST | `/api/v1/auth/login` | 200 + token if `testuser01` / `SecurePass@123`, else 401 |
| GET | `/api/v1/accounts/{id}/balance` | 200 with balance if Bearer token present, else 401 |
| POST | `/api/v1/transfer` | 200 if amount < 100000, 400 insufficient if ≥ 100000, 400 validation otherwise |
| GET | `/api/v1/transactions` | 200 list if Bearer token, else 401 |

## Quick start

### Option A – Standalone JAR (recommended for Newman / Playwright)

```bash
cd wiremock
chmod +x start-wiremock.sh
./start-wiremock.sh          # default port 8089
# or
./start-wiremock.sh 9090     # custom port
```

Requires Java 11+.

### Option B – Docker

```bash
docker run --rm -p 8089:8080 \
  -v "$(pwd)/mappings:/home/wiremock/mappings" \
  wiremock/wiremock:3.9.1 --global-response-templating --verbose
```

## Point your suites at the mock

| Tool | How |
|------|-----|
| **Newman** | Use environment `Banking-Mock-WireMock` or `--env-var "base_url=http://localhost:8089"` |
| **Playwright** | Set `BASE_URL=http://localhost:8089` in `config/.env` |
| **Rest Assured** | Set env `BASE_URL=http://localhost:8089` |

### Example – Newman against WireMock

```bash
# Terminal 1
cd wiremock && ./start-wiremock.sh

# Terminal 2
cd postman
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --reporters cli,htmlextra
```

Happy mocking!
