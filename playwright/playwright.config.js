// @ts-check
const { defineConfig } = require('@playwright/test');
require('dotenv').config({ path: './config/.env' });

/**
 * Playwright configuration optimized for API testing.
 * Best practices applied:
 * - No browser needed for pure API tests
 * - Controlled parallelism
 * - Clear reporting
 * - Environment-driven baseURL
 */
module.exports = defineConfig({
  testDir: './tests',
  fullyParallel: false,          // Banking APIs often have concurrency limits
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  workers: process.env.CI ? 2 : 1,
  timeout: 30 * 1000,
  expect: {
    timeout: 10 * 1000,
  },
  reporter: [
    ['list'],
    ['html', { open: 'never', outputFolder: 'playwright-report' }],
    ['json', { outputFile: 'test-results/results.json' }],
  ],
  use: {
    baseURL: process.env.BASE_URL || 'https://api.yourbank.com',
    extraHTTPHeaders: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    // Useful for debugging failed requests
    trace: 'on-first-retry',
  },
});
