package de.derfirefox.simplePerms;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import java.util.HashMap;
import java.util.Map;

public class GlobalPermissionManager {

    private final Map<Player, PermissionAttachment> attachments = new HashMap<>();

    public void registerPlayer(Player player) {
        if (!attachments.containsKey(player)) {
            PermissionAttachment attachment = player.addAttachment(null); // Plugin wird später gesetzt
            attachments.put(player, attachment);
        }
    }

    public void unregisterPlayer(Player player) {
        PermissionAttachment attachment = attachments.remove(player);
        if (attachment != null) {
            player.removeAttachment(attachment);
        }
    }

    public void setPermission(Player player, String permission, boolean value) {
        PermissionAttachment attachment = attachments.get(player);
        if (attachment != null) {
            attachment.setPermission(permission, value);
        }
    }
}
