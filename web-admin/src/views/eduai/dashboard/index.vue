<template>
  <div class="eduai-dashboard">
    <header class="page-header">
      <div>
        <h1>AI 伴学工作台</h1>
        <p>查看学生成长趋势、伴学策略和家校协同记录。</p>
      </div>
      <n-button type="primary" tag="a" href="/dev-api/api/scores/import-template">
        下载成绩模板
      </n-button>
    </header>

    <section class="metric-grid">
      <n-card size="small" title="当前学生">
        <strong>{{ summary.studentName }}</strong>
        <span>{{ summary.weeklySummary }}</span>
      </n-card>
      <n-card size="small" title="推送提醒">
        <strong>{{ summary.settings.pushEnabled ? '已开启' : '默认关闭' }}</strong>
        <span>{{ summary.settings.pushRule }}</span>
      </n-card>
      <n-card size="small" title="长期记忆">
        <strong>{{ summary.settings.memoryRetentionDays }} 天</strong>
        <span>允许配置，最大 {{ summary.settings.maxMemoryRetentionDays }} 天</span>
      </n-card>
      <n-card size="small" title="奖励记录">
        <strong>{{ rewards.length }} 条</strong>
        <span>仅记录品级和兑现状态，不处理支付</span>
      </n-card>
    </section>

    <section class="content-grid">
      <n-card title="各科成绩趋势">
        <div class="trend-list">
          <div v-for="subject in subjectSeries" :key="subject.name" class="trend-row">
            <span class="subject-name">{{ subject.name }}</span>
            <div class="bars">
              <div
                v-for="point in subject.points"
                :key="point.examName"
                class="bar"
                :style="{ height: `${Math.max(12, point.score)}%` }"
                :title="`${point.examName}: ${point.score}`"
              />
            </div>
            <strong>{{ latestScore(subject.points) }} 分</strong>
          </div>
        </div>
      </n-card>

      <n-card title="今日伴学建议">
        <n-list>
          <n-list-item v-for="item in summary.todaySuggestions" :key="item">
            {{ item }}
          </n-list-item>
        </n-list>
        <div class="weakness">
          <span>当前薄弱项</span>
          <strong>{{ summary.weakSubjects.join('、') }}</strong>
        </div>
      </n-card>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { getCompanionSummary, getRewards, getScoreTrends } from '@/api/eduai'

const summary = reactive({
  studentName: '加载中',
  weeklySummary: '',
  todaySuggestions: [],
  weakSubjects: [],
  settings: {
    pushEnabled: false,
    pushRule: '未设置',
    memoryRetentionDays: 14,
    maxMemoryRetentionDays: 90,
  },
})
const scores = ref({})
const rewards = ref([])

const subjectSeries = computed(() => Object.entries(scores.value).map(([name, points]) => ({ name, points })))

function latestScore(points) {
  return points.length ? points[points.length - 1].score : 0
}

onMounted(async () => {
  Object.assign(summary, await getCompanionSummary())
  scores.value = await getScoreTrends()
  rewards.value = await getRewards()
})
</script>

<style scoped>
.eduai-dashboard {
  min-height: 100%;
  padding: 20px;
  background: #f6f8fb;
}

.page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 18px;
}

h1 {
  margin: 0 0 6px;
  font-size: 24px;
  letter-spacing: 0;
}

p,
.metric-grid span {
  margin: 0;
  color: #64748b;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
  margin-bottom: 14px;
}

.metric-grid strong {
  display: block;
  margin-bottom: 8px;
  font-size: 22px;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.5fr) minmax(300px, 0.8fr);
  gap: 14px;
}

.trend-list {
  display: grid;
  gap: 18px;
}

.trend-row {
  display: grid;
  grid-template-columns: 72px minmax(160px, 1fr) 64px;
  align-items: end;
  gap: 14px;
}

.subject-name {
  align-self: center;
  font-weight: 600;
}

.bars {
  height: 150px;
  display: flex;
  align-items: end;
  gap: 10px;
  padding: 12px;
  background: #f1f5f9;
  border-radius: 6px;
}

.bar {
  width: 38px;
  min-height: 12px;
  border-radius: 5px 5px 2px 2px;
  background: #16a36a;
}

.weakness {
  display: grid;
  gap: 6px;
  margin-top: 16px;
  padding: 14px;
  border-left: 3px solid #e8a317;
  background: #fff8e8;
}

@media (max-width: 1000px) {
  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 620px) {
  .page-header {
    flex-direction: column;
  }

  .metric-grid {
    grid-template-columns: 1fr;
  }
}
</style>
