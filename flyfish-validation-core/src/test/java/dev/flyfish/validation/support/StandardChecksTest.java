package dev.flyfish.validation.support;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.flyfish.validation.model.IpVersion;
import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.model.PhoneType;

/** 常见生产规则的核心算法回归测试。 */
class StandardChecksTest {

    @Test
    void shouldValidateChineseIdentityAndContactData() {
        assertTrue(StandardChecks.isPhone("+86 138-0013-8000",
        PhoneType.CHINA_MOBILE));
        assertFalse(StandardChecks.isPhone("12800138000",
        PhoneType.CHINA_MOBILE));
        assertTrue(StandardChecks.isChineseIdCard("11010519491231002X", false, null));
        assertFalse(StandardChecks.isChineseIdCard("110105194912310021", false, null));
        assertTrue(StandardChecks.isPersonName("欧阳娜娜", NameType.CHINESE, 2, 64));
        assertTrue(StandardChecks.isPersonName("Jean-Luc Picard",
        NameType.INTERNATIONAL, 2, 64));
    }

    @Test
    void shouldValidateFinanceAndNetworkData() {
        assertTrue(StandardChecks.isLuhn("4532015112830366", false));
        assertTrue(StandardChecks.isIban("GB82 WEST 1234 5698 7654 32"));
        assertTrue(StandardChecks.isCidr("192.168.0.0/24", IpVersion.ANY));
        assertTrue(StandardChecks.isUrl("https://例子.测试/path",
        new String[] {"http", "https"}, true));
        assertTrue(StandardChecks.isMoney(new BigDecimal("9999.99"),
        "0.00", "10000.00", 2, 12, false));
    }
}
