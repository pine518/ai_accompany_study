# Forge Upstream Baseline

EduAI Campus 基座代码来源：

- 仓库：`https://github.com/pine518/forge-admin.git`
- 分支：`main`
- 基线提交：`5e347db89f34d6f8d81b8fb95dbf565c9cfe817e`
- 许可证：MIT，见 `backend/LICENSE`
- 引入日期：2026-06-18

## 本地改造原则

- `forge-framework` 和 `forge-plugin-*` 只维护通用基座能力与必要安全补丁。
- 教育领域代码全部放在 `eduai-business` 或后续 `eduai-*` 模块。
- 不在教育业务中依赖 `com.mdframe.forge.plugin..` 实现包。
- 上游升级采用人工评审和选择性合并，不直接覆盖本地安全修复。

## 当前裁剪

运行时保留：系统管理、认证、多租户、数据权限、日志、Excel、文件、消息、社交登录和 AI 通用插件。

首期停用：报表大屏、Flowable、代码生成器、动态数据集、外部代理和相关演示业务。
