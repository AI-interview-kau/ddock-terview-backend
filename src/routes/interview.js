const express = require('express');
const { body, validationResult } = require('express-validator');
const Interview = require('../models/Interview');
const auth = require('../middleware/auth');
const aiService = require('../services/aiService');

const router = express.Router();

// @route   POST api/interview
// @desc    Create a new interview
// @access  Private
router.post('/', auth, [
  body('title').notEmpty().withMessage('Interview title is required'),
  body('position').notEmpty().withMessage('Position is required'),
  body('interviewType').isIn(['technical', 'behavioral', 'mixed']).withMessage('Invalid interview type'),
  body('difficulty').isIn(['beginner', 'intermediate', 'advanced']).withMessage('Invalid difficulty level')
], async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const interviewData = {
      ...req.body,
      user: req.user.id
    };

    const interview = await Interview.create(interviewData);

    res.status(201).json({
      success: true,
      data: interview
    });
  } catch (error) {
    next(error);
  }
});

// @route   GET api/interview
// @desc    Get all interviews for the authenticated user
// @access  Private
router.get('/', auth, async (req, res, next) => {
  try {
    const page = parseInt(req.query.page, 10) || 1;
    const limit = parseInt(req.query.limit, 10) || 10;
    const status = req.query.status;
    const interviewType = req.query.type;

    const query = { user: req.user.id };
    
    if (status) query.status = status;
    if (interviewType) query.interviewType = interviewType;

    const startIndex = (page - 1) * limit;

    const interviews = await Interview.find(query)
      .sort({ createdAt: -1 })
      .limit(limit)
      .skip(startIndex);

    const total = await Interview.countDocuments(query);

    res.json({
      success: true,
      count: interviews.length,
      total,
      page,
      pages: Math.ceil(total / limit),
      data: interviews
    });
  } catch (error) {
    next(error);
  }
});

// @route   GET api/interview/:id
// @desc    Get single interview
// @access  Private
router.get('/:id', auth, async (req, res, next) => {
  try {
    const interview = await Interview.findById(req.params.id);

    if (!interview) {
      return res.status(404).json({
        success: false,
        error: 'Interview not found'
      });
    }

    // Check if user owns the interview or it's public
    if (interview.user.toString() !== req.user.id && !interview.isPublic) {
      return res.status(403).json({
        success: false,
        error: 'Not authorized to access this interview'
      });
    }

    res.json({
      success: true,
      data: interview
    });
  } catch (error) {
    next(error);
  }
});

// @route   PUT api/interview/:id
// @desc    Update interview
// @access  Private
router.put('/:id', auth, async (req, res, next) => {
  try {
    let interview = await Interview.findById(req.params.id);

    if (!interview) {
      return res.status(404).json({
        success: false,
        error: 'Interview not found'
      });
    }

    // Check ownership
    if (interview.user.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        error: 'Not authorized to update this interview'
      });
    }

    interview = await Interview.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true, runValidators: true }
    );

    res.json({
      success: true,
      data: interview
    });
  } catch (error) {
    next(error);
  }
});

// @route   POST api/interview/:id/start
// @desc    Start an interview
// @access  Private
router.post('/:id/start', auth, async (req, res, next) => {
  try {
    const interview = await Interview.findById(req.params.id);

    if (!interview) {
      return res.status(404).json({
        success: false,
        error: 'Interview not found'
      });
    }

    if (interview.user.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        error: 'Not authorized'
      });
    }

    if (interview.status !== 'scheduled') {
      return res.status(400).json({
        success: false,
        error: 'Interview cannot be started'
      });
    }

    // Generate questions using AI service
    const questions = await aiService.generateQuestions(
      interview.position,
      interview.interviewType,
      interview.difficulty
    );

    interview.questions = questions.map((q, index) => ({
      question: q,
      order: index + 1
    }));
    interview.status = 'in-progress';
    interview.startedAt = new Date();

    await interview.save();

    res.json({
      success: true,
      data: interview
    });
  } catch (error) {
    next(error);
  }
});

// @route   POST api/interview/:id/answer
// @desc    Submit answer for a question
// @access  Private
router.post('/:id/answer', auth, [
  body('questionIndex').isNumeric().withMessage('Question index is required'),
  body('answer').notEmpty().withMessage('Answer is required')
], async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        errors: errors.array()
      });
    }

    const { questionIndex, answer, timeSpent } = req.body;
    const interview = await Interview.findById(req.params.id);

    if (!interview) {
      return res.status(404).json({
        success: false,
        error: 'Interview not found'
      });
    }

    if (interview.user.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        error: 'Not authorized'
      });
    }

    if (interview.status !== 'in-progress') {
      return res.status(400).json({
        success: false,
        error: 'Interview is not in progress'
      });
    }

    // Update the answer
    interview.questions[questionIndex].answer = answer;
    interview.questions[questionIndex].timeSpent = timeSpent;

    // Get AI analysis for the answer
    const analysis = await aiService.analyzeAnswer(
      interview.questions[questionIndex].question,
      answer,
      interview.position
    );

    interview.questions[questionIndex].aiAnalysis = analysis;

    await interview.save();

    res.json({
      success: true,
      data: {
        analysis,
        nextQuestion: interview.questions[questionIndex + 1]?.question || null
      }
    });
  } catch (error) {
    next(error);
  }
});

// @route   POST api/interview/:id/complete
// @desc    Complete an interview
// @access  Private
router.post('/:id/complete', auth, async (req, res, next) => {
  try {
    const interview = await Interview.findById(req.params.id);

    if (!interview) {
      return res.status(404).json({
        success: false,
        error: 'Interview not found'
      });
    }

    if (interview.user.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        error: 'Not authorized'
      });
    }

    if (interview.status !== 'in-progress') {
      return res.status(400).json({
        success: false,
        error: 'Interview is not in progress'
      });
    }

    // Calculate total score and generate AI summary
    const totalScore = interview.questions.reduce((sum, q) => {
      return sum + (q.aiAnalysis?.score || 0);
    }, 0) / interview.questions.length;

    const aiSummary = await aiService.generateInterviewSummary(interview);

    interview.totalScore = totalScore;
    interview.aiSummary = aiSummary;
    interview.status = 'completed';
    interview.completedAt = new Date();
    interview.duration = Math.round((interview.completedAt - interview.startedAt) / 60000); // in minutes

    await interview.save();

    res.json({
      success: true,
      data: interview
    });
  } catch (error) {
    next(error);
  }
});

// @route   DELETE api/interview/:id
// @desc    Delete interview
// @access  Private
router.delete('/:id', auth, async (req, res, next) => {
  try {
    const interview = await Interview.findById(req.params.id);

    if (!interview) {
      return res.status(404).json({
        success: false,
        error: 'Interview not found'
      });
    }

    if (interview.user.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        error: 'Not authorized'
      });
    }

    await interview.deleteOne();

    res.json({
      success: true,
      data: {}
    });
  } catch (error) {
    next(error);
  }
});

module.exports = router;