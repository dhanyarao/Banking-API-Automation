# Banking API Automation – Practical Package

[![CI](https://github.com/dhanyarao/Banking-API-Automation/actions/workflows/ci.yml/badge.svg)](https://github.com/dhanyarao/Banking-API-Automation/actions/workflows/ci.yml)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange?logo=allure)](https://dhanyarao.github.io/Banking-API-Automation/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Full step-by-step runbook:** [EXECUTION_GUIDE.md](./EXECUTION_GUIDE.md)

This package contains **three complete automation approaches** for the same Banking APIs:

1. **Postman + Newman** (no-code / low-code)
2. **Rest Assured + TestNG** (Java)
3. **Playwright** (JavaScript) – modern API testing with best practices

All three cover:
- Login (valid + invalid)
- Account Balance
- Fund Transfer (success, insufficient funds, validation errors)
- Transaction History
- Boundary / edge cases (min amount, concurrent, special chars, large amounts)

---

## Quick start (with WireMock mock)

```bash
# Terminal 1 – mock API
cd wiremock && ./start-wiremock.sh

# Terminal 2 – Newman
cd postman
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json --reporters cli
```

See **[EXECUTION_GUIDE.md](./EXECUTION_GUIDE.md)** for Playwright, Rest Assured, Allure, CI, and troubleshooting.

---

## 1. Postman + Newman

**Folder:** `postman/`

```bash
cd postman
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export newman-report.html
```

---

## 2. Rest Assured (Java)

**Folder:** `rest-assured/`

```bash
cd rest-assured
export BASE_URL=http://localhost:8089
mvn clean test
mvn allure:serve
```

---

## 3. Playwright (JavaScript)

**Folder:** `playwright/`

```bash
cd playwright
npm install
# set BASE_URL=http://localhost:8089 in config/.env
npm test
npx playwright show-report
```

---

## Continuous Integration + GitHub Pages

Workflow: `.github/workflows/ci.yml`

- Starts WireMock in Docker
- Runs Playwright, Newman, Rest Assured
- Publishes Allure to https://dhanyarao.github.io/Banking-API-Automation/

---

## Recommended Learning Path

1. Read [EXECUTION_GUIDE.md](./EXECUTION_GUIDE.md)
2. Run WireMock + Newman
3. Explore Playwright fixtures
4. Explore Rest Assured + Allure
5. Watch CI on GitHub Actions

Happy automating!
