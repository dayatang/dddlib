package org.openkoala.bpm.processdyna.core;

public enum FieldType {

    String("字符串"), Boolean("布尔型"),Date("日期"), Long("整数型"), Float("浮点型");
    private final String text;

    public String getText() {
        return text;
    }

    private FieldType(String text) {
        this.text = text;
    }

    public static FieldType name2Enum(String name) {
        for (FieldType rule : FieldType.values()) {
            if (rule.name().equalsIgnoreCase(name)) {
                return rule;
            }
        }
        return null;
    }
}
