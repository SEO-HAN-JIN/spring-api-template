package com.framework.template.domain.member.constant;

public enum Role {
    USER("유저"), ADMIN("관리자");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}