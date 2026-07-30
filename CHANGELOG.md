# Changelog

## 1.0.0 — 2026-07-29

- 建立 Java 8 纯核心、Bean Validation `javax`/`jakarta` 双适配架构。
- 提供 Spring Boot 2、3、4 独立 AutoConfigure 与 Starter。
- 首批交付 72 个业务约束：58 个字段/类型约束、14 个跨字段约束。
- 提供同步、异步、Spring `Validator` 风格的业务规则 SPI 和自动注册。
- 提供统一错误模型、生命周期监听、失败处理、脱敏与 MVC/WebFlux Advice。
- 支持 Spring 构造器注入自定义 `ConstraintValidator`。
- 行政区划 Provider 已贯通身份证、行政区划约束与 Starter 注入链路。
- 增加中英文消息、BOM、三代示例、离线严格编译与 CI 验证脚本。
