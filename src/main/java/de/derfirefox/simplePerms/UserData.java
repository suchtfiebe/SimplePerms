package de.derfirefox.simplePerms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Klasse für Benutzerdaten
public class UserData {

    private final UUID uuid;
    private final List<String> permissions = new ArrayList<>();
    private final List<String> groups = new ArrayList<>();

    public UserData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getGroups() {
        return groups;
    }
}
