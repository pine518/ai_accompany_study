<template>
  <view class="page">
    <view class="hero">
      <text class="title">AI 伴学</text>
      <text class="subtitle">默认关闭推送，长期记忆默认 2 周，最大 90 天。</text>
    </view>

    <view class="card">
      <view class="card-head">
        <text>今日建议</text>
        <text class="tag">学生端</text>
      </view>
      <view v-for="item in suggestions" :key="item" class="suggestion">{{ item }}</view>
    </view>

    <view class="grid">
      <view class="metric">
        <text>数学</text>
        <strong>100</strong>
        <small>期中考试</small>
      </view>
      <view class="metric">
        <text>英语</text>
        <strong>91</strong>
        <small>稳步提升</small>
      </view>
    </view>

    <view class="card">
      <view class="card-head">
        <text>家长奖励</text>
        <text class="tag">仅记录</text>
      </view>
      <view class="reward">
        <text>物质奖励 A 级</text>
        <strong>数学 100 分，奖励 100 元零花钱</strong>
        <small>状态：待兑现</small>
      </view>
      <view class="reward">
        <text>精神奖励 B 级</text>
        <strong>周末家庭表扬与自主活动时间</strong>
        <small>状态：已记录</small>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
const suggestions = [
  '数学已达成 100 分，建议保持错题复盘节奏。',
  '英语本周建议补强阅读理解，每天 20 分钟。'
]
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 28rpx;
  background: #f5f7fb;
  color: #18212f;
}

.hero {
  padding: 34rpx 0 26rpx;
}

.title {
  display: block;
  font-size: 48rpx;
  font-weight: 800;
}

.subtitle {
  display: block;
  margin-top: 12rpx;
  color: #667085;
  font-size: 26rpx;
  line-height: 1.6;
}

.card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 16rpx;
  padding: 26rpx;
  margin-bottom: 22rpx;
}

.card-head {
  display: flex;
  justify-content: space-between;
  margin-bottom: 18rpx;
  font-weight: 700;
}

.tag {
  color: #2563eb;
  font-size: 24rpx;
}

.suggestion,
.reward {
  padding: 18rpx;
  border-radius: 12rpx;
  background: #f8fafc;
  margin-top: 12rpx;
  line-height: 1.6;
}

.grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18rpx;
  margin-bottom: 22rpx;
}

.metric {
  background: #ecfdf5;
  border: 1px solid #bbf7d0;
  border-radius: 16rpx;
  padding: 24rpx;
}

.metric text,
.reward text {
  display: block;
  color: #667085;
  font-size: 24rpx;
}

.metric strong {
  display: block;
  margin: 10rpx 0;
  font-size: 52rpx;
}

.reward strong {
  display: block;
  margin: 8rpx 0;
  font-size: 28rpx;
}

small {
  color: #667085;
}
</style>
