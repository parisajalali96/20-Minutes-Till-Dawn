package io.github.parisajalali96.Models.Enums;

public enum Weapon {
    AXE("Axe"),
    SWORD("Sword");
    private final String name;
    Weapon (String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
