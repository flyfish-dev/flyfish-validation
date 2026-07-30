# 兼容性

## 版本轨道

| 轨道 | Spring Boot 基线 | 验证命名空间 | 本库编译目标 | 运行要求 |
|---|---:|---|---:|---|
| Boot 2 | 2.7.18 | `javax.validation` | Java 8 | 遵循 Boot 2 与所用依赖的 JDK 支持范围 |
| Boot 3 | 3.5.16 | `jakarta.validation` | Java 17 | Java 17+ |
| Boot 4 | 4.1.0 | `jakarta.validation` | Java 17 | Java 17+ |

核心、Javax、Jakarta 与 Spring Support 的源码保持 Java 8 语言级别；Boot 3/4 适配模块使用 Java 17 编译。Jakarta 模块被 Java 8 编译只是为了保持库代码语法兼容，不表示 Hibernate Validator 8 或 Spring Boot 3/4 可以运行在 Java 8 上。

## 推荐组合

- 仍在 Boot 2 的 Java 8/11 系统：使用 Boot 2 Starter。
- 已迁移到 Boot 3：使用 Boot 3 Starter，不要同时引入 Javax 模块。
- Boot 4 新系统：使用 Boot 4 Starter；该模块独立适配 Boot 4 验证自动装配包结构。
- 普通 Java：自行选择 Javax 或 Jakarta 模块，并提供对应 Bean Validation Provider。

## 禁止混用

同一个应用中不要同时引入以下组合：

- `flyfish-validation-javax` 与 `flyfish-validation-jakarta`；
- Boot 2 Starter 与 Boot 3/4 Starter；
- 两个不同 Boot 主版本的 AutoConfigure。

BOM 只管理版本，不会自动选择轨道。应用必须显式依赖唯一 Starter。

## JDK 21

所有轨道均以“代码可在 JDK 21 上加载和执行”为目标；Boot 2 应结合实际第三方依赖、容器和代理库执行完整回归。源码包提供 JDK 8/17 字节码编译检查与 JDK 21 运行期核心回归，真实应用仍应在自己的依赖树和运行参数下测试。

## 升级策略

- 补丁版本：修复规则、消息、兼容性，不主动破坏公开 API。
- 次版本：新增注解、配置和扩展点，旧行为默认保持。
- 主版本：允许删除废弃 API 或调整验证边界，并提供迁移文档。
