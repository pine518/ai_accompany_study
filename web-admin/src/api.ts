export interface CompanionSettings {
  studentId: string
  pushEnabled: boolean
  pushRule: string
  memoryRetentionDays: number
  maxMemoryRetentionDays: number
}

export interface CompanionSummary {
  studentId: string
  studentName: string
  todaySuggestions: string[]
  weakSubjects: string[]
  weeklySummary: string
  settings: CompanionSettings
}

export interface ScoreRecord {
  id: string
  studentId: string
  subject: string
  examName: string
  examDate: string
  score: number
  fullScore: number
  examType: string
  remark: string
}

export interface RewardRule {
  id: string
  studentId: string
  subject: string
  rewardType: string
  rewardLevel: string
  conditionText: string
  rewardText: string
  fulfilled: boolean
  createdAt: string
}

const fallbackSummary: CompanionSummary = {
  studentId: 'stu-001',
  studentName: '张三',
  todaySuggestions: ['数学已达成 100 分，建议保持错题复盘节奏。', '英语本周建议补强阅读理解。'],
  weakSubjects: ['英语阅读', '物理受力分析'],
  weeklySummary: '本周学习状态稳定，数学进步明显；建议下周保持数学复盘，同时增加英语阅读训练。',
  settings: {
    studentId: 'stu-001',
    pushEnabled: false,
    pushRule: '每周日晚 19:00 生成周计划提醒',
    memoryRetentionDays: 14,
    maxMemoryRetentionDays: 90
  }
}

const fallbackScores: Record<string, ScoreRecord[]> = {
  数学: [
    { id: '1', studentId: 'stu-001', subject: '数学', examName: '第一次月考', examDate: '2026-09-30', score: 92, fullScore: 100, examType: '月考', remark: '' },
    { id: '2', studentId: 'stu-001', subject: '数学', examName: '期中考试', examDate: '2026-11-15', score: 100, fullScore: 100, examType: '期中', remark: '' }
  ],
  英语: [
    { id: '3', studentId: 'stu-001', subject: '英语', examName: '第一次月考', examDate: '2026-09-30', score: 86, fullScore: 100, examType: '月考', remark: '' },
    { id: '4', studentId: 'stu-001', subject: '英语', examName: '期中考试', examDate: '2026-11-15', score: 91, fullScore: 100, examType: '期中', remark: '' }
  ]
}

export async function getCompanionSummary(studentId = 'stu-001'): Promise<CompanionSummary> {
  try {
    const response = await fetch(`/api/ai/companion/summary?studentId=${studentId}`)
    if (!response.ok) throw new Error('summary request failed')
    return response.json()
  } catch {
    return fallbackSummary
  }
}

export async function getScoreTrends(studentId = 'stu-001'): Promise<Record<string, ScoreRecord[]>> {
  try {
    const response = await fetch(`/api/scores/trends?studentId=${studentId}`)
    if (!response.ok) throw new Error('score request failed')
    const payload = await response.json()
    return payload.subjects
  } catch {
    return fallbackScores
  }
}

export async function getRewards(studentId = 'stu-001'): Promise<RewardRule[]> {
  try {
    const response = await fetch(`/api/rewards?studentId=${studentId}`)
    if (!response.ok) throw new Error('reward request failed')
    return response.json()
  } catch {
    return []
  }
}
