package de.derfirefox.simplePerms;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SimplePermsTabCompleter implements TabCompleter {

    private final PermissionManager pm;

    public SimplePermsTabCompleter(PermissionManager pm) {
        this.pm = pm;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("user");
            completions.add("group");
            completions.add("save");
        } else {
            String mainCmd = args[0].toLowerCase(Locale.ROOT);
            switch (mainCmd) {
                case "user":
                    completions.addAll(handleUserTab(args));
                    break;
                case "group":
                    completions.addAll(handleGroupTab(args));
                    break;
            }
        }

        String currentArg = args[args.length - 1].toLowerCase(Locale.ROOT);
        return completions.stream()
                .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(currentArg))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> handleUserTab(String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) list.add(p.getName());
        } else if (args.length == 3) {
            list.add("addperm");
            list.add("removeperm");
            list.add("addgroup");
            list.add("removegroup");
            list.add("list");
        } else if (args.length == 4) {
            String action = args[2].toLowerCase(Locale.ROOT);
            switch (action) {
                case "addgroup":
                case "removegroup":
                    pm.getAllGroups().forEach(g -> list.add(g.getName()));
                    break;
                case "addperm":
                case "removeperm":
                    // Alle Permissions von allen Plugins
                    for (Permission perm : Bukkit.getServer().getPluginManager().getPermissions()) {
                        list.add(perm.getName());
                    }
                    break;
            }
        }

        return list;
    }

    private List<String> handleGroupTab(String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 2) {
            list.add("create");
            list.add("delete");
            list.add("setprefix");
            list.add("setweight");
            list.add("addperm");
            list.add("removeperm");
            list.add("list");
        } else if (args.length >= 3) {
            String action = args[1].toLowerCase(Locale.ROOT);
            switch (action) {
                case "delete":
                case "setprefix":
                case "setweight":
                    pm.getAllGroups().forEach(g -> list.add(g.getName()));
                    break;
                case "addperm":
                case "removeperm":
                    for (Permission perm : Bukkit.getServer().getPluginManager().getPermissions()) {
                        list.add(perm.getName());
                    }
                    break;
                default:
                    // Kein Fehler werfen, einfach ignorieren
                    break;
            }
        }

        return list;
    }
}
