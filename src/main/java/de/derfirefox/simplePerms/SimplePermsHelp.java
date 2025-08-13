package de.derfirefox.simplePerms;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SimplePermsHelp {

    private final SimplePerms plugin;

    public SimplePermsHelp(SimplePerms plugin) {
        this.plugin = plugin;
    }

    // Sendet die komplette Hilfe an den Spieler
    public void sendFullHelp(CommandSender sender) {
        String prefix = plugin.getMessagePrefix();
        sender.sendMessage(prefix + ChatColor.YELLOW + "=== SimplePerms Hilfe ===");

        // User Befehle
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp user <player> addperm <permission> " + ChatColor.GRAY + "- Fügt dem Spieler eine Permission hinzu");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp user <player> removeperm <permission> " + ChatColor.GRAY + "- Entfernt eine Permission vom Spieler");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp user <player> addgroup <group> [dauer] " + ChatColor.GRAY + "- Fügt den Spieler einer Gruppe hinzu");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp user <player> removegroup <group> " + ChatColor.GRAY + "- Entfernt den Spieler aus einer Gruppe");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp user <player> list " + ChatColor.GRAY + "- Listet alle Permissions und Gruppen des Spielers auf");

        // Group Befehle
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group create <name> " + ChatColor.GRAY + "- Erstellt eine neue Gruppe");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group delete <name> " + ChatColor.GRAY + "- Löscht eine Gruppe");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group setprefix <name> <prefix> " + ChatColor.GRAY + "- Setzt das Prefix einer Gruppe");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group setweight <name> <gewicht> " + ChatColor.GRAY + "- Setzt das Gewicht einer Gruppe");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group addperm <name> <permission> " + ChatColor.GRAY + "- Fügt der Gruppe eine Permission hinzu");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group removeperm <name> <permission> " + ChatColor.GRAY + "- Entfernt eine Permission aus der Gruppe");
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp group list " + ChatColor.GRAY + "- Listet alle Gruppen auf");

        // Allgemeiner Befehl
        sender.sendMessage(prefix + ChatColor.AQUA + "/sp save " + ChatColor.GRAY + "- Speichert alle Änderungen");

        sender.sendMessage(prefix + ChatColor.YELLOW + "========================");
    }
}
