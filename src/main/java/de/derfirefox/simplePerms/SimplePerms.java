package de.derfirefox.simplePerms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SimplePerms extends JavaPlugin {

    private PermissionManager permManager;
    private GlobalPermissionManager globalPermissionManager;

    @Override
    public void onEnable() {
        // Standard config speichern, falls noch nicht vorhanden
        saveDefaultConfig();

        // GlobalPermissionManager erstellen
        globalPermissionManager = new GlobalPermissionManager();

        // PermissionManager erstellen und Daten laden
        permManager = new PermissionManager(this, globalPermissionManager);
        try {
            permManager.loadAll();
        } catch (Exception e) {
            getLogger().severe("Fehler beim Laden der Daten: " + e.getMessage());
            e.printStackTrace();
        }

        // Temporäre Gruppenverwaltung starten (falls implementiert)
        permManager.startTempGroupTask();

        // Command und TabCompleter registrieren
        Objects.requireNonNull(getCommand("sp")).setExecutor(new SimplePermsCommand(permManager, this));
        Objects.requireNonNull(getCommand("sp")).setTabCompleter(new SimplePermsTabCompleter(permManager));

        // Event-Listener registrieren
        getServer().getPluginManager().registerEvents(new ChatListener(permManager, globalPermissionManager), this);
        getServer().getPluginManager().registerEvents(new JoinListener(permManager), this);

        // Spieler, die bereits online sind, Permissions setzen
        Bukkit.getOnlinePlayers().forEach(globalPermissionManager::registerPlayer);

        // PlaceholderAPI Integration
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SimplePermsPlaceholder(permManager).register();
            getLogger().info("SimplePerms PlaceholderAPI Expansion registriert!");
        }

        getLogger().info("SimplePerms aktiviert.");
    }

    @Override
    public void onDisable() {
        try {
            permManager.saveAll();
        } catch (Exception e) {
            getLogger().severe("Fehler beim Speichern der Daten: " + e.getMessage());
            e.printStackTrace();
        }
        getLogger().info("SimplePerms deaktiviert.");
    }

    public static String translateColorCodes(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public String getMessagePrefix() {
        return "§8§bSimplePerms§8 §r";
    }

    public PermissionManager getPermManager() {
        return permManager;
    }

    public GlobalPermissionManager getGlobalPermissionManager() {
        return globalPermissionManager;
    }
}
