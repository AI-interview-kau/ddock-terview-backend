const request = require('supertest');
const app = require('../src/app');

describe('API Health Check', () => {
  test('GET /health should return 200', async () => {
    const response = await request(app)
      .get('/health')
      .expect(200);
    
    expect(response.body.status).toBe('OK');
    expect(response.body.message).toBe('AI Interview Backend is running');
    expect(response.body.timestamp).toBeDefined();
  });
});

describe('API 404 Handler', () => {
  test('GET /non-existent-route should return 404', async () => {
    const response = await request(app)
      .get('/non-existent-route')
      .expect(404);
    
    expect(response.body.error).toBe('Route not found');
  });
});

describe('Authentication Routes', () => {
  test('POST /api/auth/register with invalid data should return 400', async () => {
    const response = await request(app)
      .post('/api/auth/register')
      .send({
        name: '',
        email: 'invalid-email',
        password: '123'
      })
      .expect(400);
    
    expect(response.body.success).toBe(false);
    expect(response.body.errors).toBeDefined();
  });

  test('POST /api/auth/login with invalid data should return 400', async () => {
    const response = await request(app)
      .post('/api/auth/login')
      .send({
        email: 'invalid-email',
        password: ''
      })
      .expect(400);
    
    expect(response.body.success).toBe(false);
    expect(response.body.errors).toBeDefined();
  });
});