/**
 * Authentication API Tests
 * Covers: valid login, invalid password, missing credentials
 */

const { test, expect } = require('@playwright/test');

test.describe('Authentication APIs', () => {

  test('POST /login - valid credentials returns token', async ({ request }) => {
    const response = await request.post('/api/v1/auth/login', {
      data: {
        username: process.env.USERNAME,
        password: process.env.PASSWORD,
      },
    });

    expect(response.status(), 'Expected 200 for valid login').toBe(200);

    const body = await response.json();
    expect(body).toHaveProperty('token');
    expect(typeof body.token).toBe('string');
    expect(body.token.length).toBeGreaterThan(10);
  });

  test('POST /login - invalid password returns 401', async ({ request }) => {
    const response = await request.post('/api/v1/auth/login', {
      data: {
        username: process.env.USERNAME,
        password: 'WrongPass999',
      },
    });

    expect(response.status()).toBe(401);

    const body = await response.json();
    expect(body).not.toHaveProperty('token');
  });

  test('POST /login - missing credentials returns 4xx', async ({ request }) => {
    const response = await request.post('/api/v1/auth/login', {
      data: {},
    });

    expect([400, 401, 422]).toContain(response.status());
  });

  test('POST /login - empty username and password', async ({ request }) => {
    const response = await request.post('/api/v1/auth/login', {
      data: {
        username: '',
        password: '',
      },
    });

    expect([400, 401, 422]).toContain(response.status());
  });
});
