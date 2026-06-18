# EduAI Campus

EduAI Campus 是面向学校与教育机构的教育类 AI 智能体 MVP 项目。一期目标是在 2 周内打通主线闭环：机构组织、学生/教师/家长角色、AI 伴学、知识库问答、成绩趋势、教师点评、家长奖励记录、基础权限与风控。

## 当前状态

当前仓库已完成 Forge Admin 基座重构：

- `backend/forge-framework/`：认证、租户、权限、日志、Excel、文件、消息和 AI 通用基座。
- `backend/eduai-business/`：独立教育领域模块，不依赖 Forge 插件实现。
- `backend/forge-admin-server/`：应用装配和启动模块。
- `web-admin/`：基于 Forge Vue 3 + Naive UI 的学校机构管理端。
- `miniapp/`：微信小程序/uni-app 端工程骨架。
- `docs/`：产品、架构、API、数据库、伴学和风控文档。
- `templates/`：成绩导入 CSV 模板。

详细模块边界见 [基座与业务模块架构](docs/FOUNDATION_ARCHITECTURE.md)，框架选型依据见 [Forge Admin 评估报告](docs/FORGE_ADMIN_EVALUATION.md)。

## MVP 主线能力

- AI 伴学长期记忆默认保留 2 周，最大自定义上限 90 天。
- AI 伴学推送提醒默认关闭，开启后按规则推送。
- 首期学科支持数学、物理、化学、英语，以及计算机应用、Java 开发、Python 开发等非学科方向。
- 支持自定义学科分类和科目。
- 成绩支持教师手动录入和 Excel/CSV 模板导入。
- 教师可查看各科成绩趋势并点评。
- 家长可查看孩子成绩趋势，记录物质奖励或精神奖励的品级和兑现状态。

## 本地运行

推荐使用 Docker 启动 MySQL、Redis 和后端：

```powershell
docker compose up --build
```

后端地址：`http://localhost:8580`。

本地后端构建与测试：

```powershell
cd backend
mvn test -pl eduai-business -am
mvn -DskipTests=true package -pl forge-admin-server -am
```

Web 管理端：

```powershell
cd web-admin
npm.cmd install
npm.cmd run dev
```

管理端默认端口：`http://localhost:3000`，开发代理指向后端 `8580` 端口。

小程序：

```powershell
cd miniapp
npm.cmd install
npm.cmd run dev:mp-weixin
```

## 说明

当前阶段以 MVP 主线可行为目标。教育业务 API 暂时保留内存仓库以兼容演示流程，MySQL 表结构已经建立；后续会在不改变 API 契约的前提下迁移到 MySQL Repository。知识库向量检索计划独立接入 Qdrant。

Forge 基座来源和 MIT 许可基线见 [backend/UPSTREAM.md](backend/UPSTREAM.md)。生产部署必须替换 Docker Compose 中的开发密码和 `FORGE_CRYPTO_SECRET_KEY`。
