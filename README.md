# AI Interview Backend

A comprehensive backend API server for AI-powered interview practice platform built with Node.js, Express, and MongoDB.

## 🚀 Features

- **User Authentication & Authorization**: JWT-based secure authentication system
- **AI-Powered Interview Questions**: Dynamic question generation using OpenAI API
- **Real-time Answer Analysis**: AI-driven feedback and scoring system
- **Interview Management**: Complete CRUD operations for interview sessions
- **Progress Tracking**: User statistics and performance analytics
- **Secure API**: Rate limiting, CORS, and security middleware
- **Comprehensive Testing**: Unit and integration tests with Jest

## 🛠️ Technology Stack

- **Runtime**: Node.js
- **Framework**: Express.js
- **Database**: MongoDB with Mongoose ODM
- **Authentication**: JSON Web Tokens (JWT)
- **AI Integration**: OpenAI API
- **Testing**: Jest, Supertest
- **Security**: Helmet, CORS, Express Rate Limit
- **Code Quality**: ESLint

## 📁 Project Structure

```
src/
├── config/          # Database and app configuration
├── controllers/     # Route controllers (can be added for complex logic)
├── middleware/      # Custom middleware (auth, error handling)
├── models/          # MongoDB models/schemas
├── routes/          # API route definitions
├── services/        # Business logic and external services
├── utils/           # Utility functions and helpers
└── app.js          # Main application file

tests/              # Test files
```

## 🚦 Getting Started

### Prerequisites

- Node.js (v14 or higher)
- MongoDB (running locally or MongoDB Atlas)
- OpenAI API key (optional, fallback questions available)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd ai-interview-back
```

2. Install dependencies:
```bash
npm install
```

3. Set up environment variables:
```bash
cp .env.example .env
# Edit .env with your configuration
```

4. Start the development server:
```bash
npm run dev
```

The server will start on `http://localhost:3000`

### Environment Variables

Create a `.env` file based on `.env.example`:

```env
PORT=3000
NODE_ENV=development
MONGODB_URI=mongodb://localhost:27017/ai_interview
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRE=30d
FRONTEND_URL=http://localhost:3000
OPENAI_API_KEY=your-openai-api-key-here
```

## 📚 API Documentation

### Authentication Endpoints

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user profile
- `PUT /api/auth/profile` - Update user profile

### Interview Endpoints

- `POST /api/interview` - Create new interview
- `GET /api/interview` - Get user's interviews (with pagination)
- `GET /api/interview/:id` - Get specific interview
- `PUT /api/interview/:id` - Update interview
- `POST /api/interview/:id/start` - Start an interview session
- `POST /api/interview/:id/answer` - Submit answer for a question
- `POST /api/interview/:id/complete` - Complete interview
- `DELETE /api/interview/:id` - Delete interview

### User Statistics

- `GET /api/user/stats` - Get user interview statistics
- `GET /api/user/progress` - Get progress over time
- `GET /api/user/leaderboard` - Get public leaderboard

### Health Check

- `GET /health` - Server health status

## 🧪 Testing

Run the test suite:

```bash
# Run all tests
npm test

# Run tests with coverage
npm run test:coverage

# Run tests in watch mode
npm run test:watch
```

## 🔧 Development

### Available Scripts

- `npm start` - Start production server
- `npm run dev` - Start development server with nodemon
- `npm test` - Run tests
- `npm run lint` - Run ESLint
- `npm run lint:fix` - Fix ESLint issues

### Code Quality

The project uses ESLint for code quality and consistency. Run linting with:

```bash
npm run lint
npm run lint:fix  # Auto-fix issues
```

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Hashing**: bcryptjs for secure password storage
- **Rate Limiting**: Protection against brute force attacks
- **CORS**: Configurable cross-origin resource sharing
- **Helmet**: Security headers for Express
- **Input Validation**: Express-validator for request validation

## 🤖 AI Integration

The system integrates with OpenAI's API to provide:

- **Dynamic Question Generation**: Context-aware interview questions
- **Answer Analysis**: Detailed feedback and scoring
- **Interview Summaries**: Comprehensive performance reports

Fallback mechanisms ensure the system works even without AI integration.

## 📊 Data Models

### User Model
- Personal information and authentication
- Profile data and preferences
- Interview history tracking

### Interview Model
- Interview metadata and configuration
- Questions and answers
- AI analysis and scoring
- Progress tracking

## 🚀 Deployment

### Production Setup

1. Set environment variables for production
2. Use process manager like PM2:
```bash
npm install -g pm2
pm2 start src/app.js --name "ai-interview-api"
```

3. Set up reverse proxy with Nginx
4. Configure SSL certificates
5. Set up MongoDB replica set for production

### Docker (Optional)

Create a `Dockerfile` for containerized deployment:

```dockerfile
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY src ./src
EXPOSE 3000
CMD ["node", "src/app.js"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make changes and test thoroughly
4. Commit changes: `git commit -m 'Add feature'`
5. Push to branch: `git push origin feature-name`
6. Submit a pull request

### Development Guidelines

- Follow existing code style and conventions
- Write tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:

- Create an issue in the repository
- Check existing documentation
- Review the API examples

## 🔄 Changelog

### v1.0.0
- Initial release
- User authentication system
- Interview management
- AI integration
- Basic testing suite

---

**Built with ❤️ for better interview preparation**
