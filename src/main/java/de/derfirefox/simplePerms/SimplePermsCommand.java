package de.derfirefox.simplePerms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.UUID;

public class SimplePermsCommand implements CommandExecutor {

    private final PermissionManager pm;
    private final de.derfirefox.simplePerms.SimplePerms plugin;
    private final SimplePermsHelp help;

    private static final String MSG_UNKNOWN_SUBCOMMAND = "Unbekannter Unterbefehl.";
    private static final String MSG_NO_PERMISSION = "Keine Berechtigung.";

    public SimplePermsCommand(PermissionManager pm, de.derfirefox.simplePerms.SimplePerms plugin) {
        this.pm = pm;
        this.plugin = plugin;
        this.help = new SimplePermsHelp(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final String prefix = plugin.getMessagePrefix();

        if (!PermissionHandler.canUseSimplePerms(sender)) {
            sendNoPermission(sender, prefix);
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            help.sendFullHelp(sender);
            return true;
        }

        String mainCmd = args[0].toLowerCase(Locale.ROOT);
        try {
            switch (mainCmd) {
                case "user":
                    if (!PermissionHandler.canUseUserCommands(sender)) {
                        sendNoPermission(sender, prefix);
                        return true;
                    }
                    return handleUser(sender, args, prefix);
                case "group":
                    if (!PermissionHandler.canUseGroupCommands(sender)) {
                        sendNoPermission(sender, prefix);
                        return true;
                    }
                    return handleGroup(sender, args, prefix);
                case "save":
                    if (!PermissionHandler.canSave(sender)) {
                        sendNoPermission(sender, prefix);
                        return true;
                    }
                    pm.saveAll();
                    sender.sendMessage(prefix + ChatColor.GREEN + "Alle Daten wurden gespeichert.");
                    return true;
                default:
                    sender.sendMessage(prefix + ChatColor.RED + MSG_UNKNOWN_SUBCOMMAND);
                    return true;
            }
        } catch (Exception e) {
            sender.sendMessage(prefix + ChatColor.RED + "Fehler beim Ausführen des Befehls.");
            e.printStackTrace();
            return true;
        }
    }

    private void sendNoPermission(CommandSender sender, String prefix) {
        sender.sendMessage(prefix + ChatColor.RED + MSG_NO_PERMISSION);
    }

    private boolean saveDataAndNotify(CommandSender sender, String prefix) throws Exception {
        pm.saveAll();
        sender.sendMessage(prefix + ChatColor.GREEN + "Änderungen wurden gespeichert.");
        return true;
    }

    private boolean handleUser(CommandSender sender, String[] args, String prefix) throws Exception {
        if (args.length < 3) {
            sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp user <uuid|name> <addperm|removeperm|addgroup|removegroup|list> [args]");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        UUID uuid = target.getUniqueId();
        var ud = pm.getOrCreateUser(uuid);

        String action = args[2].toLowerCase(Locale.ROOT);

        switch (action) {
            case "addperm":
                if (!PermissionHandler.canAddUserPerm(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp user <player> addperm <perm>");
                    return true;
                }
                ud.getPermissions().add(args[3]);
                sender.sendMessage(prefix + ChatColor.GREEN + "Permission hinzugefügt.");
                return saveDataAndNotify(sender, prefix);

            case "removeperm":
                if (!PermissionHandler.canRemoveUserPerm(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp user <player> removeperm <perm>");
                    return true;
                }
                ud.getPermissions().remove(args[3]);
                sender.sendMessage(prefix + ChatColor.GREEN + "Permission entfernt.");
                return saveDataAndNotify(sender, prefix);

            case "addgroup":
                if (!PermissionHandler.canAddUserGroup(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp user <player> addgroup <group> [dauer]");
                    return true;
                }
                ud.getGroups().add(args[3].toLowerCase(Locale.ROOT));
                sender.sendMessage(prefix + ChatColor.GREEN + "Gruppe hinzugefügt.");
                return saveDataAndNotify(sender, prefix);

            case "removegroup":
                if (!PermissionHandler.canRemoveUserGroup(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp user <player> removegroup <group>");
                    return true;
                }
                ud.getGroups().remove(args[3].toLowerCase(Locale.ROOT));
                sender.sendMessage(prefix + ChatColor.GREEN + "Gruppe entfernt.");
                return saveDataAndNotify(sender, prefix);

            case "list":
                if (!PermissionHandler.canListUser(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                sender.sendMessage(prefix + ChatColor.YELLOW + "Permissions: " + String.join(", ", ud.getPermissions()));
                sender.sendMessage(prefix + ChatColor.YELLOW + "Gruppen: " + String.join(", ", ud.getGroups()));
                return true;

            default:
                sender.sendMessage(prefix + ChatColor.RED + "Unbekannte Aktion.");
                return true;
        }
    }

    private boolean handleGroup(CommandSender sender, String[] args, String prefix) throws Exception {
        if (args.length < 2) {
            sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group <create|delete|setprefix|setweight|addperm|removeperm|list> [args]");
            return true;
        }

        String action = args[1].toLowerCase(Locale.ROOT);

        switch (action) {
            case "create":
                if (!PermissionHandler.canCreateGroup(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group create <name>");
                    return true;
                }
                pm.createGroup(args[2]);
                sender.sendMessage(prefix + ChatColor.GREEN + "Gruppe erstellt.");
                return saveDataAndNotify(sender, prefix);

            case "delete":
                if (!PermissionHandler.canDeleteGroup(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group delete <name>");
                    return true;
                }
                pm.deleteGroup(args[2]);
                sender.sendMessage(prefix + ChatColor.GREEN + "Gruppe gelöscht.");
                return saveDataAndNotify(sender, prefix);

            case "setprefix":
                if (!PermissionHandler.canSetGroupPrefix(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group setprefix <name> <prefix>");
                    return true;
                }
                pm.getGroup(args[2]).ifPresentOrElse(
                        g -> {
                            g.setPrefix(de.derfirefox.simplePerms.SimplePerms.translateColorCodes(args[3]));
                            sender.sendMessage(prefix + ChatColor.GREEN + "Prefix gesetzt.");
                            try {
                                saveDataAndNotify(sender, prefix);
                            } catch (Exception e) {
                                sender.sendMessage(prefix + ChatColor.RED + "Fehler beim Speichern.");
                                e.printStackTrace();
                            }
                        },
                        () -> sender.sendMessage(prefix + ChatColor.RED + "Gruppe nicht gefunden.")
                );
                return true;

            case "setweight":
                if (!PermissionHandler.canSetGroupWeight(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group setweight <name> <gewicht>");
                    return true;
                }
                try {
                    int weight = Integer.parseInt(args[3]);
                    pm.getGroup(args[2]).ifPresentOrElse(
                            g -> {
                                g.setWeight(weight);
                                sender.sendMessage(prefix + ChatColor.GREEN + "Gewicht gesetzt.");
                                try {
                                    saveDataAndNotify(sender, prefix);
                                } catch (Exception e) {
                                    sender.sendMessage(prefix + ChatColor.RED + "Fehler beim Speichern.");
                                    e.printStackTrace();
                                }
                            },
                            () -> sender.sendMessage(prefix + ChatColor.RED + "Gruppe nicht gefunden.")
                    );
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefix + ChatColor.RED + "Ungültige Zahl für Gewicht.");
                }
                return true;

            case "addperm":
                if (!PermissionHandler.canAddGroupPerm(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group addperm <name> <permission>");
                    return true;
                }
                pm.getGroup(args[2]).ifPresentOrElse(
                        g -> {
                            g.getPermissions().add(args[3]);
                            sender.sendMessage(prefix + ChatColor.GREEN + "Permission hinzugefügt.");
                            try {
                                saveDataAndNotify(sender, prefix);
                            } catch (Exception e) {
                                sender.sendMessage(prefix + ChatColor.RED + "Fehler beim Speichern.");
                                e.printStackTrace();
                            }
                        },
                        () -> sender.sendMessage(prefix + ChatColor.RED + "Gruppe nicht gefunden.")
                );
                return true;

            case "removeperm":
                if (!PermissionHandler.canRemoveGroupPerm(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage(prefix + ChatColor.RED + "Nutzung: /sp group removeperm <name> <permission>");
                    return true;
                }
                pm.getGroup(args[2]).ifPresentOrElse(
                        g -> {
                            g.getPermissions().remove(args[3]);
                            sender.sendMessage(prefix + ChatColor.GREEN + "Permission entfernt.");
                            try {
                                saveDataAndNotify(sender, prefix);
                            } catch (Exception e) {
                                sender.sendMessage(prefix + ChatColor.RED + "Fehler beim Speichern.");
                                e.printStackTrace();
                            }
                        },
                        () -> sender.sendMessage(prefix + ChatColor.RED + "Gruppe nicht gefunden.")
                );
                return true;

            case "list":
                if (!PermissionHandler.canListGroup(sender)) {
                    sendNoPermission(sender, prefix);
                    return true;
                }
                pm.getAllGroups().forEach(g -> {
                    sender.sendMessage(prefix + ChatColor.YELLOW + "Gruppe: " + g.getName() + ", Prefix: " + g.getPrefix() + ", Gewicht: " + g.getWeight());
                });
                return true;

            default:
                sender.sendMessage(prefix + ChatColor.RED + "Unbekannte Aktion.");
                return true;
        }
    }
}
