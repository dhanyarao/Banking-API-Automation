# Playwright API Test Suite – Banking Application

Modern, maintainable API automation using **Playwright Test** (JavaScript) with **Allure** reporting.

This suite covers:

- Authentication (login positive + negative)
- Account Balance
- Fund Transfer (success + insufficient funds + validation errors)
- Transaction History

---

## Best Practices Applied

| Practice | How it is implemented |
|----------|------------------------|
| Environment variables | `config/.env` + `dotenv` |
| Reusable auth | Custom fixture (`utils/fixtures.js`) injects `apiContext` with Bearer token |
| DRY helpers | `utils/api-helpers.js` |
| Clear folder structure | `tests/`, `utils/`, `config/` |
| Controlled parallelism | `fullyParallel: false` |
| Rich reporting | Playwright HTML + **Allure** |
| No hardcoded secrets | All credentials come from `.env` |

---

## Quick Start

```bash
cd playwright
npm install
cp config/.env.example config/.env   # edit with real values
npm test
```

---

## Allure Reporting

```bash
# Run tests (Allure results written automatically)
npm test

# Generate & open Allure report
npm run allure:generate
npm run allure:open

# One-liner
npm run test:allure

# Serve without static generation
npm run allure:serve
```

Install Allure CLI locally if needed:
```bash
npm install -g allure-commandline
# or download from https://github.com/allure-framework/allure2/releases
```

### What you get

- Test status timeline
- Suites / behaviors
- Environment info (BASE_URL, Framework, etc.)
- Retry history
- Failure categories

### CI

GitHub Actions generates the Allure report and uploads it as artifact **`allure-report-playwright`**.

---

## Project Structure

```
playwright/
├── package.json
├── playwright.config.js      ← includes allure-playwright reporter
├── config/
│   └── .env.example
├── utils/
│   ├── api-helpers.js
│   └── fixtures.js
└── tests/
    ├── auth.spec.js
    ├── accounts.spec.js
    ├── fund-transfer.spec.js
    └── transactions.spec.js
```

Happy automating!
