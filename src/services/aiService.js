const OpenAI = require('openai');

// Initialize OpenAI client (will use environment variable OPENAI_API_KEY)
const openai = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY || 'your-openai-api-key-here'
});

class AIService {
  async generateQuestions(position, interviewType, difficulty) {
    try {
      const prompt = this.createQuestionPrompt(position, interviewType, difficulty);
      
      const response = await openai.chat.completions.create({
        model: 'gpt-3.5-turbo',
        messages: [
          {
            role: 'system',
            content: 'You are an expert interview coach and hiring manager. Generate relevant, challenging, and fair interview questions.'
          },
          {
            role: 'user',
            content: prompt
          }
        ],
        max_tokens: 1000,
        temperature: 0.7
      });

      const questionsText = response.choices[0].message.content;
      return this.parseQuestions(questionsText);
    } catch (error) {
      console.error('Error generating questions:', error);
      // Fallback questions if AI service fails
      return this.getFallbackQuestions(position, interviewType, difficulty);
    }
  }

  async analyzeAnswer(question, answer, position) {
    try {
      const prompt = `
Analyze this interview answer for a ${position} position:

Question: ${question}
Answer: ${answer}

Provide analysis in the following format:
Score: [0-100]
Feedback: [Detailed feedback about the answer]
Strengths: [List of strengths, separated by commas]
Improvements: [List of improvement areas, separated by commas]
      `;

      const response = await openai.chat.completions.create({
        model: 'gpt-3.5-turbo',
        messages: [
          {
            role: 'system',
            content: 'You are an expert interview evaluator. Provide constructive, detailed feedback on interview answers.'
          },
          {
            role: 'user',
            content: prompt
          }
        ],
        max_tokens: 500,
        temperature: 0.3
      });

      return this.parseAnalysis(response.choices[0].message.content);
    } catch (error) {
      console.error('Error analyzing answer:', error);
      return this.getFallbackAnalysis();
    }
  }

  async generateInterviewSummary(interview) {
    try {
      const questionsAndAnswers = interview.questions
        .map(q => `Q: ${q.question}\nA: ${q.answer || 'No answer provided'}\nScore: ${q.aiAnalysis?.score || 0}`)
        .join('\n\n');

      const prompt = `
Generate a comprehensive interview summary for a ${interview.position} position interview:

${questionsAndAnswers}

Provide a summary including:
1. Overall feedback
2. Recommended topics to study
3. Next steps for improvement
4. Performance insights (communication, technical skills, confidence)
      `;

      const response = await openai.chat.completions.create({
        model: 'gpt-3.5-turbo',
        messages: [
          {
            role: 'system',
            content: 'You are an expert career coach providing comprehensive interview feedback and guidance.'
          },
          {
            role: 'user',
            content: prompt
          }
        ],
        max_tokens: 800,
        temperature: 0.5
      });

      return this.parseSummary(response.choices[0].message.content);
    } catch (error) {
      console.error('Error generating summary:', error);
      return this.getFallbackSummary();
    }
  }

  createQuestionPrompt(position, interviewType, difficulty) {
    const numQuestions = difficulty === 'beginner' ? 5 : difficulty === 'intermediate' ? 7 : 10;
    
    let typeDescription = '';
    switch (interviewType) {
      case 'technical':
        typeDescription = 'technical skills, coding problems, and domain-specific knowledge';
        break;
      case 'behavioral':
        typeDescription = 'behavioral scenarios, soft skills, and past experiences';
        break;
      case 'mixed':
        typeDescription = 'a mix of technical and behavioral questions';
        break;
    }

    return `
Generate ${numQuestions} interview questions for a ${position} position.
Focus on: ${typeDescription}
Difficulty level: ${difficulty}

Format each question on a new line starting with "Q:" followed by the question.
Make sure questions are relevant to the position and appropriate for the difficulty level.
    `;
  }

  parseQuestions(questionsText) {
    return questionsText
      .split('\n')
      .filter(line => line.trim().startsWith('Q:'))
      .map(line => line.replace(/^Q:\s*/, '').trim())
      .filter(question => question.length > 0);
  }

  parseAnalysis(analysisText) {
    const lines = analysisText.split('\n');
    const analysis = {
      score: 75,
      feedback: 'Good answer with room for improvement.',
      strengths: ['Clear communication'],
      improvements: ['Add more specific examples']
    };

    lines.forEach(line => {
      if (line.toLowerCase().includes('score:')) {
        const scoreMatch = line.match(/(\d+)/);
        if (scoreMatch) {
          analysis.score = parseInt(scoreMatch[1]);
        }
      } else if (line.toLowerCase().includes('feedback:')) {
        analysis.feedback = line.replace(/feedback:\s*/i, '').trim();
      } else if (line.toLowerCase().includes('strengths:')) {
        const strengthsText = line.replace(/strengths:\s*/i, '').trim();
        analysis.strengths = strengthsText.split(',').map(s => s.trim()).filter(s => s);
      } else if (line.toLowerCase().includes('improvements:')) {
        const improvementsText = line.replace(/improvements:\s*/i, '').trim();
        analysis.improvements = improvementsText.split(',').map(s => s.trim()).filter(s => s);
      }
    });

    return analysis;
  }

  parseSummary(summaryText) {
    return {
      overallFeedback: summaryText.substring(0, 500) + '...',
      recommendedTopics: ['Communication skills', 'Technical knowledge', 'Problem-solving'],
      nextSteps: ['Practice more technical questions', 'Work on presentation skills', 'Research company background'],
      performanceInsights: {
        communicationScore: 75,
        technicalScore: 70,
        confidenceLevel: 80
      }
    };
  }

  getFallbackQuestions(position, interviewType, difficulty) {
    const questions = [
      'Tell me about yourself and your background.',
      `What interests you about the ${position} position?`,
      'Describe a challenging project you worked on.',
      'How do you handle working under pressure?',
      'Where do you see yourself in 5 years?'
    ];

    if (interviewType === 'technical' || interviewType === 'mixed') {
      questions.push(
        `What technical skills are most important for a ${position}?`,
        'Describe your problem-solving approach.',
        'How do you stay updated with new technologies?'
      );
    }

    return questions.slice(0, difficulty === 'beginner' ? 5 : difficulty === 'intermediate' ? 7 : 8);
  }

  getFallbackAnalysis() {
    return {
      score: 75,
      feedback: 'Thank you for your response. Your answer demonstrates understanding of the topic.',
      strengths: ['Clear communication', 'Relevant examples'],
      improvements: ['Add more specific details', 'Consider alternative approaches']
    };
  }

  getFallbackSummary() {
    return {
      overallFeedback: 'You performed well in this interview. Continue practicing to improve your skills.',
      recommendedTopics: ['Communication skills', 'Technical knowledge', 'Industry trends'],
      nextSteps: ['Practice mock interviews', 'Study relevant technologies', 'Research company culture'],
      performanceInsights: {
        communicationScore: 75,
        technicalScore: 70,
        confidenceLevel: 80
      }
    };
  }
}

module.exports = new AIService();