<template>
  <main class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-mark">E</span>
        <div>
          <strong>EduAI Campus</strong>
          <small>AI 伴学 MVP</small>
        </div>
      </div>
      <nav>
        <button class="active">工作台</button>
        <button>学生画像</button>
        <button>成绩趋势</button>
        <button>伴学设置</button>
        <button>奖励记录</button>
      </nav>
    </aside>

    <section class="content">
      <header class="topbar">
        <div>
          <h1>学校机构 AI 伴学工作台</h1>
          <p>2 周 MVP 主线：AI 伴学、成绩趋势、教师点评、家长奖励记录。</p>
        </div>
        <a class="template-link" href="/api/scores/import-template">下载成绩导入模板</a>
      </header>

      <section class="summary-grid">
        <article class="panel highlight">
          <span>伴学学生</span>
          <strong>{{ summary?.studentName || '加载中' }}</strong>
          <p>{{ summary?.weeklySummary }}</p>
        </article>
        <article class="panel">
          <span>推送提醒</span>
          <strong>{{ summary?.settings.pushEnabled ? '已开启' : '默认关闭' }}</strong>
          <p>{{ summary?.settings.pushRule }}</p>
        </article>
        <article class="panel">
          <span>长期记忆</span>
          <strong>{{ summary?.settings.memoryRetentionDays }} 天</strong>
          <p>默认 14 天，最大 {{ summary?.settings.maxMemoryRetentionDays }} 天。</p>
        </article>
        <article class="panel">
          <span>奖励记录</span>
          <strong>{{ rewards.length }}</strong>
          <p>首期只记录物质/精神奖励和兑现状态。</p>
        </article>
      </section>

      <section class="workspace">
        <article class="panel chart-panel">
          <div class="section-heading">
            <div>
              <h2>各科成绩变化趋势</h2>
              <p>教师可据此点评，家长可查看孩子成长变化。</p>
            </div>
            <span class="status">教师录入 + Excel 导入</span>
          </div>
          <div class="chart">
            <div v-for="subject in subjectSeries" :key="subject.name" class="chart-row">
              <strong>{{ subject.name }}</strong>
              <div class="bars">
                <span
                  v-for="point in subject.points"
                  :key="point.examName"
                  :style="{ height: `${Math.max(12, point.score)}%` }"
                  :title="`${point.examName}: ${point.score}`"
                />
              </div>
              <em>{{ subject.points[subject.points.length - 1]?.score || 0 }} 分</em>
            </div>
          </div>
        </article>

        <article class="panel">
          <div class="section-heading">
            <div>
              <h2>AI 伴学建议</h2>
              <p>建议必须包含依据、动作和完成标准。</p>
            </div>
          </div>
          <ul class="suggestions">
            <li v-for="item in summary?.todaySuggestions" :key="item">{{ item }}</li>
          </ul>
          <div class="weak-box">
            <span>薄弱点</span>
            <strong>{{ summary?.weakSubjects.join('、') }}</strong>
          </div>
        </article>
      </section>

      <section class="workspace lower">
        <article class="panel">
          <h2>教师点评</h2>
          <textarea v-model="teacherComment" placeholder="针对薄弱部分输入指导建议"></textarea>
          <button class="primary" @click="teacherComment = ''">保存点评</button>
        </article>
        <article class="panel">
          <h2>家长奖励记录</h2>
          <div class="reward-card">
            <span>物质奖励</span>
            <strong>数学达到 100 分，奖励 100 元零花钱</strong>
            <small>品级：A 级，状态：待兑现</small>
          </div>
          <div class="reward-card">
            <span>精神奖励</span>
            <strong>周末家庭表扬与自主活动时间</strong>
            <small>品级：B 级，状态：已记录</small>
          </div>
        </article>
      </section>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { getCompanionSummary, getRewards, getScoreTrends, type CompanionSummary, type RewardRule, type ScoreRecord } from './api'

const summary = ref<CompanionSummary>()
const scoreTrends = ref<Record<string, ScoreRecord[]>>({})
const rewards = ref<RewardRule[]>([])
const teacherComment = ref('数学提升明显，建议保持基础题满分率；英语阅读需要每天 20 分钟精读。')

const subjectSeries = computed(() =>
  Object.entries(scoreTrends.value).map(([name, points]) => ({ name, points }))
)

onMounted(async () => {
  summary.value = await getCompanionSummary()
  scoreTrends.value = await getScoreTrends()
  rewards.value = await getRewards()
})
</script>
