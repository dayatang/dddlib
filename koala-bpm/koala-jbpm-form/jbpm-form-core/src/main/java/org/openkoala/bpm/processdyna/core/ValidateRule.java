package org.openkoala.bpm.processdyna.core;

public enum ValidateRule {

	English("英文字符"),Chinese("中文字符"),Number("数字"),Email("邮箱"), Mobile("手机号"),Regex("自定义规则");
    private final String text;

    public String getText() {
        return text;
    }

    private ValidateRule(String text) {
        this.text = text;
    }

    public static ValidateRule name2Enum(String name) {
        for (ValidateRule rule : ValidateRule.values()) {
            if (rule.name().equalsIgnoreCase(name)) {
                return rule;
            }
        }
        return null;
    }
}
