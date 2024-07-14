/*
 * Decompiled with CFR 0.151.
 */
package me.rok.main.enums;

public enum Permission {
    BROADCAST("core.command.broadcast"),
    VANISH_US2E("core.command.broadcast");

    private final String permission;

    private Permission(String permission) {
        this.permission = permission;
    }

    public String get() {
        return this.permission;
    }
}

