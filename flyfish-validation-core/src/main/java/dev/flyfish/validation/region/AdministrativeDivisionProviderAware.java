package dev.flyfish.validation.region;

/**
 * 需要行政区划主数据的验证器所实现的注入契约。
 *
 * <p>该接口位于纯 Java 核心模块，不依赖 Spring。Spring Boot Starter 会在
 * 创建 Bean Validation 验证器时注入应用中的
 * {@link AdministrativeDivisionProvider}；脱离 Spring 使用时，验证器仍会
 * 采用内置的保守实现。</p>
 */
public interface AdministrativeDivisionProviderAware {

    /**
     * 设置行政区划数据提供者。
     *
     * @param provider 不可为 {@code null} 的数据提供者
     */
    void setAdministrativeDivisionProvider(AdministrativeDivisionProvider provider);
}
