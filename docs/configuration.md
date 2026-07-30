# 配置项

前缀：`flyfish.validation`

| 属性 | 默认值 | 说明 |
|---|---:|---|
| `enabled` | `true` | 是否启用 Flyfish 基础自动装配 |
| `fail-fast` | `false` | Hibernate Validator 是否遇到首个错误即停止 |
| `allow-duplicate-business-rules` | `false` | 是否允许重复业务规则 key 覆盖 |
| `propagate-listener-exception` | `false` | 生命周期监听异常是否中断业务验证 |
| `expose-rejected-value` | `false` | Web 响应是否暴露原始错误值，生产环境不建议开启 |
| `expose-constraint-attributes` | `true` | 是否保留约束参数元数据 |
| `web.enabled` | `true` | 是否启用默认 MVC/WebFlux Advice |
| `web.status` | `400` | 默认 HTTP 状态码 |
| `web.code` | `VALIDATION_FAILED` | 顶层业务错误码 |
| `web.message` | `请求参数验证失败` | 顶层提示文案 |

## 覆盖策略

公共 Bean 均采用 `@ConditionalOnMissingBean`。应用可声明同类型 Bean 替换：

- `AdministrativeDivisionProvider`
- `BusinessValidatorRegistry`
- `BusinessValidationExecutor`
- `FlyfishValidator`
- `ValidationRejectedValueSanitizer`
- `ValidationFailureHandler`
- `ValidationFailurePipeline`

错误定制器和生命周期监听器允许多个 Bean，并按 Spring Order/自身 order 稳定执行。

## 生产建议

- 保持 `expose-rejected-value=false`。
- 对外 API 使用稳定错误码，文案仅用于展示。
- 不建议开启重复业务规则覆盖；确需多租户替换时应显式设计路由规则。
- 生命周期监听异常默认隔离，监控系统故障不应阻断核心请求。
