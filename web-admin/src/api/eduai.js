import { request } from '@/utils'

const fallbackSummary = {
  studentId: 'stu-001',
  studentName: '张三',
  todaySuggestions: ['保持数学错题复盘节奏', '每天完成 20 分钟英语精读'],
  weakSubjects: ['英语阅读', '物理受力分析'],
  weeklySummary: '本周学习状态稳定，数学进步明显。',
  settings: {
    pushEnabled: false,
    pushRule: '每周日晚 19:00 生成周计划提醒',
    memoryRetentionDays: 14,
    maxMemoryRetentionDays: 90,
  },
}

const fallbackScores = {
  数学: [
    { examName: '第一次月考', score: 92 },
    { examName: '期中考试', score: 100 },
  ],
  英语: [
    { examName: '第一次月考', score: 86 },
    { examName: '期中考试', score: 91 },
  ],
}

export async function getCompanionSummary(studentId = 'stu-001') {
  try {
    return await request.get('/api/ai/companion/summary', { params: { studentId } })
  }
  catch {
    return fallbackSummary
  }
}

export async function getScoreTrends(studentId = 'stu-001') {
  try {
    const payload = await request.get('/api/scores/trends', { params: { studentId } })
    return payload.subjects || {}
  }
  catch {
    return fallbackScores
  }
}

export async function getRewards(studentId = 'stu-001') {
  try {
    return await request.get('/api/rewards', { params: { studentId } })
  }
  catch {
    return []
  }
}
