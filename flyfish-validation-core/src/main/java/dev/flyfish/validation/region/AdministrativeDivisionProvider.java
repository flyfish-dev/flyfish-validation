package dev.flyfish.validation.region;

import java.time.LocalDate;

/** 可替换的行政区划主数据来源。 */
public interface AdministrativeDivisionProvider {
    boolean exists(String code, AdministrativeDivisionLevel level,
    LocalDate effectiveDate);
}
