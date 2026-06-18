# Forge Admin 框架采用评估报告

> 评估对象：[pine518/forge-admin](https://github.com/pine518/forge-admin)  
> 评估基线：`main` 分支，提交 `5e347db89f34d6f8d81b8fb95dbf565c9cfe817e`  
> 评估日期：2026-06-18  
> 评估范围：代码结构、主要功能、技术栈、许可证、可维护性、安全与测试风险，以及与 EduAI Campus MVP 的适配度。

## 1. 结论摘要

**建议有条件采用，定位为 EduAI 的后台基础框架，而不是完整教育 AI 解决方案。**

推荐采用的部分：

- Spring Boot 多模块基础工程。
- 多租户、RBAC、菜单、组织、数据权限、日志与审计底座。
- Vue 3 管理后台壳、动态菜单和通用管理页面。
- Excel、文件、消息、定时任务、配置、AI 模型供应商管理等通用能力。
- AI 流式对话、会话持久化和多模型接入代码，作为 EduAI AI 网关的起点。

不应直接视为已解决的部分：

- 教育知识库 RAG、文档切片、向量检索和引用溯源。
- 学生、教师、家长、班级、成绩、奖励等教育领域模型。
- 个体 AI 伴学智能体、学生画像、评估、规划和长期记忆策略。
- 长期记忆默认 14 天、最大 90 天的到期清理与授权审计。
- 未成年人内容安全、透明干预、家校协同和风险升级流程。
- 微信小程序业务端及可靠的小程序登录绑定闭环。

综合评分：**7.2/10，适合作为后台底座复用；不适合原样上线。**

采用前置条件：

1. 修复已识别的 AI 会话越权风险，并完成租户隔离专项测试。
2. 将 EduAI 业务放入独立业务模块，避免直接修改 Forge 核心代码。
3. 明确数据库路线。两周 MVP 推荐使用 Forge 原生 MySQL 8，向量库独立采用 Qdrant；坚持 PostgreSQL 会明显扩大改造范围。
4. 关闭非 MVP 插件，补齐测试和安全基线后再进入业务开发。

## 2. 仓库概况

| 项目 | 评估结果 |
|---|---|
| 开源许可证 | MIT，可商用、修改和再分发；派生分发需保留版权和许可证声明 |
| 后端 | Java 17、Spring Boot 3.5.13、MyBatis-Plus、Sa-Token、Redisson |
| 管理端 | Vue 3.5、TypeScript、Vite 7、Naive UI、Pinia、UnoCSS、ECharts |
| 数据库 | 默认 MySQL 8，初始化和升级 SQL 大量使用 MySQL 语法 |
| 缓存与任务 | Redis/Redisson、Quartz/任务插件 |
| AI | Spring AI、Spring AI Alibaba，多供应商模型配置、Agent、SSE 对话、数据库会话记忆 |
| 工作流 | Flowable 7，独立服务与客户端模块 |
| 工程规模 | 约 946 个 Java 文件、206 个管理端 Vue/TS 文件 |
| 测试现状 | 后端仅发现 9 个测试文件，前端未发现测试文件；根 POM 默认跳过测试 |
| 维护记录 | 当前分支 257 个提交，主要由两名贡献者完成；未发现 Git tag |
| 最新评估提交 | 2026-05-15 |

### 2.1 来源与维护风险

当前仓库存在来源标识不一致：仓库地址为 `pine518/forge-admin`，README 同时指向 Gitee `ForgeLab/forge-admin` 和 GitHub `yaomindong1996/forge-admin`，许可证版权人为 `yaomd`。这不影响 MIT 许可本身，但会影响后续上游同步和问题归属。

采用后应：

- 在项目 NOTICE 或第三方依赖说明中保留原 MIT 声明。
- 明确唯一上游仓库，并记录本项目采用的基线提交。
- 不依赖未发布 tag，内部建立自己的版本号和升级记录。

## 3. 主要功能总结

### 3.1 系统管理与权限

- 用户、角色、菜单/资源、组织、岗位、租户管理。
- Sa-Token 登录认证、客户端类型和在线用户管理。
- 动态菜单和资源权限。
- 数据权限配置，支持角色、组织和字段/行级数据范围控制。
- 多租户上下文及 MyBatis-Plus 租户拦截。
- 登录日志、操作日志、在线用户和强制下线。

**对 EduAI 的价值：高。** 可承载平台管理员、机构管理员、教师等 Web 后台角色，但学生和家长关系仍需扩展。

### 3.2 通用平台能力

- 系统配置、配置分组、字典、区域和通知公告。
- 文件存储配置和文件元数据管理，支持本地/S3 兼容存储等扩展。
- Redis 缓存管理、系统监控、Actuator。
- 定时任务、任务日志、幂等、分布式锁和 ID 生成。
- 消息模板、消息配置、站内消息和业务消息类型。
- Excel 导入导出配置和通用导出。
- 外部 API 配置、代理转发和调用日志。

**对 EduAI 的价值：中高。** 成绩导入、伴学提醒、文件知识库、审计均可复用部分能力。

### 3.3 开发与低代码能力

- 数据源管理、数据库表导入、代码生成、动态 CRUD。
- AI 表单/CRUD 配置和代码模板管理。
- API 行为配置。
- 数据集、数据维度、数据连接和运行时查询。

**对 EduAI 的价值：中。** 可加快后台 CRUD，但动态能力会放大权限和维护复杂度，MVP 只建议用于内部开发，不应开放给普通租户。

### 3.4 AI 能力

- AI 供应商、模型、Agent 和上下文配置管理。
- 支持 OpenAI 兼容模型及阿里系等供应商扩展。
- SSE 流式对话。
- 会话和消息数据库持久化。
- 默认按最近 20 条消息构造短期上下文。
- 模型调用解析、上下文注入、熔断/降级相关代码。
- AI 生成大屏和 AI 辅助代码生成。

**对 EduAI 的价值：中。** 适合复用模型配置和流式对话基础设施，但目前不是教育知识库或 AI 伴学实现。

### 3.5 数据可视化

- 独立报表服务和 `forge-report-ui`。
- 拖拽式大屏设计、项目发布、模板、数据源和多画布。
- ECharts/VChart 图表及大屏装饰组件。
- AI 自然语言生成大屏配置。

**对 EduAI 的价值：中低（MVP）。** EduAI 成绩趋势和教师工作台用普通 ECharts 页面即可，大屏编辑器体量过大，首期不建议引入。

### 3.6 工作流

- Flowable 流程模型、表单、节点配置、任务、抄送、评论、监控和版本管理。
- 流程服务与业务客户端分离。

**对 EduAI 的价值：低（MVP），中（后期）。** 后期可用于风险复核、内容申诉和审批，首期直接引入会增加部署与数据库成本。

### 3.7 小程序与社交登录

- 框架包含微信、微信公众号、微信开放平台和微信小程序的社交平台枚举与授权请求工厂。
- 用户模型预留 `wechat` 客户端类型。
- `forge-app-server` 当前基本是通用插件聚合启动器，没有现成学生/家长小程序业务 API。

**对 EduAI 的价值：有限。** 可参考微信身份接入，但不能替代现有 uni-app 小程序和家长/学生绑定设计。

## 4. 与 EduAI 需求的适配矩阵

| EduAI 能力 | Forge 现状 | 适配度 | 建议 |
|---|---|---:|---|
| 学校/机构多租户 | 已有租户 starter、租户表和拦截器 | 高 | 复用并做跨租户自动化测试 |
| 角色与菜单权限 | 已有用户、角色、资源、组织和数据权限 | 高 | 复用，增加教育角色模板 |
| 学生/教师/家长账号 | 只有通用用户和组织 | 中 | 新建教育档案和监护关系表 |
| 班级、年级、学科 | 无教育模型 | 低 | 在独立 EduAI 业务模块实现 |
| 成绩录入与 Excel 导入 | 有 Excel 基础设施，无成绩业务 | 中 | 复用解析/导出能力，重写校验和批次逻辑 |
| 成绩趋势图 | 有 ECharts/数据集能力 | 中高 | 管理端直接实现业务图表，不引入大屏编辑器 |
| 教师点评 | 无 | 低 | 新建点评、薄弱点和审计模型 |
| 家长奖励记录 | 无 | 低 | 新建奖励规则、品级和兑现状态模型 |
| AI 多模型接入 | 已有供应商、模型和调用封装 | 高 | 复用并增加成本、配额和调用审计 |
| AI 流式对话 | 已有 SSE 对话 | 高 | 修复会话归属校验后复用 |
| AI 知识库 RAG | 未发现向量库、检索和引用实现 | 低 | 新建知识库模块，独立 Qdrant/对象存储 |
| AI 伴学智能体 | 仅通用 Agent 配置 | 低 | 新建学生 Agent 实例、画像、计划和评估模块 |
| 14-90 天长期记忆 | 当前仅最近 20 条消息上下文 | 低 | 新建记忆策略、摘要、到期删除和授权审计 |
| 推送默认关闭 | 有消息/任务底座，无该业务规则 | 中 | 复用消息和任务，业务层强制默认关闭 |
| 权限透明干预 | 有 RBAC/数据权限，无教育干预策略 | 中 | 新建策略、原因展示和干预审计模块 |
| 未成年人风控 | 无完整实现 | 低 | 新建输入/输出审核、分级、告警和人工复核 |
| 微信小程序 | 无可直接使用的业务端 | 低 | 保留当前 uni-app 工程，调用新后端 API |

## 5. 代码与工程质量评估

### 5.1 正面因素

- 模块拆分清晰，starter、plugin 和 server 边界相对明确。
- Java 17 与 Spring Boot 3 符合 EduAI 选型方向。
- 通用后台能力覆盖广，可显著减少认证、租户、菜单、日志、Excel 等重复建设。
- 源码可编译，核心模块并非只有页面截图或空壳。
- 多租户 ID 来源优先取认证会话，而非直接信任请求头。
- AI 会话、模型、供应商等数据结构可作为 EduAI 模型网关基础。

### 5.2 已确认错误与风险

#### P0：AI 会话存在潜在越权访问

`GET /ai/session/{sessionId}/messages` 和 `DELETE /ai/session/{sessionId}` 只使用 `sessionId` 查询/更新，Controller 和 Service 未显式追加当前 `userId` 条件。若攻击者获得其他会话 ID，可能读取或删除他人会话。

采用前必须：

- 所有学生会话查询同时校验 `tenant_id + user_id + session_id`。
- 教师/家长查看摘要走独立授权接口，不直接读取学生完整会话。
- 增加同租户越权、跨租户越权和角色越权测试。

#### P0：教育 AI 风控缺口

通用 AI 对话接口未形成适用于未成年人的输入审核、输出审核、风险分级和人工复核闭环。错误处理还会把部分异常消息直接返回客户端，不应在生产环境暴露内部信息。

#### P1：测试默认被跳过

根 POM 配置了 `skipTests=true`，因此常规 Maven 构建不能证明测试通过。后端测试数量与代码规模不匹配，管理端未发现测试文件。

采用后必须：

- CI 显式执行测试并移除默认跳过配置。
- 优先补租户、认证、数据权限、AI 会话和教育业务测试。
- 管理端增加 Vitest，关键流程增加 Playwright E2E。

#### P1：数据库选型冲突

EduAI 原方案是 PostgreSQL，Forge 默认是 MySQL，SQL 文件含反引号、MySQL 类型和 MySQL 分页写法。直接改造成 PostgreSQL 会涉及初始化脚本、动态数据源、代码生成器、报表和部分手写 SQL 的全面验证。

#### P1：框架体量超过 MVP 需要

完整启用报表大屏、Flowable、动态 CRUD、外部代理和全部任务能力，会增加攻击面、启动时间、数据库表数量和维护负担。应采用白名单方式选择模块。

#### P1：租户隔离不能只依赖拦截器

框架提供租户拦截，但 EduAI 还包含对象存储、向量库、缓存、任务和消息。上述资源也必须带租户命名空间，不能只验证 MySQL 查询。

#### P2：依赖和编译告警

编译存在过时 API、未检查操作告警；依赖中仍包含 Fastjson 1.x。采用前应运行 OWASP Dependency-Check 或同类 SCA，并按结果升级或替换高风险依赖。

#### P2：版本治理较弱

CHANGELOG 只有 1.0.0 初始版本，仓库未发现 tag，且上游地址不一致。后续升级必须由 EduAI 自己控制，不宜自动追随上游主分支。

## 6. 验证记录

本次执行：

```text
mvn -DskipTests compile -pl forge-admin-server -am
```

结果：

- 33 个 Reactor 模块全部编译成功。
- 总耗时约 4 分 35 秒。
- AI、租户、认证、数据权限、Excel、消息、任务和系统插件均进入编译链。
- 出现过时 API 和未检查操作警告。
- 本次未启动 MySQL/Redis 进行运行时验证，也未执行测试，因为上游默认测试策略需要先整改。
- 本次未验证管理端和报表端生产构建；是否采用不应依赖大屏模块。

## 7. 推荐目标架构

```text
eduai/
├── backend/
│   ├── forge-framework/          # 受控保留的 Forge starter/plugin
│   ├── eduai-business/           # 教育领域模型与服务
│   ├── eduai-ai/                 # RAG、伴学、记忆、风控、模型网关扩展
│   └── eduai-server/             # 单体 MVP 启动模块
├── web-admin/                    # 基于 forge-admin-ui 改造
├── miniapp/                      # 保留现有 uni-app，学生/家长双角色
├── docs/
└── deploy/
```

### 7.1 MVP 建议启用模块

- `forge-starter-core`
- `forge-starter-web`
- `forge-starter-auth`
- `forge-starter-tenant`
- `forge-starter-datascope`
- `forge-starter-log`
- `forge-starter-cache`
- `forge-starter-file`
- `forge-starter-excel`
- `forge-plugin-system`
- `forge-plugin-message`（裁剪后）
- `forge-plugin-ai`（修复安全问题并扩展后）

### 7.2 MVP 建议暂缓模块

- `forge-report-server` / `forge-report-ui`
- `forge-flow`
- `forge-plugin-generator` 的运行时动态 CRUD
- `forge-plugin-external`
- `forge-plugin-data`
- 非必要的社交平台和存储供应商实现

暂缓不代表删除源码，而是避免打入首期运行包和暴露对应接口。

## 8. 数据库路线建议

### 方案 A：MySQL 8 + Qdrant（推荐）

- Forge 系统表和 EduAI 业务表使用 MySQL 8。
- 知识库原文放对象存储。
- 文档元数据、权限和引用关系放 MySQL。
- 向量切片放 Qdrant，并以 `tenantId/knowledgeBaseId/subjectId` 过滤。

优点：改造最少，适合两周 MVP。  
缺点：业务数据和向量数据分为两个存储系统。

### 方案 B：PostgreSQL + pgvector（不推荐用于两周 MVP）

- 保持原 EduAI 方案。
- 需要迁移 Forge 全部系统 SQL，并验证 MyBatis-Plus、动态查询、代码生成和报表相关 SQL。

优点：关系数据与向量能力统一。  
缺点：框架适配成本高，容易挤占教育主线开发时间。

## 9. 重构策略

### 9.1 推荐方式

采用“受控引入 + 独立业务模块”，不要把 EduAI 业务直接写进 `forge-plugin-system` 或原 AI 插件内部。

1. 记录 Forge 基线提交和 MIT 许可。
2. 引入后先完成最小启动、数据库初始化、登录和租户隔离。
3. 新建 `eduai-business` 与 `eduai-ai` 模块。
4. Web 管理端保留 Forge 布局、登录、动态菜单和系统管理，替换品牌并增加教育页面。
5. 小程序保留现有 uni-app 工程，仅调整认证和 API 契约。
6. 通过适配层迁移当前 MVP API，避免前端同时大规模返工。

### 9.2 不推荐方式

- 不建议把 Forge 整仓直接覆盖当前项目后立即开发。
- 不建议首期启用所有插件。
- 不建议直接继承上游默认账号、密码和演示数据。
- 不建议让教师/家长直接访问学生 AI 原始对话记录。
- 不建议用通用 Agent 表直接承担每个学生的伴学实例和长期记忆。

## 10. 工作量与两周 MVP 影响

按单名熟悉 Java/Vue 的全栈开发者估算：

| 工作项 | 估算 |
|---|---:|
| 框架裁剪、配置、数据库和启动 | 1.5-2.5 人日 |
| 品牌、菜单、角色和管理端壳适配 | 1-1.5 人日 |
| 安全修复与租户隔离测试 | 1.5-2 人日 |
| 当前接口适配和小程序登录联调 | 1.5-2 人日 |
| 教育业务模型迁移 | 2-3 人日 |
| 合计，仅框架重构 | 7.5-11 人日 |

这意味着：**若仍要求两周内完成原计划全部 MVP，单人实施风险较高。**

建议二选一：

- 两周目标调整为“框架重构 + 账号租户 + 成绩主线 + AI 对话最小闭环”。
- 保持原 MVP 范围，增加后端/前端开发资源或延长至 3-4 周。

## 11. 验收门槛

完成框架重构后，进入教育业务开发前必须满足：

- 后端和管理端可重复构建。
- MySQL、Redis、对象存储和向量库配置无明文密钥。
- 平台、机构、教师、学生、家长角色权限矩阵通过测试。
- 租户隔离覆盖数据库、缓存、文件、向量、任务和消息。
- AI 会话不能跨用户或跨租户读取、修改和删除。
- 默认账号和演示数据已移除，首次管理员密码由部署流程生成。
- Maven 测试不再默认跳过，CI 能阻断失败测试。
- 未启用模块不打包、不注册菜单、不暴露接口。
- API、数据库、部署和上游基线说明同步更新。

## 12. 最终建议

**可以基于 Forge Admin 开发，但应将它作为基础设施代码库，而不是产品主体。**

当前 EduAI 工程仍处于轻量 MVP 骨架阶段，现在切换底座的沉没成本较低。Forge 能节省租户、RBAC、数据权限、日志、Excel、消息和管理端基础能力的开发时间；同时，其通用 AI 能力不足以替代 EduAI 的知识库、伴学、记忆和风控设计。

建议批准后先执行一个独立的“框架重构阶段”，以安全修复、模块裁剪和最小运行闭环为验收目标。重构通过后再按更新后的 `PLAN.md` 开发教育业务，避免框架迁移和业务开发同时失控。

## 13. 参考源码

- [Forge Admin README](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/README.md)
- [MIT License](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/LICENSE)
- [后端根 POM](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/forge/pom.xml)
- [管理端 package.json](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/forge-admin-ui/package.json)
- [AI 对话 Controller](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/forge/forge-framework/forge-plugin-parent/forge-plugin-ai/src/main/java/com/mdframe/forge/plugin/ai/chat/controller/AiChatController.java)
- [数据库对话记忆](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/forge/forge-framework/forge-plugin-parent/forge-plugin-ai/src/main/java/com/mdframe/forge/plugin/ai/chat/memory/DbChatMemory.java)
- [租户拦截器](https://github.com/pine518/forge-admin/blob/5e347db89f34d6f8d81b8fb95dbf565c9cfe817e/forge/forge-framework/forge-starter-parent/forge-starter-tenant/src/main/java/com/mdframe/forge/starter/tenant/interceptor/TenantInterceptor.java)

