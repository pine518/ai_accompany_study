# 数据库设计说明

当前 MVP 后端使用内存数据源打通主线。后续落库时建议按以下表结构拆分。

## 1. 组织与用户

- tenants：机构租户
- classes：班级
- courses：课程
- users：用户
- student_profiles：学生档案
- teacher_profiles：教师档案
- parent_profiles：家长档案
- guardian_student_relations：家长学生关系

## 2. 学科

- subjects：学科与非学科科目

字段建议：

- id
- tenant_id
- category：主修学科、非学科、自定义分类
- name
- core：是否主修
- enabled

## 3. AI 伴学

- companion_agents：学生伴学智能体
- student_ai_profiles：学生 AI 学习画像
- companion_memories：伴学记忆摘要
- companion_memory_policies：记忆保留策略
- companion_push_settings：推送提醒设置
- companion_push_logs：推送记录
- companion_plans：伴学计划
- companion_assessments：伴学评估
- companion_suggestions：伴学建议

约束：

- 推送默认关闭。
- 长期记忆默认 14 天。
- 长期记忆最大 90 天。

## 4. 成绩与点评

- exam_scores：学生考试与测评成绩
- score_import_templates：成绩导入模板
- score_import_batches：导入批次
- score_comments：教师或家长点评

成绩来源：

- 教师录入
- Excel 导入

## 5. 家长奖励

- reward_rules：奖励规则
- reward_redemptions：奖励兑现记录

字段建议：

- reward_type：material / spiritual
- reward_level：奖励品级
- condition_text：触发条件
- reward_text：奖励内容
- fulfilled：是否兑现

首期只记录，不接入真实支付。
