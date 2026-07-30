# 行政区划验证

## 默认策略

国家行政区划会调整，且不同业务对“当前代码”“历史代码”“统计用代码”“民政区划”定义并不完全相同。基础库因此不声称携带永久准确的县乡村全量快照。

`DefaultChinaAdministrativeDivisionProvider` 只执行：

- 全数字；
- 层级对应长度；
- 稳定省级前缀；
- 未知层级拒绝。

它适合作为输入格式防线，不适合作为权威主数据。

## 精确 Provider

```java
@Component
public final class DatabaseDivisionProvider
        implements AdministrativeDivisionProvider {

    private final DivisionRepository repository;

    public DatabaseDivisionProvider(DivisionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean exists(String code,
                          AdministrativeDivisionLevel level,
                          LocalDate effectiveDate) {
        return repository.existsEffectiveCode(code, level, effectiveDate);
    }
}
```

Starter 会把该 Bean 注入：

- `@ChinaAdministrativeDivisionCode` 验证器；
- `@ChinaIdCard` 验证器；
- 应用直接调用的身份证解析链路（通过显式 Provider 参数）。

## 数据模型建议

至少保存：

- code、name、level；
- parentCode；
- validFrom、validTo；
- source、sourceVersion；
- status（启用、撤销、合并、历史）；
- aliases 或历史映射。

不要只用 code 主键覆盖更新，否则历史身份证、合同和地址在行政区划调整后可能被错误拒绝。

## 缓存

区划数据读多写少，可按版本加载到不可变 Map 或 Redis。更新时构建新快照后原子切换，避免请求读到半更新状态。
