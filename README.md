# Banking API Automation – Practical Package

[![CI](https://github.com/dhanyarao/Banking-API-Automation/actions/workflows/ci.yml/badge.svg)](https://github.com/dhanyarao/Banking-API-Automation/actions/workflows/ci.yml)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange?logo=allure)](https://dhanyarao.github.io/Banking-API-Automation/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This package contains **three complete automation approaches** for the same Banking APIs:

1. **Postman + Newman** (no-code / low-code)
2. **Rest Assured + TestNG** (Java)
3. **Playwright** (JavaScript) – modern API testing with best practices

All three cover:
- Login (valid + invalid)
- Account Balance
- Fund Transfer (success, insufficient funds, validation errors)
- Transaction History

---

## 1. Postman + Newman

**Folder:** `postman/`

```bash
cd postman
chmod +x run-newman.sh
./run-newman.sh
```

---

## 2. Rest Assured (Java)

**Folder:** `rest-assured/`

```bash
cd rest-assured
mvn clean test
mvn allure:report   # or mvn allure:serve
```

---

## 3. Playwright (JavaScript)

**Folder:** `playwright/`

```bash
cd playwright
npm install
cp config/.env.example config/.env
npm test
npm run test:allure
```

| Feature              | Postman + Newman | Rest Assured | Playwright      |
|----------------------|------------------|--------------|-----------------|
| Language             | None / JS        | Java         | JavaScript      |
| Same tool for UI     | No               | No           | **Yes**         |
| Custom fixtures      | Limited          | Manual       | Native          |
| Allure reporting     | Via Newman HTML  | Yes          | Yes             |

---

## Continuous Integration + GitHub Pages

Workflow: `.github/workflows/ci.yml`

Runs on every push / PR to `main`:

| Job              | Tool              | Output                    |
|------------------|-------------------|---------------------------|
| `playwright`     | Playwright        | Allure + HTML artifacts   |
| `newman`         | Postman + Newman  | HTML + JUnit artifacts    |
| `rest-assured`   | Maven + TestNG    | Allure + Surefire         |
| `publish-pages`  | GitHub Pages      | Live Allure reports       |

### Live Allure Reports

**URL:** https://dhanyarao.github.io/Banking-API-Automation/

| Path | Report |
|------|--------|
| `/` | Landing page |
| `/playwright/` | Playwright Allure report |
| `/rest-assured/` | Rest Assured Allure report |

### One-time setup for GitHub Pages

1. Repo → **Settings → Pages**
2. **Source** → select **GitHub Actions**
3. Save

Then push to `main` (or run the workflow manually). Reports will appear at the URL above.

### Secrets (recommended)

| Secret           | Example                    |
|------------------|----------------------------|
| `BASE_URL`       | `https://api.yourbank.com` |
| `API_USERNAME`   | `testuser01`               |
| `API_PASSWORD`   | `your-real-password`       |
| `ACCOUNT_ID`     | `1234567890`               |
| `FROM_ACCOUNT`   | `1234567890`               |
| `TO_ACCOUNT`     | `9876543210`               |

---

## Recommended Learning Path

1. Run Postman collection manually
2. Run Newman + study HTML report
3. Explore Rest Assured + Allure annotations
4. Explore Playwright fixtures + Allure
5. Watch reports update on GitHub Pages after each push

Happy automating!
