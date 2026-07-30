# Flyfish Validation

[![CI](https://github.com/flyfish-dev/flyfish-validation/actions/workflows/ci.yml/badge.svg)](https://github.com/flyfish-dev/flyfish-validation/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/license-Apache--2.0-5c49e8.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8—21-0b1017.svg)](docs/compatibility.md)

面向企业级 Spring Boot 项目的轻量验证增强库。它不重新实现 Bean Validation，
而是在 Hibernate Validator 与 Spring Validation 的成熟基础设施上补齐常见业务
规则、跨字段关系、数据库关联验证、统一失败行为和多版本 Starter。

**官方网站：** <https://validation.flyfish.group>

## 设计目标

- **开箱即用**：引入对应 Starter，即可使用标准约束和 72 个业务约束。
- **跨代兼容**：Java 8–21；Spring Boot 2 使用 `javax.validation`，Boot 3/4
  使用 `jakarta.validation`。
- **轻量复用**：算法、错误模型和业务规则引擎位于纯 Java 8 核心，不绑定 Web、
  ORM、数据库或特定 Spring Boot 版本。
- **复杂业务可扩展**：实现 `BusinessValidator<T>`、`AsyncBusinessValidator<T>`
  或 `SpringBusinessValidator` 并声明为 Bean，即可自动注册。
- **安全可定制**：默认不返回 rejected value；错误映射、脱敏、HTTP 状态、响应体、
  生命周期和行政区划数据源均可替换。
- **双命名空间同 API**：`javax` 与 `jakarta` 模块中的 Flyfish 注解包名保持
  `dev.flyfish.validation.constraints` 不变，迁移时只需更换 Starter 与标准注解导入。

## 兼容矩阵

| 使用场景 | Starter | Bean Validation | 编译目标 | 推荐运行 JDK |
|---|---|---|---:|---|
| Spring Boot 2.7.x | `flyfish-validation-spring-boot2-starter` | `javax.validation` / BV 2 | 8 | 8–21 |
| Spring Boot 3.x | `flyfish-validation-spring-boot3-starter` | `jakarta.validation` / BV 3 | 17 | 17–21 |
| Spring Boot 4.x | `flyfish-validation-spring-boot4-starter` | `jakarta.validation` / BV 3 | 17 | 17–21 |
| 非 Spring Java 8+ | `flyfish-validation-core` + `flyfish-validation-javax` | BV 2 | 8 | 8–21 |
| 非 Spring Java 17+ | `flyfish-validation-core` + `flyfish-validation-jakarta` | BV 3 | 8 | 17–21 |

Spring Boot 自身的 JDK 下限优先于本库字节码目标。完整说明见
[兼容性文档](docs/compatibility.md)。

## 模块

| 模块 | 职责 |
|---|---|
| `flyfish-validation-core` | 纯 Java 规则、统一错误、业务验证引擎、行政区划 SPI |
| `flyfish-validation-javax` | Bean Validation 2 / `javax.validation` 注解和验证器 |
| `flyfish-validation-jakarta` | Bean Validation 3 / `jakarta.validation` 注解和验证器 |
| `flyfish-validation-spring-support` | Spring `Validator` 适配、失败流水线和公共配置模型 |
| `*-boot2-*` | Spring Boot 2 自动装配与 Starter |
| `*-boot3-*` | Spring Boot 3 自动装配与 Starter |
| `*-boot4-*` | Spring Boot 4 自动装配与 Starter |
| `flyfish-validation-bom` | 所有公开模块统一版本 |

## 快速接入

### Spring Boot 2

```xml
<dependency>
    <groupId>dev.flyfish</groupId>
    <artifactId>flyfish-validation-spring-boot2-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Spring Boot 3

```xml
<dependency>
    <groupId>dev.flyfish</groupId>
    <artifactId>flyfish-validation-spring-boot3-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Spring Boot 4

```xml
<dependency>
    <groupId>dev.flyfish</groupId>
    <artifactId>flyfish-validation-spring-boot4-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

批量管理版本时可以导入 BOM：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.flyfish</groupId>
            <artifactId>flyfish-validation-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 使用业务约束

```java
@FieldsMatch(first = "password", second = "confirmation",
        reportOn = "confirmation")
@BirthdayAgeConsistent(birthday = "birthday", age = "age",
        reportOn = "age")
public class RegisterCommand {

    @NotBlank
    @ChineseMobile
    private String mobile;

    @ChinaIdCard
    private String idCard;

    @PersonName
    private String realName;

    @StrictEmail
    private String email;

    @StrongPassword(min = 10)
    private String password;

    private String confirmation;

    @Birthday(minAge = 18, maxAge = 70)
    private String birthday;

    @Age(min = 18, max = 70)
    private Integer age;
}
```

自定义约束与标准 `@NotNull`、`@NotBlank`、`@NotEmpty` 一样遵循组合使用：
绝大多数字段规则将 `null` 视为有效，是否必填由标准约束明确表达。

完整清单见 [72 个约束参考](docs/constraints-reference.md)。

## 数据库关联验证

```java
@Component
public final class UsernameUniqueValidator
        implements BusinessValidator<RegisterCommand> {

    private final UserRepository repository;

    public UsernameUniqueValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public String key() {
        return "username-unique";
    }

    @Override
    public Class<RegisterCommand> targetType() {
        return RegisterCommand.class;
    }

    @Override
    public ValidationReport validate(RegisterCommand command,
                                     BusinessValidationContext context) {
        if (!repository.existsByUsername(command.getUsername())) {
            return ValidationReport.valid();
        }
        return ValidationReport.invalid(ValidationError.builder(
                        "USERNAME_TAKEN", "用户名已存在")
                .propertyPath("username")
                .validator(key())
                .build());
    }
}
```

Bean 会被 Starter 自动收集。调用时只指定规则 key：

```java
flyfishValidator.validateBusinessOrThrow(command, "username-unique");
```

也可以直接实现 `SpringBusinessValidator`，继续使用熟悉的
`supports`、`Errors.rejectValue` 语义；远程校验则实现
`AsyncBusinessValidator<T>`。详见[业务验证指南](docs/business-validation.md)。

> “先查询再写入”不能替代数据库唯一约束。唯一性验证用于友好提示，最终一致性仍应由
> 唯一索引、事务和异常映射保证。

## 自定义 ConstraintValidator 自动注入

Starter 配置了 Spring 感知的 `ConstraintValidatorFactory`，自定义验证器可以
直接构造器注入 Repository、Mapper、缓存或客户端，不需要静态 Holder：

```java
public final class TenantCodeAvailableValidator
        implements ConstraintValidator<TenantCodeAvailable, String> {

    private final TenantRepository repository;

    public TenantCodeAvailableValidator(TenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !repository.existsByCode(value);
    }
}
```

## 默认 Web 失败响应

MVC 与 WebFlux Advice 默认返回 HTTP 400，结构为：

```json
{
  "code": "VALIDATION_FAILED",
  "message": "请求参数验证失败",
  "path": "/users",
  "method": "POST",
  "errors": [
    {
      "code": "ChineseMobile",
      "message": "手机号码格式不正确",
      "propertyPath": "mobile",
      "severity": "ERROR",
      "validator": "ChineseMobile"
    }
  ]
}
```

默认不会返回被拒绝的原始值。应用可以提供自己的
`ValidationFailureHandler`、`ValidationErrorCustomizer` 或更高优先级 Advice。

## 配置

```yaml
flyfish:
  validation:
    enabled: true
    fail-fast: false
    allow-duplicate-business-rules: false
    propagate-listener-exception: false
    expose-rejected-value: false
    expose-constraint-attributes: true
    web:
      enabled: true
      status: 400
      code: VALIDATION_FAILED
      message: 请求参数验证失败
```

## 构建与验证

```bash
# 无 Maven、无网络也可执行：严格编译全部生产源码和测试源码，并运行核心回归
./scripts/verify-source-compatibility.sh

# 有 Maven 时同时执行真实 Hibernate Validator 与 Spring Boot 测试
./scripts/verify-project.sh

# 发布构建
./scripts/build-release.sh

# 生成可复现的完整源码 ZIP 与 SHA-256
./scripts/package-source.py
```

当前源码包内置的离线校验会覆盖：

- Java 8 字节码核心、Javax、Jakarta、Spring Support、Boot 2；
- Java 17 API 面的 Boot 3、Boot 4；
- 两套命名空间的 72 个注解与资源对称性；
- 生产包名、POM XML、完全限定类名策略和编译产物污染检查；
- 109 项无第三方核心算法与业务 SPI 断言；
- JUnit/Hibernate Validator 集成测试源码编译。

## 文档导航

- [架构设计](docs/architecture.md)
- [兼容矩阵](docs/compatibility.md)
- [约束参考](docs/constraints-reference.md)
- [业务验证与数据库关联](docs/business-validation.md)
- [自定义约束](docs/custom-constraint.md)
- [配置项](docs/configuration.md)
- [统一 Web 错误处理](docs/web-error-handling.md)
- [行政区划数据源](docs/administrative-division.md)
- [大规模推广建议](docs/large-scale-practices.md)
- [Boot 2 → 3/4 迁移](docs/migration.md)
- [验证与发布清单](docs/verification.md)

## License

Apache License 2.0。
