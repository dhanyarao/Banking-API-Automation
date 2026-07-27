// @ts-check
const { defineConfig } = require('@playwright/test');
require('dotenv').config({ path: './config/.env' });

/**
 * Playwright configuration optimized for API testing + Allure reporting.
 */
module.exports = defineConfig({
  testDir: './tests',
  fullyParallel: false,
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
    ['allure-playwright', {
      detail: true,
      outputFolder: 'allure-results',
      suiteTitle: true,
      environmentInfo: {
        Framework: 'Playwright',
        Project: 'Banking API Automation',
        BASE_URL: process.env.BASE_URL || 'https://api.yourbank.com',
      },
    }],
  ],
  use: {
    baseURL: process.env.BASE_URL || 'https://api.yourbank.com',
    extraHTTPHeaders: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    trace: 'on-first-retry',
  },
});
