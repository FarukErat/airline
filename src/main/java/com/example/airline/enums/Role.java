package com.example.airline.enums;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN(0),
    USER(1),
    MODERATOR(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        return null;
    }

    public static List<Role> getRoles(int bitmask) {
        List<Role> roles = new ArrayList<>();
        for (Role role : Role.values()) {
            if ((bitmask & (1 << role.getValue())) != 0) {
                roles.add(role);
            }
        }
        return roles;
    }
}
