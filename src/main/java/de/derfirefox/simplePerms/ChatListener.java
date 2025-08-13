package de.derfirefox.simplePerms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class ChatListener implements Listener {

    private final PermissionManager pm;
    private final GlobalPermissionManager globalPermissionManager;

    // Konstruktor: globalPermissionManager korrekt übergeben
    public ChatListener(PermissionManager pm, GlobalPermissionManager gpm) {
        this.pm = pm;
        this.globalPermissionManager = gpm;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Spieler bei der globalen Permission-Verwaltung registrieren
        globalPermissionManager.registerPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Spieler aus der globalen Permission-Verwaltung entfernen
        globalPermissionManager.unregisterPlayer(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // Hole das Chatformat für den Spieler
        String format = pm.getChatFormat(player);

        if (format != null && !format.endsWith(" ")) {
            format += " ";
        }

        if (format != null) {
            // %1$s = Spielername, %2$s = Nachricht
            event.setFormat(format + "%2$s");
        }
    }
}
