package dev.flyfish.validation.region;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 只内置稳定省级前缀的保守实现。
 *
 * <p>县、乡、村代码经常调整，生产系统需要精确校验时应替换为数据库、
 * Redis 或主数据服务 Provider，避免固定快照误拒合法历史数据。</p>
 */
public final class DefaultChinaAdministrativeDivisionProvider
    implements AdministrativeDivisionProvider {
    private static final Set<String> PROVINCES = Collections.unmodifiableSet(
    new HashSet<String>(Arrays.asList(
    "11","12","13","14","15","21","22","23",
    "31","32","33","34","35","36","37","41",
    "42","43","44","45","46","50","51","52",
    "53","54","61","62","63","64","65","71",
    "81","82")));
    @Override public boolean exists(String code, AdministrativeDivisionLevel level,
    LocalDate effectiveDate) {
        if (code == null || !code.matches("\\d+")) { return false; }
        AdministrativeDivisionLevel effective = level == null || level == AdministrativeDivisionLevel.AUTO
            ? AdministrativeDivisionLevel.fromLength(code.length()) : level;
        return effective != AdministrativeDivisionLevel.AUTO
            && code.length() == effective.getLength()
            && code.length() >= 2 && PROVINCES.contains(code.substring(0, 2));
    }
}
