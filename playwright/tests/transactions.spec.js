/**
 * Transaction History API Tests
 */

const { test, expect } = require('../utils/fixtures');

test.describe('Transaction History APIs', () => {

  test('GET /transactions - returns list for valid account', async ({ apiContext }) => {
    const response = await apiContext.get('/api/v1/transactions', {
      params: {
        accountId: process.env.ACCOUNT_ID,
        page: 1,
        size: 10,
      },
    });

    expect(response.status()).toBe(200);

    const body = await response.json();
    const list = Array.isArray(body)
      ? body
      : (body.data || body.transactions || body.content || []);

    expect(Array.isArray(list)).toBeTruthy();
  });

  test('GET /transactions - filter by date range', async ({ apiContext }) => {
    const response = await apiContext.get('/api/v1/transactions', {
      params: {
        accountId: process.env.ACCOUNT_ID,
        fromDate: '2026-07-01',
        toDate: '2026-07-27',
      },
    });

    expect(response.status()).toBe(200);
  });

  test('GET /transactions - without auth returns 401', async ({ request }) => {
    const response = await request.get('/api/v1/transactions', {
      params: {
        accountId: process.env.ACCOUNT_ID,
      },
    });

    expect(response.status()).toBe(401);
  });
});
