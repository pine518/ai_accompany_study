# EduAI Campus

EduAI Campus 是面向学校与教育机构的教育类 AI 智能体 MVP 项目。一期目标是在 2 周内打通主线闭环：机构组织、学生/教师/家长角色、AI 伴学、知识库问答、成绩趋势、教师点评、家长奖励记录、基础权限与风控。

## 当前状态

当前仓库已初始化 MVP 工程骨架：

- `backend/`：Spring Boot 后端服务。
- `web-admin/`：Vue 3 + Vite Web 管理端。
- `miniapp/`：微信小程序/uni-app 端工程骨架。
- `docs/`：产品设计、伴学设计、成绩导入模板说明。
- `templates/`：成绩导入 CSV 模板。

## MVP 主线能力

- AI 伴学长期记忆默认保留 2 周，最大自定义上限 90 天。
- AI 伴学推送提醒默认关闭，开启后按规则推送。
- 首期学科支持数学、物理、化学、英语，以及计算机应用、Java 开发、Python 开发等非学科方向。
- 支持自定义学科分类和科目。
- 成绩支持教师手动录入和 Excel/CSV 模板导入。
- 教师可查看各科成绩趋势并点评。
- 家长可查看孩子成绩趋势，记录物质奖励或精神奖励的品级和兑现状态。

## 本地运行

后端：

```powershell
cd backend
mvn spring-boot:run
```

Web 管理端：

```powershell
cd web-admin
npm.cmd install
npm.cmd run dev
```

小程序：

```powershell
cd miniapp
npm.cmd install
npm.cmd run dev:mp-weixin
```

## 说明

当前阶段以 MVP 主线可行为目标，后端先使用内存数据演示业务闭环。后续进入数据库落地时，会将内存数据源替换为 PostgreSQL、Redis 和向量库。
