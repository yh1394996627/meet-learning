---
name: "java-dev-guide"
description: "Java development conventions, code patterns, and project-specific rules. Invoke when writing Java code, creating Spring Boot services, or following project standards."
---

# Java Dev Guide

> 本 Skill 为跨项目共享的 Java 开发指南，后续按需逐步补充。

## 实体类规范

### 必须有 recordId 业务主键

- 所有实体类必须包含 `private String recordId;` 字段，作为**业务主键**
- `recordId` 的值为 **UUID**（`UUID.randomUUID().toString()`）
- 外部表关联一律使用对方的 **`recordId`**（String 类型），禁止使用数据库自增 `id`（Long 类型）做外部关联
- 关联字段命名格式：`{对方实体名去掉开头小写}RecordId`，例如：
  - 关联 `UserFinanceRecord` → `financeRecordRecordId`（String）
  - 关联 `StudentClass` → `classRecordId`（String）
  - 关联 `TeacherSalary` → `teacherSalaryRecordId`（String）

### 实体类标准字段

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 数据库自增主键 |
| recordId | String | 业务主键（UUID） |
| deleted | Boolean | 逻辑删除（0 有效，1 已删除） |
| creator | String | 创建人 recordId |
| createName | String | 创建人名称 |
| createTime | Date | 创建时间 |
| updator | String | 更新人 recordId |
| updateName | String | 更新人名称 |
| updateTime | Date | 更新时间 |

## 待补充内容

- [ ] Spring Boot 最佳实践
- [ ] MyBatis-Plus / Mapper XML 模板
- [ ] 异常处理与日志规范
- [ ] 事务边界约定
- [ ] 常用工具类与枚举
