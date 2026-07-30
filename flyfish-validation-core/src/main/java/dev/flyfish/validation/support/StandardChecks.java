package dev.flyfish.validation.support;

import java.math.BigDecimal;
import java.net.IDN;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import dev.flyfish.validation.model.IpVersion;
import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.model.PhoneType;
import dev.flyfish.validation.region.AdministrativeDivisionLevel;
import dev.flyfish.validation.region.AdministrativeDivisionProvider;
import dev.flyfish.validation.region.DefaultChinaAdministrativeDivisionProvider;

/**
 * 内置约束共享的纯 Java、无状态验证算法。
 *
 * <p>所有方法均不修改输入。正则和映射表在类加载时构造，可并发复用。
 * 除明确注明外，空值由 Bean Validation 标准的 {@code @NotNull}、
 * {@code @NotBlank} 负责，本类的格式方法对空文本返回 {@code false}。</p>
 */
public final class StandardChecks {
    private static final Pattern CHINA_MOBILE =
    Pattern.compile("^(?:\\+?86|0086)?1[3-9]\\d{9}$");
    private static final Pattern CHINA_LANDLINE = Pattern.compile(
    "^(?:(?:\\+?86|0086)-?)?(?:0\\d{2,3}-?)?"
    + "\\d{7,8}(?:-\\d{1,6})?$");
    private static final Pattern E164 = Pattern.compile("^\\+[1-9]\\d{7,14}$");
    private static final Pattern POSTAL_CODE = Pattern.compile("^(?!000000)\\d{6}$");
    private static final Pattern MAC = Pattern.compile(
    "^(?:[0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$");
    private static final Pattern HEX = Pattern.compile("^[0-9A-Fa-f]+$");
    private static final Pattern NUMERIC = Pattern.compile("^[+-]?\\d+(?:\\.\\d+)?$");
    private static final Pattern INTEGER = Pattern.compile("^[+-]?\\d+$");
    private static final Pattern COMPACT_UUID = Pattern.compile("^[0-9A-Fa-f]{32}$");
    private static final Pattern MIME_TYPE = Pattern.compile(
    "^[A-Za-z0-9!#$&^_.+-]+/[A-Za-z0-9!#$&^_.+-]+$");
    private static final Pattern USERNAME = Pattern.compile(
    "^[A-Za-z0-9](?:[A-Za-z0-9._-]*[A-Za-z0-9])?$");
    private static final Pattern VIN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");
    private static final Pattern NORMAL_PLATE = Pattern.compile(
    "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]"
    + "[A-Z][A-HJ-NP-Z0-9]{5}$");
    private static final Pattern NEW_ENERGY_PLATE = Pattern.compile(
    "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]"
    + "[A-Z](?:[DF][A-HJ-NP-Z0-9]\\d{4}|\\d{5}[DF])$");
    private static final Pattern DOMAIN_LABEL = Pattern.compile(
    "^[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?$");
    private static final Pattern IBAN = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z0-9]{11,30}$");
    private static final Pattern BIC = Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}(?:[A-Z0-9]{3})?$");
    private static final Pattern ORGANIZATION_CODE = Pattern.compile("^[0-9A-Z]{8}[0-9X]$");
    private static final Pattern CHINESE_PASSPORT = Pattern.compile("^[EGDSP]\\d{8}$");
    private static final Pattern EMAIL_LOCAL = Pattern.compile(
    "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+"
    + "(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*$");

    private static final String CREDIT_CHARSET = "0123456789ABCDEFGHJKLMNPQRTUWXY";
    private static final int[] CREDIT_WEIGHTS =
    {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
    private static final int[] VIN_WEIGHTS =
    {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final Map<Character, Integer> VIN_VALUES;
    private static final Set<String> ISO_COUNTRIES = Collections.unmodifiableSet(
    new HashSet<String>(Arrays.asList(Locale.getISOCountries())));
    private static final Set<String> COMMON_PASSWORDS = Collections.unmodifiableSet(
    new HashSet<String>(Arrays.asList(
    "password", "password1", "123456", "12345678", "123456789",
    "qwerty", "qwerty123", "admin", "admin123", "letmein",
    "welcome", "abc123", "111111", "000000")));
    private static final DefaultChinaAdministrativeDivisionProvider DEFAULT_DIVISIONS =
    new DefaultChinaAdministrativeDivisionProvider();

    static {
        Map<Character, Integer> values = new HashMap<Character, Integer>();
        String letters = "ABCDEFGHJKLMNPRSTUVWXYZ";
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4,
            5, 7, 9, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int index = 0; index < letters.length(); index++) {
            values.put(letters.charAt(index), numbers[index]);
        }
        VIN_VALUES = Collections.unmodifiableMap(values);
    }

    private StandardChecks() { }

    public static boolean isBlank(CharSequence value) {
        return value == null || value.toString().trim().isEmpty();
    }

    public static boolean isPhone(CharSequence value, PhoneType type) {
        if (isBlank(value)) { return false; }
        PhoneType effective = type == null ? PhoneType.ANY : type;
        String compact = compactPhone(value.toString());
        String landline = normalizeLandline(value.toString());
        switch (effective) {
            case CHINA_MOBILE: return CHINA_MOBILE.matcher(compact).matches();
            case CHINA_LANDLINE: return CHINA_LANDLINE.matcher(landline).matches();
            case E164: return E164.matcher(compact).matches();
            default:
            return CHINA_MOBILE.matcher(compact).matches()
                || CHINA_LANDLINE.matcher(landline).matches()
                || E164.matcher(compact).matches();
        }
    }

    public static boolean isChineseIdCard(CharSequence value,
    boolean allowLegacy15, Clock clock) {
        return value != null && ChineseIdCardParser.parse(
        value.toString(), allowLegacy15, clock).isPresent();
    }

    public static boolean isUnifiedSocialCreditCode(CharSequence value) {
        if (isBlank(value)) { return false; }
        String code = value.toString().trim().toUpperCase(Locale.ROOT);
        if (code.length() != 18) { return false; }
        int sum = 0;
        for (int index = 0; index < 17; index++) {
            int mapped = CREDIT_CHARSET.indexOf(code.charAt(index));
            if (mapped < 0) { return false; }
            sum += mapped * CREDIT_WEIGHTS[index];
        }
        return CREDIT_CHARSET.charAt((31 - sum % 31) % 31) == code.charAt(17);
    }

    public static boolean isBankCard(CharSequence value) {
        return isLuhn(value, 12, 19);
    }

    public static boolean isLuhn(CharSequence value, int minimum, int maximum) {
        if (isBlank(value) || minimum < 1 || maximum < minimum) { return false; }
        String digits = compactDigits(value.toString());
        return digits != null && digits.length() >= minimum
            && digits.length() <= maximum && luhn(digits);
    }

    public static boolean isLuhn(CharSequence value, boolean allowSeparators) {
        if (isBlank(value)) { return false; }
        String source = value.toString().trim();
        String digits = allowSeparators ? compactDigits(source) : source;
        return digits != null && luhn(digits);
    }

    public static boolean luhn(String digits) {
        if (digits == null || digits.isEmpty()) { return false; }
        int sum = 0;
        boolean doubleDigit = false;
        for (int index = digits.length() - 1; index >= 0; index--) {
            char current = digits.charAt(index);
            if (!Character.isDigit(current)) { return false; }
            int number = current - '0';
            if (doubleDigit) {
                number *= 2;
                if (number > 9) { number -= 9; }
            }
            sum += number;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }

    public static boolean isImei(CharSequence value) {
        return isLuhn(value, 15, 15);
    }

    public static boolean isIsbn(CharSequence value) {
        if (isBlank(value)) { return false; }
        String compact = value.toString().trim().toUpperCase(Locale.ROOT)
            .replace("-", "").replace(" ", "");
        if (compact.length() == 10) {
            int sum = 0;
            for (int index = 0; index < 10; index++) {
                char current = compact.charAt(index);
                int digit;
                if (index == 9 && current == 'X') { digit = 10; }
                else if (Character.isDigit(current)) { digit = current - '0'; }
                else { return false; }
                sum += digit * (10 - index);
            }
            return sum % 11 == 0;
        }
        if (compact.length() == 13) {
            int sum = 0;
            for (int index = 0; index < 13; index++) {
                char current = compact.charAt(index);
                if (!Character.isDigit(current)) { return false; }
                sum += (current - '0') * (index % 2 == 0 ? 1 : 3);
            }
            return sum % 10 == 0;
        }
        return false;
    }

    public static boolean isIban(CharSequence value) {
        if (isBlank(value)) { return false; }
        String iban = value.toString().replace(" ", "")
            .toUpperCase(Locale.ROOT);
        if (!IBAN.matcher(iban).matches()
            || !ISO_COUNTRIES.contains(iban.substring(0, 2))) { return false; }
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        int remainder = 0;
        for (int index = 0; index < rearranged.length(); index++) {
            char current = rearranged.charAt(index);
            if (Character.isDigit(current)) {
                remainder = (remainder * 10 + current - '0') % 97;
            } else if (current >= 'A' && current <= 'Z') {
                remainder = (remainder * 100 + current - 'A' + 10) % 97;
            } else { return false; }
        }
        return remainder == 1;
    }

    public static boolean isBic(CharSequence value) {
        return !isBlank(value) && BIC.matcher(
        value.toString().trim().toUpperCase(Locale.ROOT)).matches();
    }

    public static boolean isCurrencyCode(CharSequence value) {
        if (isBlank(value)) { return false; }
        String code = value.toString().trim().toUpperCase(Locale.ROOT);
        try { return code.length() == 3
                && Currency.getInstance(code).getCurrencyCode().equals(code); }
        catch (IllegalArgumentException exception) { return false; }
    }

    public static boolean isOrganizationCode(CharSequence value) {
        if (isBlank(value)) { return false; }
        String code = value.toString().trim().replace("-", "")
            .toUpperCase(Locale.ROOT);
        if (!ORGANIZATION_CODE.matcher(code).matches()) { return false; }
        int[] weights = {3, 7, 9, 10, 5, 8, 4, 2};
        int sum = 0;
        for (int index = 0; index < 8; index++) {
            char current = code.charAt(index);
            int mapped = Character.isDigit(current)
                ? current - '0' : current - 'A' + 10;
            sum += mapped * weights[index];
        }
        int check = 11 - sum % 11;
        char expected = check == 10 ? 'X' : check == 11 ? '0' : (char) ('0' + check);
        return code.charAt(8) == expected;
    }

    /** 仅验证公开可确认的常见中国护照号码结构，不替代权威证件库核验。 */
    public static boolean isChinesePassport(CharSequence value) {
        return !isBlank(value) && CHINESE_PASSPORT.matcher(
        value.toString().trim().toUpperCase(Locale.ROOT)).matches();
    }

    public static boolean isVin(CharSequence value) {
        if (isBlank(value)) { return false; }
        String vin = value.toString().trim().toUpperCase(Locale.ROOT);
        if (!VIN.matcher(vin).matches()) { return false; }
        int sum = 0;
        for (int index = 0; index < vin.length(); index++) {
            char current = vin.charAt(index);
            Integer translated = Character.isDigit(current)
                ? Integer.valueOf(current - '0') : VIN_VALUES.get(current);
            if (translated == null) { return false; }
            sum += translated.intValue() * VIN_WEIGHTS[index];
        }
        int remainder = sum % 11;
        char expected = remainder == 10 ? 'X' : (char) ('0' + remainder);
        return vin.charAt(8) == expected;
    }

    public static boolean isChinaLicensePlate(CharSequence value,
    boolean includeNewEnergy) {
        if (isBlank(value)) { return false; }
        String plate = value.toString().trim().toUpperCase(Locale.ROOT);
        return NORMAL_PLATE.matcher(plate).matches()
            || includeNewEnergy && NEW_ENERGY_PLATE.matcher(plate).matches();
    }

    public static boolean isChinaPostalCode(CharSequence value) {
        return !isBlank(value)
            && POSTAL_CODE.matcher(value.toString().trim()).matches();
    }

    public static boolean isPersonName(CharSequence value, NameType type,
    int minimum, int maximum) {
        if (isBlank(value) || minimum < 1 || maximum < minimum) { return false; }
        String text = value.toString().trim();
        int count = text.codePointCount(0, text.length());
        if (count < minimum || count > maximum) { return false; }
        NameType effective = type == null ? NameType.ANY : type;
        boolean hasLetter = false;
        for (int offset = 0; offset < text.length();) {
            int codePoint = text.codePointAt(offset);
            offset += Character.charCount(codePoint);
            if (isNameSeparator(codePoint)) { continue; }
            if (!Character.isLetter(codePoint)
                && Character.getType(codePoint) != Character.NON_SPACING_MARK
                && Character.getType(codePoint) != Character.COMBINING_SPACING_MARK) {
                return false;
            }
            hasLetter = true;
            boolean han = Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN;
            if (effective == NameType.CHINESE && !han) { return false; }
            if (effective == NameType.INTERNATIONAL && han) { return false; }
        }
        return hasLetter && !isNameSeparator(text.codePointAt(0))
            && !isNameSeparator(text.codePointBefore(text.length()));
    }

    public static boolean isUsername(CharSequence value, int minimum,
    int maximum) {
        if (isBlank(value)) { return false; }
        String text = value.toString();
        return text.length() >= minimum && text.length() <= maximum
            && USERNAME.matcher(text).matches() && !text.contains("..")
            && !text.contains("__") && !text.contains("--");
    }

    public static boolean isStrongPassword(CharSequence value,
    int minimum, int maximum, boolean requireUpper,
    boolean requireLower, boolean requireDigit,
    boolean requireSpecial, boolean rejectCommon,
    int maximumRepeated) {
        if (value == null) { return false; }
        String password = value.toString();
        int length = password.codePointCount(0, password.length());
        if (length < minimum || length > maximum || containsWhitespace(password)) {
            return false;
        }
        boolean upper = false, lower = false, digit = false, special = false;
        int repeated = 1, longestRepeated = 1;
        int previous = -1;
        for (int offset = 0; offset < password.length();) {
            int codePoint = password.codePointAt(offset);
            offset += Character.charCount(codePoint);
            upper |= Character.isUpperCase(codePoint);
            lower |= Character.isLowerCase(codePoint);
            digit |= Character.isDigit(codePoint);
            special |= !Character.isLetterOrDigit(codePoint);
            if (codePoint == previous) { repeated++; }
            else { repeated = 1; previous = codePoint; }
            longestRepeated = Math.max(longestRepeated, repeated);
        }
        if (requireUpper && !upper || requireLower && !lower
            || requireDigit && !digit || requireSpecial && !special) { return false; }
        if (maximumRepeated > 0 && longestRepeated > maximumRepeated) { return false; }
        String lowerPassword = password.toLowerCase(Locale.ROOT);
        return (!rejectCommon || !COMMON_PASSWORDS.contains(lowerPassword))
            && !containsLongSequence(lowerPassword, 5);
    }

    public static boolean isStrictEmail(CharSequence value) {
        if (isBlank(value)) { return false; }
        String email = value.toString().trim();
        if (email.length() > 254 || email.indexOf('@') <= 0
            || email.indexOf('@') != email.lastIndexOf('@')) { return false; }
        int at = email.indexOf('@');
        String local = email.substring(0, at);
        String domain = email.substring(at + 1);
        return local.length() <= 64 && EMAIL_LOCAL.matcher(local).matches()
            && isDomainName(domain);
    }

    public static boolean isIpAddress(CharSequence value, IpVersion version) {
        if (isBlank(value)) { return false; }
        String text = value.toString().trim();
        IpVersion effective = version == null ? IpVersion.ANY : version;
        if (effective == IpVersion.IPV4) { return isIpv4(text); }
        if (effective == IpVersion.IPV6) { return isIpv6(text); }
        return isIpv4(text) || isIpv6(text);
    }

    public static boolean isCidr(CharSequence value, IpVersion version) {
        if (isBlank(value)) { return false; }
        String[] parts = value.toString().trim().split("/", -1);
        if (parts.length != 2 || !parts[1].matches("\\d{1,3}")) { return false; }
        int prefix;
        try { prefix = Integer.parseInt(parts[1]); }
        catch (NumberFormatException exception) { return false; }
        boolean ipv4 = isIpv4(parts[0]);
        boolean ipv6 = isIpv6(parts[0]);
        IpVersion effective = version == null ? IpVersion.ANY : version;
        return (effective == IpVersion.ANY || effective == IpVersion.IPV4)
            && ipv4 && prefix <= 32
            || (effective == IpVersion.ANY || effective == IpVersion.IPV6)
            && ipv6 && prefix <= 128;
    }

    public static boolean isDomainName(CharSequence value) {
        if (isBlank(value)) { return false; }
        String domain = value.toString().trim();
        if (domain.endsWith(".")) { domain = domain.substring(0, domain.length() - 1); }
        if (domain.isEmpty() || domain.length() > 253) { return false; }
        final String ascii;
        try { ascii = IDN.toASCII(domain, IDN.USE_STD3_ASCII_RULES); }
        catch (IllegalArgumentException exception) { return false; }
        String[] labels = ascii.split("\\.", -1);
        if (labels.length < 2) { return false; }
        for (String label : labels) {
            if (!DOMAIN_LABEL.matcher(label).matches()) { return false; }
        }
        return !labels[labels.length - 1].matches("\\d+");
    }

    public static boolean isUrl(CharSequence value, String[] schemes,
    boolean requireHost) {
        if (isBlank(value)) { return false; }
        try {
            URI uri = new URI(value.toString().trim());
            if (uri.getScheme() == null || !allowedIgnoreCase(uri.getScheme(), schemes)) {
                return false;
            }
            if (uri.getUserInfo() != null) { return false; }
            if (!requireHost) { return true; }
            String host = uri.getHost();
            if (host == null) { host = hostFromAuthority(uri.getRawAuthority()); }
            if (host == null || host.isEmpty()) { return false; }
            return isIpv4(host) || isIpv6(stripIpv6Brackets(host)) || isDomainName(host);
        } catch (Exception exception) { return false; }
    }

    public static boolean isMacAddress(CharSequence value) {
        return !isBlank(value) && MAC.matcher(value.toString().trim()).matches();
    }

    public static boolean isUuid(CharSequence value, boolean allowCompact) {
        if (isBlank(value)) { return false; }
        String text = value.toString().trim();
        if (allowCompact && COMPACT_UUID.matcher(text).matches()) {
            text = text.substring(0, 8) + "-" + text.substring(8, 12) + "-"
            + text.substring(12, 16) + "-" + text.substring(16, 20)
            + "-" + text.substring(20);
        }
        try { return UUID.fromString(text).toString().equalsIgnoreCase(text); }
        catch (IllegalArgumentException exception) { return false; }
    }

    public static boolean isBase64(CharSequence value, boolean urlSafe) {
        if (isBlank(value)) { return false; }
        try {
            if (urlSafe) { Base64.getUrlDecoder().decode(value.toString()); }
            else { Base64.getDecoder().decode(value.toString()); }
            return true;
        } catch (IllegalArgumentException exception) { return false; }
    }

    public static boolean isHex(CharSequence value, boolean evenLength) {
        if (isBlank(value)) { return false; }
        String text = value.toString().trim();
        return (!evenLength || text.length() % 2 == 0) && HEX.matcher(text).matches();
    }

    public static boolean isLatitude(Object value) {
        return inRange(value, new BigDecimal("-90"), new BigDecimal("90"));
    }

    public static boolean isLongitude(Object value) {
        return inRange(value, new BigDecimal("-180"), new BigDecimal("180"));
    }

    public static boolean isPercentage(Object value, boolean includeMinimum,
    boolean includeMaximum) {
        BigDecimal number = decimal(value);
        if (number == null) { return false; }
        int lower = number.compareTo(BigDecimal.ZERO);
        int upper = number.compareTo(new BigDecimal("100"));
        return (includeMinimum ? lower >= 0 : lower > 0)
            && (includeMaximum ? upper <= 0 : upper < 0);
    }

    public static boolean isPort(Object value, int minimum, int maximum) {
        if (value == null || minimum < 0 || maximum > 65535 || minimum > maximum) {
            return false;
        }
        String text = value.toString().trim();
        if (!text.matches("\\d+")) { return false; }
        try {
            int port = Integer.parseInt(text);
            return port >= minimum && port <= maximum;
        } catch (NumberFormatException exception) { return false; }
    }

    public static boolean isMoney(Object value, String minimum, String maximum,
    int fraction, int precision,
    boolean allowNegative) {
        BigDecimal number = decimal(value);
        if (number == null || number.scale() > fraction || number.precision() > precision) {
            return false;
        }
        if (!allowNegative && number.signum() < 0) { return false; }
        BigDecimal min = decimal(minimum);
        BigDecimal max = decimal(maximum);
        return (min == null || number.compareTo(min) >= 0)
            && (max == null || number.compareTo(max) <= 0);
    }

    public static boolean isNumeric(CharSequence value, boolean integerOnly) {
        if (isBlank(value)) { return false; }
        return (integerOnly ? INTEGER : NUMERIC).matcher(value.toString().trim()).matches();
    }

    public static boolean matchesDatePattern(CharSequence value, String pattern) {
        return parseDate(value, pattern) != null;
    }

    public static boolean matchesTimePattern(CharSequence value, String pattern) {
        if (isBlank(value) || isBlank(pattern)) { return false; }
        try { LocalTime.parse(value.toString(), strictFormatter(pattern.toString())); return true; }
        catch (DateTimeException exception) { return false; }
    }

    public static boolean matchesDateTimePattern(CharSequence value, String pattern) {
        if (isBlank(value) || isBlank(pattern)) { return false; }
        try { LocalDateTime.parse(value.toString(), strictFormatter(pattern.toString())); return true; }
        catch (DateTimeException exception) { return false; }
    }

    public static boolean isBirthday(CharSequence value, String pattern,
    String zone, int minimumAge, int maximumAge, Clock clock) {
        LocalDate birthday = parseDate(value, pattern);
        if (birthday == null || minimumAge < 0 || maximumAge < minimumAge) { return false; }
        ZoneId zoneId;
        try { zoneId = isBlank(zone) ? effectiveClock(clock).getZone() : ZoneId.of(zone); }
        catch (DateTimeException exception) { return false; }
        LocalDate today = LocalDate.now(effectiveClock(clock).withZone(zoneId));
        if (birthday.isAfter(today)) { return false; }
        int age = Period.between(birthday, today).getYears();
        return age >= minimumAge && age <= maximumAge;
    }

    public static boolean isAge(Object value, int minimum, int maximum) {
        BigDecimal number = decimal(value);
        if (number == null || number.scale() > 0) { return false; }
        return number.compareTo(BigDecimal.valueOf(minimum)) >= 0
            && number.compareTo(BigDecimal.valueOf(maximum)) <= 0;
    }

    public static boolean isByteLength(CharSequence value, int minimum,
    int maximum) {
        if (value == null || minimum < 0 || maximum < minimum) { return false; }
        int length = value.toString().getBytes(StandardCharsets.UTF_8).length;
        return length >= minimum && length <= maximum;
    }

    public static boolean isCodePointLength(CharSequence value, int minimum,
    int maximum) {
        if (value == null || minimum < 0 || maximum < minimum) { return false; }
        String text = value.toString();
        int length = text.codePointCount(0, text.length());
        return length >= minimum && length <= maximum;
    }

    public static boolean containsNoWhitespace(CharSequence value) {
        return value != null && !containsWhitespace(value.toString());
    }

    public static boolean isTrimmed(CharSequence value) {
        return value != null && value.toString().equals(value.toString().trim());
    }

    public static boolean isAllowed(Object value, String[] candidates,
    boolean ignoreCase) {
        if (value == null || candidates == null) { return false; }
        String text = value.toString();
        for (String candidate : candidates) {
            if (candidate != null && (ignoreCase
                ? candidate.equalsIgnoreCase(text) : candidate.equals(text))) { return true; }
        }
        return false;
    }

    public static boolean isForbidden(Object value, String[] candidates,
    boolean ignoreCase) {
        return value != null && !isAllowed(value, candidates, ignoreCase);
    }

    public static boolean isEnumValue(Object value, Class<? extends Enum<?>> type,
    boolean ignoreCase) {
        if (value == null || type == null) { return false; }
        String text = value.toString();
        for (Enum<?> constant : type.getEnumConstants()) {
            if (ignoreCase ? constant.name().equalsIgnoreCase(text)
                : constant.name().equals(text)) { return true; }
        }
        return false;
    }

    public static boolean hasUniqueElements(Collection<?> values,
    boolean ignoreNull) {
        if (values == null) { return false; }
        Set<Object> seen = new HashSet<Object>();
        for (Object value : values) {
            if (value == null && ignoreNull) { continue; }
            if (!seen.add(value)) { return false; }
        }
        return true;
    }

    public static boolean hasNoNullElements(Iterable<?> values) {
        if (values == null) { return false; }
        for (Object value : values) { if (value == null) { return false; } }
        return true;
    }

    public static boolean isFileExtension(CharSequence value,
    String[] extensions) {
        if (isBlank(value) || extensions == null) { return false; }
        String text = value.toString().trim();
        int index = text.lastIndexOf('.');
        if (index < 0 || index == text.length() - 1) { return false; }
        String extension = text.substring(index + 1);
        return allowedIgnoreCase(extension, extensions);
    }

    public static boolean isMimeType(CharSequence value, String[] allowed) {
        if (isBlank(value) || allowed == null) { return false; }
        String mime = value.toString().trim().toLowerCase(Locale.ROOT);
        if (!MIME_TYPE.matcher(mime).matches()) { return false; }
        for (String candidate : allowed) {
            if (candidate == null) { continue; }
            String normalized = candidate.trim().toLowerCase(Locale.ROOT);
            if (normalized.endsWith("/*")) {
                if (mime.startsWith(normalized.substring(0, normalized.length() - 1))) { return true; }
            } else if (normalized.equals(mime)) { return true; }
        }
        return false;
    }

    public static boolean isChineseCharacters(CharSequence value,
    boolean allowWhitespace, boolean allowDigits,
    boolean allowPunctuation) {
        return characterPolicy(value, true, false, allowWhitespace,
        allowDigits, allowPunctuation);
    }

    public static boolean isEnglishCharacters(CharSequence value,
    boolean allowWhitespace, boolean allowDigits,
    boolean allowPunctuation) {
        return characterPolicy(value, false, true, allowWhitespace,
        allowDigits, allowPunctuation);
    }

    public static boolean isChineseOrEnglish(CharSequence value,
    boolean allowWhitespace, boolean allowDigits,
    boolean allowPunctuation) {
        return characterPolicy(value, true, true, allowWhitespace,
        allowDigits, allowPunctuation);
    }

    public static boolean isAdministrativeDivisionCode(CharSequence value,
    AdministrativeDivisionLevel level) {
        return isAdministrativeDivisionCode(value, level, DEFAULT_DIVISIONS);
    }

    public static boolean isAdministrativeDivisionCode(CharSequence value,
    AdministrativeDivisionLevel level, AdministrativeDivisionProvider provider) {
        if (isBlank(value)) { return false; }
        String code = value.toString().trim();
        AdministrativeDivisionLevel effective = level == null
            ? AdministrativeDivisionLevel.AUTO : level;
        AdministrativeDivisionProvider source = provider == null
            ? DEFAULT_DIVISIONS : provider;
        return source.exists(code, effective, LocalDate.now());
    }

    private static String compactPhone(String value) {
        return value.trim().replace(" ", "").replace("-", "")
            .replace("(", "").replace(")", "");
    }

    private static String normalizeLandline(String value) {
        return value.trim().replace("转", "-").replaceAll("(?i)ext\\.?", "-")
            .replaceAll("(?i)x(?=\\s*\\d+\\s*$)", "-")
            .replace(" ", "").replace("(", "").replace(")", "");
    }

    private static String compactDigits(String value) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (Character.isDigit(current)) { result.append(current); }
            else if (!Character.isWhitespace(current) && current != '-') { return null; }
        }
        return result.toString();
    }

    private static boolean isNameSeparator(int codePoint) {
        return codePoint == ' ' || codePoint == '-' || codePoint == '\''
            || codePoint == '.' || codePoint == '·' || codePoint == '•';
    }

    private static boolean containsWhitespace(String value) {
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            if (Character.isWhitespace(codePoint)) { return true; }
            offset += Character.charCount(codePoint);
        }
        return false;
    }

    private static boolean containsLongSequence(String value, int threshold) {
        int ascending = 1, descending = 1;
        for (int index = 1; index < value.length(); index++) {
            int difference = value.charAt(index) - value.charAt(index - 1);
            ascending = difference == 1 ? ascending + 1 : 1;
            descending = difference == -1 ? descending + 1 : 1;
            if (ascending >= threshold || descending >= threshold) { return true; }
        }
        return false;
    }

    private static boolean isIpv4(String value) {
        String[] parts = value.split("\\.", -1);
        if (parts.length != 4) { return false; }
        for (String part : parts) {
            if (!part.matches("\\d{1,3}") || part.length() > 1 && part.startsWith("0")) { return false; }
            int number = Integer.parseInt(part);
            if (number > 255) { return false; }
        }
        return true;
    }

    private static boolean isIpv6(String value) {
        if (value == null || !value.contains(":")) { return false; }
        try { return InetAddress.getByName(stripIpv6Brackets(value)) instanceof Inet6Address; }
        catch (Exception exception) { return false; }
    }

    private static String stripIpv6Brackets(String value) {
        return value != null && value.startsWith("[") && value.endsWith("]")
            ? value.substring(1, value.length() - 1) : value;
    }

    private static String hostFromAuthority(String authority) {
        if (authority == null) { return null; }
        String value = authority;
        int at = value.lastIndexOf('@');
        if (at >= 0) { return null; }
        if (value.startsWith("[")) {
            int end = value.indexOf(']');
            return end > 0 ? value.substring(0, end + 1) : null;
        }
        int colon = value.lastIndexOf(':');
        return colon >= 0 ? value.substring(0, colon) : value;
    }

    private static boolean inRange(Object value, BigDecimal minimum,
    BigDecimal maximum) {
        BigDecimal number = decimal(value);
        return number != null && number.compareTo(minimum) >= 0
            && number.compareTo(maximum) <= 0;
    }

    private static BigDecimal decimal(Object value) {
        if (value == null) { return null; }
        String text = value.toString().trim();
        if (text.isEmpty()) { return null; }
        try { return new BigDecimal(text); }
        catch (NumberFormatException exception) { return null; }
    }

    private static LocalDate parseDate(CharSequence value, String pattern) {
        if (isBlank(value) || isBlank(pattern)) { return null; }
        try { return LocalDate.parse(value.toString(), strictFormatter(pattern)); }
        catch (DateTimeException exception) { return null; }
    }

    private static DateTimeFormatter strictFormatter(String pattern) {
        String normalized = pattern.replace("yyyy", "uuuu");
        return DateTimeFormatter.ofPattern(normalized).withResolverStyle(ResolverStyle.STRICT);
    }

    private static Clock effectiveClock(Clock clock) {
        return clock == null ? Clock.systemDefaultZone() : clock;
    }

    private static boolean allowedIgnoreCase(String value, String[] allowed) {
        if (allowed == null || allowed.length == 0) { return false; }
        for (String candidate : allowed) {
            if (candidate != null && candidate.equalsIgnoreCase(value)) { return true; }
        }
        return false;
    }

    private static boolean characterPolicy(CharSequence value,
    boolean allowHan, boolean allowLatin, boolean allowWhitespace,
    boolean allowDigits, boolean allowPunctuation) {
        if (isBlank(value)) { return false; }
        String text = value.toString();
        boolean hasLetter = false;
        for (int offset = 0; offset < text.length();) {
            int codePoint = text.codePointAt(offset);
            offset += Character.charCount(codePoint);
            if (Character.isWhitespace(codePoint)) {
                if (!allowWhitespace) { return false; }
                continue;
            }
            if (Character.isDigit(codePoint)) {
                if (!allowDigits) { return false; }
                continue;
            }
            if (Character.isLetter(codePoint)) {
                Character.UnicodeScript script = Character.UnicodeScript.of(codePoint);
                if (script == Character.UnicodeScript.HAN && allowHan
                    || (script == Character.UnicodeScript.LATIN
                    || script == Character.UnicodeScript.COMMON) && allowLatin) {
                    hasLetter = true;
                    continue;
                }
                return false;
            }
            int type = Character.getType(codePoint);
            boolean punctuation = type == Character.CONNECTOR_PUNCTUATION
                || type == Character.DASH_PUNCTUATION
                || type == Character.START_PUNCTUATION
                || type == Character.END_PUNCTUATION
                || type == Character.INITIAL_QUOTE_PUNCTUATION
                || type == Character.FINAL_QUOTE_PUNCTUATION
                || type == Character.OTHER_PUNCTUATION;
            if (!allowPunctuation || !punctuation) { return false; }
        }
        return hasLetter;
    }
}
