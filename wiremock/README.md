# WireMock – Mock Banking API

Local HTTP mock server that implements the same endpoints used by Postman, Playwright and Rest Assured.

## Endpoints mocked

| Method | Path | Behaviour |
|--------|------|-----------|
| POST | `/api/v1/auth/login` | 200 + token if `testuser01` / `SecurePass@123`, else 401 |
| GET | `/api/v1/accounts/{id}/balance` | 200 with balance if Bearer token present, else 401 |
| POST | `/api/v1/transfer` | 200 if amount < 100000, 400 insufficient if ≥ 100000, 400 validation otherwise |
| GET | `/api/v1/transactions` | 200 list if Bearer token, else 401 |

## Quick start (local)

```bash
cd wiremock
chmod +x start-wiremock.sh
./start-wiremock.sh          # port 8089
```

Or Docker:

```bash
docker run --rm -p 8089:8080 \
  -v "$(pwd)/mappings:/home/wiremock/mappings" \
  wiremock/wiremock:3.9.1 --global-response-templating --verbose
```

## Point suites at the mock

| Tool | How |
|------|-----|
| **Newman** | `-e Banking-Mock.postman_environment.json` |
| **Playwright** | `BASE_URL=http://localhost:8089` in `config/.env` |
| **Rest Assured** | `export BASE_URL=http://localhost:8089` |

## GitHub Actions integration

The CI workflow starts WireMock via Docker in **every** test job:

```yaml
docker run -d --name wiremock \
  -p 8089:8080 \
  -v "${{ github.workspace }}/wiremock/mappings:/home/wiremock/mappings" \
  wiremock/wiremock:3.9.1 --verbose
```

Then sets `BASE_URL=http://localhost:8089` for Playwright, Newman and Rest Assured.

No real banking API is required for green CI builds.

Happy mocking!
