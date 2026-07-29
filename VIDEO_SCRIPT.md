# Video Script Outline – Banking API Automation Demo

**Audience:** QA engineers / automation learners (beginner → intermediate)  
**Tone:** Instructor with 8+ years experience – clear, practical, no hype  
**Total length (suggested):** 18–25 minutes  
**Props:** Repo open in IDE, two terminals, browser (Newman report + optional Allure/Pages)

**Related doc:** [EXECUTION_GUIDE.md](./EXECUTION_GUIDE.md)

---

## Video metadata (for YouTube / internal LMS)

| Field | Suggestion |
|-------|------------|
| Title | Banking API Testing End-to-End: Postman, Newman, Playwright, Rest Assured + WireMock |
| Thumbnail text | Mock API → 3 tools → CI |
| Description | Full local run: WireMock mock bank API, Newman collection, Playwright JS, Rest Assured Java, Allure, GitHub Actions |
| Tags | API testing, Postman, Newman, Playwright, Rest Assured, WireMock, banking, QA automation |

---

## Act 0 – Cold open (30–45 sec)

**On screen:** Terminal with a failed Newman run (`ENOTFOUND api.yourbank.com`).

**Say:**
> “If you’ve ever written API tests against a banking URL that doesn’t exist yet—or UAT is down—you know this error. Today we fix that with a local mock, then run the *same* scenarios in Postman/Newman, Playwright, and Rest Assured. One contract, three tools, CI-ready.”

**Cut to:** Repo README with badges.

---

## Act 1 – Agenda (1 min)

**Talking points:**
1. Why mock a banking API (WireMock)
2. Run Newman against the mock (including boundary cases)
3. Same APIs in Playwright (JS fixtures)
4. Same APIs in Rest Assured (Java + Allure)
5. What CI does automatically

**Say:**
> “We won’t boil the ocean. You’ll see a repeatable path you can re-run after this video in under thirty minutes.”

---

## Act 2 – Project tour (2 min)

**On screen:** IDE file tree.

| Folder | One-line explanation |
|--------|----------------------|
| `wiremock/` | Fake bank: login, balance, transfer, history |
| `postman/` | Collection + Mock/Dev environments |
| `playwright/` | JS API tests + fixtures |
| `rest-assured/` | Java TestNG + Allure |
| `.github/workflows/` | CI with WireMock service |
| `EXECUTION_GUIDE.md` | Written runbook |

**Say:**
> “Same endpoints everywhere. Boundary cases—min amount, negative, concurrent integrity—are in the collection so you’re not only testing happy path.”

---

## Act 3 – Start WireMock (3 min)

```bash
cd wiremock && chmod +x start-wiremock.sh && ./start-wiremock.sh
```

Verify:

```bash
curl -s http://localhost:8089/__admin/mappings | head
curl -s -X POST http://localhost:8089/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser01","password":"SecurePass@123"}'
```

**Say:**
> “Token means the mock is alive. Demo credentials only—never real customer data. Skip WireMock and you get ENOTFOUND.”

---

## Act 4 – Newman full run (4–5 min)

```bash
cd postman
newman run Banking_API_Automation.postman_collection.json \
  -e Banking-Mock.postman_environment.json \
  --delay-request 200 \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export newman-report.html
```

Narrate folders while running: Setup → Accounts → Fund Transfer (boundaries + concurrent) → Transactions.

Open HTML report. Then selective:

```bash
newman run ... --folder "02_Fund_Transfer"
```

**Say:**
> “Sanity after a transfer-only fix—don’t run the world every time.”

---

## Act 5 – Playwright (3–4 min)

```bash
cd playwright && npm install
# config/.env → BASE_URL=http://localhost:8089
npx playwright test
npx playwright show-report
```

**Say:**
> “Fixture pattern: login once, reuse authenticated context. Same tool as UI automation.”

---

## Act 6 – Rest Assured (3–4 min)

```bash
cd rest-assured
export BASE_URL=http://localhost:8089 API_USERNAME=testuser01 API_PASSWORD=SecurePass@123
mvn -B clean test
mvn allure:serve
```

**Say:**
> “Java teams get readable HTTP tests plus Allure Epic/Feature/Story for stakeholders.”

---

## Act 7 – CI and reports (2 min)

Show GitHub Actions + https://dhanyarao.github.io/Banking-API-Automation/

**Say:**
> “CI starts WireMock in Docker, runs all three tools, publishes Allure. Enable Pages once under Settings.”

---

## Act 8 – Recap and homework (1–2 min)

1. Mock first (WireMock)
2. Newman proves the collection
3. Playwright / Rest Assured prove frameworks
4. CI keeps it honest

**Homework:** Add one Newman request · tag Playwright `@smoke` · break a WireMock mapping on purpose then fix it.

**Close:**
> “Full written steps: EXECUTION_GUIDE.md. Clone, start the mock, run the happy path today.”

---

## Shot list

| # | Shot | Duration |
|---|------|----------|
| 1 | Failed Newman ENOTFOUND | 10s |
| 2 | README badges | 10s |
| 3 | File tree tour | 40s |
| 4 | WireMock start + curl login | 90s |
| 5 | Full Newman run | 2–3 min |
| 6 | Newman HTML report | 30s |
| 7 | Playwright test + report | 2 min |
| 8 | Maven + Allure | 2 min |
| 9 | Actions / Pages | 60s |
| 10 | Recap + repo link | 45s |

---

## Presenter cheat sheet

```
T0 Agenda
T1 Tree + why mock
T2 WireMock up + curl token
T3 Newman full + report + --folder
T4 Playwright .env + test + show-report
T5 BASE_URL + mvn test + allure:serve
T6 Actions + Pages
T7 Recap + guide + homework
```

**Repo:** https://github.com/dhanyarao/Banking-API-Automation
