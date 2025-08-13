package de.derfirefox.simplePerms;

import org.bukkit.command.CommandSender;

public class PermissionHandler {

    public static boolean canUseSimplePerms(CommandSender sender) {
        return sender.hasPermission("simpleperms.use");
    }

    public static boolean canUseUserCommands(CommandSender sender) {
        return sender.hasPermission("simpleperms.user");
    }

    public static boolean canAddUserPerm(CommandSender sender) {
        return sender.hasPermission("simpleperms.user.addperm");
    }

    public static boolean canRemoveUserPerm(CommandSender sender) {
        return sender.hasPermission("simpleperms.user.removeperm");
    }

    public static boolean canAddUserGroup(CommandSender sender) {
        return sender.hasPermission("simpleperms.user.addgroup");
    }

    public static boolean canRemoveUserGroup(CommandSender sender) {
        return sender.hasPermission("simpleperms.user.removegroup");
    }

    public static boolean canListUser(CommandSender sender) {
        return sender.hasPermission("simpleperms.user.list");
    }

    public static boolean canUseGroupCommands(CommandSender sender) {
        return sender.hasPermission("simpleperms.group");
    }

    public static boolean canCreateGroup(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.create");
    }

    public static boolean canDeleteGroup(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.delete");
    }

    public static boolean canSetGroupPrefix(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.setprefix");
    }

    public static boolean canSetGroupWeight(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.setweight");
    }

    public static boolean canAddGroupPerm(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.addperm");
    }

    public static boolean canRemoveGroupPerm(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.removeperm");
    }

    public static boolean canListGroups(CommandSender sender) {
        return sender.hasPermission("simpleperms.group.list");
    }

    public static boolean canSave(CommandSender sender) {
        return sender.hasPermission("simpleperms.save");
    }

    public static void sendNoPermission(CommandSender sender, String prefix) {
        sender.sendMessage(prefix + "§cDafür hast du keine Rechte.");
    }

    public static boolean canListGroup(CommandSender sender) {
        return false;
    }
}
