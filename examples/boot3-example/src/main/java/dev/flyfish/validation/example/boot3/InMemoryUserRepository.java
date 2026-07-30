package dev.flyfish.validation.example.boot3;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

/** 仅用于示例的内存仓库。 */
@Repository
public final class InMemoryUserRepository implements UserRepository {
    private final Set<String> usernames = Collections.synchronizedSet(
    new HashSet<String>(Collections.singleton("admin")));

    @Override
    public boolean existsByUsername(String username) {
        return username != null && usernames.contains(username);
    }
}
