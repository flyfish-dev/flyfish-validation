package dev.flyfish.validation.example.boot2;

/** 示例中的用户查询端口；生产项目通常由 JPA、MyBatis 或远程客户端实现。 */
public interface UserRepository {
    boolean existsByUsername(String username);
}
