/**
 * Custom Playwright fixtures for Banking API tests.
 * Best practice: reuse authenticated request context across tests
 * without repeating login code.
 */

const { test as base, expect } = require('@playwright/test');
const { getAuthToken } = require('./api-helpers');

/**
 * Extended test object that provides:
 * - authToken   → string
 * - apiContext  → already authenticated APIRequestContext
 */
const test = base.extend({
  // Token is obtained once per worker and shared
  authToken: async ({ request }, use) => {
    const token = await getAuthToken(request);
    await use(token);
  },

  // Authenticated request context (token already injected)
  apiContext: async ({ playwright, authToken }, use) => {
    const context = await playwright.request.newContext({
      baseURL: process.env.BASE_URL || 'https://api.yourbank.com',
      extraHTTPHeaders: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${authToken}`,
      },
    });
    await use(context);
    await context.dispose();
  },
});

module.exports = { test, expect };
