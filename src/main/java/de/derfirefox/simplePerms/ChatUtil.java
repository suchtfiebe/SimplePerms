package de.derfirefox.simplePerms;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utility-Klasse für Farb-Codes und formatierten Spielernamen.
 */
public class ChatUtil {

    /**
     * Wandelt &-Farbcodes in §-Farbcodes um.
     * @param input String mit & Farbcode
     * @return String mit § Farbcode
     */
    public static String translateColorCodes(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * Gibt den formatierten Spielernamen zurück mit Prefix.
     * @param player Spieler
     * @param prefix Präfix, z.B. Gruppenpräfix
     * @return formatierter Name
     */
    public static String getFormattedName(Player player, String prefix) {
        prefix = translateColorCodes(prefix);

        if (player == null) return prefix;
        return prefix + player.getName();
    }
}
