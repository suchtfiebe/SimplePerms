package de.derfirefox.simplePerms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final PermissionManager pm;
    private GlobalPermissionManager gpm = null;

    public JoinListener(PermissionManager pm) {
        this.pm = pm;
        this.gpm = gpm;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        pm.applyPermissions(event.getPlayer());
        gpm.registerPlayer(event.getPlayer()); // Global registrieren
    }
}
