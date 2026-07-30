package dev.flyfish.validation.support;

import java.time.LocalDate;

/** 已完成校验的中国居民身份证结构化信息。 */
public final class ChineseIdCardInfo {
    private final String normalizedNumber;
    private final LocalDate birthday;
    private final int age;
    private final String administrativeDivisionCode;
    private final boolean male;
    public ChineseIdCardInfo(String normalizedNumber, LocalDate birthday,
    int age, String administrativeDivisionCode, boolean male) {
        this.normalizedNumber = normalizedNumber;
        this.birthday = birthday;
        this.age = age;
        this.administrativeDivisionCode = administrativeDivisionCode;
        this.male = male;
    }
    public String getNormalizedNumber() { return normalizedNumber; }
    public LocalDate getBirthday() { return birthday; }
    public int getAge() { return age; }
    public String getAdministrativeDivisionCode() { return administrativeDivisionCode; }
    public boolean isMale() { return male; }
}
