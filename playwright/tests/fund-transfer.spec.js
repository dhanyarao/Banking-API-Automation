/**
 * Fund Transfer API Tests
 * Positive + critical negative scenarios
 */

const { test, expect } = require('../utils/fixtures');

test.describe('Fund Transfer APIs', () => {

  test('POST /transfer - successful transfer', async ({ apiContext }) => {
    const payload = {
      fromAccount: process.env.FROM_ACCOUNT,
      toAccount: process.env.TO_ACCOUNT,
      amount: 500.00,
      currency: 'INR',
      remarks: 'Playwright automated transfer',
    };

    const response = await apiContext.post('/api/v1/transfer', {
      data: payload,
    });

    expect([200, 201]).toContain(response.status());

    const body = await response.json();
    const hasId = body.transactionId || body.referenceNumber || body.id;
    expect(hasId, 'Response should contain a transaction identifier').toBeTruthy();
  });

  test('POST /transfer - insufficient funds returns 400', async ({ apiContext }) => {
    const payload = {
      fromAccount: process.env.FROM_ACCOUNT,
      toAccount: process.env.TO_ACCOUNT,
      amount: 99999999.00,
      currency: 'INR',
      remarks: 'Should fail - insufficient funds',
    };

    const response = await apiContext.post('/api/v1/transfer', {
      data: payload,
    });

    expect(response.status()).toBe(400);

    const body = await response.json();
    const message = (body.message || body.error || JSON.stringify(body)).toLowerCase();
    expect(message).toContain('insufficient');
  });

  test('POST /transfer - missing mandatory fields returns 400/422', async ({ apiContext }) => {
    const payload = {
      fromAccount: process.env.FROM_ACCOUNT,
    };

    const response = await apiContext.post('/api/v1/transfer', {
      data: payload,
    });

    expect([400, 422]).toContain(response.status());
  });

  test('POST /transfer - zero amount is rejected', async ({ apiContext }) => {
    const payload = {
      fromAccount: process.env.FROM_ACCOUNT,
      toAccount: process.env.TO_ACCOUNT,
      amount: 0,
      currency: 'INR',
      remarks: 'Zero amount test',
    };

    const response = await apiContext.post('/api/v1/transfer', {
      data: payload,
    });

    expect([400, 422]).toContain(response.status());
  });

  test('POST /transfer - negative amount is rejected', async ({ apiContext }) => {
    const payload = {
      fromAccount: process.env.FROM_ACCOUNT,
      toAccount: process.env.TO_ACCOUNT,
      amount: -100,
      currency: 'INR',
      remarks: 'Negative amount test',
    };

    const response = await apiContext.post('/api/v1/transfer', {
      data: payload,
    });

    expect([400, 422]).toContain(response.status());
  });
});
