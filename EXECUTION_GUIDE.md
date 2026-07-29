# Banking API Automation – Step-by-Step Execution Guide

Practical guide to run the full stack on your machine: **WireMock → Newman → Playwright → Rest Assured → reports**.

Use this when you are learning or when you join a banking QA team and need a repeatable local runbook.

---

## 0. What you will run

```
┌─────────────────┐
│   WireMock      │  Mock banking API on http://localhost:8089
│   (mappings/)   │
└────────┬────────┘
         │ BASE_URL=http://localhost:8089
    ┌────┴────┬────────────┐
    ▼         ▼            ▼
 Newman   Playwright   Rest Assured
 (Postman)  (JS)         (Java)
    │         │            │
    └────┬────┴────────────┘
         ▼
   CLI / HTML / Allure reports
```

**Recommended order (first time):** WireMock → Newman → Playwright → Rest Assured.

---

## 1. Prerequisites

| Tool | Version (suggested) | Check command |
|------|---------------------|---------------|
| Git | any recent | `git --version` |
| Java | 11+ | `java -version` |
| Maven | 3.8+ | `mvn -v` |
| Node.js | 18 or 20 LTS | `node -v` |
| npm | comes with Node | `npm -v` |
| Docker (optional) | for WireMock image | `docker -v` |
| Postman app (optional) | for manual exploration | — |

Clone the repo:

```bash
git clone https://github.com/dhanyarao/Banking-API-Automation.git
cd Banking-API-Automation
```

---

## 2. Start the mock banking API (WireMock)

Without a real bank API, **always start WireMock first**.

### Option A – Script (standalone JAR)

```bash
cd wiremock
chmod +x start-wiremock.sh
./start-wiremock.sh
```

- First run downloads `wiremock-standalone.jar` (needs network once).
- Default port: **8089**
- Custom port: `./start-wiremock.sh 9090`

Leave this terminal open. You should see logs when requests hit the mock.

### Option B – Docker

```bash
cd wiremock
docker run --rm -p 8089:8080 \
  -v "$(pwd)/mappings:/home/wiremock/mappings" \
  wiremock/wiremock:3.9.1 \
  --global-response-templating --verbose
```

### Verify WireMock is up

Open a **second** terminal:

```bash
curl -s http://localhost:8089/__admin/mappings | head
```

You should see JSON listing loaded stubs (login, balance, transfer, etc.).

### Quick smoke against the mock

```bash
# Login
curl -s -X POST http://localhost:8089/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser01","password":"SecurePass@123"}'
```

Expect HTTP 200 and a `token` field.

**Keep WireMock running** for all steps below.

---

## 3. Run Postman collection with Newman

### 3.1 One-time install

```bash
npm install -g newman newman-reporter-htmlextra
```

### 3.2 Run against WireMock (recommended first run)

```bash
cd postman

newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --delay-request 200 \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export newman-report.html \
  --reporter-htmlextra-title "Banking API – Local WireMock Run"
```

Or use the helper script (if present):

```bash
chmod +x run-newman.sh
./run-newman.sh
```

### 3.3 What to expect

| Area | Typical result against WireMock |
|------|----------------------------------|
| Login valid | Pass – token saved |
| Login injection / empty body | Pass – 401 / 4xx |
| Balance + invalid token | Pass |
| Transfer success / min / special remarks | Pass |
| Insufficient / negative / zero / large | Pass – 400 |
| Concurrent A/B + integrity | Pass – balance ≥ 0 |
| Transactions + invalid dates | Pass |

Open `newman-report.html` in a browser for the HTML report.

### 3.4 Run only one folder (smoke / selective)

```bash
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --folder "00_Setup"
```

Useful folders: `00_Setup`, `01_Accounts`, `02_Fund_Transfer`, `03_Transactions`.

### 3.5 Run against a real / UAT API

```bash
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Dev.postman_environment.json \
  --env-var "base_url=https://your-uat-api.example.com" \
  --reporters cli,htmlextra
```

---

## 4. Run Playwright (JavaScript) API tests

### 4.1 Install

```bash
cd playwright
npm install
npx playwright install --with-deps chromium   # once
```

### 4.2 Configure environment

Create `config/.env`:

```env
BASE_URL=http://localhost:8089
USERNAME=testuser01
PASSWORD=SecurePass@123
ACCOUNT_ID=1234567890
FROM_ACCOUNT=1234567890
TO_ACCOUNT=9876543210
```

### 4.3 Run all tests

```bash
npm test
# or
npx playwright test
```

### 4.4 Run with filters

```bash
npx playwright test --grep @smoke
npx playwright test tests/auth.spec.js
npx playwright test --ui
```

### 4.5 Reports

```bash
npx playwright show-report
```

---

## 5. Run Rest Assured (Java + TestNG)

### 5.1 Point to WireMock

```bash
cd rest-assured
export BASE_URL=http://localhost:8089
export API_USERNAME=testuser01
export API_PASSWORD=SecurePass@123
export ACCOUNT_ID=1234567890
export FROM_ACCOUNT=1234567890
export TO_ACCOUNT=9876543210
```

### 5.2 Run tests

```bash
mvn -B clean test
```

### 5.3 Allure report

```bash
mvn -B allure:report
mvn allure:serve
```

### 5.4 Single class

```bash
mvn -B test -Dtest=AuthTests
mvn -B test -Dtest=FundTransferTests
```

---

## 6. End-to-end local checklist (30 minutes)

| Step | Action | Pass criteria |
|------|--------|----------------|
| 1 | Start WireMock | `curl` admin mappings works |
| 2 | `curl` login | JSON with `token` |
| 3 | Newman full collection | Majority green on WireMock |
| 4 | Open Newman HTML report | Charts + request details visible |
| 5 | Playwright `npm test` | Specs finish; HTML report opens |
| 6 | Rest Assured `mvn test` | Tests run; Allure generates |
| 7 | Stop WireMock (Ctrl+C) | Clean shutdown |

---

## 7. CI (GitHub Actions)

On push/PR to `main`, CI starts WireMock in Docker, runs Playwright + Newman + Rest Assured, uploads artifacts, and publishes Allure to GitHub Pages.

**Reports:** https://dhanyarao.github.io/Banking-API-Automation/

Enable Pages once: **Settings → Pages → Source = GitHub Actions**.

---

## 8. Troubleshooting

| Problem | What to do |
|---------|------------|
| `ENOTFOUND api.yourbank.com` | Set `base_url` / `BASE_URL` to `http://localhost:8089` |
| Newman 401 on protected calls | Run `00_Setup` first; ensure Tests save `auth_token` |
| WireMock 404 | Check mappings volume mount; restart WireMock |
| Port 8089 in use | `./start-wiremock.sh 9090` and update env |
| Concurrent both succeed | Mock has no shared balance; integrity check still asserts balance ≥ 0 |

```bash
curl -s http://localhost:8089/__admin/requests/unmatched | jq .
```

---

## 9. Mock credentials (not production)

| Field | Value |
|-------|--------|
| Username | `testuser01` |
| Password | `SecurePass@123` |
| Account | `1234567890` |
| Mock URL | `http://localhost:8089` |

Never commit real banking passwords. Use GitHub Secrets for UAT in CI.

---

## 10. Quick copy-paste (happy path)

```bash
# Terminal 1
cd Banking-API-Automation/wiremock && ./start-wiremock.sh

# Terminal 2
cd Banking-API-Automation/postman
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export newman-report.html

cd ../playwright && npm install && \
  printf 'BASE_URL=http://localhost:8089\nUSERNAME=testuser01\nPASSWORD=SecurePass@123\nACCOUNT_ID=1234567890\nFROM_ACCOUNT=1234567890\nTO_ACCOUNT=9876543210\n' > config/.env && \
  npx playwright test

cd ../rest-assured && \
  export BASE_URL=http://localhost:8089 API_USERNAME=testuser01 API_PASSWORD=SecurePass@123 && \
  mvn -B clean test
```
