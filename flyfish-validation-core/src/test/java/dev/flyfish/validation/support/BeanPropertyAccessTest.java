package dev.flyfish.validation.support;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.flyfish.validation.support.BeanPropertyAccess.PropertyValue;

/** JavaBean 属性访问的可见性与回退行为回归测试。 */
class BeanPropertyAccessTest {

    @Test
    void shouldInvokePublicGetterOnPackagePrivateBean() {
        PropertyValue value = BeanPropertyAccess.read(
        new PackagePrivateBean("flyfish"), "name");

        assertTrue(value.isPresent());
        assertEquals("flyfish", value.getValue());
    }

    private static final class PackagePrivateBean {
        private final String name;

        private PackagePrivateBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
