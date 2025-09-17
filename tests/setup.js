// Test setup file
process.env.NODE_ENV = 'test';
process.env.JWT_SECRET = 'test-secret';
process.env.MONGODB_URI = 'mongodb://localhost:27017/ai_interview_test';

// Mock console.log and console.error for cleaner test output
global.console = {
  ...console,
  log: jest.fn(),
  error: jest.fn(),
};

// Global test timeout
jest.setTimeout(30000);