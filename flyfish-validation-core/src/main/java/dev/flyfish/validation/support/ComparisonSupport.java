package dev.flyfish.validation.support;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import dev.flyfish.validation.model.ComparisonOperator;

/** 跨字段数值、日期和 {@link Comparable} 值的统一比较工具。 */
public final class ComparisonSupport {

    private ComparisonSupport() {
    }

    public static boolean test(
            Object left, Object right, ComparisonOperator operator,
            String pattern, ZoneId zoneId) {
        ComparisonOperator effective = operator == null
                ? ComparisonOperator.EQUAL : operator;
        if (effective == ComparisonOperator.EQUAL) {
            return left == null ? right == null : left.equals(right);
        }
        if (effective == ComparisonOperator.NOT_EQUAL) {
            return left == null ? right != null : !left.equals(right);
        }
        if (left == null || right == null) {
            return false;
        }
        int result = compare(left, right, pattern, zoneId);
        switch (effective) {
            case LESS_THAN:
                return result < 0;
            case LESS_THAN_OR_EQUAL:
                return result <= 0;
            case GREATER_THAN:
                return result > 0;
            case GREATER_THAN_OR_EQUAL:
                return result >= 0;
            default:
                return false;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static int compare(
            Object left, Object right, String pattern, ZoneId zoneId) {
        if (left instanceof Number || right instanceof Number) {
            return new BigDecimal(left.toString())
                    .compareTo(new BigDecimal(right.toString()));
        }
        if (left instanceof LocalDate && right instanceof LocalDate) {
            return ((LocalDate) left).compareTo((LocalDate) right);
        }
        if (left instanceof LocalDateTime && right instanceof LocalDateTime) {
            return ((LocalDateTime) left)
                    .compareTo((LocalDateTime) right);
        }
        if (left instanceof CharSequence && right instanceof CharSequence
                && pattern != null && !pattern.trim().isEmpty()) {
            return compareTemporalText(
                    left.toString(), right.toString(), pattern, zoneId);
        }
        if (left instanceof Comparable && left.getClass().isInstance(right)) {
            return ((Comparable) left).compareTo(right);
        }
        return left.toString().compareTo(right.toString());
    }

    private static int compareTemporalText(
            String left, String right, String pattern, ZoneId zoneId) {
        // zoneId 作为稳定扩展参数保留，当前 LocalDate/LocalDateTime 比较不需要换区。
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (pattern.contains("H") || pattern.contains("m")
                || pattern.contains("s")) {
            return LocalDateTime.parse(left, formatter)
                    .compareTo(LocalDateTime.parse(right, formatter));
        }
        return LocalDate.parse(left, formatter)
                .compareTo(LocalDate.parse(right, formatter));
    }
}
