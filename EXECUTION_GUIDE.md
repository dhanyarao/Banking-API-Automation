# Banking API Automation – Step-by-Step Execution Guide

Practical guide to run the full stack on your machine: **WireMock → Newman → Playwright → Rest Assured → reports**.

**Video demo outline (18–25 min):** [VIDEO_SCRIPT.md](./VIDEO_SCRIPT.md)

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

```bash
git clone https://github.com/dhanyarao/Banking-API-Automation.git
cd Banking-API-Automation
```

---

## 2. Start WireMock

```bash
cd wiremock && chmod +x start-wiremock.sh && ./start-wiremock.sh
```

Or Docker:

```bash
docker run --rm -p 8089:8080 \
  -v "$(pwd)/mappings:/home/wiremock/mappings" \
  wiremock/wiremock:3.9.1 --verbose
```

Verify:

```bash
curl -s http://localhost:8089/__admin/mappings | head
curl -s -X POST http://localhost:8089/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser01","password":"SecurePass@123"}'
```

---

## 3. Newman

```bash
cd postman
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export newman-report.html
```

Single folder: `--folder "02_Fund_Transfer"`

---

## 4. Playwright

```bash
cd playwright && npm install
# config/.env with BASE_URL=http://localhost:8089
npx playwright test && npx playwright show-report
```

---

## 5. Rest Assured

```bash
cd rest-assured
export BASE_URL=http://localhost:8089 API_USERNAME=testuser01 API_PASSWORD=SecurePass@123
mvn -B clean test && mvn allure:serve
```

---

## 6. Checklist

| Step | Pass criteria |
|------|----------------|
| WireMock up | curl mappings + login token |
| Newman | Majority green + HTML report |
| Playwright | Specs finish + show-report |
| Rest Assured | mvn test + Allure |

---

## 7. CI

WireMock in Docker → three tools → Allure on GitHub Pages: https://dhanyarao.github.io/Banking-API-Automation/

---

## 8. Troubleshooting

| Problem | Fix |
|---------|-----|
| ENOTFOUND | BASE_URL=http://localhost:8089 |
| 401 on protected APIs | Run 00_Setup first; auth_token saved |
| Port in use | `./start-wiremock.sh 9090` |

Full detail and copy-paste block: see repo history for longer guide sections, or follow [VIDEO_SCRIPT.md](./VIDEO_SCRIPT.md) for a timed demo.

**Mock user:** testuser01 / SecurePass@123 — not for production.
