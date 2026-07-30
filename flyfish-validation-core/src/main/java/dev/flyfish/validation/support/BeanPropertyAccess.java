package dev.flyfish.validation.support;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 支持 JavaBean、字段、数组、List 和 Map 的只读点路径访问器。
 *
 * <p>访问元数据按“类型+属性”缓存；不会修改 accessible 标志之外的对象状态。</p>
 */
public final class BeanPropertyAccess {
    private static final ConcurrentMap<Key, Accessor> CACHE = new ConcurrentHashMap<Key, Accessor>();
    private BeanPropertyAccess() { }

    public static PropertyValue read(Object root, String path) {
        if (root == null || path == null || path.trim().isEmpty()) { return PropertyValue.missing(); }
        Object current = root;
        String[] parts = path.trim().split("\\.");
        for (String part : parts) {
            if (current == null) { return PropertyValue.present(null); }
            PropertyValue next = readOne(current, part);
            if (!next.isPresent()) { return next; }
            current = next.getValue();
        }
        return PropertyValue.present(current);
    }

    private static PropertyValue readOne(Object target, String name) {
        if (target instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) target;
            return map.containsKey(name) ? PropertyValue.present(map.get(name)) : PropertyValue.missing();
        }
        Integer index = parseIndex(name);
        if (index != null) {
            if (target.getClass().isArray()) {
                return index >= 0 && index < Array.getLength(target)
                    ? PropertyValue.present(Array.get(target, index)) : PropertyValue.missing();
            }
            if (target instanceof List<?>) {
                List<?> list = (List<?>) target;
                return index >= 0 && index < list.size()
                    ? PropertyValue.present(list.get(index)) : PropertyValue.missing();
            }
        }
        Key key = new Key(target.getClass(), name);
        Accessor accessor = CACHE.get(key);
        if (accessor == null) {
            Accessor discovered = discover(key.type, key.name);
            Accessor previous = CACHE.putIfAbsent(key, discovered);
            accessor = previous == null ? discovered : previous;
        }
        return accessor.read(target);
    }

    private static Accessor discover(Class<?> type, String name) {
        String suffix = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (String methodName : new String[]{"get" + suffix, "is" + suffix, name}) {
            try {
                Method method = type.getMethod(methodName);
                if (method.getParameterTypes().length == 0) { return new MethodAccessor(method); }
            } catch (ReflectiveOperationException ignored) { }
        }
        Class<?> current = type;
        while (current != null && current != Object.class) {
            try { return new FieldAccessor(current.getDeclaredField(name)); }
            catch (NoSuchFieldException ignored) { current = current.getSuperclass(); }
        }
        return MissingAccessor.INSTANCE;
    }
    private static Integer parseIndex(String value) {
        try { return Integer.valueOf(value); }
        catch (NumberFormatException exception) { return null; }
    }

    public static final class PropertyValue {
        private static final PropertyValue MISSING = new PropertyValue(false, null);
        private final boolean present;
        private final Object value;
        private PropertyValue(boolean present, Object value) { this.present = present; this.value = value; }
        public static PropertyValue present(Object value) { return new PropertyValue(true, value); }
        public static PropertyValue missing() { return MISSING; }
        public boolean isPresent() { return present; }
        public Object getValue() { return value; }
    }
    private interface Accessor { PropertyValue read(Object target); }
    private static final class MethodAccessor implements Accessor {
        private final Method method;
        private MethodAccessor(Method method) {
            this.method = method;
            if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
                try { method.setAccessible(true); }
                catch (RuntimeException ignored) { }
            }
        }
        @Override public PropertyValue read(Object target) {
            try { return PropertyValue.present(method.invoke(target)); }
            catch (ReflectiveOperationException exception) { return PropertyValue.missing(); }
        }
    }
    private static final class FieldAccessor implements Accessor {
        private final Field field;
        private FieldAccessor(Field field) { this.field = field; if (!field.isAccessible()) { field.setAccessible(true); } }
        @Override public PropertyValue read(Object target) {
            try { return PropertyValue.present(field.get(target)); }
            catch (IllegalAccessException exception) { return PropertyValue.missing(); }
        }
    }
    private enum MissingAccessor implements Accessor {
        INSTANCE;
        @Override public PropertyValue read(Object target) { return PropertyValue.missing(); }
    }
    private static final class Key {
        private final Class<?> type; private final String name;
        private Key(Class<?> type, String name) { this.type = type; this.name = name; }
        @Override public int hashCode() { return 31 * type.hashCode() + name.hashCode(); }
        @Override public boolean equals(Object other) {
            if (this == other) { return true; }
            if (!(other instanceof Key)) { return false; }
            Key key = (Key) other; return type.equals(key.type) && name.equals(key.name);
        }
    }
}
