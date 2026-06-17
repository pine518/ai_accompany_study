# API 设计说明

## 1. 基础

后端基础地址：

- 本地：`http://localhost:8080`

## 2. MVP 接口

### 健康检查

- `GET /api/health`

### 学科管理

- `GET /api/subjects`
- `POST /api/subjects`

首期内置：

- 主修学科：数学、物理、化学、英语
- 非学科方向：计算机应用、Java 开发、Python 开发
- 支持自定义学科分类和科目

### AI 伴学

- `GET /api/ai/companion/summary?studentId=stu-001`
- `GET /api/ai/companion/push-settings?studentId=stu-001`
- `PUT /api/ai/companion/push-settings`

规则：

- 推送提醒默认关闭。
- 长期记忆默认保留 14 天。
- 长期记忆最大上限 90 天。

### 成绩

- `GET /api/scores/trends?studentId=stu-001`
- `POST /api/scores/manual`
- `GET /api/scores/import-template`

成绩来源：

- 教师手动录入。
- Excel/CSV 模板导入。

### 点评

- `GET /api/score-comments?studentId=stu-001`
- `POST /api/score-comments`

### 奖励

- `GET /api/rewards?studentId=stu-001`
- `POST /api/rewards`

奖励类型：

- 物质奖励
- 精神奖励

首期只记录奖励规则、品级、达成情况和兑现状态，不接入真实支付。
