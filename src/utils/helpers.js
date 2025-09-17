const jwt = require('jsonwebtoken');

// Generate JWT token
const generateToken = (payload, expiresIn = '30d') => {
  return jwt.sign(payload, process.env.JWT_SECRET || 'fallback_secret', {
    expiresIn
  });
};

// Verify JWT token
const verifyToken = (token) => {
  return jwt.verify(token, process.env.JWT_SECRET || 'fallback_secret');
};

// Format date for display
const formatDate = (date) => {
  return new Date(date).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// Calculate time difference in minutes
const getTimeDifferenceInMinutes = (startDate, endDate) => {
  const diff = new Date(endDate) - new Date(startDate);
  return Math.round(diff / 60000); // Convert milliseconds to minutes
};

// Validate email format
const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Generate random string
const generateRandomString = (length = 10) => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
};

// Sanitize user input
const sanitizeInput = (input) => {
  if (typeof input !== 'string') return input;
  return input.trim().replace(/[<>]/g, '');
};

// Calculate interview completion percentage
const calculateCompletionPercentage = (interview) => {
  if (!interview.questions || interview.questions.length === 0) return 0;
  
  const answeredQuestions = interview.questions.filter(q => q.answer && q.answer.trim() !== '').length;
  return Math.round((answeredQuestions / interview.questions.length) * 100);
};

// Get interview difficulty color
const getDifficultyColor = (difficulty) => {
  const colors = {
    beginner: '#4CAF50',
    intermediate: '#FF9800',
    advanced: '#F44336'
  };
  return colors[difficulty] || '#9E9E9E';
};

// Get status badge color
const getStatusColor = (status) => {
  const colors = {
    scheduled: '#2196F3',
    'in-progress': '#FF9800',
    completed: '#4CAF50',
    cancelled: '#F44336'
  };
  return colors[status] || '#9E9E9E';
};

// Paginate results
const paginate = (query, page = 1, limit = 10) => {
  const startIndex = (page - 1) * limit;
  return query.limit(limit).skip(startIndex);
};

// Sort query
const sortQuery = (query, sortBy = 'createdAt', order = 'desc') => {
  const sortOrder = order === 'asc' ? 1 : -1;
  return query.sort({ [sortBy]: sortOrder });
};

// Response formatter
const formatResponse = (success, data = null, error = null, meta = null) => {
  const response = { success };
  
  if (data) response.data = data;
  if (error) response.error = error;
  if (meta) response.meta = meta;
  
  return response;
};

// Error logger
const logError = (error, context = '') => {
  console.error(`❌ Error ${context}:`, {
    message: error.message,
    stack: error.stack,
    timestamp: new Date().toISOString()
  });
};

// Success logger
const logSuccess = (message, data = null) => {
  console.log(`✅ ${message}`, data ? { data, timestamp: new Date().toISOString() } : { timestamp: new Date().toISOString() });
};

module.exports = {
  generateToken,
  verifyToken,
  formatDate,
  getTimeDifferenceInMinutes,
  isValidEmail,
  generateRandomString,
  sanitizeInput,
  calculateCompletionPercentage,
  getDifficultyColor,
  getStatusColor,
  paginate,
  sortQuery,
  formatResponse,
  logError,
  logSuccess
};