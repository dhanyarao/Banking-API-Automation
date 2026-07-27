# Playwright API Test Suite – Banking Application

Modern, maintainable API automation using **Playwright Test** (JavaScript).

This suite covers the same banking scenarios we built earlier:

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
| Controlled parallelism | `fullyParallel: false` (banking APIs often rate-limit) |
| Rich reporting | HTML + JSON reporters |
| No hardcoded secrets | All credentials come from `.env` |
| Flexible assertions | Handle different response shapes from real banks |

---

## Quick Start

```bash
cd playwright

# 1. Install dependencies
npm install

# 2. Copy and edit environment file
cp config/.env.example config/.env
# → update BASE_URL, USERNAME, PASSWORD, ACCOUNT_ID etc.

# 3. Run all tests
npm test

# 4. Run specific module
npm run test:auth
npm run test:accounts
npm run test:transfer
npm run test:transactions

# 5. View HTML report
npm run test:report
```

---

## Project Structure

```
playwright/
├── package.json
├── playwright.config.js
├── config/
│   ├── .env.example
│   └── .env                 ← your real values (git-ignored)
├── utils/
│   ├── api-helpers.js       ← login helper + context factory
│   └── fixtures.js          ← custom test + apiContext fixture
└── tests/
    ├── auth.spec.js
    ├── accounts.spec.js
    ├── fund-transfer.spec.js
    └── transactions.spec.js
```

---

## How Authentication Works (Best Practice)

1. `fixtures.js` creates a custom `test` object.
2. Before any test that needs auth, it calls `getAuthToken()`.
3. It builds a new `APIRequestContext` that already contains  
   `Authorization: Bearer <token>`.
4. Tests simply use `{ apiContext }` – no login code duplicated.

This is cleaner than putting login inside every `beforeEach`.

---

Happy automating!
