const mongoose = require('mongoose');

const questionSchema = new mongoose.Schema({
  question: {
    type: String,
    required: true
  },
  answer: String,
  aiAnalysis: {
    score: Number,
    feedback: String,
    strengths: [String],
    improvements: [String]
  },
  timeSpent: Number, // in seconds
  order: Number
});

const interviewSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  title: {
    type: String,
    required: [true, 'Please add an interview title'],
    trim: true
  },
  position: {
    type: String,
    required: [true, 'Please add the position']
  },
  company: String,
  interviewType: {
    type: String,
    enum: ['technical', 'behavioral', 'mixed'],
    default: 'mixed'
  },
  difficulty: {
    type: String,
    enum: ['beginner', 'intermediate', 'advanced'],
    default: 'intermediate'
  },
  questions: [questionSchema],
  status: {
    type: String,
    enum: ['scheduled', 'in-progress', 'completed', 'cancelled'],
    default: 'scheduled'
  },
  totalScore: {
    type: Number,
    min: 0,
    max: 100
  },
  duration: Number, // total duration in minutes
  scheduledAt: Date,
  startedAt: Date,
  completedAt: Date,
  aiSummary: {
    overallFeedback: String,
    recommendedTopics: [String],
    nextSteps: [String],
    performanceInsights: {
      communicationScore: Number,
      technicalScore: Number,
      confidenceLevel: Number
    }
  },
  isPublic: {
    type: Boolean,
    default: false
  },
  tags: [String],
  createdAt: {
    type: Date,
    default: Date.now
  }
});

// Index for efficient querying
interviewSchema.index({ user: 1, createdAt: -1 });
interviewSchema.index({ status: 1 });
interviewSchema.index({ interviewType: 1, difficulty: 1 });

module.exports = mongoose.model('Interview', interviewSchema);