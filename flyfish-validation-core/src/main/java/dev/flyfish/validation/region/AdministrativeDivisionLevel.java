package dev.flyfish.validation.region;

/** 中国行政区划代码层级。 */
public enum AdministrativeDivisionLevel {
    AUTO(0), PROVINCE(2), PREFECTURE(4), COUNTY(6), TOWNSHIP(9), VILLAGE(12);
    private final int length;
    AdministrativeDivisionLevel(int length) { this.length = length; }
    public int getLength() { return length; }
    public static AdministrativeDivisionLevel fromLength(int length) {
        for (AdministrativeDivisionLevel value : values()) {
            if (value.length == length) { return value; }
        }
        return AUTO;
    }
}
