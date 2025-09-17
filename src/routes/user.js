const express = require('express');
const User = require('../models/User');
const Interview = require('../models/Interview');
const auth = require('../middleware/auth');

const router = express.Router();

// @route   GET api/user/stats
// @desc    Get user statistics
// @access  Private
router.get('/stats', auth, async (req, res, next) => {
  try {
    const userId = req.user.id;

    // Get interview statistics
    const totalInterviews = await Interview.countDocuments({ user: userId });
    const completedInterviews = await Interview.countDocuments({ 
      user: userId, 
      status: 'completed' 
    });
    const inProgressInterviews = await Interview.countDocuments({ 
      user: userId, 
      status: 'in-progress' 
    });

    // Get average score
    const completedInterviewsData = await Interview.find({ 
      user: userId, 
      status: 'completed',
      totalScore: { $exists: true }
    }).select('totalScore');

    const averageScore = completedInterviewsData.length > 0 
      ? completedInterviewsData.reduce((sum, interview) => sum + interview.totalScore, 0) / completedInterviewsData.length
      : 0;

    // Get recent interviews
    const recentInterviews = await Interview.find({ user: userId })
      .sort({ createdAt: -1 })
      .limit(5)
      .select('title position status totalScore createdAt');

    // Interview type breakdown
    const interviewTypeStats = await Interview.aggregate([
      { $match: { user: userId } },
      { $group: { _id: '$interviewType', count: { $sum: 1 } } }
    ]);

    res.json({
      success: true,
      data: {
        totalInterviews,
        completedInterviews,
        inProgressInterviews,
        averageScore: Math.round(averageScore * 100) / 100,
        recentInterviews,
        interviewTypeStats
      }
    });
  } catch (error) {
    next(error);
  }
});

// @route   GET api/user/progress
// @desc    Get user progress over time
// @access  Private
router.get('/progress', auth, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const days = parseInt(req.query.days) || 30;
    const fromDate = new Date();
    fromDate.setDate(fromDate.getDate() - days);

    const progressData = await Interview.aggregate([
      {
        $match: {
          user: userId,
          status: 'completed',
          completedAt: { $gte: fromDate }
        }
      },
      {
        $group: {
          _id: {
            $dateToString: { format: '%Y-%m-%d', date: '$completedAt' }
          },
          averageScore: { $avg: '$totalScore' },
          count: { $sum: 1 }
        }
      },
      { $sort: { _id: 1 } }
    ]);

    res.json({
      success: true,
      data: progressData
    });
  } catch (error) {
    next(error);
  }
});

// @route   GET api/user/leaderboard
// @desc    Get public leaderboard (anonymized)
// @access  Public
router.get('/leaderboard', async (req, res, next) => {
  try {
    const limit = parseInt(req.query.limit) || 10;

    const leaderboard = await User.aggregate([
      {
        $lookup: {
          from: 'interviews',
          localField: '_id',
          foreignField: 'user',
          as: 'interviews'
        }
      },
      {
        $addFields: {
          completedInterviews: {
            $filter: {
              input: '$interviews',
              cond: { $eq: ['$$this.status', 'completed'] }
            }
          }
        }
      },
      {
        $addFields: {
          averageScore: {
            $cond: {
              if: { $gt: [{ $size: '$completedInterviews' }, 0] },
              then: { $avg: '$completedInterviews.totalScore' },
              else: 0
            }
          },
          totalCompleted: { $size: '$completedInterviews' }
        }
      },
      {
        $match: {
          totalCompleted: { $gte: 3 } // Only users with at least 3 completed interviews
        }
      },
      {
        $project: {
          name: { $concat: [{ $substr: ['$name', 0, 1] }, '***'] }, // Anonymize name
          averageScore: { $round: ['$averageScore', 2] },
          totalCompleted: 1
        }
      },
      { $sort: { averageScore: -1 } },
      { $limit: limit }
    ]);

    res.json({
      success: true,
      data: leaderboard
    });
  } catch (error) {
    next(error);
  }
});

module.exports = router;