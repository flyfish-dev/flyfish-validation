# 业务验证与数据库关联

## 何时使用

Bean Validation 适合“单个对象在当前内存状态下是否满足约束”；以下场景应使用业务验证器：

- 用户名、手机号、证件号是否已存在；
- 当前用户是否有权操作目标资源；
- 订单状态是否允许变更；
- 优惠券、库存、额度、黑名单、租户配置；
- 跨聚合、跨数据库或远程服务规则。

## 同步规则

```java
@Component
public final class UsernameUniqueValidator
        implements BusinessValidator<RegisterCommand> {

    private final UserRepository repository;

    public UsernameUniqueValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override public String key() { return "username-unique"; }
    @Override public Class<RegisterCommand> targetType() {
        return RegisterCommand.class;
    }

    @Override
    public ValidationReport validate(RegisterCommand value,
                                     BusinessValidationContext context) {
        if (!repository.existsByUsername(value.getUsername())) {
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

## Spring Validator 风格

```java
@Component
public final class OrderStateValidator
        implements SpringBusinessValidator {

    @Override public String key() { return "order-state"; }
    @Override public boolean supports(Class<?> type) {
        return ChangeOrderCommand.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangeOrderCommand command = (ChangeOrderCommand) target;
        if (!command.canChange()) {
            errors.rejectValue("status", "ORDER_STATE_INVALID",
                    "当前订单状态不允许执行该操作");
        }
    }
}
```

需要 Locale、Clock、租户或调用参数时实现 `ContextAwareSpringBusinessValidator`。

## 异步规则

`AsyncBusinessValidator<T>` 返回 `CompletionStage<ValidationReport>`。它适合已经具备非阻塞客户端的 WebFlux 场景。不要为了“看起来异步”把阻塞 JDBC 调用放到公共线程池；阻塞数据源应继续使用同步规则，或由应用明确调度到受控执行器。

## 调用方式

```java
ValidationReport report = flyfishValidator.validateBusiness(
        command, "username-unique", "tenant-enabled");

flyfishValidator.validateBusinessOrThrow(
        command, "username-unique", "tenant-enabled");

CompletionStage<ValidationReport> async =
        flyfishValidator.validateBusinessAsync(command, "risk-check");
```

高级参数：

```java
BusinessValidationOptions options = BusinessValidationOptions.builder()
        .failFast(true)
        .failOnMissingRule(true)
        .clock(clock)
        .parameter("scene", "REGISTER")
        .attribute("tenantId", tenantId)
        .build();
```

## 顺序与重复 key

- 调用参数中的 key 顺序决定业务执行顺序。
- 注册表会按 `order()` 和 key 形成稳定注册顺序。
- 默认发现重复 key 就在启动阶段失败。
- `allow-duplicate-business-rules=true` 只用于受控覆盖，后注册实现会替换前者；大规模项目不建议开启。

## 数据库并发边界

唯一性检查与实际写入之间存在竞态：

1. 两个请求同时查询“用户名不存在”；
2. 两者均通过验证；
3. 两者同时写入。

因此正确组合是：

- 业务验证器给用户友好、可定位的提前错误；
- 数据库唯一索引保证最终一致性；
- 捕获唯一键异常并映射成同一业务错误码；
- 多资源状态迁移使用事务、版本号或原子条件更新。

## 性能建议

- 把无 I/O 的 Bean Validation 放在数据库查询之前。
- 同一请求所需的相关信息尽量批量查询，避免每个字段一个 SQL。
- 缓存稳定主数据，不缓存高竞争写状态或设置极短 TTL。
- 在 `ValidationLifecycleListener` 中记录规则 key、耗时和结果数量，不记录敏感原值。
