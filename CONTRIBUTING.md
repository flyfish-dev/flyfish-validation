# 贡献指南

感谢参与 Flyfish Validation。提交变更前请遵循以下约定：

1. 生产代码包名必须以 `dev.flyfish` 开头，不在方法体中使用完全限定类名。
2. Javax 与 Jakarta 约束层必须保持相同的公开 Flyfish API；新增约束应同时提供注解、验证器、三语言消息和约束参考文档。
3. 纯规则和业务 SPI 优先放入 `flyfish-validation-core`，避免把 Web、ORM、数据库或具体 Spring Boot 版本依赖带入核心。
4. 自定义验证器应当无状态或线程安全；空值是否必填应交由标准 `@NotNull`、`@NotBlank`、`@NotEmpty` 明确表达。
5. 代码需包含解释设计意图、边界和安全考虑的中文注释，不为显而易见的语句堆砌注释。

提交前至少执行：

```bash
./scripts/verify-source-compatibility.sh
```

具备 Maven 和依赖网络时继续执行：

```bash
mvn -B -ntp clean verify
```

涉及公开 API、配置项、默认错误结构或兼容矩阵的变更，应同步更新 README、CHANGELOG 和相关文档。
