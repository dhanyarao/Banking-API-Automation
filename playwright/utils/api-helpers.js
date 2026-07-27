/**
 * Shared helpers for Banking API tests.
 * Best practice: keep authentication and common request logic in one place.
 */

/**
 * Perform login and return the auth token.
 * @param {import('@playwright/test').APIRequestContext} request
 * @returns {Promise<string>} JWT token
 */
async function getAuthToken(request) {
  const response = await request.post('/api/v1/auth/login', {
    data: {
      username: process.env.USERNAME,
      password: process.env.PASSWORD,
    },
  });

  if (response.status() !== 200) {
    const body = await response.text();
    throw new Error(`Login failed with status ${response.status()}: ${body}`);
  }

  const body = await response.json();
  if (!body.token) {
    throw new Error('Login response did not contain a token');
  }

  return body.token;
}

/**
 * Create a request context that already carries the Bearer token.
 * @param {import('@playwright/test').Playwright} playwright
 * @param {string} token
 */
async function createAuthenticatedContext(playwright, token) {
  return await playwright.request.newContext({
    baseURL: process.env.BASE_URL || 'https://api.yourbank.com',
    extraHTTPHeaders: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });
}

module.exports = {
  getAuthToken,
  createAuthenticatedContext,
};
