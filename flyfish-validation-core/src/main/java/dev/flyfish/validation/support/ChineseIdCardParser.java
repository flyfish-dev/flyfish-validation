package dev.flyfish.validation.support;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Optional;

import dev.flyfish.validation.region.AdministrativeDivisionLevel;
import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.region.DefaultChinaAdministrativeDivisionProvider;

/** 中国居民身份证号码解析和校验工具。 */
public final class ChineseIdCardParser {
    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] CHECKS = {'1','0','X','9','8','7','6','5','4','3','2'};
    private static final DateTimeFormatter BIRTHDAY = DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);
    private ChineseIdCardParser() { }

    public static Optional<ChineseIdCardInfo> parse(String value,
    boolean allowLegacy15, Clock clock) {
        return parse(value, allowLegacy15, clock,
        new DefaultChinaAdministrativeDivisionProvider(), 150);
    }

    public static Optional<ChineseIdCardInfo> parse(String value,
    boolean allowLegacy15, Clock clock,
    AdministrativeDivisionProvider provider, int maximumAge) {
        if (value == null) { return Optional.empty(); }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if (normalized.matches("\\d{15}") && allowLegacy15) {
            normalized = convertLegacy(normalized);
        }
        if (!normalized.matches("\\d{17}[0-9X]")) { return Optional.empty(); }
        int sum = 0;
        for (int index = 0; index < 17; index++) { sum += (normalized.charAt(index) - '0') * WEIGHTS[index]; }
        if (CHECKS[sum % 11] != normalized.charAt(17)) { return Optional.empty(); }
        String division = normalized.substring(0, 6);
        AdministrativeDivisionProvider effectiveProvider = provider == null
            ? new DefaultChinaAdministrativeDivisionProvider() : provider;
        if (!effectiveProvider.exists(division, AdministrativeDivisionLevel.COUNTY, LocalDate.now(effectiveClock(clock)))) {
            return Optional.empty();
        }
        try {
            LocalDate birthday = LocalDate.parse(normalized.substring(6, 14), BIRTHDAY);
            LocalDate today = LocalDate.now(effectiveClock(clock));
            if (birthday.isAfter(today)) { return Optional.empty(); }
            int age = Period.between(birthday, today).getYears();
            if (age < 0 || age > maximumAge) { return Optional.empty(); }
            boolean male = (normalized.charAt(16) - '0') % 2 == 1;
            return Optional.of(new ChineseIdCardInfo(normalized, birthday, age, division, male));
        } catch (DateTimeException exception) {
            return Optional.empty();
        }
    }

    public static String convertLegacy(String legacy) {
        if (legacy == null || !legacy.matches("\\d{15}")) { throw new IllegalArgumentException("不是合法的 15 位身份证格式"); }
        String body = legacy.substring(0, 6) + "19" + legacy.substring(6);
        int sum = 0;
        for (int index = 0; index < 17; index++) { sum += (body.charAt(index) - '0') * WEIGHTS[index]; }
        return body + CHECKS[sum % 11];
    }
    private static Clock effectiveClock(Clock clock) { return clock == null ? Clock.system(ZoneId.systemDefault()) : clock; }
}
