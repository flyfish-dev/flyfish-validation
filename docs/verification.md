# 验证与发布清单

## 离线严格检查

```bash
./scripts/verify-source-compatibility.sh
```

该脚本不下载依赖，使用 `build-support/offline-stubs` 中独立编写的最小 API 签名进行：

- 核心、Javax、Jakarta、Spring Support 与 Boot 2 的 `--release 8` 编译；
- Boot 3/4 的 `--release 17` 编译；
- 所有生产源码与正式测试源码 `-Xlint -Werror`；
- 109 项核心算法和 SPI 断言；
- 72 个约束双命名空间完全对称；
- POM XML、包名、源码树和完全限定类名策略检查。

离线桩只证明源码使用的 API 形状与设计预期一致，不替代真实框架测试，也不会进入任何运行时 JAR。

## 真实依赖验证

```bash
mvn -B -ntp clean verify
```

它将使用：

- Hibernate Validator 6（Javax）；
- Hibernate Validator 8（Jakarta）；
- Spring Boot 2/3/4 各自 BOM；
- JUnit 5 集成测试。

## 发布构建

```bash
./scripts/build-release.sh
```

发布前还应完成：

- Maven Central 元数据、GPG 签名和 staging；
- JDK 8/11/17/21 CI；
- 示例应用启动与 HTTP 回归；
- 依赖漏洞和许可证扫描；
- API 二进制兼容检查；
- Release Notes 与迁移文档复核。

## 当前源码包的验证声明

本源码包已在交付环境执行离线严格检查和核心回归。交付环境未安装 Maven，且没有可用的真实依赖缓存，因此不能据此声称已运行真实 Hibernate Validator/Spring ApplicationContext 测试；相关 JUnit 测试和 Maven 配置已经包含在源码中，需在有 Maven 依赖的环境继续执行 `mvn clean verify`。
