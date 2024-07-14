/*
 * Decompiled with CFR 0.151.
 */
package me.rok.main.enums;

public enum Message {
    NO_PLAYER("\ufffd&Je hebt niet de correcte permissions om dit uit te voeren, Wat jammer!"),
    PICKUP_OFF("&7[&5&lFKD&7] &7Je kunt nu weer items oppakken!");

    private final String message;

    private Message(String message) {
        this.message = message;
    }

    public String get() {
        return this.message;
    }
}

