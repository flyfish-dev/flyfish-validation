package dev.flyfish.validation.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 构造后只读、运行期无锁的默认业务验证器注册表。 */
public final class DefaultBusinessValidatorRegistry implements BusinessValidatorRegistry {
    private final Map<String, BusinessValidator<?>> synchronous;
    private final Map<String, AsyncBusinessValidator<?>> asynchronous;

    public DefaultBusinessValidatorRegistry(
    Collection<? extends BusinessValidator<?>> sync,
    Collection<? extends AsyncBusinessValidator<?>> async) {
        this(sync, async, false);
    }

    public DefaultBusinessValidatorRegistry(
    Collection<? extends BusinessValidator<?>> sync,
    Collection<? extends AsyncBusinessValidator<?>> async,
    boolean allowDuplicateKeys) {
        Map<String, BusinessValidator<?>> syncMap = new LinkedHashMap<String, BusinessValidator<?>>();
        Map<String, AsyncBusinessValidator<?>> asyncMap = new LinkedHashMap<String, AsyncBusinessValidator<?>>();
        for (BusinessValidator<?> value : sorted(sync)) {
            put(syncMap, asyncMap, value, allowDuplicateKeys);
        }
        for (AsyncBusinessValidator<?> value : sorted(async)) {
            put(asyncMap, syncMap, value, allowDuplicateKeys);
        }
        synchronous = Collections.unmodifiableMap(syncMap);
        asynchronous = Collections.unmodifiableMap(asyncMap);
    }

    public static DefaultBusinessValidatorRegistry empty() {
        return new DefaultBusinessValidatorRegistry(
        Collections.<BusinessValidator<?>>emptyList(),
        Collections.<AsyncBusinessValidator<?>>emptyList());
    }

    @Override public boolean contains(String key) {
        String value = normalize(key);
        return synchronous.containsKey(value) || asynchronous.containsKey(value);
    }
    @Override public boolean containsSynchronous(String key) {
        return synchronous.containsKey(normalize(key));
    }
    @Override public boolean containsAsynchronous(String key) {
        return asynchronous.containsKey(normalize(key));
    }
    @Override public Set<String> keys() {
        LinkedHashSet<String> keys = new LinkedHashSet<String>(synchronous.keySet());
        keys.addAll(asynchronous.keySet());
        return Collections.unmodifiableSet(keys);
    }
    @Override public BusinessValidator<?> synchronous(String key) {
        return synchronous.get(normalize(key));
    }
    @Override public AsyncBusinessValidator<?> asynchronous(String key) {
        return asynchronous.get(normalize(key));
    }

    private static <T extends BusinessRule<?>> List<T> sorted(Collection<? extends T> values) {
        List<T> copy = new ArrayList<T>();
        if (values != null) { for (T value : values) { if (value != null) { copy.add(value); } } }
        Collections.sort(copy, new Comparator<T>() {
            @Override public int compare(T left, T right) {
                int byOrder = Integer.compare(left.order(), right.order());
                return byOrder == 0 ? normalize(left.key()).compareTo(normalize(right.key())) : byOrder;
            }
        });
        return copy;
    }

    private static <T extends BusinessRule<?>> void put(
    Map<String, T> destination, Map<String, ?> other,
    T value, boolean allowDuplicateKeys) {
        validate(value);
        String key = normalize(value.key());
        if (!allowDuplicateKeys && (destination.containsKey(key) || other.containsKey(key))) {
            throw new DuplicateBusinessValidatorException(key);
        }
        destination.put(key, value);
    }
    private static void validate(BusinessRule<?> value) {
        if (value == null || normalize(value.key()).isEmpty()) {
            throw new IllegalArgumentException("业务验证器 key 不能为空");
        }
        if (value.targetType() == null) {
            throw new IllegalArgumentException("业务验证器 targetType 不能为空: " + value.key());
        }
    }
    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
