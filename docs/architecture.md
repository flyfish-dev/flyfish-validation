# 架构设计

## 1. 分层原则

Flyfish Validation 采用“稳定核心 + 命名空间适配 + Spring 公共能力 + Boot 版本薄适配”的结构。

```text
业务应用
  ├─ Bean Validation 注解与方法验证
  ├─ FlyfishValidator 程序式门面
  └─ BusinessValidator / SpringBusinessValidator / AsyncBusinessValidator
          │
Spring Boot 2/3/4 Starter
  ├─ 自动发现与注册
  ├─ ConstraintValidator Spring 注入
  ├─ MVC / WebFlux 统一失败处理
  └─ 配置属性
          │
javax / jakarta 约束适配层        Spring 公共支持层
          └──────────┬───────────┘
                 Java 8 核心
  ├─ StandardChecks 与解析器
  ├─ 统一错误模型
  ├─ 业务规则注册表与执行器
  ├─ 生命周期监听
  └─ AdministrativeDivisionProvider SPI
```

## 2. 为什么不重写 Hibernate Validator

标准 `@NotNull`、`@Size`、级联验证、分组、方法验证、消息插值和元数据模型已经由 Bean Validation 与 Hibernate Validator 稳定实现。本库只增加企业项目中重复出现、但标准规范没有覆盖的业务规则，并通过标准 `ConstraintValidator` 接口接入，避免形成第二套不兼容的验证体系。

## 3. javax 与 jakarta 双模块

Boot 2 依赖 Bean Validation 2 的 `javax.validation`；Boot 3/4 使用 `jakarta.validation`。Java 源码无法用一个二进制同时引用两种命名空间，因此分别构建：

- `flyfish-validation-javax`
- `flyfish-validation-jakarta`

两者公开的 Flyfish 包名、注解名、参数、验证逻辑和消息 key 保持一致，`scripts/check-namespace-parity.py` 在 CI 中强制校验对称性。

## 4. 业务验证器

### 注册阶段

Starter 收集三类 Bean：

1. `BusinessValidator<T>`：同步规则；
2. `AsyncBusinessValidator<T>`：远程服务、异步缓存等非阻塞规则；
3. `SpringBusinessValidator`：兼容 Spring `Validator`/`Errors` 风格。

它们进入构造后只读的 `DefaultBusinessValidatorRegistry`。默认禁止重复 key，启动阶段即可暴露配置冲突。

### 执行阶段

`BusinessValidationExecutor` 根据调用给出的规则 key 顺序执行，支持：

- 收集全部错误或 fail-fast；
- 缺少规则时失败或忽略；
- Locale、Clock、参数、租户等上下文；
- 同步/异步统一 `ValidationReport`；
- before/success/failure/exception 生命周期事件。

注册表不可变、读取无锁，适合高并发服务复用。

## 5. 失败处理流水线

Web 异常进入固定顺序：

```text
异常提取 → ValidationErrorCustomizer（有序）
         → ValidationRejectedValueSanitizer
         → ValidationFailureHandler
         → MVC/WebFlux 响应
```

默认脱敏先于响应序列化，避免密码、Token、身份证、银行卡等原始值泄漏。应用可以仅替换其中一个策略，也可以提供高优先级 Advice 完全接管。

## 6. 行政区划 Provider 注入

县、乡、村代码会调整，基础库不固化声称“永久准确”的大快照。默认 Provider 只验证稳定编码结构和省级前缀。Starter 的 `FlyfishConstraintValidatorFactory` 在创建身份证和行政区划验证器时注入应用自己的 Provider，同时仍委托 `SpringConstraintValidatorFactory` 为所有自定义验证器完成构造器注入。

## 7. 扩展点

| 扩展点 | 用途 |
|---|---|
| `BusinessValidator<T>` | 数据库关联和领域规则 |
| `AsyncBusinessValidator<T>` | 异步远程校验 |
| `SpringBusinessValidator` | 复用 Spring Validator 代码 |
| `ValidationLifecycleListener` | 指标、日志、审计、追踪 |
| `ValidationErrorCustomizer` | 错误码、文案、字段路径加工 |
| `ValidationRejectedValueSanitizer` | 敏感值策略 |
| `ValidationFailureHandler` | HTTP 状态、响应体、Header |
| `AdministrativeDivisionProvider` | 区划数据库/缓存/主数据服务 |
| 标准 `ConstraintValidator` | 新的声明式注解 |

## 8. 线程安全约定

- `ValidationError`、`ValidationReport`、上下文和注册表是不可变对象。
- 内置 Validator 在初始化后只读；Provider 和用户业务 Validator 应设计为无状态或线程安全 Spring Bean。
- 禁止把请求级可变状态保存到 ConstraintValidator 字段；应通过上下文、Repository 或线程安全依赖读取。
