# Banking API Automation – Practical Package

This package contains **two complete automation approaches** for the same Banking APIs we covered in the test cases:

1. **Postman + Newman** (no-code / low-code)
2. **Rest Assured + TestNG** (Java code-based)

Both cover the core scenarios:
- Login (valid + invalid)
- Get Account Balance (positive + negative)
- Fund Transfer (success, insufficient funds, missing fields)
- Transaction History

---

## 1. Postman + Newman (Recommended starting point)

### Folder: `postman/`

| File | Purpose |
|------|---------|
| `Banking_API_Automation.postman_collection.json` | Ready-to-import collection with folders & Tests |
| `Banking-Dev.postman_environment.json` | Environment variables (base_url, credentials, accounts) |
| `test-data.csv` | Sample data file for data-driven runs |
| `run-newman.sh` | One-click runner script with HTML + JUnit reports |

### How to use

1. Open Postman → Import the `.postman_collection.json` and `.postman_environment.json`
2. Select environment **Banking-Dev**
3. Update `base_url`, `username`, `password`, `account_id` etc. to match your actual API
4. Run the collection manually first (Collection Runner)
5. For CLI automation:

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

## 2. Rest Assured + TestNG (Java)

### Folder: `rest-assured/`

```
rest-assured/
├── pom.xml
└── src/test/
    ├── java/com/banking/api/
    │   ├── BaseTest.java          ← common setup + constants
    │   ├── AuthTests.java
    │   ├── AccountTests.java
    │   ├── FundTransferTests.java
    │   └── TransactionTests.java
    └── resources/
        └── testng.xml
```

### How to run

1. Open the `rest-assured` folder in IntelliJ / Eclipse / VS Code
2. Update constants in `BaseTest.java` (BASE_URL, USERNAME, PASSWORD, ACCOUNT_ID…)
3. Run:

```bash
cd rest-assured
mvn clean test
```

Or run individual classes from the IDE.

---

## Recommended Learning Path

1. Import & run the Postman collection manually → understand the flow
2. Run it with Newman → see HTML reports
3. Open the Rest Assured project → map each Postman request to the corresponding Java test
4. Add more assertions and negative cases yourself

---

## Instructor Notes

- Always keep the **Login** request first so the token is available for subsequent calls.
- In real banking projects we also add:
  - Request/Response logging
  - Retry logic for flaky network
  - Allure / Extent reports
  - Data-driven testing from Excel/CSV
  - Parallel execution (carefully – banking APIs often have concurrency limits)

Happy automating!
