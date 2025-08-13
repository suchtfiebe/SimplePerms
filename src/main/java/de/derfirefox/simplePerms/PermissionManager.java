package de.derfirefox.simplePerms;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public class PermissionManager implements CommandExecutor {

    private final JavaPlugin plugin;

    private File dataFile;
    private YamlConfiguration dataConfig;

    private final Map<UUID, UserData> users = new ConcurrentHashMap<>();
    private final Map<String, Group> groups = new ConcurrentHashMap<>();

    public PermissionManager(JavaPlugin plugin, GlobalPermissionManager globalPermissionManager) {
        this.plugin = plugin;

        // data.yml Datei laden oder erstellen
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        dataConfig = loadConfiguration(dataFile);
    }

    public void loadAll() throws Exception {
        groups.clear();
        users.clear();

        if (dataConfig.contains("groups")) {
            for (String groupName : dataConfig.getConfigurationSection("groups").getKeys(false)) {
                Group group = new Group(groupName);
                group.setPrefix(dataConfig.getString("groups." + groupName + ".prefix", ""));
                group.setWeight(dataConfig.getInt("groups." + groupName + ".weight", 0));
                List<String> perms = dataConfig.getStringList("groups." + groupName + ".permissions");
                group.getPermissions().addAll(perms);
                groups.put(groupName.toLowerCase(), group);
            }
        }

        if (dataConfig.contains("users")) {
            for (String uuidStr : dataConfig.getConfigurationSection("users").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                UserData user = new UserData(uuid);
                List<String> perms = dataConfig.getStringList("users." + uuidStr + ".permissions");
                List<String> grps = dataConfig.getStringList("users." + uuidStr + ".groups");
                user.getPermissions().addAll(perms);
                user.getGroups().addAll(grps);
                users.put(uuid, user);
            }
        }
    }

    public void saveAll() throws Exception {
        for (Group group : groups.values()) {
            String path = "groups." + group.getName().toLowerCase();
            dataConfig.set(path + ".prefix", group.getPrefix());
            dataConfig.set(path + ".weight", group.getWeight());
            dataConfig.set(path + ".permissions", new ArrayList<>(group.getPermissions()));
        }

        for (UserData user : users.values()) {
            String path = "users." + user.getUuid().toString();
            dataConfig.set(path + ".permissions", new ArrayList<>(user.getPermissions()));
            dataConfig.set(path + ".groups", new ArrayList<>(user.getGroups()));
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Fehler beim Speichern der data.yml");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sp")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("simpleperms.reload")) {
                    sender.sendMessage("§cDu hast keine Rechte, diesen Befehl auszuführen.");
                    return true;
                }

                try {
                    dataConfig = loadConfiguration(dataFile);
                    loadAll();
                    sender.sendMessage("§aSimplePerms wurde erfolgreich neu geladen.");
                } catch (Exception e) {
                    sender.sendMessage("§cFehler beim Neuladen der Daten!");
                    e.printStackTrace();
                }
                return true;
            }
            sender.sendMessage("§7Benutze: /sp reload");
            return true;
        }
        return false;
    }

    public UserData getOrCreateUser(UUID uuid) {
        return users.computeIfAbsent(uuid, UserData::new);
    }

    public int getOnlinePlayerCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    public int getGroupCount() {
        return groups.size();
    }

    public void applyPermissions(Player player) {
        UserData user = getOrCreateUser(player.getUniqueId());

        // Alle bisherigen Permissions entfernen
        player.getEffectivePermissions().forEach(perm -> player.addAttachment(plugin).unsetPermission(perm.getPermission()));

        // Gruppenpermissions hinzufügen
        for (String groupName : user.getGroups()) {
            Group group = groups.get(groupName.toLowerCase());
            if (group != null) {
                for (String perm : group.getPermissions()) {
                    player.addAttachment(plugin).setPermission(perm, true);
                }
            }
        }

        // User-spezifische Permissions hinzufügen
        for (String perm : user.getPermissions()) {
            player.addAttachment(plugin).setPermission(perm, true);
        }
    }

    public Optional<Group> getGroup(String name) {
        return Optional.ofNullable(groups.get(name.toLowerCase()));
    }

    public void createGroup(String name) {
        groups.putIfAbsent(name.toLowerCase(), new Group(name));
    }

    public void deleteGroup(String name) {
        groups.remove(name.toLowerCase());
    }

    public void listGroups(CommandSender sender, String prefix) {
        if (groups.isEmpty()) {
            sender.sendMessage(prefix + "Keine Gruppen vorhanden.");
            return;
        }
        sender.sendMessage(prefix + "Gruppen:");
        for (Group group : groups.values()) {
            sender.sendMessage(prefix + "- " + group.getName() + " (Gewicht: " + group.getWeight() + ", Prefix: " + group.getPrefix() + ")");
        }
    }

    public List<String> getOnlinePlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public List<String> getGroupNames() {
        return new ArrayList<>(groups.keySet());
    }

    public List<String> getAllPermissions() {
        Set<String> perms = new HashSet<>();
        for (Group group : groups.values()) {
            perms.addAll(group.getPermissions());
        }
        for (UserData user : users.values()) {
            perms.addAll(user.getPermissions());
        }
        return new ArrayList<>(perms);
    }

    public void startTempGroupTask() {
        // Optional: Implementierung für temporäre Gruppen
    }

    public String getChatPrefix(Player player) {
        UserData user = getOrCreateUser(player.getUniqueId());
        Group highestGroup = null;
        int highestWeight = Integer.MIN_VALUE;

        for (String groupName : user.getGroups()) {
            Group group = groups.get(groupName.toLowerCase());
            if (group != null && group.getWeight() > highestWeight) {
                highestWeight = group.getWeight();
                highestGroup = group;
            }
        }

        String prefix = highestGroup != null ? highestGroup.getPrefix() : "";
        if (!prefix.isEmpty() && !prefix.endsWith(" ")) {
            prefix += " ";
        }

        return prefix;
    }

    public String getChatFormat(Player player) {
        return getChatPrefix(player) + player.getName();
    }

    public Collection<Group> getAllGroups() {
        return groups.values();
    }
}
