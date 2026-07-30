# 从 Spring Boot 2 迁移到 Boot 3/4

## 依赖替换

```xml
<!-- 删除 -->
<artifactId>flyfish-validation-spring-boot2-starter</artifactId>

<!-- Boot 3 -->
<artifactId>flyfish-validation-spring-boot3-starter</artifactId>

<!-- Boot 4 -->
<artifactId>flyfish-validation-spring-boot4-starter</artifactId>
```

不要保留 Javax 与 Jakarta 两套模块。

## 标准注解导入

```java
// Boot 2
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

// Boot 3/4
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
```

Flyfish 注解导入不变：

```java
import dev.flyfish.validation.constraints.ChineseMobile;
```

## 自定义 Validator

把 `javax.validation.*` 改为 `jakarta.validation.*`。注解参数和逻辑通常无需调整。第三方验证扩展也必须选择 Jakarta 兼容版本。

## 方法验证异常

Spring Framework 6.1+ 原生方法验证可能抛出新的方法验证结果异常，而不再总是 `ConstraintViolationException`。Boot 3/4 AutoConfigure 已包含兼容提取；应用自定义全局 Advice 时也要同时覆盖这些异常。

## 回归重点

- JSON 绑定错误字段路径；
- 方法参数、返回值验证；
- 自定义 ConstraintValidator 构造器注入；
- 消息插值和 Locale；
- WebFlux 错误处理；
- Hibernate Validator Provider 版本；
- Servlet/Jakarta 相关依赖冲突。
