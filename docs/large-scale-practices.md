# 大规模推广实践

## 公司级治理

1. 由平台团队统一维护 BOM 和 Starter 版本。
2. 业务项目只依赖一个与 Boot 主版本匹配的 Starter。
3. 建立公司错误码、分组、消息和脱敏规范。
4. 公共业务规则进入共享模块；领域专属规则留在领域服务。
5. CI 强制运行验证测试和依赖冲突检查。

## 分组建议

验证分组用于生命周期差异，例如 Create、Update、Import，而不是为每个接口创建一个组。组过多会使 DTO 行为难以推断。复杂场景优先拆分命令对象。

## DTO 与领域边界

- 接口 DTO：格式、长度、必填、跨字段关系。
- Application Service：权限、存在性、幂等、状态迁移。
- Domain：不变量与聚合内规则。
- Database：唯一性、外键、检查约束与事务一致性。

四层互补，不能用一层完全替代另一层。

## 可观测性

通过 `ValidationLifecycleListener` 记录：

- operation / rule key；
- 成功、失败、异常；
- 错误数量；
- 耗时分布；
- 应用、租户、场景等非敏感标签。

禁止记录 rejected value、完整身份证、银行卡、密码、Token。高基数属性不要进入指标标签。

## 性能

- Validator 和注册表复用，不在请求内重复构建 ValidatorFactory。
- 正则预编译，日期格式器复用或局部安全创建。
- 业务校验合并查询，避免 N+1。
- fail-fast 只在确实能降低高成本 I/O 时使用；表单场景通常收集全部错误体验更好。
- 大集合约束设置业务上限，避免攻击者提交超大数组造成 CPU/内存压力。

## 发布流程

- 双命名空间 parity 检查；
- Java 8 与 17 字节码编译；
- JDK 8/11/17/21 CI 矩阵；
- Hibernate Validator 6/8 集成测试；
- 三代 Boot ApplicationContext 冒烟测试；
- OWASP/依赖漏洞扫描；
- 变更日志和迁移说明。
