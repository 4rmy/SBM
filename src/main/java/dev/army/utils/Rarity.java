package dev.army.utils;

public enum Rarity {
    Uncommon, // green
    Common, // white
    Rare, // blue
    Epic, // purple
    Legendary, // gold
    Mythic, // pink
    Divine, // teal
    Special, // Red
    VerySpecial, // Red

    // Special Rarities
    Ultimate, // Darkred
    Admin; // Darkred

    public int color() {
        return switch (this) {
            case Common -> 0xFFFFFF;
            case Uncommon -> 0x55FF55;
            case Rare -> 0x5555FF;
            case Epic -> 0xAA00AA;
            case Legendary -> 0xFFAA00;
            case Mythic -> 0xFF55FF;
            case Divine -> 0x55FFFF;
            case Special, VerySpecial -> 0xFF5555;
            case Ultimate, Admin -> 0xAA0000;
        };
    }
}
