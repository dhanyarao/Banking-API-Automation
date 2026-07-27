# Banking API Automation – Practical Package

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

Or manually:

```bash
npm install -g newman newman-reporter-htmlextra
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Dev.postman_environment.json \
  --reporters cli,htmlextra,junit \
  --reporter-htmlextra-export reports/banking-api-report.html
```

---

## 2. Rest Assured (Java)

**Folder:** `rest-assured/`

```bash
cd rest-assured
# Update constants in BaseTest.java first
mvn clean test
```

---

## 3. Playwright (JavaScript)

**Folder:** `playwright/`

```bash
cd playwright
npm install
cp config/.env.example config/.env   # edit with real values
npm test
npm run test:report
```

See `playwright/README.md` for fixtures, helpers and best practices.

| Feature              | Postman + Newman | Rest Assured | Playwright      |
|----------------------|------------------|--------------|-----------------|
| Language             | None / JS        | Java         | JavaScript      |
| Same tool for UI     | No               | No           | **Yes**         |
| Custom fixtures      | Limited          | Manual       | Native          |
| Learning curve       | Lowest           | Medium       | Low-Medium      |

---

## Continuous Integration (GitHub Actions)

Workflow file: `.github/workflows/ci.yml`

Runs automatically on every **push** and **pull request** to `main`.

| Job            | Tool              | Output                          |
|----------------|-------------------|---------------------------------|
| `playwright`   | Playwright        | HTML + JSON report (artifact)   |
| `newman`       | Postman + Newman  | HTML + JUnit report (artifact)  |
| `rest-assured` | Maven + TestNG    | Surefire reports (artifact)     |

### Using real credentials in CI

1. Repo → **Settings → Secrets and variables → Actions**
2. Add these secrets:

| Secret           | Example                    |
|------------------|----------------------------|
| `BASE_URL`       | `https://api.yourbank.com` |
| `API_USERNAME`   | `testuser01`               |
| `API_PASSWORD`   | `your-real-password`       |
| `ACCOUNT_ID`     | `1234567890`               |
| `FROM_ACCOUNT`   | `1234567890`               |
| `TO_ACCOUNT`     | `9876543210`               |

Until real secrets are added, the workflow uses placeholder values (tests will fail against a non-existent API – expected).

You can also trigger the workflow manually from the **Actions** tab.

---

## Recommended Learning Path

1. Run the Postman collection manually → understand the flow
2. Run it with Newman → study the HTML report
3. Explore Rest Assured → map each request to a Java test
4. Explore Playwright → notice the fixture pattern for auth
5. Add your own negative cases and assertions

---

## Instructor Notes

- Keep **Login** first so the token is available for subsequent calls.
- In real banking projects we also add: request/response logging, retry logic, Allure/Extent reports, data-driven testing from Excel/CSV, and careful parallel execution (banking APIs often have concurrency limits).

Happy automating!
