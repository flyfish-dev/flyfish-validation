# 示例应用

三个示例分别演示 Spring Boot 2、3、4 接入方式，代码结构与接口保持一致：

```bash
mvn -f examples/boot2-example/pom.xml spring-boot:run
mvn -f examples/boot3-example/pom.xml spring-boot:run
mvn -f examples/boot4-example/pom.xml spring-boot:run
```

运行示例前，需要先在项目根目录执行 `mvn install`，或者把 Starter 发布到公司的 Maven 仓库。

测试请求：

```bash
curl -X POST http://localhost:8080/users \
  -H 'Content-Type: application/json' \
  -d '{
    "username":"admin",
    "mobile":"13800138000",
    "email":"engineer@example.com",
    "password":"Flyfish@2026",
    "confirmation":"Flyfish@2026"
  }'
```

`admin` 被示例仓库占用，会触发自动注册的 `username-unique` 数据库关联规则。
