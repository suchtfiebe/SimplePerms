package de.derfirefox.simplePerms;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SimplePermsPlaceholder extends PlaceholderExpansion {

    private final PermissionManager pm;

    public SimplePermsPlaceholder(PermissionManager pm) {
        this.pm = pm;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simple"; // Platzhalter: %simple_...%
    }

    @Override
    public @NotNull String getAuthor() {
        return "DerFirefox";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // bleibt registriert
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        switch (identifier.toLowerCase()) {
            case "prefix":
                return pm.getChatPrefix(player); // nutzt deine bestehende Logik
            case "name":
                return player.getName();
            case "format":
                return pm.getChatFormat(player); // Prefix + Name
            default:
                return null; // unbekannter Placeholder
        }
    }

    public void register() {
        // registriert die Expansion bei PlaceholderAPI
        if (!super.isRegistered()) {
            super.register();
        }
    }
}
