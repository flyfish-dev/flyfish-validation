package dev.flyfish.validation.build;

import dev.flyfish.validation.api.CompositeValidationLifecycleListener;
import dev.flyfish.validation.api.ValidationError;
import dev.flyfish.validation.api.ValidationInvocation;
import dev.flyfish.validation.api.ValidationLifecycleListener;
import dev.flyfish.validation.api.ValidationReport;
import dev.flyfish.validation.business.AsyncBusinessValidator;
import dev.flyfish.validation.business.BusinessValidationContext;
import dev.flyfish.validation.business.BusinessValidationExecutor;
import dev.flyfish.validation.business.BusinessValidationOptions;
import dev.flyfish.validation.business.BusinessValidator;
import dev.flyfish.validation.business.DefaultBusinessValidatorRegistry;
import dev.flyfish.validation.business.DuplicateBusinessValidatorException;
import dev.flyfish.validation.model.ComparisonOperator;
import dev.flyfish.validation.model.IpVersion;
import dev.flyfish.validation.model.NameType;
import dev.flyfish.validation.model.PhoneType;
import dev.flyfish.validation.region.AdministrativeDivisionLevel;
import dev.flyfish.validation.support.BeanPropertyAccess;
import dev.flyfish.validation.support.ChineseIdCardInfo;
import dev.flyfish.validation.support.ChineseIdCardParser;
import dev.flyfish.validation.support.ComparisonSupport;
import dev.flyfish.validation.support.StandardChecks;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/** 无第三方依赖的核心算法、SPI 和并排兼容冒烟测试。 */
public final class CoreSelfTest {
    private static int assertions;
    private CoreSelfTest() { }

    public static void main(String[] args) {
        phonesAndIdentity();
        financeAndCodes();
        networkAndText();
        datesAndNumbers();
        propertyAndComparison();
        businessRules();
        lifecycle();
        System.out.println("CoreSelfTest passed: " + assertions + " assertions");
    }

    private static void phonesAndIdentity() {
        yes(StandardChecks.isPhone("+86 138-0013-8000", PhoneType.CHINA_MOBILE));
        yes(StandardChecks.isPhone("010-12345678 转 123", PhoneType.CHINA_LANDLINE));
        yes(StandardChecks.isPhone("+14155552671", PhoneType.E164));
        no(StandardChecks.isPhone("12345", PhoneType.ANY));
        Clock clock = fixedClock();
        Optional<ChineseIdCardInfo> parsed = ChineseIdCardParser.parse(
                "11010519491231002X", false, clock);
        yes(parsed.isPresent());
        equal(LocalDate.of(1949, 12, 31), parsed.get().getBirthday());
        equal(Integer.valueOf(76), Integer.valueOf(parsed.get().getAge()));
        no(ChineseIdCardParser.parse("110105194912310021", false, clock).isPresent());
        yes(StandardChecks.isUnifiedSocialCreditCode("91350211M000100Y46"));
        no(StandardChecks.isUnifiedSocialCreditCode("91350211M000100Y43"));
        yes(StandardChecks.isOrganizationCode("A1234567-4"));
        no(StandardChecks.isOrganizationCode("A1234567-5"));
        yes(StandardChecks.isChinesePassport("E12345678"));
        no(StandardChecks.isChinesePassport("E1234567"));
        yes(StandardChecks.isChinaPostalCode("030000"));
        yes(StandardChecks.isAdministrativeDivisionCode(
                "140100", AdministrativeDivisionLevel.COUNTY));
        no(StandardChecks.isAdministrativeDivisionCode(
                "990100", AdministrativeDivisionLevel.COUNTY));
    }

    private static void financeAndCodes() {
        yes(StandardChecks.isBankCard("4111 1111 1111 1111"));
        no(StandardChecks.isBankCard("4111 1111 1111 1112"));
        yes(StandardChecks.isImei("490154203237518"));
        no(StandardChecks.isImei("490154203237519"));
        yes(StandardChecks.isIsbn("0-306-40615-2"));
        yes(StandardChecks.isIsbn("9780306406157"));
        no(StandardChecks.isIsbn("9780306406158"));
        yes(StandardChecks.isIban("GB82 WEST 1234 5698 7654 32"));
        no(StandardChecks.isIban("GB82 WEST 1234 5698 7654 31"));
        yes(StandardChecks.isBic("DEUTDEFF"));
        yes(StandardChecks.isBic("DEUTDEFF500"));
        no(StandardChecks.isBic("DEUT-DEFF"));
        yes(StandardChecks.isCurrencyCode("CNY"));
        no(StandardChecks.isCurrencyCode("ZZZ"));
        yes(StandardChecks.isVin("1HGCM82633A004352"));
        no(StandardChecks.isVin("1HGCM82633A004353"));
        yes(StandardChecks.isChinaLicensePlate("晋A12345", true));
        yes(StandardChecks.isChinaLicensePlate("晋AD12345", true));
        yes(StandardChecks.isMoney("9999.99", "0", "10000", 2, 10, false));
        no(StandardChecks.isMoney("1.001", "0", "10000", 2, 10, false));
        yes(StandardChecks.isPercentage("0", true, true));
        yes(StandardChecks.isPercentage("100", true, true));
        no(StandardChecks.isPercentage("100.01", true, true));
    }

    private static void networkAndText() {
        yes(StandardChecks.isIpAddress("192.168.1.1", IpVersion.IPV4));
        no(StandardChecks.isIpAddress("192.168.01.1", IpVersion.IPV4));
        yes(StandardChecks.isIpAddress("2001:db8::1", IpVersion.IPV6));
        yes(StandardChecks.isCidr("10.0.0.0/8", IpVersion.IPV4));
        no(StandardChecks.isCidr("10.0.0.0/33", IpVersion.IPV4));
        yes(StandardChecks.isDomainName("例子.中国"));
        no(StandardChecks.isDomainName("-invalid.example"));
        yes(StandardChecks.isUrl("https://flyfish.dev/docs",
                new String[]{"https"}, true));
        yes(StandardChecks.isUrl("https://例子.中国/路径",
                new String[]{"https"}, true));
        no(StandardChecks.isUrl("javascript:alert(1)",
                new String[]{"http", "https"}, true));
        yes(StandardChecks.isStrictEmail("user@example.com"));
        yes(StandardChecks.isStrictEmail("user@例子.中国"));
        no(StandardChecks.isStrictEmail("user..name@example.com"));
        yes(StandardChecks.isMacAddress("00:1A:2B:3C:4D:5E"));
        yes(StandardChecks.isUuid("550e8400-e29b-41d4-a716-446655440000", false));
        yes(StandardChecks.isBase64("Zmx5ZmlzaA==", false));
        yes(StandardChecks.isHex("0A10ff", true));
        yes(StandardChecks.isPersonName("王瑜", NameType.CHINESE, 2, 32));
        yes(StandardChecks.isPersonName("François Dupont",
                NameType.INTERNATIONAL, 2, 64));
        no(StandardChecks.isPersonName("王_瑜", NameType.CHINESE, 2, 32));
        yes(StandardChecks.isUsername("flyfish.dev", 3, 32));
        no(StandardChecks.isUsername("flyfish..dev", 3, 32));
        yes(StandardChecks.isStrongPassword("Flyfish@2026", 8, 128,
                true, true, true, true, true, 3));
        no(StandardChecks.isStrongPassword("password", 8, 128,
                true, true, true, true, true, 3));
        yes(StandardChecks.isByteLength("飞鱼", 6, 6));
        yes(StandardChecks.isCodePointLength("飞鱼😀", 3, 3));
        yes(StandardChecks.containsNoWhitespace("flyfish"));
        no(StandardChecks.containsNoWhitespace("fly fish"));
        yes(StandardChecks.isTrimmed("flyfish"));
        no(StandardChecks.isTrimmed(" flyfish"));
        yes(StandardChecks.isChineseCharacters("飞鱼，开源", true, false, true));
        yes(StandardChecks.isEnglishCharacters("Flyfish Open-Source", true, false, true));
        yes(StandardChecks.isChineseOrEnglish("Flyfish 飞鱼", true, false, true));
        yes(StandardChecks.isFileExtension("report.PDF", new String[]{"pdf", "docx"}));
        yes(StandardChecks.isMimeType("image/png", new String[]{"image/*"}));
    }

    private static void datesAndNumbers() {
        yes(StandardChecks.matchesDatePattern("2024-02-29", "yyyy-MM-dd"));
        no(StandardChecks.matchesDatePattern("2023-02-29", "yyyy-MM-dd"));
        yes(StandardChecks.matchesTimePattern("23:59:59", "HH:mm:ss"));
        no(StandardChecks.matchesTimePattern("24:00:00", "HH:mm:ss"));
        yes(StandardChecks.matchesDateTimePattern(
                "2024-02-29 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        no(StandardChecks.matchesDateTimePattern(
                "2023-02-29 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        yes(StandardChecks.isBirthday("2000-07-29", "yyyy-MM-dd", "UTC",
                26, 26, fixedClock()));
        no(StandardChecks.isBirthday("2000-07-30", "yyyy-MM-dd", "UTC",
                26, 26, fixedClock()));
        yes(StandardChecks.isAge(Integer.valueOf(18), 18, 150));
        no(StandardChecks.isAge(new BigDecimal("18.5"), 18, 150));
        yes(StandardChecks.isPort(Integer.valueOf(443), 1, 65535));
        no(StandardChecks.isPort("443.5", 1, 65535));
        no(StandardChecks.isPort(Integer.valueOf(65536), 1, 65535));
        yes(StandardChecks.isLatitude("39.9042"));
        no(StandardChecks.isLatitude("90.0001"));
        yes(StandardChecks.isLongitude(new BigDecimal("116.4074")));
    }

    private static void propertyAndComparison() {
        Account account = new Account();
        account.profile = new Profile();
        account.profile.age = 30;
        account.confirmedAge = 30;
        account.tags = Arrays.asList("open", "source");
        account.metadata = new LinkedHashMap<String, Object>();
        account.metadata.put("tenant", "flyfish");
        yes(BeanPropertyAccess.read(account, "profile.age").isPresent());
        equal(Integer.valueOf(30), BeanPropertyAccess.read(
                account, "profile.age").getValue());
        equal("source", BeanPropertyAccess.read(account, "tags.1").getValue());
        equal("flyfish", BeanPropertyAccess.read(
                account, "metadata.tenant").getValue());
        no(BeanPropertyAccess.read(account, "profile.missing").isPresent());
        yes(ComparisonSupport.test(account.profile.age, account.confirmedAge,
                ComparisonOperator.EQUAL, "", ZoneId.of("UTC")));
        yes(ComparisonSupport.test("2026-01-01", "2026-12-31",
                ComparisonOperator.LESS_THAN, "yyyy-MM-dd", ZoneId.of("UTC")));
    }

    private static void businessRules() {
        final AtomicInteger secondCalls = new AtomicInteger();
        BusinessValidator<String> unique = new BusinessValidator<String>() {
            @Override public String key() { return "username-unique"; }
            @Override public Class<String> targetType() { return String.class; }
            @Override public ValidationReport validate(String value,
                    BusinessValidationContext context) {
                equal("tenant-a", context.attribute("tenant"));
                return "used".equals(value)
                        ? ValidationReport.invalid(ValidationError.builder(
                                "user.username.taken", "用户名已存在")
                                .propertyPath("username").build())
                        : ValidationReport.valid();
            }
        };
        BusinessValidator<String> second = new BusinessValidator<String>() {
            @Override public String key() { return "second"; }
            @Override public Class<String> targetType() { return String.class; }
            @Override public ValidationReport validate(String value,
                    BusinessValidationContext context) {
                secondCalls.incrementAndGet();
                return ValidationReport.valid();
            }
        };
        AsyncBusinessValidator<String> async = new AsyncBusinessValidator<String>() {
            @Override public String key() { return "remote-risk"; }
            @Override public Class<String> targetType() { return String.class; }
            @Override public java.util.concurrent.CompletionStage<ValidationReport>
                    validateAsync(String value, BusinessValidationContext context) {
                return CompletableFuture.completedFuture("blocked".equals(value)
                        ? ValidationReport.invalid(ValidationError.builder(
                                "risk.rejected", "远程风控拒绝").build())
                        : ValidationReport.valid());
            }
        };
        DefaultBusinessValidatorRegistry registry =
                new DefaultBusinessValidatorRegistry(
                        Arrays.<BusinessValidator<?>>asList(unique, second),
                        Collections.<AsyncBusinessValidator<?>>singletonList(async));
        BusinessValidationExecutor executor = new BusinessValidationExecutor(registry);
        BusinessValidationOptions options = BusinessValidationOptions.builder()
                .failFast(true).locale(Locale.CHINA)
                .attribute("tenant", "tenant-a").build();
        ValidationReport rejected = executor.validate("used",
                Arrays.asList("username-unique", "second"), options);
        no(rejected.isValid());
        equal(Integer.valueOf(0), Integer.valueOf(secondCalls.get()));
        yes(executor.validate("new", Collections.singletonList(
                "username-unique"), options).isValid());
        no(executor.validate("value", "missing-rule").isValid());
        yes(executor.validate("value", Collections.singletonList("missing-rule"),
                BusinessValidationOptions.builder().failOnMissingRule(false).build()).isValid());
        no(executor.validateAsync("blocked", "remote-risk")
                .toCompletableFuture().join().isValid());
        yes(executor.validateAsync("allowed", "remote-risk")
                .toCompletableFuture().join().isValid());
        boolean duplicate = false;
        try {
            new DefaultBusinessValidatorRegistry(
                    Arrays.<BusinessValidator<?>>asList(unique, unique),
                    Collections.<AsyncBusinessValidator<?>>emptyList());
        } catch (DuplicateBusinessValidatorException expected) {
            duplicate = true;
        }
        yes(duplicate);
    }

    private static void lifecycle() {
        final StringBuilder calls = new StringBuilder();
        ValidationLifecycleListener later = new ValidationLifecycleListener() {
            @Override public int order() { return 10; }
            @Override public void beforeValidation(ValidationInvocation value) { calls.append('B'); }
            @Override public void afterSuccess(ValidationInvocation value,
                    ValidationReport report, long elapsed) { calls.append('S'); }
        };
        ValidationLifecycleListener earlier = new ValidationLifecycleListener() {
            @Override public int order() { return -10; }
            @Override public void beforeValidation(ValidationInvocation value) { calls.append('A'); }
            @Override public void afterSuccess(ValidationInvocation value,
                    ValidationReport report, long elapsed) { calls.append('R'); }
        };
        CompositeValidationLifecycleListener composite =
                new CompositeValidationLifecycleListener(
                        Arrays.asList(later, earlier), false);
        ValidationInvocation invocation = new ValidationInvocation(
                "target", "test", Collections.<String,Object>emptyMap());
        composite.beforeValidation(invocation);
        composite.afterSuccess(invocation, ValidationReport.valid(), 1L);
        equal("ABRS", calls.toString());
    }

    private static Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-07-29T00:00:00Z"), ZoneId.of("UTC"));
    }
    private static void yes(boolean value) {
        assertions++;
        if (!value) { throw new AssertionError("expected true at assertion " + assertions); }
    }
    private static void no(boolean value) { yes(!value); }
    private static void equal(Object expected, Object actual) {
        assertions++;
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("expected " + expected + " but was " + actual
                    + " at assertion " + assertions);
        }
    }
    private static final class Account {
        private Profile profile;
        private int confirmedAge;
        private java.util.List<String> tags;
        private Map<String,Object> metadata;
    }
    private static final class Profile { private int age; }
}
