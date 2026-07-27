/**
 * Account Balance API Tests
 * Uses custom fixture for authenticated context
 */

const { test, expect } = require('../utils/fixtures');

test.describe('Account Balance APIs', () => {

  test('GET /accounts/{id}/balance - valid token returns balance', async ({ apiContext }) => {
    const accountId = process.env.ACCOUNT_ID;

    const response = await apiContext.get(`/api/v1/accounts/${accountId}/balance`);

    expect(response.status()).toBe(200);

    const body = await response.json();
    expect(body).toHaveProperty('availableBalance');
    expect(typeof body.availableBalance).toBe('number');
    expect(body.availableBalance).toBeGreaterThanOrEqual(0);
    expect(body).toHaveProperty('currency');
  });

  test('GET /accounts/{id}/balance - invalid token returns 401', async ({ request }) => {
    const accountId = process.env.ACCOUNT_ID;

    const response = await request.get(`/api/v1/accounts/${accountId}/balance`, {
      headers: {
        Authorization: 'Bearer invalidtoken12345',
      },
    });

    expect(response.status()).toBe(401);
  });

  test('GET /accounts/{id}/balance - missing Authorization header returns 401', async ({ request }) => {
    const accountId = process.env.ACCOUNT_ID;

    const response = await request.get(`/api/v1/accounts/${accountId}/balance`);

    expect(response.status()).toBe(401);
  });
});
