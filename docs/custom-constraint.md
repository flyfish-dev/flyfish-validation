# 自定义声明式约束

## 基本结构

Boot 2 使用 `javax.validation`；Boot 3/4 使用 `jakarta.validation`。除导入外写法相同。

```java
@Documented
@Target({FIELD, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = TenantCodeAvailableValidator.class)
public @interface TenantCodeAvailable {
    String message() default "租户编码不可用";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

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

Starter 使用 Spring 感知的验证器工厂，因此构造器依赖会自动注入。验证器类不需要再声明为 `@Component`，也不要使用静态 ApplicationContext Holder。

## 设计规则

1. `null` 的必填语义交给标准约束，便于组合和错误区分。
2. Validator 初始化后应线程安全，不保存请求数据。
3. 注解参数在 `initialize` 中复制到字段；数组应 clone。
4. 错误应尽可能挂载到具体字段路径。
5. 远程 I/O 不适合 Bean Validation 方法验证链；优先使用业务验证器。
6. 不要在每次 `isValid` 中创建昂贵正则、解析器或客户端。
7. 提供明确消息 key，并为默认、中文、英文资源补齐文案。

## 自定义字段路径

类级约束可以关闭默认错误，再添加字段节点：

```java
context.disableDefaultConstraintViolation();
context.buildConstraintViolationWithTemplate(
        context.getDefaultConstraintMessageTemplate())
        .addPropertyNode("confirmation")
        .addConstraintViolation();
return false;
```

## 测试

至少覆盖：

- `null`、空字符串、边界值；
- 合法与非法样例；
- 分组与级联；
- 字段路径；
- 固定 Clock 的日期边界；
- Spring 构造器注入；
- 并发复用下无可变状态串扰。
